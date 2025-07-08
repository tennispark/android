package com.luckydut97.tennispark.core.data.model

import com.google.gson.annotations.SerializedName

/**
 * 활동 인증 요청 모델
 */
data class ActivityCertificationRequest(
    @SerializedName("activityId")
    val activityId: Long,
    @SerializedName("content")
    val content: String? = null
)

/**
 * 활동 인증 응답 모델
 */
data class ActivityCertificationResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String?
)