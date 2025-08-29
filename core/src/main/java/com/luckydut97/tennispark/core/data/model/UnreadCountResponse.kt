package com.luckydut97.tennispark.core.data.model

import com.google.gson.annotations.SerializedName

/**
 * 미읽은 알림 수 조회 응답 모델
 */
data class UnreadCountResponse(
    @SerializedName("unreadCount")
    val unreadCount: Int
)