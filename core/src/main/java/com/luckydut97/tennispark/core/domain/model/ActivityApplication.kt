package com.luckydut97.tennispark.core.domain.model

import java.time.LocalDate
import java.time.LocalTime

/**
 * 활동 신청 내역 도메인 모델
 */
data class ActivityApplication(
    val id: Long,
    val applicationDate: LocalDate,          // 신청 날짜
    val applicationStatus: ApplicationStatus, // 신청 상태
    val activityDate: LocalDate,             // 활동 날짜
    val startTime: LocalTime,                // 시작 시간
    val endTime: LocalTime,                  // 종료 시간
    val place: String,                       // 장소명
    val courtType: String                    // 코트 타입 (한글 변환된 값)
) {
    /**
     * 활동 날짜 포맷팅 (05월 13일 (화))
     */
    val formattedActivityDate: String
        get() {
            val dayOfWeek = when (activityDate.dayOfWeek.value) {
                1 -> "월"
                2 -> "화"
                3 -> "수"
                4 -> "목"
                5 -> "금"
                6 -> "토"
                7 -> "일"
                else -> ""
            }
            return "${activityDate.monthValue.toString().padStart(2, '0')}월 ${
                activityDate.dayOfMonth.toString().padStart(2, '0')
            }일 ($dayOfWeek)"
        }

    /**
     * 활동 시간 포맷팅 (10:00~12:00)
     */
    val formattedActivityTime: String
        get() = "${startTime.hour.toString().padStart(2, '0')}:${
            startTime.minute.toString().padStart(2, '0')
        }~${endTime.hour.toString().padStart(2, '0')}:${
            endTime.minute.toString().padStart(2, '0')
        }"

    /**
     * 신청 날짜 포맷팅 (04월 25일)
     */
    val formattedApplicationDate: String
        get() = "${applicationDate.monthValue.toString().padStart(2, '0')}월 ${
            applicationDate.dayOfMonth.toString().padStart(2, '0')
        }일"
}

/**
 * 활동 신청 상태
 */
enum class ApplicationStatus(val displayText: String, val colorCode: String) {
    PENDING("미확정", "#D2D2D2"),
    WAITING("대기", "#FFB86C"),
    APPROVED("확정", "#145F44"),
    CANCELED("취소", "#EF3629")
}