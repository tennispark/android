package com.luckydut97.tennispark.core.data.repository

import android.util.Log
import com.luckydut97.tennispark.core.data.model.ApiResponse
import com.luckydut97.tennispark.core.data.model.TokenResponse
import com.luckydut97.tennispark.core.data.model.ErrorResponse
import com.luckydut97.tennispark.core.data.model.UpdateFcmTokenRequest
import com.luckydut97.tennispark.core.data.network.ApiService
import com.luckydut97.tennispark.core.data.storage.TokenManager

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
        Log.d(tag, "=== 토큰 재발급 시작 ===")

        val refreshToken = tokenManager.getRefreshToken()
        if (refreshToken == null) {
            Log.e(tag, "Refresh token이 없습니다.")
            return ApiResponse(
                success = false,
                error = ErrorResponse(
                    status = 401,
                    message = "No refresh token"
                )
            )
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
                ApiResponse(
                    success = false,
                    error = ErrorResponse(
                        status = response.code(),
                        message = "Token refresh failed"
                    )
                )
            }
        } catch (e: Exception) {
            Log.e(tag, "토큰 재발급 예외: ${e.message}", e)
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
        Log.d(tag, "=== 로그아웃 API 호출 시작 ===")

        return try {
            val response = apiService.logout()

            if (response.isSuccessful) {
                Log.d(tag, "✅ 로그아웃 API 호출 성공")
                // 로컬 토큰 삭제
                tokenManager.clearTokens()
                Log.d(tag, "💾 로컬 토큰 삭제 완료")

                response.body() ?: ApiResponse(
                    success = true,
                    response = null,
                    error = null
                )
            } else {
                Log.e(tag, "❌ 로그아웃 API 호출 실패: ${response.code()}")
                // API 실패해도 로컬 토큰은 삭제
                tokenManager.clearTokens()
                Log.d(tag, "💾 로컬 토큰 삭제 완료 (API 실패했지만)")

                ApiResponse(
                    success = false,
                    error = ErrorResponse(
                        status = response.code(),
                        message = "로그아웃 처리 중 오류가 발생했습니다."
                    )
                )
            }
        } catch (e: Exception) {
            Log.e(tag, "🔥 로그아웃 예외: ${e.message}", e)
            // 예외 발생해도 로컬 토큰은 삭제
            tokenManager.clearTokens()
            Log.d(tag, "💾 로컬 토큰 삭제 완료 (예외 발생했지만)")

            ApiResponse(
                success = false,
                error = ErrorResponse(
                    status = 0,
                    message = "네트워크 오류가 발생했습니다."
                )
            )
        } finally {
            Log.d(tag, "=== 로그아웃 처리 완료 ===")
        }
    }

    override suspend fun withdraw(): ApiResponse<Any> {
        Log.d(tag, "🔍 디버깅: === 회원 탈퇴 API 호출 시작 ===")
        Log.d(tag, "🔍 디버깅: Base URL: https://tennis-park.store/")
        Log.d(tag, "🔍 디버깅: Endpoint: DELETE /api/members/me")
        Log.d(tag, "🔍 디버깅: HTTP Method: DELETE")

        return try {
            Log.d(tag, "🔍 디버깅: 🚀 Retrofit API 호출 시작...")
            val response = apiService.withdraw()

            Log.d(tag, "🔍 디버깅: 📊 HTTP Status Code: ${response.code()}")
            Log.d(tag, "🔍 디버깅: 📝 HTTP Status Message: ${response.message()}")
            Log.d(tag, "🔍 디버깅: 📋 Response Headers: ${response.headers()}")

            if (response.isSuccessful) {
                Log.d(tag, "🔍 디버깅: ✅ 회원 탈퇴 API 호출 성공")
                // 로컬 토큰 삭제
                tokenManager.clearTokens()
                Log.d(tag, "🔍 디버깅: 💾 로컬 토큰 삭제 완료")

                response.body() ?: ApiResponse(
                    success = true,
                    response = null,
                    error = null
                )
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(tag, "🔍 디버깅: ❌ 회원 탈퇴 API 호출 실패: ${response.code()}")
                Log.e(tag, "🔍 디버깅: 🔥 Error Message: ${response.message()}")
                Log.e(tag, "🔍 디버깅: 🔥 Error Body: $errorBody")

                // API 실패해도 로컬 토큰은 삭제
                tokenManager.clearTokens()
                Log.d(tag, "🔍 디버깅: 💾 로컬 토큰 삭제 완료 (API 실패했지만)")

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
            Log.e(tag, "🔍 디버깅: 🔥 회원 탈퇴 예외: ${e.message}", e)
            Log.e(tag, "🔍 디버깅: 예외 타입: ${e.javaClass.simpleName}")

            // 예외 발생해도 로컬 토큰은 삭제
            tokenManager.clearTokens()
            Log.d(tag, "🔍 디버깅: 💾 로컬 토큰 삭제 완료 (예외 발생했지만)")

            ApiResponse(
                success = false,
                error = ErrorResponse(
                    status = 0,
                    message = "네트워크 오류가 발생했습니다."
                )
            )
        } finally {
            Log.d(tag, "🔍 디버깅: === 회원 탈퇴 처리 완료 ===")
        }
    }

    override suspend fun updateFcmToken(fcmToken: String): ApiResponse<Any> {
        Log.d(tag, "디버깅: === FCM 토큰 업데이트 시작 ===")
        Log.d(tag, "디버깅: 서버 전송용 FCM 토큰: $fcmToken (길이: ${fcmToken.length})")

        return try {
            val request = UpdateFcmTokenRequest(fcmToken)
            Log.d(tag, "디버깅: API 호출 준비 완료. Request Body: $request")
            val response = apiService.updateFcmToken(request)

            Log.d(tag, "디버깅: 서버 응답 코드: ${response.code()}")
            Log.d(tag, "디버깅: 서버 응답 바디: ${response.body()}")

            if (response.isSuccessful && response.body()?.success == true) {
                Log.d(tag, "✅ 디버깅: FCM 토큰 업데이트 성공"); response.body()!!
            } else {
                Log.e(tag, "❌ 디버깅: FCM 토큰 업데이트 실패, 코드: ${response.code()}")
                ApiResponse(
                    success = false,
                    error = ErrorResponse(
                        status = response.code(),
                        message = "FCM 토큰 업데이트 실패"
                    )
                )
            }
        } catch (e: Exception) {
            Log.e(tag, "🔥 디버깅: FCM 토큰 업데이트 예외: ${e.message}", e)
            ApiResponse(
                success = false,
                error = ErrorResponse(
                    status = 0,
                    message = "네트워크 오류가 발생했습니다. (${e.message})"
                )
            )
        } finally {
            Log.d(tag, "디버깅: === FCM 토큰 업데이트 완료 ===")
        }
    }
}
