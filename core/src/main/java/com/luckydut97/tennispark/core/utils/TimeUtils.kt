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
     * 요구사항에 맞는 형식:
     * - 당일: "1시간 전", "2시간 전", ..., "23시간 전"
     * - 하루 지나면: "어제"
     * - 2일 후 ~ 7일 후: "2일 전", "3일 전", ..., "7일 전"
     * - 일주일 후: "8/22" (날짜 형식)
     */
    fun getRelativeTimeString(dateTime: LocalDateTime): String {
        val now = LocalDateTime.now()
        val duration = Duration.between(dateTime, now)

        return when {
            duration.isNegative -> "방금 전" // 미래 시간인 경우
            duration.toMinutes() < 1 -> "방금 전"
            duration.toMinutes() < 60 -> "${duration.toMinutes()}분 전"
            duration.toHours() < 24 -> "${duration.toHours()}시간 전" // 1~23시간 전
            duration.toDays() == 1L -> "어제" // 정확히 하루 지났을 때
            duration.toDays() in 2..7 -> "${duration.toDays()}일 전" // 2~7일 전
            else -> {
                // 일주일 후는 M/dd 형식으로 표시
                val formatter = DateTimeFormatter.ofPattern("M/dd")
                dateTime.format(formatter)
            }
        }
    }

    /**
     * ISO 8601 형식의 문자열을 LocalDateTime으로 파싱
     * 예: "2025-09-07T02:32:59.122272"
     */
    fun parseIsoDateTime(dateTimeString: String): LocalDateTime {
        return LocalDateTime.parse(dateTimeString)
    }

    /**
     * 커뮤니티 게시글용 시간 표시 함수
     * ISO 8601 형식의 문자열을 받아서 상대 시간 문자열로 변환
     */
    fun getCommunityTimeString(createdAt: String): String {
        return try {
            val dateTime = parseIsoDateTime(createdAt)
            getRelativeTimeString(dateTime)
        } catch (e: Exception) {
            "시간 정보 없음"
        }
    }
}