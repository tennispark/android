package com.luckydut97.tennispark.core.data.model

import com.google.gson.annotations.SerializedName

/**
 * 활동 신청 내역 조회 응답 모델
 */
data class ActivityApplicationListResponse(
    @SerializedName("applications")
    val applications: List<ActivityApplicationResponse>
)

/**
 * 개별 활동 신청 내역 응답 모델
 */
data class ActivityApplicationResponse(
    @SerializedName("id")
    val id: Long,

    @SerializedName("applicationDate")
    val applicationDate: String, // "2025-04-25"

    @SerializedName("applicationStatus")
    val applicationStatus: String, // "PENDING", "WAITING", "APPROVED", "CANCELED"

    @SerializedName("activity")
    val activity: ActivityInfoResponse
)

/**
 * 활동 정보 응답 모델 (신청 내역용)
 */
data class ActivityInfoResponse(
    @SerializedName("date")
    val date: String, // "2025년 06월 09일(월)"

    @SerializedName("startAt")
    val startAt: String, // "10:00"

    @SerializedName("endAt")
    val endAt: String, // "14:00"

    @SerializedName("place")
    val place: String, // "수도공고"

    @SerializedName("courtType")
    val courtType: String // "GAME", "CHALLENGE", "RALLY", "STUDY", "BEGINNER"
)