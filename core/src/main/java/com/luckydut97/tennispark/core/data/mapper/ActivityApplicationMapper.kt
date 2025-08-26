package com.luckydut97.tennispark.core.data.mapper

import com.luckydut97.tennispark.core.data.model.ActivityApplicationResponse
import com.luckydut97.tennispark.core.domain.model.ActivityApplication
import com.luckydut97.tennispark.core.domain.model.ApplicationStatus
import com.luckydut97.tennispark.core.utils.toKoreanCourtType
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * ActivityApplicationResponse를 ActivityApplication 도메인 모델로 변환
 */
fun ActivityApplicationResponse.toActivityApplication(): ActivityApplication {
    // 신청 날짜 파싱: 한국어 형식도 처리 (서버에서 한국어로 전송됨)
    val applicationDate = parseKoreanDate(this.applicationDate)

    // 활동 날짜 파싱: "2025년 06월 09일(월)" -> LocalDate  
    val activityDate = parseKoreanDate(activity.date)

    // 시간 파싱: "10:00" -> LocalTime
    val startTime = LocalTime.parse(activity.startAt, DateTimeFormatter.ofPattern("HH:mm"))
    val endTime = LocalTime.parse(activity.endAt, DateTimeFormatter.ofPattern("HH:mm"))

    // 상태 매핑
    val status = when (applicationStatus) {
        "PENDING" -> ApplicationStatus.PENDING
        "WAITING" -> ApplicationStatus.WAITING
        "APPROVED" -> ApplicationStatus.APPROVED
        "CANCELED" -> ApplicationStatus.CANCELED
        else -> ApplicationStatus.PENDING
    }

    // 코트 타입 한글 변환
    val koreanCourtType = activity.courtType.toKoreanCourtType()

    return ActivityApplication(
        id = id,
        applicationDate = applicationDate,
        applicationStatus = status,
        activityDate = activityDate,
        startTime = startTime,
        endTime = endTime,
        place = activity.place,
        courtType = koreanCourtType
    )
}

/**
 * 한국어 날짜 문자열 파싱 (통합 함수)
 * "2025년 06월 09일(월)" 또는 "2025-04-25" -> LocalDate
 */
private fun parseKoreanDate(dateString: String): LocalDate {
    return try {
        // 1. ISO 형식 시도: "2025-04-25"
        if (dateString.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
            return LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE)
        }

        // 2. 한국어 형식 시도: "2025년 06월 09일(월)"
        val regex = """(\d{4})년\s*(\d{1,2})월\s*(\d{1,2})일""".toRegex()
        val matchResult = regex.find(dateString)

        if (matchResult != null) {
            val (year, month, day) = matchResult.destructured
            val formattedDate = "$year-${month.padStart(2, '0')}-${day.padStart(2, '0')}"
            LocalDate.parse(formattedDate, DateTimeFormatter.ISO_LOCAL_DATE)
        } else {
            // 파싱 실패 시 현재 날짜 반환
            android.util.Log.w(
                "ActivityApplicationMapper",
                "Failed to parse date: $dateString, using current date"
            )
            LocalDate.now()
        }
    } catch (e: Exception) {
        android.util.Log.e(
            "ActivityApplicationMapper",
            "Exception parsing date: $dateString",
            e
        )
        LocalDate.now()
    }
}

/**
 * 활동 날짜 문자열 파싱 (기존 함수는 호환성을 위해 유지)
 * "2025년 06월 09일(월)" -> LocalDate
 */
private fun parseActivityDate(dateString: String): LocalDate {
    return parseKoreanDate(dateString)
}