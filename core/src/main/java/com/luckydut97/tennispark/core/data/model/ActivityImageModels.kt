package com.luckydut97.tennispark.core.data.model

/**
 * 활동 이미지 목록 API 응답 데이터 모델
 */
data class ActivityImageListResponse(
    val totalCount: Int,
    val images: List<String>
)