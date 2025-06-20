package com.luckydut97.tennispark.core.data.repository

import android.util.Log
import com.luckydut97.tennispark.core.data.model.ApiResponse
import com.luckydut97.tennispark.core.data.model.TokenResponse
import com.luckydut97.tennispark.core.data.network.ApiService
import com.luckydut97.tennispark.core.data.storage.TokenManager

interface AuthRepository {
    suspend fun refreshTokens(): ApiResponse<TokenResponse>
    suspend fun isLoggedIn(): Boolean
    suspend fun logout()
}

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) : AuthRepository {

    private val tag = "🔍 디버깅: AuthRepository"

    override suspend fun refreshTokens(): ApiResponse<TokenResponse> {
        Log.d(tag, "=== 토큰 재발급 시작 ===")

        val refreshToken = tokenManager.getRefreshToken()
        if (refreshToken == null) {
            Log.e(tag, "Refresh token이 없습니다.")
            return ApiResponse(success = false, error = "No refresh token")
        }

        return try {
            Log.d(tag, "토큰 재발급 API 호출...")
            val response = apiService.refreshToken(refreshToken)

            if (response.isSuccessful && response.body()?.success == true) {
                val tokenResponse = response.body()!!.response!!
                Log.d(tag, "토큰 재발급 성공!")

                tokenManager.saveTokens(
                    tokenResponse.accessToken,
                    tokenResponse.refreshToken
                )

                response.body()!!
            } else {
                Log.e(tag, "토큰 재발급 실패: ${response.code()}")
                ApiResponse(success = false, error = "Token refresh failed")
            }
        } catch (e: Exception) {
            Log.e(tag, "토큰 재발급 예외: ${e.message}", e)
            ApiResponse(success = false, error = e.message)
        }
    }

    override suspend fun isLoggedIn(): Boolean {
        return tokenManager.isLoggedIn()
    }

    override suspend fun logout() {
        Log.d(tag, "로그아웃 - 토큰 삭제")
        tokenManager.clearTokens()
    }
}