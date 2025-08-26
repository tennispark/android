package com.luckydut97.tennispark.core.domain.usecase

import android.content.Context
import com.luckydut97.tennispark.core.domain.model.PushNotification
import com.luckydut97.tennispark.core.domain.repository.NotificationRepository
import com.luckydut97.tennispark.core.utils.NotificationBadgeManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

/**
 * 알림 목록 조회 UseCase
 * 비즈니스 로직: 30일 필터링, 정렬, 신규 알림 판단 등
 */
class GetNotificationsUseCase(
    private val notificationRepository: NotificationRepository,
    private val context: Context
) {
    suspend operator fun invoke(): Flow<List<PushNotification>> {
        val badgeManager = NotificationBadgeManager.getInstance(context) // 🔥 Singleton 사용
        val currentBadgeCount = badgeManager.getUnreadCount()

        return notificationRepository.getNotifications()
            .map { notifications ->
                // 비즈니스 규칙 적용
                val filteredNotifications = notifications
                    .filter { notification ->
                        // 30일 이내 알림만 필터링 (서버에서 이미 처리한다고 했지만 클라이언트에서도 보장)
                        val thirtyDaysAgo = LocalDateTime.now().minusDays(30)
                        notification.date.isAfter(thirtyDaysAgo)
                    }
                    .sortedByDescending { it.date } // 최신 순 정렬 보장

                // 현재 배지 수만큼 최신 알림을 신규로 표시
                filteredNotifications.mapIndexed { index, notification ->
                    notification.copy(
                        isNew = index < currentBadgeCount // 상위 N개를 신규 알림으로 표시
                    )
                }
            }
    }
}