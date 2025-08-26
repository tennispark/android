package com.luckydut97.tennispark.core.utils

import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 시간 관련 유틸리티
 */
object TimeUtils {

    /**
     * LocalDateTime을 상대 시간 문자열로 변환
     * "방금 전", "50분 전", "3시간 전", "2일 전", "07월 20일" 형식
     */
    fun getRelativeTimeString(dateTime: LocalDateTime): String {
        val now = LocalDateTime.now()
        val duration = Duration.between(dateTime, now)

        return when {
            duration.isNegative -> "방금 전" // 미래 시간인 경우
            duration.toMinutes() < 1 -> "방금 전"
            duration.toMinutes() < 60 -> "${duration.toMinutes()}분 전"
            duration.toHours() < 24 -> "${duration.toHours()}시간 전"
            duration.toDays() < 7 -> "${duration.toDays()}일 전"
            else -> {
                // 7일 이상 지난 경우 날짜 형식으로 표시
                val formatter = DateTimeFormatter.ofPattern("MM월 dd일")
                dateTime.format(formatter)
            }
        }
    }
}