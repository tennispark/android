package com.luckydut97.feature_community.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckydut97.tennispark.core.data.network.NetworkModule
import com.luckydut97.tennispark.core.data.repository.CommunityRepositoryImpl
import com.luckydut97.tennispark.core.domain.usecase.UpdateCommunityPostUseCase
import com.luckydut97.tennispark.core.domain.usecase.DeleteCommunityPostUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PostEditUiState(
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val saveError: String? = null,
    val isDeleting: Boolean = false,
    val deleteSuccess: Boolean = false,
    val deleteError: String? = null
)

class CommunityPostEditViewModel : ViewModel() {

    private val repository = CommunityRepositoryImpl(
        NetworkModule.apiService,
        NetworkModule.getContext()
    )
    private val updateUseCase = UpdateCommunityPostUseCase(repository)
    private val deleteUseCase = DeleteCommunityPostUseCase(repository)

    private val _uiState = MutableStateFlow(PostEditUiState())
    val uiState: StateFlow<PostEditUiState> = _uiState.asStateFlow()

    fun updatePost(
        postId: Int,
        title: String,
        content: String,
        deleteIndexes: List<Int>,
        newImages: List<Uri>
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSaving = true,
                saveSuccess = false,
                saveError = null
            )

            val result = updateUseCase(postId, title, content, deleteIndexes, newImages)
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
                        ?: "게시글 수정에 실패했습니다."
                )
            }
        }
    }

    fun deletePost(postId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isDeleting = true,
                deleteSuccess = false,
                deleteError = null
            )

            val result = deleteUseCase(postId)
            _uiState.value = if (result.isSuccess) {
                _uiState.value.copy(
                    isDeleting = false,
                    deleteSuccess = true,
                    deleteError = null
                )
            } else {
                _uiState.value.copy(
                    isDeleting = false,
                    deleteSuccess = false,
                    deleteError = result.exceptionOrNull()?.message
                        ?: "게시글 삭제에 실패했습니다."
                )
            }
        }
    }

    fun consumeSaveState() {
        _uiState.value = _uiState.value.copy(saveSuccess = false, saveError = null)
    }

    fun consumeDeleteState() {
        _uiState.value = _uiState.value.copy(deleteSuccess = false, deleteError = null)
    }
}
