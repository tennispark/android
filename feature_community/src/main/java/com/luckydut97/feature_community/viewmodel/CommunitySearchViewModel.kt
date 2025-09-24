package com.luckydut97.feature_community.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckydut97.tennispark.core.data.network.NetworkModule
import com.luckydut97.tennispark.core.data.repository.CommunityRepositoryImpl
import com.luckydut97.tennispark.core.domain.model.CommunityPost
import com.luckydut97.tennispark.core.domain.usecase.SearchCommunityPostsUseCase
import com.luckydut97.tennispark.core.domain.usecase.ToggleCommunityLikeUseCase
import com.luckydut97.tennispark.core.domain.usecase.TogglePostNotificationUseCase
import com.luckydut97.tennispark.core.domain.usecase.ReportCommunityPostUseCase
import com.luckydut97.tennispark.core.data.storage.CommunitySearchPreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class CommunitySearchUiState(
    val query: String = "",
    val recentSearches: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val results: List<CommunityPost> = emptyList(),
    val hasNext: Boolean = false,
    val errorMessage: String? = null,
    val lastKeyword: String? = null,
    val notificationUpdatingPostIds: Set<Int> = emptySet(),
    val notificationError: String? = null
)

class CommunitySearchViewModel : ViewModel() {

    private val repository = CommunityRepositoryImpl(
        NetworkModule.apiService,
        NetworkModule.getContext()
    )
    private val searchUseCase = SearchCommunityPostsUseCase(repository)
    private val toggleLikeUseCase = ToggleCommunityLikeUseCase(repository)
    private val toggleNotificationUseCase = TogglePostNotificationUseCase(repository)
    private val reportPostUseCase = ReportCommunityPostUseCase(repository)
    private val preferenceManager = NetworkModule.getContext()?.let { CommunitySearchPreferenceManager(it) }

    private val _uiState = MutableStateFlow(CommunitySearchUiState())
    val uiState: StateFlow<CommunitySearchUiState> = _uiState.asStateFlow()

    fun updateQuery(value: String) {
        val limited = value.take(100)
        val trimmed = limited.trim()

        _uiState.value = if (trimmed.isEmpty()) {
            _uiState.value.copy(
                query = "",
                isLoading = false,
                results = emptyList(),
                hasNext = false,
                errorMessage = null,
                lastKeyword = null,
                notificationUpdatingPostIds = emptySet(),
                notificationError = null
            )
        } else {
            _uiState.value.copy(
                query = limited,
                errorMessage = null
            )
        }
    }

    fun selectRecent(keyword: String) {
        _uiState.value = _uiState.value.copy(query = keyword, errorMessage = null)
    }

