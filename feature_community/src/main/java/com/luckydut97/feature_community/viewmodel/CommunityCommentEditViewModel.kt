package com.luckydut97.feature_community.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckydut97.tennispark.core.data.network.NetworkModule
import com.luckydut97.tennispark.core.data.repository.CommunityRepositoryImpl
import com.luckydut97.tennispark.core.domain.usecase.UpdateCommentUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 댓글 수정 화면을 위한 ViewModel
 */
data class CommentEditUiState(
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val saveError: String? = null
)

class CommunityCommentEditViewModel : ViewModel() {

    private val repository = CommunityRepositoryImpl(
        NetworkModule.apiService,
        NetworkModule.getContext()
    )
    private val updateCommentUseCase = UpdateCommentUseCase(repository)

    private val _uiState = MutableStateFlow(CommentEditUiState())
    val uiState: StateFlow<CommentEditUiState> = _uiState.asStateFlow()

    fun updateComment(
        postId: Int,
        commentId: Int,
        content: String,
        deletePhoto: Boolean,
        newImageUri: Uri?
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSaving = true,
                saveSuccess = false,
                saveError = null
            )

            val result = updateCommentUseCase(postId, commentId, content, deletePhoto, newImageUri)
            _uiState.value = if (result.isSuccess) {
                _uiState.value.copy(
                    isSaving = false,
                    saveSuccess = true,
                    saveError = null
                )
            } else {
                _uiState.value.copy(
                    isSaving = false,
                    saveSuccess = false,
                    saveError = result.exceptionOrNull()?.message
                        ?: "댓글 수정에 실패했습니다."
                )
            }
        }
    }

    fun consumeSaveState() {
        _uiState.value = _uiState.value.copy(saveSuccess = false, saveError = null)
    }
}
