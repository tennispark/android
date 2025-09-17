package com.luckydut97.tennispark.core.domain.model

import java.time.LocalDateTime

/**
 * 커뮤니티 댓글 도메인 모델 (Core Domain)
 */
data class CommunityComment(
    val id: Int,
    val authorName: String,
    val createdAt: LocalDateTime,
    val content: String,
    val photoUrl: String?,
    val authoredByMe: Boolean
) {
    /**
     * 상대 시간 텍스트 반환 ("3분 전", "1시간 전" 등)
     */
    val relativeTimeText: String
        get() = com.luckydut97.tennispark.core.utils.TimeUtils.getRelativeTimeString(createdAt)
}