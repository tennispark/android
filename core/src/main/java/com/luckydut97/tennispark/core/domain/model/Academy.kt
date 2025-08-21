package com.luckydut97.tennispark.core.domain.model

/**
 * 아카데미 도메인 모델 (Core Domain)
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
    val status: AcademyStatus = AcademyStatus.AVAILABLE,
    val actualAcademyId: Long? = null // 실제 서버 Academy ID
)

/**
 * 아카데미 상태
 */
enum class AcademyStatus(val displayText: String, val colorCode: String) {
    AVAILABLE("신청가능", "#145F44"),    // 초록색
    FULL("마감", "#EF3629")            // 빨간색
}