package com.luckydut97.tennispark.core.utils

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 알림 배지 카운트 관리 클래스 (Singleton)
 * SharedPreferences 기반으로 로컬에서 배지 상태 관리
 * StateFlow로 실시간 배지 상태 감지 지원
 */
class NotificationBadgeManager private constructor(context: Context) {

    companion object {
        private const val PREF_NAME = "notification_badge"
        private const val KEY_UNREAD_COUNT = "unread_count"

        @Volatile
        private var INSTANCE: NotificationBadgeManager? = null

        /**
         * Singleton 인스턴스 반환
         */
        fun getInstance(context: Context): NotificationBadgeManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: NotificationBadgeManager(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    // 🔥 전역 StateFlow - 앱 전체에서 하나의 인스턴스 공유
    private val _badgeCount = MutableStateFlow(getUnreadCount())
    val badgeCount: StateFlow<Int> = _badgeCount.asStateFlow()

    /**
     * FCM 알림 수신 시 배지 카운트 증가
     */
    fun incrementBadge() {
        val currentCount = getUnreadCount()
        val newCount = currentCount + 1
        setUnreadCount(newCount)
        _badgeCount.value = newCount // 🔥 전역 StateFlow 업데이트
    }

    /**
     * 알림 화면 진입 시 배지 카운트 초기화
     */
    fun clearBadge() {
        setUnreadCount(0)
        _badgeCount.value = 0 // 🔥 전역 StateFlow 업데이트
    }

    /**
     * 현재 미읽은 알림 수 조회
     */
    fun getUnreadCount(): Int {
        return sharedPreferences.getInt(KEY_UNREAD_COUNT, 0)
    }

    /**
     * 미읽은 알림 수 설정 (private)
     */
    private fun setUnreadCount(count: Int) {
        sharedPreferences.edit()
            .putInt(KEY_UNREAD_COUNT, maxOf(0, count)) // 음수 방지
            .apply()
    }

    /**
     * 특정 개수만큼 배지 감소 (선택사항)
     */
    fun decrementBadge(count: Int = 1) {
        val currentCount = getUnreadCount()
        val newCount = currentCount - count
        setUnreadCount(newCount)
        _badgeCount.value = maxOf(0, newCount) // 🔥 전역 StateFlow 업데이트
    }
}