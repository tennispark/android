package com.luckydut97.tennispark.core.data.model

/**
 * 멤버십 등록 요청 데이터 모델
 */
data class MemberRegistrationRequest(
    val phoneNumber: String,
    val name: String,
    val gender: String, // "MAN" or "WOM"
    val tennisCareer: String,
    val year: Int,
    val registrationSource: String, // "INSTAGRAM", "NAVER_SEARCH", "FRIEND_RECOMMENDATION"
    val recommender: String? = null, // 친구 추천 시에만수
    val instagramId: String
)

/**
 * 회원가입 응답 데이터 모델
 */
data class MemberRegistrationResponse(
    val accessToken: String,
    val refreshToken: String
)
