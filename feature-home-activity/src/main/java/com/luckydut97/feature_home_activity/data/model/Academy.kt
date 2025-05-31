package com.luckydut97.feature_home_activity.data.model

/**
 * 아카데미 데이터 모델
 */
data class Academy(
    val id: String,
    val date: String,        // "05.13 (화)"
    val time: String,        // "20:00 ~ 22:00"
    val court: String,       // "게임코트"
    val location: String,    // "양재 테니스코트"
    val activityType: String, // "A클래스"
    val currentParticipants: Int, // 현재 참가자 수
    val maxParticipants: Int,     // 최대 참가자 수
    val status: AcademyStatus = AcademyStatus.AVAILABLE
)

/**
 * 아카데미 상태
 */
enum class AcademyStatus(val displayText: String, val colorCode: String) {
    AVAILABLE("신청가능", "#145F44"),    // 초록색
    FULL("마감", "#EF3629")            // 빨간색
}