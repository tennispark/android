package com.luckydut97.tennispark.core.utils

/**
 * HomeTopAppBar와 AlarmScreen 간 배지 수 공유를 위한 간단한 매니저
 */
object BadgeCountManager {

    private var currentBadgeCount: Int = 0

    /**
     * HomeTopAppBar에서 서버 조회한 배지 수 저장
     */
    fun setBadgeCount(count: Int) {
        currentBadgeCount = count
    }

    /**
     * AppPushViewModel에서 사용할 배지 수 조회
     */
    fun getBadgeCount(): Int {
        return currentBadgeCount
    }

    /**
     * 배지 수 초기화
     */
    fun clearBadgeCount() {
        currentBadgeCount = 0
    }
}