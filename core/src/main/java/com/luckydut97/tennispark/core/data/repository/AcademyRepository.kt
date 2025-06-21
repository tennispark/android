package com.luckydut97.tennispark.core.data.repository

import android.util.Log
import com.luckydut97.tennispark.core.data.model.ApiResponse
import com.luckydut97.tennispark.core.data.model.AcademyListResponse
import com.luckydut97.tennispark.core.data.network.NetworkModule
import com.luckydut97.tennispark.core.data.storage.TokenManagerImpl

class AcademyRepository {

    private val tag = "🔍 디버깅: AcademyRepository"
    private val apiService = NetworkModule.apiService

    suspend fun getAcademies(): ApiResponse<AcademyListResponse> {
        Log.d(tag, "=== 아카데미 목록 조회 API 호출 시작 ===")
        Log.d(tag, "Base URL: ${NetworkModule.getContext()?.let { "https://tennis-park.store/" }}")
        Log.d(tag, "Endpoint: GET /api/members/activities/academies")
        Log.d(tag, "HTTP Method: GET")

        // 🔥 토큰 상태 확인
        val context = NetworkModule.getContext()
        if (context != null) {
            val tokenManager = TokenManagerImpl(context)
            val accessToken = tokenManager.getAccessToken()
            val refreshToken = tokenManager.getRefreshToken()

            Log.d(tag, "🔑 토큰 상태 확인:")
            Log.d(tag, "  AccessToken 존재: ${accessToken != null}")
            Log.d(tag, "  AccessToken 길이: ${accessToken?.length ?: 0}")
            Log.d(tag, "  RefreshToken 존재: ${refreshToken != null}")

            if (accessToken != null) {
                Log.d(tag, "  AccessToken 앞 20자: ${accessToken.take(20)}...")
                Log.d(tag, "  Authorization 헤더: Bearer $accessToken")
            } else {
                Log.e(tag, "  ❌ AccessToken이 null입니다!")
            }
        } else {
            Log.e(tag, "  ❌ Context가 null입니다!")
        }

        return try {
            Log.d(tag, "🔄 Retrofit API 호출 시작...")

            val response = apiService.getAcademies()

            Log.d(tag, "📡 API 응답 수신:")
            Log.d(tag, "  HTTP Status Code: ${response.code()}")
            Log.d(tag, "  HTTP Status Message: ${response.message()}")
            Log.d(tag, "  Response Headers: ${response.headers()}")

            if (response.isSuccessful) {
                val body = response.body()
                Log.d(tag, "✅ 아카데미 목록 조회 API 호출 성공!")
                Log.d(tag, "📄 Response Body: $body")

                if (body?.success == true && body.response != null) {
                    Log.d(tag, "📊 아카데미 데이터:")
                    Log.d(tag, "  아카데미 개수: ${body.response.academies.size}")
                    body.response.academies.forEachIndexed { index, academy ->
                        Log.d(
                            tag,
                            "  아카데미 ${index + 1}: ID=${academy.id}, 날짜=${academy.date}, 타입=${academy.lessonType}"
                        )
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
                Log.e(tag, "❌ 아카데미 목록 조회 API 호출 실패!")
                Log.e(tag, "  Error Code: ${response.code()}")
                Log.e(tag, "  Error Message: ${response.message()}")
                Log.e(tag, "  Error Body: $errorBody")

                // 🔥 401 오류 상세 분석
                if (response.code() == 401) {
                    Log.e(tag, "🚨 인증 오류 상세 분석:")
                    Log.e(tag, "  - Authorization 헤더가 제대로 전송되지 않았거나")
                    Log.e(tag, "  - 토큰이 만료되었거나")
                    Log.e(tag, "  - 토큰 형식이 잘못되었습니다")
                    Log.e(tag, "  - Bearer 접두사가 누락되었을 수 있습니다")
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
            Log.e(tag, "💥 네트워크 예외 발생: ${e.message}", e)
            Log.e(tag, "  예외 타입: ${e.javaClass.simpleName}")
            Log.e(tag, "  스택 트레이스: ${e.stackTrace.take(3).joinToString()}")

            ApiResponse(
                success = false,
                response = null,
                error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                    status = 0,
                    message = "네트워크 오류: ${e.message}"
                )
            )
        } finally {
            Log.d(tag, "=== 아카데미 목록 조회 API 호출 완료 ===")
        }
    }

    suspend fun applyForAcademy(academyId: Long): ApiResponse<Any> {
        Log.d(tag, "=== 아카데미 신청 API 호출 시작 ===")
        Log.d(tag, "Academy ID: $academyId")
        Log.d(tag, "Endpoint: POST /api/members/activities/academies/$academyId/apply")
        Log.d(tag, "HTTP Method: POST")

        return try {
            Log.d(tag, "🔄 Retrofit API 호출 시작...")

            val response = apiService.applyForAcademy(academyId)

            Log.d(tag, "📡 API 응답 수신:")
            Log.d(tag, "  HTTP Status Code: ${response.code()}")
            Log.d(tag, "  HTTP Status Message: ${response.message()}")

            if (response.isSuccessful) {
                val body = response.body()
                Log.d(tag, "✅ 아카데미 신청 API 호출 성공!")
                Log.d(tag, "📄 Response Body: $body")

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
                Log.e(tag, "❌ 아카데미 신청 API 호출 실패!")
                Log.e(tag, "  Error Code: ${response.code()}")
                Log.e(tag, "  Error Message: ${response.message()}")
                Log.e(tag, "  Error Body: $errorBody")

                // 서버 에러 응답 구조에 맞춰 처리
                ApiResponse(
                    success = false,
                    response = null,
                    error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                        status = response.code(),
                        message = when (response.code()) {
                            400 -> "신청 인원이 초과되었습니다."
                            401 -> "인증이 되지 않았습니다."
                            404 -> "해당 아카데미를 찾을 수 없습니다."
                            else -> "서버 오류가 발생했습니다."
                        }
                    )
                )
            }
        } catch (e: Exception) {
            Log.e(tag, "💥 네트워크 예외 발생: ${e.message}", e)
            ApiResponse(
                success = false,
                response = null,
                error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                    status = 0,
                    message = "네트워크 오류: ${e.message}"
                )
            )
        } finally {
            Log.d(tag, "=== 아카데미 신청 API 호출 완료 ===")
        }
    }
}