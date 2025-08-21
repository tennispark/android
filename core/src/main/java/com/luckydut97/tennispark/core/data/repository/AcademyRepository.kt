package com.luckydut97.tennispark.core.data.repository

import com.luckydut97.tennispark.core.data.model.ApiResponse
import com.luckydut97.tennispark.core.data.model.AcademyListResponse
import com.luckydut97.tennispark.core.data.network.NetworkModule
import com.luckydut97.tennispark.core.data.storage.TokenManagerImpl
import com.google.gson.Gson
import android.util.Log

class AcademyRepository {

    private val tag = "🔍 디버깅: AcademyRepository"
    private val apiService = NetworkModule.apiService

    suspend fun getAcademies(): ApiResponse<AcademyListResponse> {
        // 🔥 토큰 상태 확인
        val context = NetworkModule.getContext()
        if (context != null) {
            val tokenManager = TokenManagerImpl(context)
            val accessToken = tokenManager.getAccessToken()
            val refreshToken = tokenManager.getRefreshToken()
        }

        return try {
            val response = apiService.getAcademies()

            if (response.isSuccessful) {
                val body = response.body()

                if (body?.success == true && body.response != null) {
                    body.response.academies.forEachIndexed { index, academy ->
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

                // 서버에서 온 실제 에러 메시지 파싱 시도
                var serverErrorMessage: String? = null
                try {
                    if (errorBody != null) {
                        val gson = Gson()
                        val errorResponse = gson.fromJson(
                            errorBody,
                            com.luckydut97.tennispark.core.data.model.ApiResponse::class.java
                        )
                        serverErrorMessage = errorResponse?.error?.message
                    }
                } catch (e: Exception) {
                    // JSON 파싱 실패 시 무시
                }

                ApiResponse(
                    success = false,
                    response = null,
                    error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                        status = response.code(),
                        message = serverErrorMessage ?: when (response.code()) {
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

    suspend fun applyForAcademy(academyId: Long): ApiResponse<Any> {

        return try {

            val response = apiService.applyForAcademy(academyId)


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
                        Log.d(tag, "applyForAcademy errorBody: $errorBody")
                        val gson = Gson()
                        val errorResponse = gson.fromJson(
                            errorBody,
                            com.luckydut97.tennispark.core.data.model.ApiResponse::class.java
                        )
                        Log.d(tag, "applyForAcademy errorResponse: $errorResponse")
                        serverErrorMessage = errorResponse?.error?.message
                    }
                } catch (e: Exception) {
                    Log.e(tag, "applyForAcademy 서버 errorBody 파싱 실패: ${e.message}", e)
                }

                val finalMessage = serverErrorMessage ?: when (response.code()) {
                    400 -> {
                        // 400 에러는 서버 메시지를 우선 사용 (중복 신청 등)
                        if (serverErrorMessage?.contains("이미") == true) {
                            "이미 신청한 아카데미입니다."
                        } else if (serverErrorMessage?.contains("인원") == true) {
                            "신청 인원이 초과되었습니다."
                        } else {
                            serverErrorMessage ?: "아카데미 신청에 실패했습니다."
                        }
                    }
                    401 -> "인증이 되지 않았습니다."
                    404 -> "해당 아카데미를 찾을 수 없습니다."
                    else -> "서버 오류가 발생했습니다."
                }

                Log.d(tag, "applyForAcademy 최종 에러 메시지: $finalMessage")
                Log.d(tag, "applyForAcademy serverErrorMessage: $serverErrorMessage")
                Log.d(tag, "applyForAcademy 상태 코드: ${response.code()}")

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