package com.luckydut97.tennispark.core.data.model

/**
 * 토큰 재발급 응답 데이터 모델
 */
data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)