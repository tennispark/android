package com.luckydut97.tennispark.core.data.model

/**
 * FCM 토큰 업데이트 요청 데이터 모델
 */
data class UpdateFcmTokenRequest(
    val fcmToken: String // 새로운 FCM 토큰 (빈 문자열 시 알림 수신 거부)
)

/**
 * FCM 토큰 업데이트 응답 데이터 모델
 */
data class UpdateFcmTokenResponse(
    val success: Boolean,
    val message: String? = null
)