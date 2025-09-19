package com.luckydut97.feature_community.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckydut97.tennispark.core.domain.model.CommunityPost
import com.luckydut97.tennispark.core.domain.model.CommunityComment
import com.luckydut97.tennispark.core.domain.usecase.GetCommunityPostDetailUseCase
import com.luckydut97.tennispark.core.domain.usecase.ToggleCommunityLikeUseCase
import com.luckydut97.tennispark.core.domain.usecase.CreateCommentUseCase
import com.luckydut97.tennispark.core.domain.usecase.DeleteCommentUseCase
import com.luckydut97.tennispark.core.data.repository.CommunityRepositoryImpl
import com.luckydut97.tennispark.core.data.network.NetworkModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * 커뮤니티 상세 화면의 UI 상태
 */
data class CommunityDetailUiState(
    val isLoading: Boolean = false,
    val post: CommunityPost? = null,
    val comments: List<CommunityComment> = emptyList(),
    val errorMessage: String? = null,
    val isLoadingComments: Boolean = false,
    val isCreatingComment: Boolean = false, // 댓글 작성 중 상태
    val createCommentSuccess: Boolean = false, // 댓글 작성 성공 상태
    val createCommentError: String? = null, // 댓글 작성 에러 메시지
    val isDeletingComment: Boolean = false,
    val deleteCommentSuccess: Boolean = false,
    val deleteCommentError: String? = null
)

/**
 * 커뮤니티 상세 화면 ViewModel
 */
class CommunityDetailViewModel : ViewModel() {

    // Repository와 UseCase 초기화
    private val communityRepository = CommunityRepositoryImpl(
        NetworkModule.apiService,
        NetworkModule.getContext()
    )
    private val getCommunityPostDetailUseCase = GetCommunityPostDetailUseCase(communityRepository)
    private val toggleCommunityLikeUseCase = ToggleCommunityLikeUseCase(communityRepository)
    private val createCommentUseCase = CreateCommentUseCase(communityRepository)
    private val deleteCommentUseCase = DeleteCommentUseCase(communityRepository)

    private val _uiState = MutableStateFlow(CommunityDetailUiState())
    val uiState: StateFlow<CommunityDetailUiState> = _uiState.asStateFlow()

    /**
     * 게시글 상세 정보를 로드한다
     */
    fun loadPostDetail(postId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                getCommunityPostDetailUseCase(postId)
                    .catch { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "게시글을 불러올 수 없습니다."
                        )
                    }
                    .collect { post ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            post = post,
                            errorMessage = null
                        )

                        // 게시글 로드 후 댓글도 로드
                        loadComments(postId)
                    }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "네트워크 오류가 발생했습니다."
                )
            }
        }
    }

    /**
     * 댓글 목록을 로드한다
     */
    fun loadComments(postId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingComments = true)

            try {
                communityRepository.getPostComments(postId)
                    .catch { exception ->
                        // 댓글 로드 실패해도 게시글은 보이도록 에러 메시지만 설정
                        _uiState.value = _uiState.value.copy(
                            isLoadingComments = false,
                            comments = emptyList()
                        )
                    }
                    .collect { comments ->
                        _uiState.value = _uiState.value.copy(
                            isLoadingComments = false,
                            comments = comments
                        )
                    }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoadingComments = false,
                    comments = emptyList()
                )
            }
        }
    }

    /**
     * 좋아요 토글
     */
    fun toggleLike() {
        val currentPost = _uiState.value.post ?: return

        viewModelScope.launch {
            // Optimistic UI update
            _uiState.value = _uiState.value.copy(
                post = currentPost.copy(
                    likedByMe = !currentPost.likedByMe,
                    likeCount = if (currentPost.likedByMe)
                        currentPost.likeCount - 1
                    else
                        currentPost.likeCount + 1
                )
            )

            try {
                val result = toggleCommunityLikeUseCase(currentPost.id)

                if (result.isFailure) {
                    // 실패 시 롤백
                    _uiState.value = _uiState.value.copy(
                        post = currentPost
                    )
                }
            } catch (e: Exception) {
                // 에러 시 롤백
                _uiState.value = _uiState.value.copy(
                    post = currentPost
                )
            }
        }
    }

    /**
     * 댓글 작성
     */
    fun createComment(postId: Int, content: String, imageUri: Uri? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isCreatingComment = true,
                createCommentError = null,
                createCommentSuccess = false
            )

            try {
                val result = createCommentUseCase(postId, content, imageUri)

                if (result.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        isCreatingComment = false,
                        createCommentSuccess = true
                    )
                    // 성공 시 댓글 목록 새로고침
                    loadComments(postId)
                } else {
                    val errorMessage = result.exceptionOrNull()?.message ?: "댓글 작성에 실패했습니다."
                    _uiState.value = _uiState.value.copy(
                        isCreatingComment = false,
                        createCommentError = errorMessage
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isCreatingComment = false,
                    createCommentError = e.message ?: "네트워크 오류가 발생했습니다."
                )
            }
        }
    }

    /**
     * 댓글 삭제
     */
    fun deleteComment(postId: Int, commentId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isDeletingComment = true,
                deleteCommentError = null,
                deleteCommentSuccess = false
            )

            try {
                val result = deleteCommentUseCase(postId, commentId)
                if (result.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        isDeletingComment = false,
                        deleteCommentSuccess = true
                    )
                    loadComments(postId)
                } else {
                    val errorMessage = result.exceptionOrNull()?.message ?: "댓글 삭제에 실패했습니다."
                    _uiState.value = _uiState.value.copy(
                        isDeletingComment = false,
                        deleteCommentSuccess = false,
                        deleteCommentError = errorMessage
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isDeletingComment = false,
                    deleteCommentSuccess = false,
                    deleteCommentError = e.message ?: "네트워크 오류가 발생했습니다."
                )
            }
        }
    }

    /**
     * 댓글 작성 상태 초기화
     */
    fun clearCreateCommentState() {
        _uiState.value = _uiState.value.copy(
            createCommentSuccess = false,
            createCommentError = null
        )
    }

    fun clearDeleteCommentState() {
        _uiState.value = _uiState.value.copy(
            deleteCommentSuccess = false,
            deleteCommentError = null
        )
    }

    /**
     * 에러 메시지 클리어
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
