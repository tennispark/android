package com.luckydut97.tennispark.feature_auth.verification.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.luckydut97.tennispark.core.data.repository.PhoneVerificationRepository
import com.luckydut97.tennispark.core.data.storage.TokenManager
import com.luckydut97.tennispark.core.data.storage.TokenManagerImpl
import com.luckydut97.tennispark.core.data.network.NetworkModule
import com.luckydut97.tennispark.core.config.AppConfig

class PhoneVerificationViewModel : ViewModel() {

    private val tag = "🔍 디버깅: PhoneVerificationVM"
    private val phoneVerificationRepository = PhoneVerificationRepository()

    // TokenManager 인스턴스 생성
    private val tokenManager: TokenManager by lazy {
        val context = NetworkModule.getContext()
        if (context != null) {
            TokenManagerImpl(context)
        } else {
            throw IllegalStateException("NetworkModule not initialized")
        }
    }

    // 휴대폰 번호
    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber = _phoneNumber.asStateFlow()

    // 인증 코드
    private val _verificationCode = MutableStateFlow("")
    val verificationCode = _verificationCode.asStateFlow()

    // 인증 요청 여부
    private val _isVerificationRequested = MutableStateFlow(false)
    val isVerificationRequested = _isVerificationRequested.asStateFlow()

    // 인증 완료 여부
    private val _isVerified = MutableStateFlow(false)
    val isVerified = _isVerified.asStateFlow()

    // 타이머 (초 단위)
    private val _remainingTime = MutableStateFlow(180) // 3분
    val remainingTime = _remainingTime.asStateFlow()

    // 타이머 활성화 여부
    private val _isTimerActive = MutableStateFlow(false)
    val isTimerActive = _isTimerActive.asStateFlow()

    // 다음 버튼 활성화 여부
    private val _isNextButtonEnabled = MutableStateFlow(false)
    val isNextButtonEnabled = _isNextButtonEnabled.asStateFlow()

    // 이동 이벤트
    private val _navigateToSignup = MutableStateFlow(false)
    val navigateToSignup = _navigateToSignup.asStateFlow()

    // 기존 회원 로그인 완료 이벤트
    private val _navigateToMain = MutableStateFlow(false)
    val navigateToMain = _navigateToMain.asStateFlow()

    // 로딩 상태
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // 에러 메시지
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    // 재전송 제한 타이머 (10초)
    private val _resendCooldownTime = MutableStateFlow(0)
    val resendCooldownTime = _resendCooldownTime.asStateFlow()

    // 재전송 버튼 활성화 여부
    private val _isResendEnabled = MutableStateFlow(true)
    val isResendEnabled = _isResendEnabled.asStateFlow()

    fun updatePhoneNumber(number: String) {
        _phoneNumber.value = number
    }

    fun updateVerificationCode(code: String) {
        _verificationCode.value = code

        // 인증코드가 6자리이고 타이머가 활성화되어 있으면 다음 버튼 활성화
        _isNextButtonEnabled.value = code.length == 6 && _isTimerActive.value
    }

