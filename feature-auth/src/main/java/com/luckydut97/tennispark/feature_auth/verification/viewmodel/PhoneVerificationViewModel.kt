package com.luckydut97.tennispark.feature_auth.sms.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PhoneVerificationViewModel : ViewModel() {
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
            _isVerificationRequested.value = true
            _isTimerActive.value = true
            _remainingTime.value = 180 // 3분 리셋
            startTimer()
        }
    }

    fun verifyCode() {
        if (_verificationCode.value.length == 6) {
            // 실제 서버 인증 로직은 나중에 추가
            _isVerified.value = true
            _isTimerActive.value = false
            _navigateToSignup.value = true
        }
    }

    fun resendCode() {
        // 실제 서버 재전송 로직은 나중에 추가
        _remainingTime.value = 180 // 3분 리셋
        _isTimerActive.value = true
        startTimer()
    }

    fun navigateToSignupComplete() {
        _navigateToSignup.value = false
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
}