package com.luckydut97.tennispark.core.data.repository

import com.luckydut97.tennispark.core.data.model.ApiResponse
import com.luckydut97.tennispark.core.data.model.TokenResponse
import com.luckydut97.tennispark.core.data.model.ErrorResponse
import com.luckydut97.tennispark.core.data.model.UpdateFcmTokenRequest
import com.luckydut97.tennispark.core.data.network.ApiService
import com.luckydut97.tennispark.core.data.storage.TokenManager
import android.util.Log

interface AuthRepository {
    suspend fun refreshTokens(): ApiResponse<TokenResponse>
    suspend fun isLoggedIn(): Boolean
    suspend fun logout(): ApiResponse<Any>
    suspend fun withdraw(): ApiResponse<Any>
    suspend fun updateFcmToken(fcmToken: String): ApiResponse<Any>
}

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) : AuthRepository {

    private val tag = "🔍 디버깅: AuthRepository"

    override suspend fun refreshTokens(): ApiResponse<TokenResponse> {

        val refreshToken = tokenManager.getRefreshToken()
        if (refreshToken == null) {
            return ApiResponse(
                success = false,
                error = ErrorResponse(
                    status = 401,
                    message = "No refresh token"
                )
            )
        }

        return try {
            val response = apiService.refreshToken(refreshToken)

            if (response.isSuccessful && response.body()?.success == true) {
                val tokenResponse = response.body()!!.response!!

                tokenManager.saveTokens(
                    tokenResponse.accessToken,
                    tokenResponse.refreshToken
                )

                response.body()!!
            } else {
                ApiResponse(
                    success = false,
                    error = ErrorResponse(
                        status = response.code(),
                        message = "Token refresh failed"
                    )
                )
            }
        } catch (e: Exception) {
            ApiResponse(
                success = false,
                error = ErrorResponse(
                    status = 0,
                    message = e.message ?: "Unknown error"
                )
            )
        }
    }

    override suspend fun isLoggedIn(): Boolean {
        return tokenManager.isLoggedIn()
    }

    override suspend fun logout(): ApiResponse<Any> {

        return try {
            val response = apiService.logout()

            if (response.isSuccessful) {
                // 로컬 토큰 삭제
                tokenManager.clearTokens()

                response.body() ?: ApiResponse(
                    success = true,
                    response = null,
                    error = null
                )
            } else {
                // API 실패해도 로컬 토큰은 삭제
                tokenManager.clearTokens()

                ApiResponse(
                    success = false,
                    error = ErrorResponse(
                        status = response.code(),
                        message = "로그아웃 처리 중 오류가 발생했습니다."
                    )
                )
            }
        } catch (e: Exception) {
            // 예외 발생해도 로컬 토큰은 삭제
            tokenManager.clearTokens()

            ApiResponse(
                success = false,
                error = ErrorResponse(
                    status = 0,
                    message = "네트워크 오류가 발생했습니다."
                )
            )
        } finally {
        }
    }

    override suspend fun withdraw(): ApiResponse<Any> {
        Log.d(tag, "[withdraw] called")

        return try {
            val response = apiService.withdraw()
            Log.d(
                tag,
                "[withdraw] apiService.withdraw() result: isSuccessful=${response.isSuccessful}, code=${response.code()}"
            )

            if (response.isSuccessful) {
                // 로컬 토큰 삭제
                tokenManager.clearTokens()
                Log.d(tag, "[withdraw] withdraw successful - tokens cleared locally")

                response.body() ?: ApiResponse(
                    success = true,
                    response = null,
                    error = null
                )
            } else {
                val errorBody = response.errorBody()?.string()
                Log.d(
                    tag,
                    "[withdraw] withdraw failed - code=${response.code()}, errorBody=$errorBody"
                )

                // API 실패해도 로컬 토큰은 삭제
                tokenManager.clearTokens()
                Log.d(tag, "[withdraw] withdraw failed - tokens cleared locally")

                ApiResponse(
                    success = false,
                    error = ErrorResponse(
                        status = response.code(),
                        message = when (response.code()) {
                            401 -> "인증이 되지 않았습니다."
                            404 -> "회원 정보를 찾을 수 없습니다."
                            else -> "회원 탈퇴 처리 중 오류가 발생했습니다."
                        }
                    )
                )
            }
        } catch (e: Exception) {
            Log.d(tag, "[withdraw] Exception: ${e.message}", e)
            // 예외 발생해도 로컬 토큰은 삭제
            tokenManager.clearTokens()
            Log.d(tag, "[withdraw] Exception - tokens cleared locally")

            ApiResponse(
                success = false,
                error = ErrorResponse(
                    status = 0,
                    message = "네트워크 오류가 발생했습니다."
                )
            )
        } finally {
        }
    }

    override suspend fun updateFcmToken(fcmToken: String): ApiResponse<Any> {

        return try {
            val request = UpdateFcmTokenRequest(fcmToken)
            val response = apiService.updateFcmToken(request)

            if (response.isSuccessful && response.body()?.success == true) {
                response.body()!!
            } else {
                ApiResponse(
                    success = false,
                    error = ErrorResponse(
                        status = response.code(),
                        message = "FCM 토큰 업데이트 실패"
                    )
                )
            }
        } catch (e: Exception) {
            ApiResponse(
                success = false,
                error = ErrorResponse(
                    status = 0,
                    message = "네트워크 오류가 발생했습니다. (${e.message})"
                )
            )
        } finally {
        }
    }
}
