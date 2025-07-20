package com.luckydut97.tennispark.core.data.storage

import android.content.Context
import android.content.SharedPreferences

/**
 * 푸시 알림 설정을 관리하는 클래스
 */
class NotificationPreferenceManager(context: Context) {

    companion object {
        private const val TAG = "디버깅: NotificationPreferenceManager"
        private const val PREF_NAME = "notification_settings"
        private const val KEY_PUSH_ENABLED = "push_enabled"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    /**
     * 푸시 알림 수신 설정 저장
     */
    fun setPushNotificationEnabled(enabled: Boolean) {
        sharedPreferences.edit()
            .putBoolean(KEY_PUSH_ENABLED, enabled)
            .apply()
    }

    /**
     * 푸시 알림 수신 설정 조회
     * @param defaultValue 기본값 (앱 첫 실행 시 사용)
     */
    fun isPushNotificationEnabled(defaultValue: Boolean = true): Boolean {
        val enabled = sharedPreferences.getBoolean(KEY_PUSH_ENABLED, defaultValue)
        return enabled
    }

    /**
     * 푸시 알림 설정이 처음 설정되었는지 확인
     */
    fun isFirstTime(): Boolean {
        val isFirstTime = !sharedPreferences.contains(KEY_PUSH_ENABLED)
        return isFirstTime
    }
}