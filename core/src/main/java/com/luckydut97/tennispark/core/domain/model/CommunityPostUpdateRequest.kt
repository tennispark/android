package com.luckydut97.tennispark.core.domain.model

/**
 * 게시글 수정 요청 도메인 모델
 */
data class CommunityPostUpdateRequest(
    val title: String,
    val content: String,
    val deleteList: List<Int> = emptyList()
)