    fun commitSearch(onSearch: (String) -> Unit) {
        val trimmed = _uiState.value.query.trim()
        if (trimmed.length < MIN_KEYWORD_LENGTH) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "검색어는 2자 이상 입력해주세요."
            )
            return
        }

        viewModelScope.launch {
            val updatedRecent = _uiState.value.recentSearches.toMutableList().apply {
                remove(trimmed)
                add(0, trimmed)
            }.take(MAX_RECENT)

            preferenceManager?.saveRecentSearches(updatedRecent)

            _uiState.value = _uiState.value.copy(
                recentSearches = updatedRecent,
                query = trimmed,
                isLoading = true,
                errorMessage = null,
                lastKeyword = trimmed,
                results = emptyList(),
                notificationUpdatingPostIds = emptySet(),
                notificationError = null
            )

            searchUseCase(trimmed)
                .catch { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "검색 결과를 불러올 수 없습니다.",
                        results = emptyList(),
                        hasNext = false
                    )
                }
                .collect { (posts, hasNext) ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        results = posts,
                        hasNext = hasNext,
                        errorMessage = null
                    )
                    onSearch(trimmed)
                }
        }
    }

    fun removeRecent(keyword: String) {
        val filtered = _uiState.value.recentSearches.filterNot { it == keyword }
        preferenceManager?.saveRecentSearches(filtered)
        _uiState.value = _uiState.value.copy(recentSearches = filtered)
    }

    fun clearAll() {
        preferenceManager?.clear()
        _uiState.value = _uiState.value.copy(recentSearches = emptyList())
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun toggleLike(postId: Int) {
        val currentPosts = _uiState.value.results.toMutableList()
        val index = currentPosts.indexOfFirst { it.id == postId }
        if (index == -1) return

        val targetPost = currentPosts[index]
        val updated = targetPost.copy(
            likedByMe = !targetPost.likedByMe,
            likeCount = if (targetPost.likedByMe) targetPost.likeCount - 1 else targetPost.likeCount + 1
        )
        currentPosts[index] = updated
        _uiState.value = _uiState.value.copy(results = currentPosts)

        viewModelScope.launch {
            val result = toggleLikeUseCase(postId)
            if (result.isFailure) {
                currentPosts[index] = targetPost
                _uiState.value = _uiState.value.copy(results = currentPosts)
            }
        }
    }

    fun toggleNotification(postId: Int) {
        val currentState = _uiState.value
        if (currentState.notificationUpdatingPostIds.contains(postId)) return

        val posts = currentState.results.toMutableList()
        val index = posts.indexOfFirst { it.id == postId }
        if (index == -1) return

        val post = posts[index]
        val currentFlag = post.notificationEnabled ?: return
        val optimisticFlag = !currentFlag

        posts[index] = post.copy(notificationEnabled = optimisticFlag)
        _uiState.value = currentState.copy(
            results = posts,
            notificationError = null,
            notificationUpdatingPostIds = currentState.notificationUpdatingPostIds + postId
        )

        viewModelScope.launch {
            val result = toggleNotificationUseCase(postId)
            val stateAfterCall = _uiState.value
            if (result.isSuccess) {
                val actualFlag = result.getOrNull() ?: optimisticFlag
                val updatedPosts = stateAfterCall.results.toMutableList()
                val idx = updatedPosts.indexOfFirst { it.id == postId }
                if (idx != -1) {
                    val currentPost = updatedPosts[idx]
                    updatedPosts[idx] = currentPost.copy(notificationEnabled = actualFlag)
                }
                _uiState.value = stateAfterCall.copy(
                    results = updatedPosts,
                    notificationUpdatingPostIds = stateAfterCall.notificationUpdatingPostIds - postId
                )
            } else {
                val revertedPosts = stateAfterCall.results.toMutableList()
                val idx = revertedPosts.indexOfFirst { it.id == postId }
                if (idx != -1) {
                    val currentPost = revertedPosts[idx]
                    revertedPosts[idx] = currentPost.copy(notificationEnabled = currentFlag)
                }
                _uiState.value = stateAfterCall.copy(
                    results = revertedPosts,
                    notificationUpdatingPostIds = stateAfterCall.notificationUpdatingPostIds - postId,
                    notificationError = result.exceptionOrNull()?.message
                        ?: "알림 설정을 변경할 수 없습니다."
                )
            }
        }
    }

    fun clearNotificationError() {
        _uiState.value = _uiState.value.copy(notificationError = null)
    }

    suspend fun reportPost(postId: Int, reason: String): Result<Unit> {
        return reportPostUseCase(postId, reason)
    }

    fun refreshLastSearch(onSearch: (String) -> Unit) {
        val keyword = _uiState.value.lastKeyword ?: return
        _uiState.value = _uiState.value.copy(query = keyword)
        commitSearch(onSearch)
    }

    fun resetForEntry() {
        val persisted = preferenceManager?.getRecentSearches().orEmpty()
        _uiState.value = _uiState.value.copy(
            query = "",
            isLoading = false,
            results = emptyList(),
            hasNext = false,
            errorMessage = null,
            lastKeyword = null,
            recentSearches = persisted,
            notificationUpdatingPostIds = emptySet(),
            notificationError = null
        )
    }

    companion object {
        private const val MAX_RECENT = 10
        private const val MIN_KEYWORD_LENGTH = 2
    }
}
