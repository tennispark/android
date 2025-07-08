package com.luckydut97.tennispark.core.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * FCM 토큰 관리 클래스
 * Firebase Cloud Messaging 토큰을 발급, 갱신, 관리하는 역할
 */
class FcmTokenManager {

    companion object {
        private const val TAG = "FcmTokenManager"
    }

    /**
     * FCM 토큰을 비동기적으로 가져옵니다
     * @return FCM 토큰 문자열 또는 null (실패 시)
     */
    suspend fun getFcmToken(): String? = withContext(Dispatchers.IO) {
        try {
            val token = FirebaseMessaging.getInstance().token.await()
            Log.d(TAG, "FCM 토큰 가져오기 성공: $token")
            token
        } catch (e: Exception) {
            Log.e(TAG, "FCM 토큰 가져오기 실패", e)
            null
        }
    }

    /**
     * FCM 토큰 새로고침을 요청합니다
     * @return 새로 발급된 FCM 토큰 또는 null (실패 시)
     */
    suspend fun refreshFcmToken(): String? = withContext(Dispatchers.IO) {
        try {
            FirebaseMessaging.getInstance().deleteToken().await()
            val newToken = FirebaseMessaging.getInstance().token.await()
            Log.d(TAG, "FCM 토큰 새로고침 성공: $newToken")
            newToken
        } catch (e: Exception) {
            Log.e(TAG, "FCM 토큰 새로고침 실패", e)
            null
        }
    }

    /**
     * FCM 토큰 변경 시 콜백을 등록합니다
     * @param onTokenRefresh 토큰이 갱신되었을 때 호출되는 콜백
     */
    fun observeTokenRefresh(onTokenRefresh: (String) -> Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "FCM 토큰 가져오기 실패", task.exception)
                return@addOnCompleteListener
            }

            val token = task.result
            Log.d(TAG, "FCM 토큰 변경 감지: $token")
            onTokenRefresh(token)
        }
    }

    /**
     * FCM 토큰 유효성을 검사합니다
     * @param token 검사할 FCM 토큰
     * @return 유효한 토큰 여부
     */
    fun isValidFcmToken(token: String?): Boolean {
        return !token.isNullOrBlank() && token.length > 50
    }
}