package com.luckydut97.tennispark.feature_auth.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckydut97.tennispark.core.data.network.NetworkModule
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.luckydut97.tennispark.core.data.repository.AuthRepository
import com.luckydut97.tennispark.core.data.repository.AuthRepositoryImpl
import com.luckydut97.tennispark.core.data.storage.TokenManager
import com.luckydut97.tennispark.core.data.storage.TokenManagerImpl
import com.luckydut97.tennispark.core.config.AppConfig

sealed class AuthState {
    object Loading : AuthState()
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
}

class SplashViewModel : ViewModel() {

    private val tag = "🔍 디버깅: SplashViewModel"

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // AuthRepository 인스턴스 생성
    private val authRepository: AuthRepository by lazy {
        val tokenManager = TokenManagerImpl(NetworkModule.getContext()!!)
        AuthRepositoryImpl(NetworkModule.apiService, tokenManager)
    }

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            try {

                // 스플래시 화면 표시 시간
                delay(2000)

                // 🔥 개발 모드 체크 추가
                if (AppConfig.isDevelopment) {

                    // 개발용 토큰 자동 저장
                    val tokenManager = TokenManagerImpl(NetworkModule.getContext()!!)
                    tokenManager.saveTokens(
                        AppConfig.DEV_ACCESS_TOKEN,
                        AppConfig.DEV_REFRESH_TOKEN
                    )

                    _authState.value = AuthState.Authenticated
                    return@launch
                }


                val isLoggedIn = authRepository.isLoggedIn()

                if (isLoggedIn) {
                    // 토큰이 있으면 토큰 재발급을 시도해서 유효성 확인
                    val refreshResult = authRepository.refreshTokens()

                    if (refreshResult.success) {
                        _authState.value = AuthState.Authenticated
                    } else {
                        // 토큰이 만료되었거나 유효하지 않음 - 로그아웃 처리
                        authRepository.logout()
                        _authState.value = AuthState.Unauthenticated
                    }
                } else {
                    _authState.value = AuthState.Unauthenticated
                }

            } catch (e: Exception) {
                // 오류 발생 시 안전하게 인증 화면으로 이동
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

    // 개발 모드 토글 (테스트용)
    fun toggleDevMode() {
        checkAuthStatus()
    }
}
