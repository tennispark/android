package com.luckydut97.tennispark.core.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.util.Log
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
        Log.d(TAG, "🔍 디버깅: 알림 채널 생성 시작")
        Log.d(TAG, "🔍 디버깅: Android 버전: ${Build.VERSION.SDK_INT}")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "🔍 디버깅: Android 8.0+ 감지 - 알림 채널 생성 진행")

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

            Log.d(TAG, "🔍 디버깅: 알림 채널 설정 완료")
            Log.d(TAG, "🔍 디버깅: - 채널 ID: $CHANNEL_ID")
            Log.d(TAG, "🔍 디버깅: - 채널 이름: $CHANNEL_NAME")
            Log.d(TAG, "🔍 디버깅: - 중요도: HIGH")

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            Log.d(TAG, "🔍 디버깅: NotificationManager 획득 완료")

            try {
                notificationManager.createNotificationChannel(channel)
                Log.d(TAG, "✅ 디버깅: 알림 채널 생성 완료: $CHANNEL_ID")

                // 채널 생성 확인
                val createdChannel = notificationManager.getNotificationChannel(CHANNEL_ID)
                if (createdChannel != null) {
                    Log.d(TAG, "✅ 디버깅: 알림 채널 등록 확인됨")
                    Log.d(TAG, "🔍 디버깅: - 등록된 채널 이름: ${createdChannel.name}")
                    Log.d(TAG, "🔍 디버깅: - 등록된 채널 중요도: ${createdChannel.importance}")
                } else {
                    Log.e(TAG, "❌ 디버깅: 알림 채널 등록 실패!")
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ 디버깅: 알림 채널 생성 예외: ${e.message}", e)
            }
        } else {
            Log.d(TAG, "🔍 디버깅: Android 8.0 미만 - 알림 채널 생성 생략")
        }
    }

    /**
     * FCM 메시지로부터 알림 생성 및 표시
     */
    fun showNotification(title: String?, body: String?, data: Map<String, String>? = null) {
        Log.d(TAG, "🔍 디버깅: === 알림 생성 시작 ===")
        Log.d(TAG, "🔍 디버깅: 제목: $title")
        Log.d(TAG, "🔍 디버깅: 내용: $body")
        Log.d(TAG, "🔍 디버깅: 데이터: $data")
        Log.d(TAG, "🔍 디버깅: Android 버전: ${Build.VERSION.SDK_INT}")

        // 푸시 알림 설정 확인 (이중 체크)
        val isPushEnabled = notificationPreferenceManager.isPushNotificationEnabled()
        Log.d(TAG, "🔍 디버깅: 푸시 알림 설정 상태: $isPushEnabled")

        if (!isPushEnabled) {
            Log.d(TAG, "🔍 디버깅: 푸시 알림이 비활성화되어 있어 알림을 표시하지 않습니다.")
            Log.d(TAG, "🔍 디버깅: === 알림 생성 완료 (표시 안함) ===")
            return
        }

        // 알림 권한 확인 (Android 13+)
        Log.d(TAG, "🔍 디버깅: 알림 권한 확인 시작")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Log.d(TAG, "🔍 디버깅: Android 13+ 감지 - 알림 권한 확인 필요")
            val hasPermission = NotificationManagerCompat.from(context).areNotificationsEnabled()
            Log.d(TAG, "🔍 디버깅: 알림 권한 상태: $hasPermission")

            if (!hasPermission) {
                Log.e(TAG, "❌ 디버깅: 알림 권한이 없습니다!")
                Log.e(TAG, "❌ 디버깅: 설정에서 알림 권한을 허용해주세요.")
                return
            } else {
                Log.d(TAG, "✅ 디버깅: 알림 권한 확인됨")
            }
        } else {
            Log.d(TAG, "🔍 디버깅: Android 13 미만 - 알림 권한 확인 생략")
        }

        // NotificationManager 상태 확인
        Log.d(TAG, "🔍 디버깅: NotificationManager 상태 확인")
        val notificationManager = NotificationManagerCompat.from(context)
        val isEnabled = notificationManager.areNotificationsEnabled()
        Log.d(TAG, "🔍 디버깅: 시스템 알림 활성화 상태: $isEnabled")

        if (!isEnabled) {
            Log.e(TAG, "❌ 디버깅: 시스템 알림이 비활성화되어 있습니다!")
            return
        }

        // 메인 액티비티로 이동하는 Intent 생성
        Log.d(TAG, "🔍 디버깅: PendingIntent 생성 시작")
        val intent = getLaunchIntent()
        Log.d(TAG, "🔍 디버깅: Launch Intent 생성 완료: ${intent.component}")

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        Log.d(TAG, "🔍 디버깅: PendingIntent 생성 완료")

        // 알림 빌더 생성
        Log.d(TAG, "🔍 디버깅: NotificationCompat.Builder 생성 시작")
        Log.d(TAG, "🔍 디버깅: 사용할 채널 ID: $CHANNEL_ID")

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // 기본 알림 아이콘
            .setContentTitle(title ?: "테니스파크")
            .setContentText(body ?: "새로운 알림이 도착했습니다")
            .setAutoCancel(true) // 클릭 시 자동 삭제
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        Log.d(TAG, "🔍 디버깅: 기본 알림 빌더 설정 완료")
        Log.d(TAG, "🔍 디버깅: - 아이콘: android.R.drawable.ic_dialog_info")
        Log.d(TAG, "🔍 디버깅: - 제목: ${title ?: "테니스파크"}")
        Log.d(TAG, "🔍 디버깅: - 내용: ${body ?: "새로운 알림이 도착했습니다"}")
        Log.d(TAG, "🔍 디버깅: - 우선순위: HIGH")

        // 긴 텍스트 스타일 적용
        if (!body.isNullOrEmpty() && body.length > 50) {
            Log.d(TAG, "🔍 디버깅: 긴 텍스트 스타일 적용 (${body.length}자)")
            notificationBuilder.setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(body)
            )
        } else {
            Log.d(TAG, "🔍 디버깅: 일반 텍스트 스타일 사용")
        }

        // 알림 표시
        Log.d(TAG, "🔍 디버깅: 알림 표시 시작")
        try {
            val notification = notificationBuilder.build()
            Log.d(TAG, "🔍 디버깅: Notification 객체 생성 완료")

            notificationManager.notify(NOTIFICATION_ID, notification)
            Log.d(TAG, "✅ 디버깅: 알림 표시 완료!")
            Log.d(TAG, "🔍 디버깅: - 알림 ID: $NOTIFICATION_ID")
            Log.d(TAG, "🔍 디버깅: - 채널 ID: $CHANNEL_ID")

        } catch (e: SecurityException) {
            Log.e(TAG, "❌ 디버깅: 보안 예외 - 알림 권한 없음: ${e.message}", e)
        } catch (e: Exception) {
            Log.e(TAG, "❌ 디버깅: 알림 표시 실패: ${e.message}", e)
            Log.e(TAG, "❌ 디버깅: 예외 타입: ${e.javaClass.simpleName}")
        }

        Log.d(TAG, "🔍 디버깅: === 알림 생성 완료 ===")
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