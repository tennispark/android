package com.luckydut97.tennispark.core.data.model

/**
 * 휴대폰 인증 요청 데이터 모델
 */
data class PhoneVerificationRequest(
    val phoneNumber: String
)

/**
 * 휴대폰 인증번호 확인 요청 데이터 모델
 */
data class PhoneVerificationCodeRequest(
    val phoneNumber: String,
    val code: String
)
