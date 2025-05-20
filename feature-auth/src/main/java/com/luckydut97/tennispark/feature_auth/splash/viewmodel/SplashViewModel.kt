package com.luckydut97.tennispark.feature_auth.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _navigateToLogin = MutableStateFlow(false)
    val navigateToLogin: StateFlow<Boolean> = _navigateToLogin.asStateFlow()

    init {
        viewModelScope.launch {
            // 실제 프로덕션 코드에서는 여기에 세션 확인, 자동 로그인 체크 등의 로직 추가
            delay(2000) // 스플래시 화면 표시 시간
            _isLoading.value = false
            _navigateToLogin.value = true
        }
    }
}