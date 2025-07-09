package com.luckydut97.tennispark.core.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.luckydut97.tennispark.core.data.repository.AuthRepository
import com.luckydut97.tennispark.core.data.repository.AuthRepositoryImpl
import com.luckydut97.tennispark.core.data.storage.TokenManagerImpl
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

    /**
     * FCM 토큰이 갱신되었을 때 호출됩니다
     * 앱 재설치, 복원, 토큰 갱신 시 발생
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "새로운 FCM 토큰 발급: $token")

        // 토큰이 갱신되었을 때 서버로 전송
        sendTokenToServer(token)
    }

    /**
     * 푸시 메시지를 수신했을 때 호출됩니다
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d(TAG, "🔍 디버깅: === FCM 메시지 수신 시작 ===")
        Log.d(TAG, "🔍 디버깅: 발신자: ${remoteMessage.from}")
        Log.d(TAG, "🔍 디버깅: 메시지 ID: ${remoteMessage.messageId}")
        Log.d(TAG, "🔍 디버깅: 메시지 타입: ${remoteMessage.messageType}")
        Log.d(TAG, "🔍 디버깅: 전송 시간: ${remoteMessage.sentTime}")

        // 알림 데이터 추출
        val title = remoteMessage.notification?.title
        val body = remoteMessage.notification?.body
        val data = remoteMessage.data

        Log.d(TAG, "🔍 디버깅: 알림 데이터 추출 완료")
        Log.d(TAG, "🔍 디버깅: - 제목: $title")
        Log.d(TAG, "🔍 디버깅: - 내용: $body")
        Log.d(TAG, "🔍 디버깅: - 데이터 개수: ${data.size}")

        // 알림 데이터 로그 (기존 유지)
        remoteMessage.notification?.let {
            Log.d(TAG, "알림 제목: ${it.title}")
            Log.d(TAG, "알림 내용: ${it.body}")
        }

        // 데이터 메시지 처리 (기존 유지)
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "데이터 메시지: ${remoteMessage.data}")
            // 상세 데이터 로그
            Log.d(TAG, "🔍 디버깅: 데이터 메시지 상세:")
            for ((key, value) in remoteMessage.data) {
                Log.d(TAG, "🔍 디버깅: - $key: $value")
            }
        } else {
            Log.d(TAG, "🔍 디버깅: 데이터 메시지 없음")
        }

        // 실제 알림 표시
        Log.d(TAG, "🔍 디버깅: 알림 표시 프로세스 시작")
        try {
            Log.d(TAG, "🔍 디버깅: NotificationHelper 생성 시작")
            val notificationHelper = NotificationHelper(applicationContext)
            Log.d(TAG, "🔍 디버깅: NotificationHelper 생성 완료")

            Log.d(TAG, "🔍 디버깅: showNotification 호출 시작")
            notificationHelper.showNotification(title, body, data)
            Log.d(TAG, "✅ 디버깅: 알림 표시 요청 완료")

        } catch (e: Exception) {
            Log.e(TAG, "❌ 디버깅: 알림 표시 실패: ${e.message}", e)
            Log.e(TAG, "❌ 디버깅: 예외 타입: ${e.javaClass.simpleName}")
            Log.e(TAG, "❌ 디버깅: 스택 트레이스:")
            e.printStackTrace()
        }

        Log.d(TAG, "🔍 디버깅: === FCM 메시지 수신 완료 ===")
    }

    /**
     * 서버로 FCM 토큰을 전송합니다
     * AuthRepository를 통해 실제 API 호출
     */
    private fun sendTokenToServer(token: String) {
        Log.d(TAG, "서버로 FCM 토큰 전송 시작: $token")

        // 코루틴 스코프에서 비동기 처리
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 로그인 상태 확인
                if (authRepository.isLoggedIn()) {
                    Log.d(TAG, "로그인 상태 확인됨 - FCM 토큰 업데이트 진행")
                    val response = authRepository.updateFcmToken(token)

                    if (response.success) {
                        Log.d(TAG, "✅ FCM 토큰 서버 업데이트 성공")
                    } else {
                        Log.e(TAG, "❌ FCM 토큰 서버 업데이트 실패: ${response.error?.message}")
                    }
                } else {
                    Log.d(TAG, "로그인 상태가 아님 - FCM 토큰 업데이트 생략")
                }
            } catch (e: Exception) {
                Log.e(TAG, "🔥 FCM 토큰 서버 전송 예외: ${e.message}", e)
            }
        }
    }
}