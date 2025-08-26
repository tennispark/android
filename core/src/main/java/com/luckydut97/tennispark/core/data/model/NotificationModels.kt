package com.luckydut97.tennispark.core.data.model

import com.google.gson.annotations.SerializedName

/**
 * 알림 목록 조회 응답 모델
 */
data class NotificationListResponse(
    @SerializedName("notifications")
    val notifications: List<NotificationResponse>
)

/**
 * 개별 알림 응답 모델
 */
data class NotificationResponse(
    @SerializedName("type")
    val type: String, // "ANNOUNCEMENT", "ACTIVITY_GUIDE", "MATCHING_GUIDE", "COMMUNITY", "ETC"

    @SerializedName("content")
    val content: String, // 알림 내용

    @SerializedName("date")
    val date: String // "2025-08-16T22:35:03.505738" ISO 8601 형식
)