package com.luckydut97.tennispark.core.data.model

import com.google.gson.annotations.SerializedName

/**
 * 멤버십 등록 요청 모델
 */
data class MembershipRegistrationRequest(
    @SerializedName("membershipType")
    val membershipType: String, // "NEW", "EXISTING"

    @SerializedName("reason")
    val reason: String, // 멤버십 가입 이유

    @SerializedName("courtType")
    val courtType: String, // "GAME_CHALLENGE", "RALLY", "STUDY", "BEGINNER"

    @SerializedName("period")
    val period: String, // "7WEEKS", "9WEEKS", "13WEEKS"

    @SerializedName("recommender")
    val recommender: String? = null // 추천인 (선택사항)
)