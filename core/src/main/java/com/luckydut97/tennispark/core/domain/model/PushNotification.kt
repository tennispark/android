package com.luckydut97.tennispark.core.domain.model

import java.time.LocalDateTime

/**
 * 푸시 알림 도메인 모델 (Core Domain)
 */
data class PushNotification(
    val type: NotificationType,
    val content: String,
    val date: LocalDateTime,
    val isExpanded: Boolean = false, // UI 상태: 더보기 확장 여부
    val id: String = java.util.UUID.randomUUID().toString(), // 로컬 ID (UI용)
    val isNew: Boolean = false // 신규 알림 여부 (연한 초록색 배경 표시용)
) {
    /**
     * 상대 시간 텍스트 반환 ("50분 전", "3시간 전" 등)
     */
    val relativeTimeText: String
        get() = com.luckydut97.tennispark.core.utils.TimeUtils.getRelativeTimeString(date)

    /**
     * 알림 타입별 제목 반환
     */
    val title: String
        get() = when (type) {
            NotificationType.ANNOUNCEMENT -> "공지"
            NotificationType.ACTIVITY_GUIDE -> "활동 안내"
            NotificationType.MATCHING_GUIDE -> "매칭 안내"
            NotificationType.COMMUNITY -> "커뮤니티"
            NotificationType.ETC -> "알림"
        }
}

/**
 * 알림 타입 (서버 API 명세와 일치)
 */
enum class NotificationType {
    ANNOUNCEMENT,     // 공지(전체 공지/추가모집)
    ACTIVITY_GUIDE,   // 활동 안내(승인 상태 알림)
    MATCHING_GUIDE,   // 매칭 안내(활동 리마인더)
    COMMUNITY,        // 커뮤니티 댓글 알림
    ETC              // 기타
}