    fun requestVerification() {
        if (_phoneNumber.value.isNotEmpty()) {
            // 리뷰어/테스트 예외 - 이 부분 배포 후 주석처리
            if (_phoneNumber.value == "00000000") {
                viewModelScope.launch {
                    _isLoading.value = true
                    tokenManager.saveTokens(
                        AppConfig.DEV_ACCESS_TOKEN,
                        AppConfig.DEV_REFRESH_TOKEN
                    )
                    _isLoading.value = false
                    _isVerified.value = true
                    _navigateToMain.value = true
                }
                return
            }

            viewModelScope.launch {
                try {
                    _isLoading.value = true
                    _errorMessage.value = null

                    val response =
                        phoneVerificationRepository.requestVerificationCode(_phoneNumber.value)

                    if (response.success) {
                        _isVerificationRequested.value = true
                        _isTimerActive.value = true
                        _remainingTime.value = 180 // 3분 리셋
                        startTimer()
                    } else {
                        val errorMessage = response.error?.message ?: "인증번호 요청에 실패했습니다."
                        _errorMessage.value = errorMessage
                    }
                } catch (e: Exception) {
                    _errorMessage.value = "네트워크 오류가 발생했습니다: ${e.message}"
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    fun verifyCode() {
        if (_verificationCode.value.length == 6) {

            viewModelScope.launch {
                try {
                    _isLoading.value = true
                    _errorMessage.value = null

                    val response = phoneVerificationRepository.verifyPhoneCode(
                        phoneNumber = _phoneNumber.value,
                        code = _verificationCode.value
                    )

                    if (response.success) {
                        val verificationResponse = response.response

                        if (verificationResponse?.isRegister == true) {
                            // 기존 회원 로그인 처리

                            // 토큰 저장 - null 체크를 명시적으로 수행
                            val accessToken = verificationResponse.accessToken
                            val refreshToken = verificationResponse.refreshToken

                            if (accessToken != null && refreshToken != null) {
                                tokenManager.saveTokens(accessToken, refreshToken)

                                // FCM 토큰 서버 전송 추가 (기존 회원 로그인 성공 후)
                                val fcmTokenManager =
                                    com.luckydut97.tennispark.core.fcm.FcmTokenManager()
                                val context = NetworkModule.getContext()
                                if (context != null) {
                                    val tokenManagerForRepo =
                                        com.luckydut97.tennispark.core.data.storage.TokenManagerImpl(
                                            context
                                        )
                                    val authRepository =
                                        com.luckydut97.tennispark.core.data.repository.AuthRepositoryImpl(
                                            apiService = com.luckydut97.tennispark.core.data.network.NetworkModule.apiService,
                                            tokenManager = tokenManagerForRepo
                                        )
                                    viewModelScope.launch {
                                        val fcmToken = fcmTokenManager.getFcmToken()
                                        if (!fcmToken.isNullOrBlank()) {
                                            if (fcmTokenManager.isValidFcmToken(fcmToken)) {
                                                val response =
                                                    authRepository.updateFcmToken(fcmToken)
                                                if (response.success) {
                                                } else {
                                                }
                                            } else {
                                            }
                                        } else {
                                        }
                                    }
                                }
                            } else {
                            }

                            _isVerified.value = true
                            _isTimerActive.value = false
                            _navigateToMain.value = true
                        } else {
                            // 신규 사용자 - 회원가입 화면으로
                            _isVerified.value = true
                            _isTimerActive.value = false
                            _navigateToSignup.value = true
                        }
                    } else {
                        val errorMessage = response.error?.message ?: "인증번호 확인에 실패했습니다."
                        _errorMessage.value = errorMessage
                    }
                } catch (e: Exception) {
                    _errorMessage.value = "네트워크 오류가 발생했습니다: ${e.message}"
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    fun resendCode() {
        if (_resendCooldownTime.value > 0) {
            return
        }


        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null

                val response =
                    phoneVerificationRepository.requestVerificationCode(_phoneNumber.value)

                if (response.success) {
                    _remainingTime.value = 180 // 3분 리셋
                    _isTimerActive.value = true
                    startTimer()
                    startResendCooldown()
                } else {
                    val errorMessage = response.error?.message ?: "인증번호 재전송에 실패했습니다."
                    _errorMessage.value = errorMessage
                }
            } catch (e: Exception) {
                _errorMessage.value = "네트워크 오류가 발생했습니다: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun navigateToSignupComplete() {
        _navigateToSignup.value = false
    }

    fun navigateToMainComplete() {
        _navigateToMain.value = false
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (_remainingTime.value > 0 && _isTimerActive.value) {
                delay(1000)
                _remainingTime.value--

                if (_remainingTime.value == 0) {
                    _isTimerActive.value = false
                    _isNextButtonEnabled.value = false
                }
            }
        }
    }

    private fun startResendCooldown() {
        _resendCooldownTime.value = 10
        viewModelScope.launch {
            while (_resendCooldownTime.value > 0) {
                delay(1000)
                _resendCooldownTime.value--
            }
        }
    }
}
