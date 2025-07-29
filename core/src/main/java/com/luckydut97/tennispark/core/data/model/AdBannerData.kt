package com.luckydut97.tennispark.core.data.model

import com.google.gson.annotations.SerializedName

/**
 * 광고 배너 데이터 모델 (기존 - 로컬 리소스용)
 */
data class AdBannerData(
    val imageRes: Int,
    val url: String
)

/**
 * 광고 배너 API 응답 모델
 */
data class Advertisement(
    val id: Long,
    val position: String,
    val imageUrl: String,
    val linkUrl: String
)

/**
 * 광고 배너 목록 API 응답 데이터 모델
 */
data class AdvertisementListResponse(
    val advertisements: List<Advertisement>
)

/**
 * 광고 배너 Position 열거형
 */
enum class AdPosition(val value: String) {
    MAIN("MAIN"),
    ACTIVITY("ACTIVITY"),
    PURCHASE("PURCHASE"),
    MEMBER("MEMBER")
}
