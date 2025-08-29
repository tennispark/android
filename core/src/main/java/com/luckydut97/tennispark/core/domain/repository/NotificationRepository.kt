package com.luckydut97.tennispark.core.domain.repository

import com.luckydut97.tennispark.core.domain.model.PushNotification
import kotlinx.coroutines.flow.Flow

/**
 * 알림 도메인 Repository 인터페이스 (Clean Architecture)
 */
interface NotificationRepository {

    /**
     * 알림 목록 조회 (30일치)
     * 서버에서 날짜 내림차순으로 정렬된 알림 목록 반환
     */
    suspend fun getNotifications(): Flow<List<PushNotification>>

    /**
     * 미읽은 알림 수 조회
     */
    suspend fun getUnreadCount(): Int

    /**
     * 모든 알림을 읽음 처리
     */
    suspend fun markAllAsRead(): Result<Unit>
}