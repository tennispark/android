package com.luckydut97.tennispark.core.data.model

/**
 * 서버 에러 응답 모델
 */
data class ErrorResponse(
    val status: Int,
    val message: String
)

/**
 * API 응답 데이터 모델
 */
data class ApiResponse<T>(
    val success: Boolean,
    val response: T? = null,
    val error: ErrorResponse? = null
)

/**
 * 인증번호 확인 응답 데이터 모델
 */
data class PhoneVerificationResponse(
    val isRegister: Boolean,
    val accessToken: String? = null,
    val refreshToken: String? = null
)
