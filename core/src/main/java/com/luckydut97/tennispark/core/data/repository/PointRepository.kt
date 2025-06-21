package com.luckydut97.tennispark.core.data.repository

import android.util.Log
import com.luckydut97.tennispark.core.data.model.ApiResponse
import com.luckydut97.tennispark.core.data.model.PointHistoryItem
import com.luckydut97.tennispark.core.data.network.NetworkModule

class PointRepository {

    private val tag = "🔍 디버깅: PointRepository"
    private val apiService = NetworkModule.apiService

    suspend fun postQrEvent(eventUrl: String): ApiResponse<Any> {
        Log.d(tag, "=== QR 이벤트 처리 API 호출 시작 ===")
        Log.d(tag, "Event URL: $eventUrl")
        Log.d(tag, "HTTP Method: POST")

        return try {
            Log.d(tag, "🚀 Retrofit API 호출 시작...")

            val response = apiService.postQrEvent(eventUrl)

            Log.d(tag, "📊 HTTP Status Code: ${response.code()}")
            Log.d(tag, "📝 HTTP Status Message: ${response.message()}")

            if (response.isSuccessful) {
                val body = response.body()
                Log.d(tag, "✅ QR 이벤트 API 호출 성공!")
                Log.d(tag, "📦 Response Body: $body")

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
                Log.e(tag, "❌ QR 이벤트 API 호출 실패!")
                Log.e(tag, "🔥 Error Code: ${response.code()}")
                Log.e(tag, "🔥 Error Message: ${response.message()}")
                Log.e(tag, "🔥 Error Body: $errorBody")

                // 서버에서 온 실제 에러 메시지 파싱
                val actualErrorMessage = try {
                    if (errorBody != null) {
                        val gson = com.google.gson.Gson()
                        val errorResponse = gson.fromJson(
                            errorBody,
                            com.luckydut97.tennispark.core.data.model.ApiResponse::class.java
                        )
                        errorResponse.error?.message
                    } else null
                } catch (e: Exception) {
                    null
                }

                ApiResponse(
                    success = false,
                    response = null,
                    error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                        status = response.code(),
                        message = when (response.code()) {
                            400 -> actualErrorMessage ?: "잘못된 요청입니다."
                            401 -> "인증이 되지 않았습니다."
                            404 -> "해당 이벤트를 찾을 수 없습니다."
                            else -> "서버 오류가 발생했습니다."
                        }
                    )
                )
            }
        } catch (e: Exception) {
            Log.e(tag, "🔥 네트워크 예외 발생: ${e.message}", e)
            ApiResponse(
                success = false,
                response = null,
                error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                    status = 0,
                    message = "네트워크 오류: ${e.message}"
                )
            )
        } finally {
            Log.d(tag, "=== QR 이벤트 API 호출 완료 ===")
        }
    }

    suspend fun getMyPoints(): ApiResponse<com.luckydut97.tennispark.core.data.model.MyPointResponse> {
        Log.d(tag, "=== 내 포인트 조회 API 호출 시작 ===")
        Log.d(tag, "Endpoint: GET /api/members/points/me")
        Log.d(tag, "HTTP Method: GET")

        return try {
            Log.d(tag, "🚀 Retrofit API 호출 시작...")

            val response = apiService.getMyPoints()

            Log.d(tag, "📡 API 응답 수신:")
            Log.d(tag, "  HTTP Status Code: ${response.code()}")
            Log.d(tag, "  HTTP Status Message: ${response.message()}")

            if (response.isSuccessful) {
                val body = response.body()
                Log.d(tag, "✅ 내 포인트 조회 API 호출 성공!")
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
                Log.e(tag, "❌ 내 포인트 조회 API 호출 실패!")
                Log.e(tag, "  Error Code: ${response.code()}")
                Log.e(tag, "  Error Message: ${response.message()}")
                Log.e(tag, "  Error Body: $errorBody")

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
            ApiResponse(
                success = false,
                response = null,
                error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                    status = 0,
                    message = "네트워크 오류: ${e.message}"
                )
            )
        } finally {
            Log.d(tag, "=== 내 포인트 조회 API 호출 완료 ===")
        }
    }

    suspend fun getPointHistories(): ApiResponse<com.luckydut97.tennispark.core.data.model.PointHistoryListResponse> {
        Log.d(tag, "=== 포인트 내역 조회 API 호출 시작 ===")
        Log.d(tag, "Endpoint: GET /api/members/points/me/history")
        Log.d(tag, "HTTP Method: GET")

        return try {
            Log.d(tag, "🚀 Retrofit API 호출 시작...")

            val response = apiService.getPointHistories()

            Log.d(tag, "📡 API 응답 수신:")
            Log.d(tag, "  HTTP Status Code: ${response.code()}")
            Log.d(tag, "  HTTP Status Message: ${response.message()}")

            if (response.isSuccessful) {
                val body = response.body()
                Log.d(tag, "✅ 포인트 내역 조회 API 호출 성공!")
                Log.d(tag, "📄 Response Body: $body")

                if (body?.success == true && body.response != null) {
                    Log.d(tag, "📊 포인트 내역 데이터:")
                    Log.d(tag, "  내역 개수: ${body.response.histories.size}")
                    body.response.histories.forEachIndexed { index, history ->
                        Log.d(
                            tag,
                            "  내역 ${index + 1}: ID=${history.historyId}, 제목=${history.title}, 포인트=${history.point}, 타입=${history.type}"
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
                Log.e(tag, "❌ 포인트 내역 조회 API 호출 실패!")
                Log.e(tag, "  Error Code: ${response.code()}")
                Log.e(tag, "  Error Message: ${response.message()}")
                Log.e(tag, "  Error Body: $errorBody")

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
            ApiResponse(
                success = false,
                response = null,
                error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                    status = 0,
                    message = "네트워크 오류: ${e.message}"
                )
            )
        } finally {
            Log.d(tag, "=== 포인트 내역 조회 API 호출 완료 ===")
        }
    }
}
