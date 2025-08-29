package com.luckydut97.tennispark.core.domain.usecase

import com.luckydut97.tennispark.core.domain.repository.NotificationRepository

/**
 * 미읽은 알림 수 조회 UseCase (서버 기반)
 */
class GetUnreadCountUseCase(
    private val notificationRepository: NotificationRepository
) {

    suspend operator fun invoke(): Int {
        return try {
            val count = notificationRepository.getUnreadCount()
            count
        } catch (e: Exception) {
            // 에러 시 0 반환 (안전한 기본값)
            0
        }
    }
}