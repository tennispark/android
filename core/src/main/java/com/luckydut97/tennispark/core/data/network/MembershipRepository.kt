package com.luckydut97.tennispark.core.data.network

import android.util.Log
import com.luckydut97.tennispark.core.data.model.ApiResponse
import com.luckydut97.tennispark.core.data.model.MemberRegistrationRequest

class MembershipRepository {

    private val tag = "🔍 디버깅: MembershipRepository"
    private val apiService = NetworkModule.apiService

    suspend fun registerMember(request: MemberRegistrationRequest): ApiResponse<Any> {
        Log.d(tag, "=== 실제 API 호출 시작 ===")
        Log.d(tag, "Base URL: $BASE_URL")
        Log.d(tag, "Endpoint: $REGISTER_MEMBER_ENDPOINT")
        Log.d(tag, "Full URL: $BASE_URL$REGISTER_MEMBER_ENDPOINT")
        Log.d(tag, "HTTP Method: POST")
        Log.d(tag, "Content-Type: application/json")

        Log.d(tag, "Request Data:")
        Log.d(tag, "  phoneNumber: ${request.phoneNumber}")
        Log.d(tag, "  name: ${request.name}")
        Log.d(tag, "  gender: ${request.gender}")
        Log.d(tag, "  tennisCareer: ${request.tennisCareer}")
        Log.d(tag, "  year: ${request.year}")
        Log.d(tag, "  registrationSource: ${request.registrationSource}")
        Log.d(tag, "  recommender: ${request.recommender}")
        Log.d(tag, "  instagramId: ${request.instagramId}")

        return try {
            Log.d(tag, "Retrofit API 호출 시작...")

            val response = apiService.registerMember(request)

            Log.d(tag, "HTTP Status Code: ${response.code()}")
            Log.d(tag, "HTTP Status Message: ${response.message()}")
            Log.d(tag, "Response Headers: ${response.headers()}")

            if (response.isSuccessful) {
                val body = response.body()
                Log.d(tag, "API 호출 성공!")
                Log.d(tag, "Response Body: $body")

                body ?: ApiResponse(
                    success = false,
                    response = null,
                    error = "응답 본문이 비어있습니다."
                )
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(tag, "API 호출 실패!")
                Log.e(tag, "Error Code: ${response.code()}")
                Log.e(tag, "Error Message: ${response.message()}")
                Log.e(tag, "Error Body: $errorBody")

                ApiResponse(
                    success = false,
                    response = null,
                    error = "서버 오류: ${response.code()} - ${response.message()}"
                )
            }
        } catch (e: Exception) {
            Log.e(tag, "네트워크 예외 발생: ${e.message}", e)
            ApiResponse(
                success = false,
                response = null,
                error = "네트워크 오류: ${e.message}"
            )
        } finally {
            Log.d(tag, "=== 실제 API 호출 완료 ===")
        }
    }

    companion object {
        const val BASE_URL = "http://3.34.83.48:8080/"
        const val REGISTER_MEMBER_ENDPOINT = "api/members"
    }
}
