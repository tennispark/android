package com.luckydut97.tennispark.core.data.repository

import android.util.Log
import com.luckydut97.tennispark.core.data.model.ApiResponse
import com.luckydut97.tennispark.core.data.model.PhoneVerificationRequest
import com.luckydut97.tennispark.core.data.model.PhoneVerificationCodeRequest
import com.luckydut97.tennispark.core.data.model.PhoneVerificationResponse
import com.luckydut97.tennispark.core.data.network.NetworkModule

class PhoneVerificationRepository {

    private val tag = "🔍 디버깅: PhoneVerificationRepo"
    private val apiService = NetworkModule.apiService

    suspend fun requestVerificationCode(phoneNumber: String): ApiResponse<Any> {
        Log.d(tag, "=== 실제 인증번호 요청 API 호출 시작 ===")
        Log.d(tag, "입력받은 전화번호: $phoneNumber")
        Log.d(tag, "Base URL: $BASE_URL")
        Log.d(tag, "Endpoint: $PHONE_VERIFICATION_ENDPOINT")
        Log.d(tag, "Full URL: $BASE_URL$PHONE_VERIFICATION_ENDPOINT")
        Log.d(tag, "HTTP Method: POST")
        Log.d(tag, "Content-Type: application/json")

        val request = PhoneVerificationRequest(phoneNumber = phoneNumber)

        Log.d(tag, "Request Body 생성:")
        Log.d(tag, "  phoneNumber: ${request.phoneNumber}")
        Log.d(tag, "Request Body JSON: {\"phoneNumber\": \"${request.phoneNumber}\"}")

        return try {
            Log.d(tag, "Retrofit API 호출 시작...")

            val response = apiService.requestPhoneVerification(request)

            Log.d(tag, "HTTP Status Code: ${response.code()}")
            Log.d(tag, "HTTP Status Message: ${response.message()}")
            Log.d(tag, "Response Headers: ${response.headers()}")

            if (response.isSuccessful) {
                val body = response.body()
                Log.d(tag, "인증번호 요청 API 호출 성공!")
                Log.d(tag, "Response Body: $body")

                body ?: ApiResponse(
                    success = false,
                    response = null,
                    error = "응답 본문이 비어있습니다."
                )
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(tag, "인증번호 요청 API 호출 실패!")
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
            Log.d(tag, "=== 실제 인증번호 요청 API 호출 완료 ===")
        }
    }

    suspend fun verifyPhoneCode(
        phoneNumber: String,
        code: String
    ): ApiResponse<PhoneVerificationResponse> {
        Log.d(tag, "=== 인증번호 확인 API 호출 시작 ===")
        Log.d(tag, "입력받은 전화번호: $phoneNumber")
        Log.d(tag, "입력받은 인증번호: $code")
        Log.d(tag, "Base URL: $BASE_URL")
        Log.d(tag, "Endpoint: $PHONE_CODE_VERIFY_ENDPOINT")
        Log.d(tag, "Full URL: $BASE_URL$PHONE_CODE_VERIFY_ENDPOINT")
        Log.d(tag, "HTTP Method: POST")
        Log.d(tag, "Content-Type: application/json")

        val request = PhoneVerificationCodeRequest(phoneNumber = phoneNumber, code = code)

        Log.d(tag, "Request Body 생성:")
        Log.d(tag, "  phoneNumber: ${request.phoneNumber}")
        Log.d(tag, "  code: ${request.code}")
        Log.d(
            tag,
            "Request Body JSON: {\"phoneNumber\": \"${request.phoneNumber}\", \"code\": \"${request.code}\"}"
        )

        return try {
            Log.d(tag, "Retrofit API 호출 시작...")

            val response = apiService.verifyPhoneCode(request)

            Log.d(tag, "HTTP Status Code: ${response.code()}")
            Log.d(tag, "HTTP Status Message: ${response.message()}")
            Log.d(tag, "Response Headers: ${response.headers()}")

            if (response.isSuccessful) {
                val body = response.body()
                Log.d(tag, "인증번호 확인 API 호출 성공!")
                Log.d(tag, "Response Body: $body")

                body ?: ApiResponse(
                    success = false,
                    response = null,
                    error = "응답 본문이 비어있습니다."
                )
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(tag, "인증번호 확인 API 호출 실패!")
                Log.e(tag, "Error Code: ${response.code()}")
                Log.e(tag, "Error Message: ${response.message()}")
                Log.e(tag, "Error Body: $errorBody")

                ApiResponse(
                    success = false,
                    response = null,
                    error = when (response.code()) {
                        401 -> "인증번호가 올바르지 않습니다."
                        else -> "서버 오류: ${response.code()} - ${response.message()}"
                    }
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
            Log.d(tag, "=== 인증번호 확인 API 호출 완료 ===")
        }
    }

    companion object {
        const val BASE_URL = "http://3.34.83.48:8080/"
        const val PHONE_VERIFICATION_ENDPOINT = "api/members/auth/phones/code"
        const val PHONE_CODE_VERIFY_ENDPOINT = "api/members/auth/phones/code/verify"
    }
}