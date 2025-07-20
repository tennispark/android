package com.luckydut97.tennispark.core.data.repository

import com.luckydut97.tennispark.core.data.model.ApiResponse
import com.luckydut97.tennispark.core.data.model.PhoneVerificationRequest
import com.luckydut97.tennispark.core.data.model.PhoneVerificationCodeRequest
import com.luckydut97.tennispark.core.data.model.PhoneVerificationResponse
import com.luckydut97.tennispark.core.data.model.ErrorResponse
import com.luckydut97.tennispark.core.data.network.NetworkModule

class PhoneVerificationRepository {

    private val tag = "🔍 디버깅: PhoneVerificationRepo"
    private val apiService = NetworkModule.apiService

    suspend fun requestVerificationCode(phoneNumber: String): ApiResponse<Any> {
        val request = PhoneVerificationRequest(phoneNumber = phoneNumber)

        return try {
            val response = apiService.requestPhoneVerification(request)

            if (response.isSuccessful) {
                val body = response.body()

                body ?: ApiResponse(
                    success = false,
                    response = null,
                    error = ErrorResponse(
                        status = 500,
                        message = "응답 본문이 비어있습니다."
                    )
                )
            } else {
                val errorBody = response.errorBody()?.string()

                ApiResponse(
                    success = false,
                    response = null,
                    error = ErrorResponse(
                        status = response.code(),
                        message = "서버 오류가 발생했습니다."
                    )
                )
            }
        } catch (e: Exception) {
            ApiResponse(
                success = false,
                response = null,
                error = ErrorResponse(
                    status = 0,
                    message = "네트워크 오류: ${e.message}"
                )
            )
        }
    }

    suspend fun verifyPhoneCode(
        phoneNumber: String,
        code: String
    ): ApiResponse<PhoneVerificationResponse> {
        val request = PhoneVerificationCodeRequest(phoneNumber = phoneNumber, code = code)

        return try {
            val response = apiService.verifyPhoneCode(request)

            if (response.isSuccessful) {
                val body = response.body()

                body ?: ApiResponse(
                    success = false,
                    response = null,
                    error = ErrorResponse(
                        status = 500,
                        message = "응답 본문이 비어있습니다."
                    )
                )
            } else {
                val errorBody = response.errorBody()?.string()

                ApiResponse<PhoneVerificationResponse>(
                    success = false,
                    response = null,
                    error = when (response.code()) {
                        401 -> ErrorResponse(
                            status = 401,
                            message = "인증번호가 올바르지 않습니다."
                        )
                        else -> ErrorResponse(
                            status = response.code(),
                            message = "서버 오류: ${response.code()} - ${response.message()}"
                        )
                    }
                )
            }
        } catch (e: Exception) {
            ApiResponse(
                success = false,
                response = null,
                error = ErrorResponse(
                    status = 0,
                    message = "네트워크 오류: ${e.message}"
                )
            )
        }
    }

    companion object {
        const val BASE_URL = "https://tennis-park.store/"
        const val PHONE_VERIFICATION_ENDPOINT = "api/members/auth/phones/code"
        const val PHONE_CODE_VERIFY_ENDPOINT = "api/members/auth/phones/code/verify"
    }
}
