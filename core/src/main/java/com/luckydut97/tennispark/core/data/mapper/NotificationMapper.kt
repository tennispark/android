package com.luckydut97.tennispark.core.data.mapper

import com.luckydut97.tennispark.core.data.model.NotificationResponse
import com.luckydut97.tennispark.core.domain.model.NotificationType
import com.luckydut97.tennispark.core.domain.model.PushNotification
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * NotificationResponse를 PushNotification 도메인 모델로 변환
 */
fun NotificationResponse.toPushNotification(): PushNotification {
    // 알림 타입 변환
    val notificationType = when (type.uppercase()) {
        "ANNOUNCEMENT" -> NotificationType.ANNOUNCEMENT
        "ACTIVITY_GUIDE" -> NotificationType.ACTIVITY_GUIDE
        "MATCHING_GUIDE" -> NotificationType.MATCHING_GUIDE
        "COMMUNITY" -> NotificationType.COMMUNITY
        "ETC" -> NotificationType.ETC
        else -> NotificationType.ETC // 기본값
    }

    // ISO 8601 형식 날짜 파싱: "2025-08-16T22:35:03.505738"
    val dateTime = try {
        // 마이크로초가 있는 경우와 없는 경우 모두 처리
        when {
            date.contains(".") -> {
                // 마이크로초 포함: "2025-08-16T22:35:03.505738"
                LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            }

            else -> {
                // 마이크로초 없음: "2025-08-16T22:35:03"
                LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            }
        }
    } catch (e: Exception) {
        android.util.Log.w(
            "NotificationMapper",
            "Failed to parse date: $date, using current time",
            e
        )
        LocalDateTime.now()
    }

    return PushNotification(
        type = notificationType,
        content = content,
        date = dateTime,
        isExpanded = false // 초기 상태는 접힌 상태
    )
}