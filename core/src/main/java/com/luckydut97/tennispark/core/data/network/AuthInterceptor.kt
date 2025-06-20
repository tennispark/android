package com.luckydut97.tennispark.core.data.network

import android.util.Log
import com.luckydut97.tennispark.core.data.storage.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthInterceptor(
    private val tokenManager: TokenManager
) : Interceptor {

    private val tag = "🔍 디버깅: AuthInterceptor"

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // 토큰이 필요 없는 API들 (인증, 회원가입 등)
        val skipAuthUrls = listOf(
            "api/members/auth/phones/code",
            "api/members/auth/phones/code/verify",
            "api/members/auth/token/refresh",
            "api/members" // 회원가입
        )

        val shouldSkipAuth = skipAuthUrls.any { url ->
            originalRequest.url.encodedPath.contains(url)
        }

        if (shouldSkipAuth) {
            Log.d(tag, "인증 헤더 생략: ${originalRequest.url}")
            return chain.proceed(originalRequest)
        }

        // 액세스 토큰 가져오기
        val accessToken = runBlocking { tokenManager.getAccessToken() }

        if (accessToken.isNullOrEmpty()) {
            Log.d(tag, "액세스 토큰이 없음 - 헤더 추가 생략")
            return chain.proceed(originalRequest)
        }

        // Authorization 헤더 추가
        val authenticatedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

        Log.d(tag, "Authorization 헤더 추가: ${authenticatedRequest.url}")

        val response = chain.proceed(authenticatedRequest)

        // 401 응답 시 토큰 재발급 시도
        if (response.code == 401) {
            Log.d(tag, "401 응답 받음 - 토큰 재발급 시도")
            response.close()

            return try {
                val refreshToken = runBlocking { tokenManager.getRefreshToken() }

                if (refreshToken.isNullOrEmpty()) {
                    Log.e(tag, "리프레시 토큰이 없음 - 재발급 불가")
                    return chain.proceed(originalRequest)
                }

                // 토큰 재발급 API 호출
                val refreshRequest = originalRequest.newBuilder()
                    .url("${originalRequest.url.scheme}://${originalRequest.url.host}:${originalRequest.url.port}/api/members/auth/token/refresh")
                    .post(okhttp3.RequestBody.create(null, ""))
                    .header("Refresh-Token", refreshToken)
                    .build()

                Log.d(tag, "토큰 재발급 API 호출")
                val refreshResponse = chain.proceed(refreshRequest)

                if (refreshResponse.isSuccessful) {
                    Log.d(tag, "토큰 재발급 성공")

                    // TODO: 응답에서 새 토큰 파싱하여 저장
                    // 지금은 단순하게 원래 요청 재시도
                    refreshResponse.close()

                    val newAccessToken = runBlocking { tokenManager.getAccessToken() }
                    val retryRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer $newAccessToken")
                        .build()

                    Log.d(tag, "새 토큰으로 요청 재시도")
                    chain.proceed(retryRequest)
                } else {
                    Log.e(tag, "토큰 재발급 실패: ${refreshResponse.code}")
                    refreshResponse.close()
                    // 로그아웃 처리
                    runBlocking { tokenManager.clearTokens() }
                    chain.proceed(originalRequest)
                }
            } catch (e: Exception) {
                Log.e(tag, "토큰 재발급 중 예외: ${e.message}", e)
                chain.proceed(originalRequest)
            }
        }

        return response
    }
}