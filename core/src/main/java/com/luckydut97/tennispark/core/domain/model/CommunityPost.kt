package com.luckydut97.tennispark.core.domain.model

import java.time.LocalDateTime

/**
 * 커뮤니티 게시물 도메인 모델 (Core Domain)
 */
data class CommunityPost(
    val id: Int,
    val authorName: String,
    val createdAt: LocalDateTime,
    val title: String,
    val content: String,
    val mainImage: String?,
    val photos: Map<String, String> = emptyMap(), // 인덱스:URL 형식의 사진 맵
    val likeCount: Int,
    val commentCount: Int,
    val viewCount: Int,
    val likedByMe: Boolean,
    val authoredByMe: Boolean,
    val notificationEnabled: Boolean? = null // 작성자일 때만 값 있음
) {
    /**
     * 상대 시간 텍스트 반환 ("3분 전", "1시간 전" 등)
     */
    val relativeTimeText: String
        get() = com.luckydut97.tennispark.core.utils.TimeUtils.getRelativeTimeString(createdAt)

    /**
     * 조회수 포맷팅 (천 단위 콤마)
     */
    val formattedViewCount: String
        get() = String.format("%,d", viewCount)

    /**
     * 내용이 긴지 여부 (5줄 이상)
     */
    val isContentLong: Boolean
        get() = content.lines().size > 5 || content.length > 200

    /**
     * 축약된 내용 (최대 5줄)
     */
    val truncatedContent: String
        get() = if (isContentLong) {
            val lines = content.lines()
            if (lines.size > 5) {
                lines.take(5).joinToString("\n")
            } else {
                content.take(200) + if (content.length > 200) "..." else ""
            }
        } else {
            content
        }

    /**
     * 사진 URL 리스트 (순서대로 정렬됨)
     */
    val sortedPhotos: List<String>
        get() = photos.toList()
            .sortedBy { it.first.toIntOrNull() ?: 0 }
            .map { it.second }
}