package com.luckydut97.tennispark.core.data.network

import com.luckydut97.tennispark.core.data.storage.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthInterceptor(
    private val tokenManager: TokenManager
) : Interceptor {

    private val tag = "🔍 디버깅: AuthInterceptor"

    // 재시도 횟수 추적을 위한 ThreadLocal
    private val retryAttempts = ThreadLocal<Int>()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // 재시도 카운트 초기화
        val retryCount = retryAttempts.get() ?: 0
        if (retryCount == 0) {
            retryAttempts.set(0)
        }


        // 토큰이 필요 없는 API들 (인증, 회원가입 등)
        val skipAuthUrls = listOf(
            "/api/members/auth/phones/code",
            "/api/members/auth/phones/code/verify",
            "/api/members/auth/token/refresh",
            "/api/members" // 회원가입만 정확히 매치
        )

        val shouldSkipAuth = skipAuthUrls.any { url ->
            originalRequest.url.encodedPath == url
        }


        if (shouldSkipAuth) {
            retryAttempts.remove() // ThreadLocal 정리
            return chain.proceed(originalRequest)
        }

        // 액세스 토큰 가져오기
        val accessToken = runBlocking { tokenManager.getAccessToken() }


        if (accessToken.isNullOrEmpty()) {
            retryAttempts.remove() // ThreadLocal 정리
            return chain.proceed(originalRequest)
        }


        // Authorization 헤더 추가
        val authenticatedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()


        val response = chain.proceed(authenticatedRequest)


        // 401 응답 시 토큰 재발급 시도
        if (response.code == 401) {
            response.close()

            val currentRetryCount = retryAttempts.get() ?: 0

            // 재시도 횟수 초과 시 로그아웃
            if (currentRetryCount >= 1) {
                retryAttempts.remove() // ThreadLocal 정리
                runBlocking { tokenManager.clearTokens() }
                return chain.proceed(originalRequest)
            }

            // 재시도 카운트 증가
            retryAttempts.set(currentRetryCount + 1)

            return try {
                val refreshToken = runBlocking { tokenManager.getRefreshToken() }

                if (refreshToken.isNullOrEmpty()) {
                    retryAttempts.remove() // ThreadLocal 정리
                    return chain.proceed(originalRequest)
                }

                // 토큰 재발급 API 호출
                val refreshRequest = originalRequest.newBuilder()
                    .url("${originalRequest.url.scheme}://${originalRequest.url.host}:${originalRequest.url.port}/api/members/auth/token/refresh")
                    .post(okhttp3.RequestBody.create(null, ""))
                    .header("Refresh-Token", refreshToken)
                    .build()

                val refreshResponse = chain.proceed(refreshRequest)

                if (refreshResponse.isSuccessful) {

                    // 응답에서 새 토큰 파싱하여 저장
                    try {
                        val responseBody = refreshResponse.body?.string()

                        if (responseBody != null) {
                            val gson = com.google.gson.Gson()
                            val apiResponse = gson.fromJson(
                                responseBody,
                                com.luckydut97.tennispark.core.data.model.ApiResponse::class.java
                            )

                            if (apiResponse.success && apiResponse.response != null) {
                                val tokenResponseMap = apiResponse.response as? Map<*, *>
                                val newAccessToken = tokenResponseMap?.get("accessToken") as? String
                                val newRefreshToken =
                                    tokenResponseMap?.get("refreshToken") as? String

                                if (newAccessToken != null && newRefreshToken != null) {
                                    runBlocking {
                                        tokenManager.saveTokens(newAccessToken, newRefreshToken)
                                    }
                                }
                            }
                        }
                    } catch (e: Exception) {
                    }

                    refreshResponse.close()

                    val newAccessToken = runBlocking { tokenManager.getAccessToken() }
                    val retryRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer $newAccessToken")
                        .build()

                    val finalResponse = chain.proceed(retryRequest)
                    retryAttempts.remove() // ThreadLocal 정리
                    return finalResponse
                } else {
                    refreshResponse.close()
                    retryAttempts.remove() // ThreadLocal 정리
                    // 로그아웃 처리
                    runBlocking { tokenManager.clearTokens() }
                    return chain.proceed(originalRequest)
                }
            } catch (e: Exception) {
                retryAttempts.remove() // ThreadLocal 정리
                return chain.proceed(originalRequest)
            }
        }

        // 정상 응답 시 ThreadLocal 정리
        retryAttempts.remove()
        return response
    }
}
