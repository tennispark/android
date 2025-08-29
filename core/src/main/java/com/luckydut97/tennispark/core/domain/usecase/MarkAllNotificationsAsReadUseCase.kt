package com.luckydut97.tennispark.core.domain.usecase

import com.luckydut97.tennispark.core.domain.repository.NotificationRepository

/**
 * 모든 알림 읽음 처리 UseCase (서버 기반)
 */
class MarkAllNotificationsAsReadUseCase(
    private val notificationRepository: NotificationRepository
) {

    suspend operator fun invoke(): Result<Unit> {
        return try {
            val result = notificationRepository.markAllAsRead()
            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}