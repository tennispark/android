package com.luckydut97.tennispark.feature_auth.splash.viewmodel

import android.util.Log
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
                Log.d(tag, "=== 인증 상태 확인 시작 ===")

                // 스플래시 화면 표시 시간
                delay(2000)

                Log.d(tag, "🚀 프로덕션 모드 - 토큰 상태 확인")

                val isLoggedIn = authRepository.isLoggedIn()
                Log.d(tag, "현재 로그인 상태: $isLoggedIn")

                if (isLoggedIn) {
                    // 토큰이 있으면 토큰 재발급을 시도해서 유효성 확인
                    Log.d(tag, "토큰이 존재함 - 토큰 재발급 시도")
                    val refreshResult = authRepository.refreshTokens()

                    if (refreshResult.success) {
                        Log.d(tag, "✅ 토큰 재발급 성공 - 인증된 상태로 메인 화면 이동")
                        _authState.value = AuthState.Authenticated
                    } else {
                        Log.e(tag, "❌ 토큰 재발급 실패 - 로그아웃 후 인증 화면 이동")
                        // 토큰이 만료되었거나 유효하지 않음 - 로그아웃 처리
                        authRepository.logout()
                        _authState.value = AuthState.Unauthenticated
                    }
                } else {
                    Log.d(tag, "토큰이 없음 - 인증 화면으로 이동")
                    _authState.value = AuthState.Unauthenticated
                }

                Log.d(tag, "인증 상태 확인 완료: ${_authState.value}")
            } catch (e: Exception) {
                Log.e(tag, "인증 상태 확인 중 오류: ${e.message}", e)
                // 오류 발생 시 안전하게 인증 화면으로 이동
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

    // 개발 모드 토글 (테스트용)
    fun toggleDevMode() {
        Log.d(tag, "개발 모드 토글 요청")
        checkAuthStatus()
    }
}
