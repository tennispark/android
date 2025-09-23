package com.luckydut97.feature_community.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckydut97.tennispark.core.domain.model.CommunityPost
import com.luckydut97.tennispark.core.domain.usecase.GetCommunityPostsUseCase
import com.luckydut97.tennispark.core.domain.usecase.ToggleCommunityLikeUseCase
import com.luckydut97.tennispark.core.domain.usecase.CreateCommunityPostUseCase
import com.luckydut97.tennispark.core.domain.usecase.ReportCommunityPostUseCase
import com.luckydut97.tennispark.core.domain.usecase.TogglePostNotificationUseCase
import com.luckydut97.tennispark.core.data.repository.CommunityRepositoryImpl
import com.luckydut97.tennispark.core.data.network.NetworkModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * 커뮤니티 홈 화면의 UI 상태
 */
data class CommunityHomeUiState(
    val isLoading: Boolean = false,
    val posts: List<CommunityPost> = emptyList(),
    val errorMessage: String? = null,
    val isRefreshing: Boolean = false,
    val isCreatingPost: Boolean = false, // 게시글 작성 중 상태
    val createPostSuccess: Boolean = false, // 게시글 작성 성공 상태
    val createPostError: String? = null, // 게시글 작성 에러 메시지
    val currentPage: Int = 0, // 현재 페이지
    val hasNextPage: Boolean = false, // 다음 페이지 존재 여부
    val isLoadingMore: Boolean = false, // 추가 로딩 중
    val canLoadMore: Boolean = true, // 더 불러올 수 있는지
    val notificationError: String? = null,
    val notificationUpdatingPostIds: Set<Int> = emptySet()
)

/**
 * 커뮤니티 홈 화면 ViewModel
 */
class CommunityHomeViewModel : ViewModel() {

    // Repository와 UseCase 초기화
    private val communityRepository = CommunityRepositoryImpl(
        NetworkModule.apiService,
        NetworkModule.getContext()
    )
    private val getCommunityPostsUseCase = GetCommunityPostsUseCase(communityRepository)
    private val toggleCommunityLikeUseCase = ToggleCommunityLikeUseCase(communityRepository)
    private val createCommunityPostUseCase = CreateCommunityPostUseCase(communityRepository)
    private val reportCommunityPostUseCase = ReportCommunityPostUseCase(communityRepository)
    private val togglePostNotificationUseCase = TogglePostNotificationUseCase(communityRepository)

    private val _uiState = MutableStateFlow(CommunityHomeUiState())
    val uiState: StateFlow<CommunityHomeUiState> = _uiState.asStateFlow()

    private val PAGE_SIZE = 20

    init {
        loadCommunityPosts()
    }

