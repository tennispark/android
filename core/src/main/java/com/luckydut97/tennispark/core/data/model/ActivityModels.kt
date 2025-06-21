package com.luckydut97.tennispark.core.data.model

import com.google.gson.annotations.SerializedName

/**
 * 활동 목록 조회 응답 모델
 */
data class ActivityListResponse(
    @SerializedName("activities")
    val activities: List<ActivityResponse>
)

/**
 * 활동 정보 응답 모델
 */
data class ActivityResponse(
    @SerializedName("id")
    val activityId: Long,

    @SerializedName("date")
    val date: String, // "2025년 06월 09일(월)" 형식

    @SerializedName("startAt")
    val startAt: String, // "10:00" 형식

    @SerializedName("endAt")
    val endAt: String, // "14:00" 형식

    @SerializedName("participantCount")
    val participantCount: Int,

    @SerializedName("capacity")
    val capacity: Int,

    @SerializedName("courtType")
    val courtType: String, // "GAME", "CHALLENGE", "RALLY", "STUDY", "BEGINNER"

    @SerializedName("courtName")
    val courtName: String, // "F코트"

    @SerializedName("place")
    val place: PlaceResponse
)

/**
 * 장소 정보 응답 모델
 */
data class PlaceResponse(
    @SerializedName("name")
    val name: String, // "수도공고"

    @SerializedName("address")
    val address: String // "서울 강남구 개포로 410 수도전기공업고등학교"
)
