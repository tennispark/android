package com.luckydut97.feature_home_activity.data.mapper

import com.luckydut97.tennispark.core.data.model.AcademyResponse
import com.luckydut97.feature_home_activity.data.model.Academy
import com.luckydut97.feature_home_activity.data.model.AcademyStatus
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * AcademyResponse를 Academy 도메인 모델로 변환
 */
fun AcademyResponse.toAcademy(): Academy {
    // 디버깅을 위한 로그
    android.util.Log.d("AcademyMapper", "변환 중: academyId=$id, date=$date")

    // 시간 파싱: "10:00" -> LocalTime
    val startTime = LocalTime.parse(startAt, DateTimeFormatter.ofPattern("HH:mm"))
    val endTime = LocalTime.parse(endAt, DateTimeFormatter.ofPattern("HH:mm"))

    // 시간 범위를 문자열로 변환: "10:00 ~ 14:00"
    val timeRange = "${startAt} ~ ${endAt}"

    // lessonType을 activityType으로 매핑
    val activityType = when (lessonType) {
        "FOREHAND_BACKHAND" -> "포핸드/백핸드"
        "VOLLEY_SERVE" -> "발리/서브"
        else -> lessonType // 기본값으로 원래 값 사용
    }

    // 상태 결정
    val status = when {
        participantCount >= capacity -> AcademyStatus.FULL
        else -> AcademyStatus.AVAILABLE
    }

    android.util.Log.d("AcademyMapper", "✅ 아카데미 변환 완료: $id")

    return Academy(
        id = id.toString(),
        date = date, // "05.13(화)" 형식 그대로 사용
        time = timeRange,
        court = courtName,
        location = place.name,
        activityType = activityType,
        currentParticipants = participantCount,
        maxParticipants = capacity,
        status = status
    )
}