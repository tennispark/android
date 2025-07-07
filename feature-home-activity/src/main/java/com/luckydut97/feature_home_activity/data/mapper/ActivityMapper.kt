package com.luckydut97.feature_home_activity.data.mapper

import com.luckydut97.tennispark.core.data.model.ActivityResponse
import com.luckydut97.feature_home_activity.domain.model.WeeklyActivity
import com.luckydut97.feature_home_activity.domain.model.ActivityStatus
import com.luckydut97.tennispark.core.utils.toKoreanCourtType
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * ActivityResponse를 WeeklyActivity 도메인 모델로 변환
 */
fun ActivityResponse.toWeeklyActivity(): WeeklyActivity {
    // 디버깅을 위한 로그
    android.util.Log.d("ActivityMapper", "변환 중: activityId=$activityId, date=$date")

    // 날짜 파싱: "2025년 06월 24일 (화)" -> LocalDate
    val localDate = parseDate(date)

    // 시간 파싱: "10:00" -> LocalTime
    val startTime = LocalTime.parse(startAt, DateTimeFormatter.ofPattern("HH:mm"))
    val endTime = LocalTime.parse(endAt, DateTimeFormatter.ofPattern("HH:mm"))

    // 상태 결정 - 임시로 activityId=0인 경우도 신청 가능하도록 수정
    val status = when {
        participantCount >= capacity -> ActivityStatus.FULL
        capacity - participantCount == 1 -> ActivityStatus.ALMOST_FULL
        else -> ActivityStatus.RECRUITING
    }

    // activityId를 실제 서버 값으로 사용 (매핑 오류 수정됨)
    val finalActualActivityId = activityId

    // 표시용 ID도 실제 activityId 사용
    val finalId = activityId.toString()

    android.util.Log.d("ActivityMapper", "✅ 실제 activityId 사용: $activityId")

    return WeeklyActivity(
        id = finalId,
        date = localDate,
        startTime = startTime,
        endTime = endTime,
        gameCode = courtType.toKoreanCourtType(),
        location = place.name,
        court = courtName,
        currentParticipants = participantCount,
        maxParticipants = capacity,
        status = status,
        actualActivityId = finalActualActivityId // 실제 서버 ID 저장
    )
}

/**
 * 날짜 문자열 파싱 함수
 * "2025년 06월 24일 (화)" -> LocalDate
 */
private fun parseDate(dateString: String): LocalDate {
    // 서버 응답 형식: "2025년 06월 24일 (화)" 또는 "2025년 06월 24일(화)"
    // 정규식을 사용해서 년, 월, 일을 추출
    val regex = """(\d{4})년\s*(\d{1,2})월\s*(\d{1,2})일""".toRegex()
    val matchResult = regex.find(dateString)

    if (matchResult != null) {
        val (year, month, day) = matchResult.destructured
        val formattedDate = "$year-${month.padStart(2, '0')}-${day.padStart(2, '0')}"
        return LocalDate.parse(formattedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    } else {
        // 파싱 실패 시 로그를 남기고 현재 날짜 반환
        android.util.Log.e("ActivityMapper", "날짜 파싱 실패: $dateString")
        return LocalDate.now()
    }
}
