package com.luckydut97.feature_home_activity.domain.model

import java.time.LocalDate
import java.time.LocalTime

/**
 * 신청한 활동 도메인 모델
 */
data class AppliedActivity(
    val id: String,
    val activityId: String,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val gameCode: String,
    val location: String,
    val court: String,
    val applicationDate: LocalDate,
    val status: AppliedActivityStatus
) {
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
     * 신청일 포맷팅 (MM.dd 신청)
     */
    val formattedApplicationDate: String
        get() = "${applicationDate.monthValue.toString().padStart(2, '0')}.${applicationDate.dayOfMonth.toString().padStart(2, '0')} 신청"

    /**
     * 취소 가능 여부
     */
    val isCancellable: Boolean
        get() = status == AppliedActivityStatus.APPLIED || status == AppliedActivityStatus.CONFIRMED

    /**
     * 상태 표시 텍스트
     */
    val statusText: String
        get() = when (status) {
            AppliedActivityStatus.APPLIED -> "신청완료"
            AppliedActivityStatus.CONFIRMED -> "확정"
            AppliedActivityStatus.CANCELLED -> "취소됨"
            AppliedActivityStatus.COMPLETED -> "완료"
        }
}

/**
 * 신청한 활동 상태
 */
enum class AppliedActivityStatus {
    APPLIED,        // 신청완료
    CONFIRMED,      // 확정
    CANCELLED,      // 취소됨
    COMPLETED       // 완료
}