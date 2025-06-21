package com.luckydut97.tennispark.core.data.model

import com.google.gson.annotations.SerializedName

/**
 * 아카데미 목록 조회 응답 모델
 */
data class AcademyListResponse(
    @SerializedName("academies")
    val academies: List<AcademyResponse>
)

/**
 * 아카데미 정보 응답 모델
 */
data class AcademyResponse(
    @SerializedName("id")
    val id: Long,

    @SerializedName("lessonType")
    val lessonType: String, // "FOREHAND_BACKHAND", "VOLLEY_SERVE"

    @SerializedName("date")
    val date: String, // "05.13(화)" 형식

    @SerializedName("startAt")
    val startAt: String, // "10:00" 형식

    @SerializedName("endAt")
    val endAt: String, // "14:00" 형식

    @SerializedName("participantCount")
    val participantCount: Int,

    @SerializedName("capacity")
    val capacity: Int,

    @SerializedName("courtName")
    val courtName: String, // "F코트"

    @SerializedName("place")
    val place: PlaceResponse
)