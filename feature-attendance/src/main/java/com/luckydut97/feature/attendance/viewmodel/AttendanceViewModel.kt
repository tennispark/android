package com.luckydut97.feature.attendance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckydut97.tennispark.core.data.repository.PointRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AttendanceViewModel(
    private val pointRepository: PointRepository = PointRepository()
) : ViewModel() {

    private val tag = "🔍 디버깅: AttendanceViewModel"

    private val _uiState = MutableStateFlow(AttendanceUiState())
    val uiState: StateFlow<AttendanceUiState> = _uiState.asStateFlow()

    fun processQrCode(qrCode: String) {
        // 중복 스캔 방지
        if (_uiState.value.isLoading) return


        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                // QR 코드에서 URL 추출 (간단한 예시)
                val eventUrl = extractEventUrl(qrCode)

                if (eventUrl != null) {

                    // 실제 QR 이벤트 API 호출
                    val response = pointRepository.postQrEvent(eventUrl)

                    if (response.success) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            showSuccessDialog = true,
                            successMessage = "출석체크가 완료되었습니다!"
                        )
                    } else {

                        val errorMessage =
                            if (response.error?.message?.contains("이미 신청한") == true) {
                                "이미 출석 체크된 이벤트입니다."
                            } else {
                                "이미 출석 체크된 이벤트입니다."
                        }

                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = errorMessage
                        )
                    }
                } else {
                    // 테스트용 시뮬레이션 (개발 중)
                    delay(1000) // 네트워크 요청 시뮬레이션

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        showSuccessDialog = true,
                        successMessage = "출석체크가 완료되었습니다! (테스트)"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "QR 코드 처리 중 오류가 발생했습니다."
                )
            }
        }
    }

    /**
     * QR 코드에서 이벤트 URL 추출
     * 실제 구현에서는 QR 코드 형식에 따라 파싱 로직 구현
     */
    private fun extractEventUrl(qrCode: String): String? {
        return when {
            // 실제 URL 형태의 QR 코드인 경우
            qrCode.startsWith("http://") || qrCode.startsWith("https://") -> {
                if (qrCode.contains("/api/") || qrCode.contains("event") || qrCode.contains("checkin")) {
                    qrCode
                } else {
                    null
                }
            }
            // 테스트용 QR 코드 패턴
            qrCode.startsWith("TENNIS_PARK_") || qrCode.contains("TEST_QR") -> {
                // 실제 서버가 준비되면 여기서 실제 URL로 변환
                null // 지금은 테스트 모드로 처리
            }

            else -> null
        }
    }

    fun dismissSuccessDialog() {
        _uiState.value = _uiState.value.copy(showSuccessDialog = false)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

data class AttendanceUiState(
    val isLoading: Boolean = false,
    val showSuccessDialog: Boolean = false,
    val successMessage: String = "",
    val errorMessage: String? = null
)
