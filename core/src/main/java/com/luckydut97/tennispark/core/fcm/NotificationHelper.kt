package com.luckydut97.tennispark.core.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.luckydut97.tennispark.core.R
import com.luckydut97.tennispark.core.data.storage.NotificationPreferenceManager

/**
 * FCM 푸시 알림 생성 및 표시 헬퍼 클래스
 */
class NotificationHelper(private val context: Context) {

    companion object {
        private const val TAG = "NotificationHelper"
        private const val CHANNEL_ID = "tennis_park_notifications"
        private const val CHANNEL_NAME = "테니스파크 알림"
        private const val CHANNEL_DESCRIPTION = "테니스파크 앱의 중요한 알림을 받습니다"
        private const val NOTIFICATION_ID = 1001
    }

    private val notificationPreferenceManager = NotificationPreferenceManager(context)

    init {
        createNotificationChannel()
    }

    /**
     * 알림 채널 생성 (Android 8.0+)
     */
    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
                enableLights(true)
                enableVibration(true)
                setShowBadge(true)
            }


            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


            try {
                notificationManager.createNotificationChannel(channel)

                // 채널 생성 확인
                val createdChannel = notificationManager.getNotificationChannel(CHANNEL_ID)
                if (createdChannel != null) {
                } else {
                }
            } catch (e: Exception) {
            }
        } else {
        }
    }

    /**
     * FCM 메시지로부터 알림 생성 및 표시
     */
    fun showNotification(title: String?, body: String?, data: Map<String, String>? = null) {

        // 푸시 알림 설정 확인 (이중 체크)
        val isPushEnabled = notificationPreferenceManager.isPushNotificationEnabled()

        if (!isPushEnabled) {
            return
        }

        // 알림 권한 확인 (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = NotificationManagerCompat.from(context).areNotificationsEnabled()

            if (!hasPermission) {
                return
            } else {
            }
        } else {
        }

        // NotificationManager 상태 확인
        val notificationManager = NotificationManagerCompat.from(context)
        val isEnabled = notificationManager.areNotificationsEnabled()

        if (!isEnabled) {
            return
        }

        // 메인 액티비티로 이동하는 Intent 생성
        val intent = getLaunchIntent()

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 알림 빌더 생성

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // 기본 알림 아이콘
            .setContentTitle(title ?: "테니스파크")
            .setContentText(body ?: "새로운 알림이 도착했습니다")
            .setAutoCancel(true) // 클릭 시 자동 삭제
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)


        // 긴 텍스트 스타일 적용
        if (!body.isNullOrEmpty() && body.length > 50) {
            notificationBuilder.setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(body)
            )
        } else {
        }

        // 알림 표시
        try {
            val notification = notificationBuilder.build()

            notificationManager.notify(NOTIFICATION_ID, notification)

        } catch (e: SecurityException) {
        } catch (e: Exception) {
        }

    }

    /**
     * 앱 런처 Intent 생성
     */
    private fun getLaunchIntent(): Intent {
        return context.packageManager.getLaunchIntentForPackage(context.packageName)
            ?: Intent().apply {
                action = Intent.ACTION_MAIN
                addCategory(Intent.CATEGORY_LAUNCHER)
                `package` = context.packageName
            }
    }

    /**
     * 알림 권한 상태 확인
     */
    fun areNotificationsEnabled(): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }
}