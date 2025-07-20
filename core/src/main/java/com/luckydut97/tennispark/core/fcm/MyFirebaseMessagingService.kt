package com.luckydut97.tennispark.core.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.luckydut97.tennispark.core.data.repository.AuthRepository
import com.luckydut97.tennispark.core.data.repository.AuthRepositoryImpl
import com.luckydut97.tennispark.core.data.storage.TokenManagerImpl
import com.luckydut97.tennispark.core.data.storage.NotificationPreferenceManager
import com.luckydut97.tennispark.core.data.network.NetworkModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Firebase Cloud Messaging 서비스
 * FCM 토큰 갱신 및 푸시 메시지 수신을 처리
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "MyFirebaseMessaging 디버깅"
    }

    private val authRepository: AuthRepository by lazy {
        val tokenManager = TokenManagerImpl(applicationContext)
        AuthRepositoryImpl(
            apiService = NetworkModule.apiService,
            tokenManager = tokenManager
        )
    }

    private val notificationPreferenceManager: NotificationPreferenceManager by lazy {
        NotificationPreferenceManager(applicationContext)
    }

    /**
     * FCM 토큰이 갱신되었을 때 호출됩니다
     * 앱 재설치, 복원, 토큰 갱신 시 발생
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        // 토큰이 갱신되었을 때 서버로 전송
        sendTokenToServer(token)
    }

    /**
     * 푸시 메시지를 수신했을 때 호출됩니다
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // 푸시 알림 설정 확인
        val isPushEnabled = notificationPreferenceManager.isPushNotificationEnabled()

        if (!isPushEnabled) {
            return
        }

        // 알림 데이터 추출
        val title = remoteMessage.notification?.title
        val body = remoteMessage.notification?.body
        val data = remoteMessage.data

        // 실제 알림 표시
        try {
            val notificationHelper = NotificationHelper(applicationContext)
            notificationHelper.showNotification(title, body, data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 서버로 FCM 토큰을 전송합니다
     * AuthRepository를 통해 실제 API 호출
     */
    private fun sendTokenToServer(token: String) {
        // 코루틴 스코프에서 비동기 처리
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 푸시 알림 설정 확인
                val isPushEnabled = notificationPreferenceManager.isPushNotificationEnabled()

                // 로그인 상태 확인
                val isLoggedIn = authRepository.isLoggedIn()

                if (isLoggedIn) {
                    // 푸시 알림 설정에 따라 토큰 결정
                    val tokenToSend = if (isPushEnabled) {
                        token
                    } else {
                        ""
                    }

                    val response = authRepository.updateFcmToken(tokenToSend)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}