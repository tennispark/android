package com.luckydut97.tennispark.core.data.repository

import com.luckydut97.tennispark.core.domain.repository.NotificationRepository
import com.luckydut97.tennispark.core.domain.model.PushNotification
import com.luckydut97.tennispark.core.data.mapper.toPushNotification
import com.luckydut97.tennispark.core.data.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * 알림 Repository 구현체 (Clean Architecture)
 */
class NotificationRepositoryImpl(
    private val apiService: ApiService
) : NotificationRepository {

    override suspend fun getNotifications(): Flow<List<PushNotification>> = flow {
        try {
            val response = apiService.getNotifications()

            if (response.isSuccessful && response.body()?.success == true) {
                val notificationListResponse = response.body()?.response

                if (notificationListResponse != null) {
                    val notifications =
                        notificationListResponse.notifications.map { it.toPushNotification() }

                    // 서버에서 이미 날짜 내림차순으로 정렬되어 온다고 가정
                    emit(notifications)
                } else {
                    throw Exception("알림 데이터가 없습니다.")
                }
            } else {
                val errorMessage = when (response.code()) {
                    401 -> "인증이 되지 않았습니다."
                    else -> "알림 목록을 가져올 수 없습니다."
                }
                throw Exception(errorMessage)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getUnreadCount(): Int {
        return try {
            val response = apiService.getUnreadNotificationCount()

            if (response.isSuccessful && response.body()?.success == true) {
                val unreadCountResponse = response.body()?.response
                val count = unreadCountResponse?.unreadCount ?: 0
                count
            } else {
                0 // 실패 시 0 반환
            }
        } catch (e: Exception) {
            0 // 예외 시 0 반환
        }
    }

    override suspend fun markAllAsRead(): Result<Unit> {
        return try {
            val response = apiService.markAllNotificationsAsRead()

            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                val errorMessage = when (response.code()) {
                    401 -> "인증이 되지 않았습니다."
                    else -> "읽음 처리에 실패했습니다."
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}