package com.luckydut97.tennispark.core.data.mapper

import com.luckydut97.tennispark.core.data.model.AcademyResponse
import com.luckydut97.tennispark.core.domain.model.Academy
import com.luckydut97.tennispark.core.domain.model.AcademyStatus
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * AcademyResponse를 Academy 도메인 모델로 변환 (Core Mapper)
 */
fun AcademyResponse.toAcademy(): Academy {
    // 날짜 형식 변환: "2025년 06월 24일 (화)" -> "06월 24일 (화)"
    val formattedDate = formatDateString(date)

    // 시간 범위를 문자열로 변환: "10:00 ~ 14:00"
    val timeRange = "${startAt} ~ ${endAt}"

    // lessonType을 activityType으로 매핑
    val activityType = when (lessonType) {
        "LEVEL1" -> "레벨1"
        "LEVEL2" -> "레벨2"
        "LEVEL3" -> "레벨3"
        else -> lessonType // 기본값으로 원래 값 사용
    }

    // 상태 결정
    val status = when {
        participantCount >= capacity -> AcademyStatus.FULL
        else -> AcademyStatus.AVAILABLE
    }

    return Academy(
        id = id.toString(),
        date = formattedDate,
        time = timeRange,
        court = courtName,
        location = place.name,
        activityType = activityType,
        currentParticipants = participantCount,
        maxParticipants = capacity,
        status = status,
        actualAcademyId = id // 실제 서버 ID 저장
    )
}

/**
 * 날짜 문자열을 "2025년 06월 24일 (화)" 형식에서 "06월 24일 (화)" 형식으로 변환
 */
private fun formatDateString(dateString: String): String {
    return try {
        // 이미 "05월 13일 (화)" 형식이면 그대로 반환
        if (dateString.matches(Regex("\\d{2}월\\s*\\d{2}일\\s*\\([가-힣]\\)"))) {
            return dateString
        }

        // "2025년 06월 24일 (화)" 또는 "2025년 06월 24일(화)" 형식 파싱
        val regex = Regex("(\\d{4})년\\s*(\\d{1,2})월\\s*(\\d{1,2})일\\s*\\(?([가-힣])\\)?")
        val matchResult = regex.find(dateString)

        if (matchResult != null) {
            val (_, month, day, dayOfWeek) = matchResult.destructured
            val formattedMonth = month.padStart(2, '0')
            val formattedDay = day.padStart(2, '0')
            "${formattedMonth}월 ${formattedDay}일 ($dayOfWeek)"
        } else {
            // 파싱 실패 시 원본 반환
            dateString
        }
    } catch (e: Exception) {
        dateString
    }
}