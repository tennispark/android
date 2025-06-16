package com.luckydut97.feature.attendance.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AttendanceViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AttendanceUiState())
    val uiState: StateFlow<AttendanceUiState> = _uiState.asStateFlow()

    fun processQrCode(qrCode: String) {
        // 중복 스캔 방지
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // QR 코드 로그 출력
            Log.d("카메라 디버깅:", "AttendanceViewModel - QR Code detected: $qrCode")

            // 실제 앱에서는 여기서 서버 API를 호출하여 출석 체크를 처리합니다
            delay(1000) // 네트워크 요청 시뮬레이션

            // 성공 처리
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                showSuccessDialog = true
            )
        }
    }

    fun dismissSuccessDialog() {
        _uiState.value = _uiState.value.copy(showSuccessDialog = false)
    }
}

data class AttendanceUiState(
    val isLoading: Boolean = false,
    val showSuccessDialog: Boolean = false
)
