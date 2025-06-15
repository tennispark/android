/*
도메인 모델 + ActivityStatus enum
 */
package com.luckydut97.feature_home_activity.domain.model

import java.time.LocalDate
import java.time.LocalTime

/**
 * 주간 활동 도메인 모델
 */
data class WeeklyActivity(
    val id: String,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val gameCode: String,
    val location: String,
    val court: String,
    val currentParticipants: Int,
    val maxParticipants: Int,
    val status: ActivityStatus
) {
    /**
     * 마감임박 여부 확인 (최대인원 - 현재인원 = 1)
     */
    val isAlmostFull: Boolean
        get() = maxParticipants - currentParticipants == 1 && status == ActivityStatus.RECRUITING

    /**
     * 날짜 포맷팅 (MM.dd (요일))
     */
    val formattedDate: String
        get() {
            val dayOfWeek = when (date.dayOfWeek.value) {
                1 -> "월"
                2 -> "화"
                3 -> "수"
                4 -> "목"
                5 -> "금"
                6 -> "토"
                7 -> "일"
                else -> ""
            }
            return "${date.monthValue.toString().padStart(2, '0')}.${date.dayOfMonth.toString().padStart(2, '0')} ($dayOfWeek)"
        }

    /**
     * 시간 포맷팅 (HH:mm ~ HH:mm)
     */
    val formattedTime: String
        get() = "${startTime.hour.toString().padStart(2, '0')}:${startTime.minute.toString().padStart(2, '0')} ~ ${endTime.hour.toString().padStart(2, '0')}:${endTime.minute.toString().padStart(2, '0')}"

    /**
     * 인원 정보 포맷팅 (현재인원/최대인원)
     */
    val participantInfo: String
        get() = "$currentParticipants/$maxParticipants"
}

/**
 * 활동 상태
 */
enum class ActivityStatus {
    RECRUITING,     // 모집 중
    ALMOST_FULL,    // 마감임박
    FULL           // 모집완료
}