package com.luckydut97.tennispark.core.data.repository

import com.luckydut97.tennispark.core.data.model.ApiResponse
import com.luckydut97.tennispark.core.data.model.ActivityListResponse
import com.luckydut97.tennispark.core.data.network.NetworkModule
import com.luckydut97.tennispark.core.data.storage.TokenManagerImpl
import com.google.gson.Gson
import android.util.Log

class WeeklyActivityRepository {

    private val tag = "🔍 디버깅: WeeklyActivityRepository"
    private val apiService = NetworkModule.apiService

    suspend fun getActivities(): ApiResponse<ActivityListResponse> {
        // 🔥 토큰 상태 확인
        val context = NetworkModule.getContext()
        if (context != null) {
            val tokenManager = TokenManagerImpl(context)
            val accessToken = tokenManager.getAccessToken()
            val refreshToken = tokenManager.getRefreshToken()
        }

        return try {
            val response = apiService.getActivities()

            if (response.isSuccessful) {
                val body = response.body()

                if (body?.success == true && body.response != null) {
                    body.response.activities.forEachIndexed { index, activity ->
                    }
                }

                body ?: ApiResponse(
                    success = false,
                    response = null,
                    error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                        status = 500,
                        message = "응답 본문이 비어있습니다."
                    )
                )
            } else {
                val errorBody = response.errorBody()?.string()

                // 🔥 401 오류 상세 분석
                if (response.code() == 401) {
                }

                ApiResponse(
                    success = false,
                    response = null,
                    error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                        status = response.code(),
                        message = when (response.code()) {
                            401 -> "인증이 되지 않았습니다."
                            else -> "서버 오류가 발생했습니다."
                        }
                    )
                )
            }
        } catch (e: Exception) {

            ApiResponse(
                success = false,
                response = null,
                error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                    status = 0,
                    message = "네트워크 오류: ${e.message}"
                )
            )
        } finally {
        }
    }

    suspend fun applyForActivity(activityId: Long): ApiResponse<Any> {

        return try {

            val response = apiService.applyForActivity(activityId)


            if (response.isSuccessful) {
                val body = response.body()

                body ?: ApiResponse(
                    success = false,
                    response = null,
                    error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                        status = 500,
                        message = "응답 본문이 비어있습니다."
                    )
                )
            } else {
                val errorBody = response.errorBody()?.string()

                // 서버에서 온 실제 에러 메시지 파싱 시도
                var serverErrorMessage: String? = null
                try {
                    if (errorBody != null) {
                        Log.d(tag, "applyForActivity errorBody: $errorBody")
                        val gson = Gson()
                        val errorResponse = gson.fromJson(
                            errorBody,
                            com.luckydut97.tennispark.core.data.model.ApiResponse::class.java
                        )
                        Log.d(tag, "applyForActivity errorResponse: $errorResponse")
                        serverErrorMessage = errorResponse?.error?.message
                    }
                } catch (e: Exception) {
                    Log.e(tag, "applyForActivity 서버 errorBody 파싱 실패: ${e.message}", e)
                }

                val finalMessage = serverErrorMessage ?: when (response.code()) {
                    400 -> "신청 인원이 초과되었습니다."
                    401 -> "인증이 되지 않았습니다."
                    404 -> "해당 활동을 찾을 수 없습니다."
                    else -> "서버 오류가 발생했습니다."
                }
                Log.d(tag, "applyForActivity 최종 에러 메시지: $finalMessage")
                Log.d(tag, "applyForActivity serverErrorMessage: $serverErrorMessage")
                Log.d(tag, "applyForActivity 상태 코드: ${response.code()}")

                // 서버 에러 응답 구조에 맞춰 처리
                ApiResponse(
                    success = false,
                    response = null,
                    error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                        status = response.code(),
                        message = finalMessage
                    )
                )
            }
        } catch (e: Exception) {
            ApiResponse(
                success = false,
                response = null,
                error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                    status = 0,
                    message = "네트워크 오류: ${e.message}"
                )
            )
        } finally {
        }
    }
}
