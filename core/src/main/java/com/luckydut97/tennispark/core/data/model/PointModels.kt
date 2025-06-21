package com.luckydut97.tennispark.core.data.model

import com.google.gson.annotations.SerializedName

/**
 * 내 포인트 잔액 조회 응답 모델
 */
data class MyPointResponse(
    @SerializedName("points")
    val points: Int
)

/**
 * 포인트 내역 아이템 모델
 */
data class PointHistoryItem(
    @SerializedName("historyId")
    val historyId: Long,

    @SerializedName("title")
    val title: String,

    @SerializedName("point")
    val point: Int,

    @SerializedName("type")
    val type: String, // "EARNED" | "USED"

    @SerializedName("date")
    val date: String // "YYYY.MM.DD" 형식
)

/**
 * 포인트 내역 조회 응답 모델
 */
data class PointHistoryListResponse(
    @SerializedName("histories")
    val histories: List<PointHistoryItem>
)