    /**
     * 커뮤니티 게시글을 로드한다 (첫 페이지)
     */
    fun loadCommunityPosts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                currentPage = 0,
                notificationError = null,
                notificationUpdatingPostIds = emptySet()
            )

            try {
                getCommunityPostsUseCase(0, PAGE_SIZE)
                    .catch { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "게시글을 불러올 수 없습니다."
                        )
                    }
                    .collect { (posts, hasNext) ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            posts = posts,
                            errorMessage = null,
                            currentPage = 0,
                            hasNextPage = hasNext,
                            canLoadMore = hasNext,
                            notificationUpdatingPostIds = emptySet()
                        )
                    }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "네트워크 오류가 발생했습니다.",
                    notificationUpdatingPostIds = emptySet()
                )
            }
        }
    }

    /**
     * Pull-to-Refresh (새로고침)
     */
    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isRefreshing = true,
                currentPage = 0,
                notificationError = null
            )

            try {
                getCommunityPostsUseCase(0, PAGE_SIZE)
                    .catch { exception ->
                        _uiState.value = _uiState.value.copy(
                            isRefreshing = false,
                            errorMessage = exception.message ?: "게시글을 불러올 수 없습니다."
                        )
                    }
                    .collect { (posts, hasNext) ->
                        _uiState.value = _uiState.value.copy(
                            isRefreshing = false,
                            posts = posts,
                            errorMessage = null,
                            currentPage = 0,
                            hasNextPage = hasNext,
                            canLoadMore = hasNext,
                            notificationUpdatingPostIds = emptySet()
                        )
                    }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isRefreshing = false,
                    errorMessage = e.message ?: "네트워크 오류가 발생했습니다."
                )
            }
        }
    }

    /**
     * 다음 페이지 로드 (무한 스크롤)
     */
    fun loadNextPage() {
        if (_uiState.value.isLoadingMore || !_uiState.value.canLoadMore) {
            return // 이미 로딩 중이거나 더 불러올 수 없으면 리턴
        }

        viewModelScope.launch {
            val nextPage = _uiState.value.currentPage + 1
            _uiState.value = _uiState.value.copy(isLoadingMore = true)

            try {
                getCommunityPostsUseCase(nextPage, PAGE_SIZE)
                    .catch { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoadingMore = false,
                            errorMessage = exception.message ?: "게시글을 불러올 수 없습니다."
                        )
                    }
                    .collect { (newPosts, hasNext) ->
                        val currentPosts = _uiState.value.posts
                        _uiState.value = _uiState.value.copy(
                            isLoadingMore = false,
                            posts = currentPosts + newPosts, // 기존 게시글에 추가
                            currentPage = nextPage,
                            hasNextPage = hasNext,
                            canLoadMore = hasNext,
                            errorMessage = null
                        )
                    }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoadingMore = false,
                    errorMessage = e.message ?: "네트워크 오류가 발생했습니다."
                )
            }
        }
    }

    /**
     * 게시글 좋아요 토글
     */
    fun toggleLike(postId: Int) {
        viewModelScope.launch {
            // 먼저 UI에서 즉시 반영 (Optimistic Update)
            val currentPosts = _uiState.value.posts.toMutableList()
            val postIndex = currentPosts.indexOfFirst { it.id == postId }

            if (postIndex != -1) {
                val post = currentPosts[postIndex]
                val updatedPost = post.copy(
                    likedByMe = !post.likedByMe,
                    likeCount = if (post.likedByMe) post.likeCount - 1 else post.likeCount + 1
                )
                currentPosts[postIndex] = updatedPost

                _uiState.value = _uiState.value.copy(posts = currentPosts)

                // 백그라운드에서 실제 API 호출
                val result = toggleCommunityLikeUseCase(postId)
                if (result.isFailure) {
                    // API 실패 시 원래 상태로 되돌리기
                    val revertedPost = post.copy(
                        likedByMe = post.likedByMe,
                        likeCount = post.likeCount
                    )
                    currentPosts[postIndex] = revertedPost
                    _uiState.value = _uiState.value.copy(posts = currentPosts.toList())

                    // 에러 메시지는 표시하지 않음 (사용자 경험 고려)
                }
            }
        }
    }

    /**
     * 에러 메시지 초기화
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    /**
     * 게시글 작성
     */
    fun createPost(title: String, content: String, images: List<Uri> = emptyList()) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isCreatingPost = true,
                createPostError = null,
                createPostSuccess = false
            )

            try {
                val result = createCommunityPostUseCase(title, content, images)

                if (result.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        isCreatingPost = false,
                        createPostSuccess = true
                    )
                    // 성공 시 게시글 목록 새로고침
                    loadCommunityPosts()
                } else {
                    val errorMessage = result.exceptionOrNull()?.message ?: "게시글 작성에 실패했습니다."
                    _uiState.value = _uiState.value.copy(
                        isCreatingPost = false,
                        createPostError = errorMessage
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isCreatingPost = false,
                    createPostError = e.message ?: "네트워크 오류가 발생했습니다."
                )
            }
        }
    }

    /**
     * 게시글 작성 상태 초기화
     */
    fun clearCreatePostState() {
        _uiState.value = _uiState.value.copy(
            createPostSuccess = false,
            createPostError = null
        )
    }

    suspend fun reportPost(postId: Int, reason: String): Result<Unit> {
        return reportCommunityPostUseCase(postId, reason)
    }

    fun clearNotificationError() {
        _uiState.value = _uiState.value.copy(notificationError = null)
    }

    fun toggleNotification(postId: Int) {
        viewModelScope.launch {
            if (_uiState.value.notificationUpdatingPostIds.contains(postId)) return@launch
            val currentState = _uiState.value
            val index = currentState.posts.indexOfFirst { it.id == postId }
            if (index == -1) return@launch

            val post = currentState.posts[index]
            val currentFlag = post.notificationEnabled ?: return@launch

            val optimisticFlag = !currentFlag
            val optimisticPosts = currentState.posts.toMutableList().apply {
                this[index] = post.copy(notificationEnabled = optimisticFlag)
            }

            _uiState.value = currentState.copy(
                posts = optimisticPosts,
                notificationError = null,
                notificationUpdatingPostIds = currentState.notificationUpdatingPostIds + postId
            )

            val result = togglePostNotificationUseCase(postId)
            val stateAfterCall = _uiState.value
            if (result.isSuccess) {
                val actualFlag = result.getOrNull() ?: optimisticFlag
                val successPosts = stateAfterCall.posts.toMutableList().apply {
                    val successIndex = indexOfFirst { it.id == postId }
                    if (successIndex != -1) {
                        val currentPostState = this[successIndex]
                        this[successIndex] = currentPostState.copy(notificationEnabled = actualFlag)
                    }
                }
                _uiState.value = stateAfterCall.copy(
                    posts = successPosts,
                    notificationUpdatingPostIds = stateAfterCall.notificationUpdatingPostIds - postId
                )
            } else {
                val revertedPosts = stateAfterCall.posts.toMutableList().apply {
                    val failureIndex = indexOfFirst { it.id == postId }
                    if (failureIndex != -1) {
                        val currentPostState = this[failureIndex]
                        this[failureIndex] = currentPostState.copy(notificationEnabled = currentFlag)
                    }
                }
                _uiState.value = stateAfterCall.copy(
                    posts = revertedPosts,
                    notificationUpdatingPostIds = stateAfterCall.notificationUpdatingPostIds - postId,
                    notificationError = result.exceptionOrNull()?.message
                        ?: "알림 설정을 변경할 수 없습니다."
                )
            }
        }
    }
}
