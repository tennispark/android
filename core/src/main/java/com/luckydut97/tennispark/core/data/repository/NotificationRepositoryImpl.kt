package com.luckydut97.tennispark.core.data.repository

import android.util.Log
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

    private val tag = "🔍 NotificationRepository"

    override suspend fun getNotifications(): Flow<List<PushNotification>> = flow {
        Log.d(tag, "[getNotifications] called")

        try {
            val response = apiService.getNotifications()
            Log.d(
                tag,
                "[getNotifications] API response: isSuccessful=${response.isSuccessful}, code=${response.code()}"
            )

            if (response.isSuccessful && response.body()?.success == true) {
                val notificationListResponse = response.body()?.response

                if (notificationListResponse != null) {
                    val notifications =
                        notificationListResponse.notifications.map { it.toPushNotification() }
                    Log.d(
                        tag,
                        "[getNotifications] Successfully mapped ${notifications.size} notifications"
                    )

                    // 서버에서 이미 날짜 내림차순으로 정렬되어 온다고 가정
                    emit(notifications)
                } else {
                    Log.w(tag, "[getNotifications] Response data is null")
                    throw Exception("알림 데이터가 없습니다.")
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(
                    tag,
                    "[getNotifications] API failed - code=${response.code()}, errorBody=$errorBody"
                )

                val errorMessage = when (response.code()) {
                    401 -> "인증이 되지 않았습니다."
                    else -> "알림 목록을 가져올 수 없습니다."
                }
                throw Exception(errorMessage)
            }
        } catch (e: Exception) {
            Log.e(tag, "[getNotifications] Exception: ${e.message}", e)
            throw e
        }
    }
}