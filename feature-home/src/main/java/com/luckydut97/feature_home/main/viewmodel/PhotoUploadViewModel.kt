package com.luckydut97.feature_home.main.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckydut97.tennispark.core.data.repository.ActivityCertificationRepository
import com.luckydut97.tennispark.core.utils.ImagePickerUtils
import com.luckydut97.tennispark.core.utils.PermissionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class PhotoUploadViewModel(
    private val activityCertificationRepository: ActivityCertificationRepository
) : ViewModel() {

    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet.asStateFlow()

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri.asStateFlow()

    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading.asStateFlow()

    private val _uploadSuccess = MutableStateFlow(false)
    val uploadSuccess: StateFlow<Boolean> = _uploadSuccess.asStateFlow()

    private val _showSuccessDialog = MutableStateFlow(false)
    val showSuccessDialog: StateFlow<Boolean> = _showSuccessDialog.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _permissionState = MutableStateFlow(PermissionState.DENIED)
    val permissionState: StateFlow<PermissionState> = _permissionState.asStateFlow()

    fun showPhotoUploadSheet() {
        _showBottomSheet.value = true
        resetState()
    }

    fun hidePhotoUploadSheet() {
        _showBottomSheet.value = false
        resetState()
    }

    fun onImageSelected(uri: Uri?) {
        _selectedImageUri.value = uri
        _errorMessage.value = null
    }

    fun onPermissionGranted() {
        _permissionState.value = PermissionState.GRANTED
    }

    fun onPermissionDenied() {
        _permissionState.value = PermissionState.DENIED
        _errorMessage.value = "갤러리 접근 권한이 필요합니다."
    }

    fun onPermissionPermanentlyDenied() {
        _permissionState.value = PermissionState.PERMANENTLY_DENIED
        _errorMessage.value = "설정에서 갤러리 접근 권한을 허용해주세요."
    }

    fun onUploadClick(context: Context) {
        val uri = _selectedImageUri.value
        if (uri == null) {
            _errorMessage.value = "업로드할 이미지를 선택해주세요."
            return
        }

        val file = ImagePickerUtils.uriToFile(context, uri)
        if (file == null) {
            _errorMessage.value = "이미지 파일을 처리할 수 없습니다."
            return
        }

        uploadImage(file)
    }

    private fun uploadImage(file: File) {
        viewModelScope.launch {
            _isUploading.value = true
            _errorMessage.value = null

            try {
                val result = activityCertificationRepository.certifyActivity(
                    activityId = 1L,
                    images = listOf(file),
                    content = null
                )

                result.fold(
                    onSuccess = { response ->
                        _uploadSuccess.value = true
                        _isUploading.value = false
                        _showSuccessDialog.value = true
                    },
                    onFailure = { error ->
                        _errorMessage.value = "사진 업로드에 실패하였습니다."
                        _isUploading.value = false
                        _uploadSuccess.value = false
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "사진 업로드에 실패하였습니다."
                _isUploading.value = false
                _uploadSuccess.value = false
            }
        }
    }

    fun onCloseSuccessDialog() {
        _showSuccessDialog.value = false
        hidePhotoUploadSheet()
    }

    private fun resetState() {
        _selectedImageUri.value = null
        _isUploading.value = false
        _uploadSuccess.value = false
        _showSuccessDialog.value = false
        _errorMessage.value = null
        _permissionState.value = PermissionState.DENIED
    }
}