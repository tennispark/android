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
        private const val TAG = "MyFirebaseMessaging"
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

        Log.d(TAG, "푸시 메시지 수신: ${remoteMessage.from}")

        // 알림 데이터 처리
        remoteMessage.notification?.let {
            Log.d(TAG, "알림 제목: ${it.title}")
            Log.d(TAG, "알림 내용: ${it.body}")
        }

        // 데이터 메시지 처리
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "데이터 메시지: ${remoteMessage.data}")
        }
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