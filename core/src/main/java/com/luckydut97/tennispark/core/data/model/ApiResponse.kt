package com.luckydut97.tennispark.core.data.model

/**
 * API 응답 데이터 모델
 */
data class ApiResponse<T>(
    val success: Boolean,
    val response: T? = null,
    val error: String? = null
)

/**
 * 인증번호 확인 응답 데이터 모델
 */
data class PhoneVerificationResponse(
    val isRegister: Boolean,
    val accessToken: String? = null,
    val refreshToken: String? = null
)
