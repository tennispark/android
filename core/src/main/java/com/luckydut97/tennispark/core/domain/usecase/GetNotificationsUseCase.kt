package com.luckydut97.tennispark.core.domain.usecase

import com.luckydut97.tennispark.core.domain.model.PushNotification
import com.luckydut97.tennispark.core.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

/**
 * 알림 목록 조회 UseCase
 * 비즈니스 로직: 30일 필터링, 정렬, 서버 기반 신규 알림 판단 등
 */
class GetNotificationsUseCase(
    private val notificationRepository: NotificationRepository
) {
    /**
     * @param preloadedUnreadCount 미리 조회된 미읽은 알림 수 (HomeTopAppBar에서 전달)
     * null인 경우 서버에서 새로 조회
     */
    suspend operator fun invoke(preloadedUnreadCount: Int? = null): Flow<List<PushNotification>> {
        // 전달받은 unreadCount 사용, 없으면 서버에서 조회
        val unreadCount = preloadedUnreadCount ?: try {
            notificationRepository.getUnreadCount()
        } catch (e: Exception) {
            0 // 실패 시 0으로 처리
        }

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

                // 서버의 미읽은 알림 수만큼 최신 알림을 신규로 표시
                filteredNotifications.mapIndexed { index, notification ->
                    notification.copy(
                        isNew = index < unreadCount // 상위 N개를 신규 알림으로 표시
                    )
                }
            }
    }
}