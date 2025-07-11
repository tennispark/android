package com.luckydut97.feature_myinfo.viewmodel

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.luckydut97.tennispark.core.data.storage.NotificationPreferenceManager
import com.luckydut97.tennispark.core.data.storage.TokenManagerImpl
import com.luckydut97.tennispark.core.data.repository.AuthRepositoryImpl
import com.luckydut97.tennispark.core.data.network.NetworkModule
import com.luckydut97.tennispark.core.fcm.FcmTokenManager

class AppSettingsViewModel : ViewModel() {

    companion object {
        private const val TAG = "디버깅: AppSettingsViewModel"
    }

    private val notificationPreferenceManager: NotificationPreferenceManager by lazy {
        val context = NetworkModule.getContext()
        if (context != null) {
            NotificationPreferenceManager(context)
        } else {
            throw IllegalStateException("NetworkModule not initialized")
        }
    }

    private val fcmTokenManager = FcmTokenManager()

    private val authRepository by lazy {
        val context = NetworkModule.getContext()
        if (context != null) {
            val tokenManager = TokenManagerImpl(context)
            AuthRepositoryImpl(
                apiService = NetworkModule.apiService,
                tokenManager = tokenManager
            )
        } else {
            throw IllegalStateException("NetworkModule not initialized")
        }
    }

    private val _adPushEnabled = MutableStateFlow(true)
    val adPushEnabled: StateFlow<Boolean> = _adPushEnabled.asStateFlow()

    private val _infoPushEnabled = MutableStateFlow(false)
    val infoPushEnabled: StateFlow<Boolean> = _infoPushEnabled.asStateFlow()

    init {
        initializePushSettings()
    }

    /**
     * 푸시 알림 설정 초기화
     * 시스템 권한 상태와 저장된 설정을 확인하여 초기값 설정
     */
    private fun initializePushSettings() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "=== 푸시 알림 설정 초기화 시작 ===")

                val context = NetworkModule.getContext()
                if (context == null) {
                    Log.e(TAG, "Context가 null입니다.")
                    return@launch
                }

                // 시스템 알림 권한 확인
                val hasSystemPermission = checkSystemNotificationPermission(context)
                Log.d(TAG, "시스템 알림 권한 상태: $hasSystemPermission")

                // 저장된 설정 확인
                val isFirstTime = notificationPreferenceManager.isFirstTime()
                Log.d(TAG, "첫 실행 여부: $isFirstTime")

                val defaultValue = if (isFirstTime) {
                    // 첫 실행 시 시스템 권한 상태에 따라 기본값 설정
                    Log.d(TAG, "첫 실행 - 시스템 권한 상태에 따라 기본값 설정: $hasSystemPermission")
                    hasSystemPermission
                } else {
                    // 기존 설정 유지
                    true
                }

                val savedSetting =
                    notificationPreferenceManager.isPushNotificationEnabled(defaultValue)
                Log.d(TAG, "최종 푸시 알림 설정: $savedSetting")

                // 시스템 권한이 없으면 강제로 false
                val finalSetting = if (hasSystemPermission) savedSetting else false
                Log.d(TAG, "시스템 권한 확인 후 최종 설정: $finalSetting")

                _adPushEnabled.value = finalSetting

                // 첫 실행이면 설정 저장
                if (isFirstTime) {
                    notificationPreferenceManager.setPushNotificationEnabled(finalSetting)
                    Log.d(TAG, "첫 실행 - 초기 설정 저장 완료: $finalSetting")

                    // 첫 실행 시 FCM 토큰 업데이트
                    updateFcmTokenToServer(finalSetting)
                }

                Log.d(TAG, "=== 푸시 알림 설정 초기화 완료 ===")

            } catch (e: Exception) {
                Log.e(TAG, "푸시 알림 설정 초기화 실패: ${e.message}", e)
                _adPushEnabled.value = true // 기본값으로 설정
            }
        }
    }

    /**
     * 시스템 알림 권한 확인
     */
    private fun checkSystemNotificationPermission(context: Context): Boolean {
        return try {
            val notificationManager = NotificationManagerCompat.from(context)
            val isEnabled = notificationManager.areNotificationsEnabled()
            Log.d(TAG, "시스템 알림 권한 확인: $isEnabled")
            isEnabled
        } catch (e: Exception) {
            Log.e(TAG, "시스템 알림 권한 확인 실패: ${e.message}", e)
            false
        }
    }

    /**
     * FCM 토큰을 서버에 업데이트
     * @param isPushEnabled 푸시 알림 활성화 여부
     */
    private fun updateFcmTokenToServer(isPushEnabled: Boolean) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "=== FCM 토큰 서버 업데이트 시작 ===")
                Log.d(TAG, "푸시 알림 활성화: $isPushEnabled")

                // 로그인 상태 확인
                if (!authRepository.isLoggedIn()) {
                    Log.d(TAG, "로그인 상태가 아님 - FCM 토큰 업데이트 생략")
                    return@launch
                }

                val fcmToken = if (isPushEnabled) {
                    // 푸시 알림 활성화 시 실제 FCM 토큰 사용
                    val token = fcmTokenManager.getFcmToken()
                    Log.d(TAG, "FCM 토큰 발급: $token")
                    token ?: ""
                } else {
                    // 푸시 알림 비활성화 시 빈 문자열 사용
                    Log.d(TAG, "푸시 알림 비활성화 - 빈 문자열 사용")
                    ""
                }

                Log.d(
                    TAG,
                    "서버로 전송할 FCM 토큰: ${if (fcmToken.isEmpty()) "빈 문자열" else "실제 토큰 (${fcmToken.length}자)"}"
                )

                val response = authRepository.updateFcmToken(fcmToken)

                if (response.success) {
                    Log.d(TAG, "✅ FCM 토큰 서버 업데이트 성공!")
                } else {
                    Log.e(TAG, "❌ FCM 토큰 서버 업데이트 실패: ${response.error?.message}")
                }

            } catch (e: Exception) {
                Log.e(TAG, "🔥 FCM 토큰 서버 업데이트 예외: ${e.message}", e)
            }

            Log.d(TAG, "=== FCM 토큰 서버 업데이트 완료 ===")
        }
    }

    fun setAdPushEnabled(value: Boolean) {
        Log.d(TAG, "푸시 알림 설정 변경 요청: $value")

        val context = NetworkModule.getContext()
        if (context == null) {
            Log.e(TAG, "Context가 null입니다.")
            return
        }

        // 시스템 권한 확인
        val hasSystemPermission = checkSystemNotificationPermission(context)
        Log.d(TAG, "시스템 권한 상태: $hasSystemPermission")

        if (value && !hasSystemPermission) {
            Log.w(TAG, "시스템 알림 권한이 없어 푸시 알림을 켤 수 없습니다.")
            // 시스템 권한이 없으면 강제로 false
            _adPushEnabled.value = false
            notificationPreferenceManager.setPushNotificationEnabled(false)
            updateFcmTokenToServer(false)
            return
        }

        // 설정 저장
        Log.d(TAG, "푸시 알림 설정 저장: $value")
        _adPushEnabled.value = value
        notificationPreferenceManager.setPushNotificationEnabled(value)

        // FCM 토큰 서버 업데이트
        updateFcmTokenToServer(value)

        Log.d(TAG, "푸시 알림 설정 변경 완료: $value")
    }

    fun setInfoPushEnabled(value: Boolean) {
        _infoPushEnabled.value = value
    }
}
