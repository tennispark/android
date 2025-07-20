package com.luckydut97.feature_myinfo.viewmodel

import android.app.NotificationManager
import android.content.Context
import android.os.Build
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
                val context = NetworkModule.getContext()
                if (context == null) {
                    return@launch
                }

                // 시스템 알림 권한 확인
                val hasSystemPermission = checkSystemNotificationPermission(context)

                // 저장된 설정 확인
                val isFirstTime = notificationPreferenceManager.isFirstTime()

                val defaultValue = if (isFirstTime) {
                    // 첫 실행 시 시스템 권한 상태에 따라 기본값 설정
                    hasSystemPermission
                } else {
                    // 기존 설정 유지
                    true
                }

                val savedSetting =
                    notificationPreferenceManager.isPushNotificationEnabled(defaultValue)

                // 시스템 권한이 없으면 강제로 false
                val finalSetting = if (hasSystemPermission) savedSetting else false

                _adPushEnabled.value = finalSetting

                // 첫 실행이면 설정 저장
                if (isFirstTime) {
                    notificationPreferenceManager.setPushNotificationEnabled(finalSetting)

                    // 첫 실행 시 FCM 토큰 업데이트
                    updateFcmTokenToServer(finalSetting)
                }
            } catch (e: Exception) {
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
            isEnabled
        } catch (e: Exception) {
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
                // 로그인 상태 확인
                if (!authRepository.isLoggedIn()) {
                    return@launch
                }

                val fcmToken = if (isPushEnabled) {
                    // 푸시 알림 활성화 시 실제 FCM 토큰 사용
                    val token = fcmTokenManager.getFcmToken()
                    token ?: ""
                } else {
                    // 푸시 알림 비활성화 시 빈 문자열 사용
                    ""
                }

                val response = authRepository.updateFcmToken(fcmToken)

                if (response.success) {
                } else {
                }

            } catch (e: Exception) {
            }

        }
    }

    fun setAdPushEnabled(value: Boolean) {

        val context = NetworkModule.getContext()
        if (context == null) {
            return
        }

        // 시스템 권한 확인
        val hasSystemPermission = checkSystemNotificationPermission(context)

        if (value && !hasSystemPermission) {
            // 시스템 권한이 없으면 강제로 false
            _adPushEnabled.value = false
            notificationPreferenceManager.setPushNotificationEnabled(false)
            updateFcmTokenToServer(false)
            return
        }

        // 설정 저장
        _adPushEnabled.value = value
        notificationPreferenceManager.setPushNotificationEnabled(value)

        // FCM 토큰 서버 업데이트
        updateFcmTokenToServer(value)

    }

    fun setInfoPushEnabled(value: Boolean) {
        _infoPushEnabled.value = value
    }
}
