package com.luckydut97.tennispark.core.data.model

/**
 * 멤버십 등록 요청 데이터 모델
 */
data class MemberRegistrationRequest(
    val phoneNumber: String,
    val name: String,
    val gender: String,
    val tennisCareer: String,
    val year: Int,
    val registrationSource: String,
    val recommender: String,
    val instagramId: String
)
