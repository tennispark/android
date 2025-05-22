
package com.luckydut97.feature_home_activity.data.repository
/*
인터페이스 + Mock 구현체
서버 API가 준비되면 MockWeeklyActivityRepository를 실제 구현체로 교체
 */
import com.luckydut97.feature_home_activity.domain.model.WeeklyActivity
import kotlinx.coroutines.flow.Flow

/**
 * 주간 활동 데이터 Repository 인터페이스
 */
interface WeeklyActivityRepository {

    /**
     * 주간 활동 목록 조회
     */
    suspend fun getWeeklyActivities(): Flow<List<WeeklyActivity>>

    /**
     * 활동 신청
     */
    suspend fun applyForActivity(activityId: String): Result<Unit>

    /**
     * 활동 신청 취소
     */
    suspend fun cancelActivity(activityId: String): Result<Unit>
}

/**
 * Mock Repository 구현체 (서버 API 준비 전까지 사용)
 */
class MockWeeklyActivityRepository : WeeklyActivityRepository {

    override suspend fun getWeeklyActivities(): Flow<List<WeeklyActivity>> {
        return kotlinx.coroutines.flow.flowOf(getMockActivities())
    }

    override suspend fun applyForActivity(activityId: String): Result<Unit> {
        // Mock 구현 - 실제로는 서버 API 호출
        return Result.success(Unit)
    }

    override suspend fun cancelActivity(activityId: String): Result<Unit> {
        // Mock 구현 - 실제로는 서버 API 호출
        return Result.success(Unit)
    }

    private fun getMockActivities(): List<WeeklyActivity> {
        return listOf(
            WeeklyActivity(
                id = "1",
                date = java.time.LocalDate.of(2024, 5, 13),
                startTime = java.time.LocalTime.of(20, 0),
                endTime = java.time.LocalTime.of(22, 0),
                gameCode = "게임코드",
                location = "양재 테니스장",
                court = "A코트",
                currentParticipants = 1,
                maxParticipants = 6,
                status = com.luckydut97.feature_home_activity.domain.model.ActivityStatus.RECRUITING
            ),
            WeeklyActivity(
                id = "2",
                date = java.time.LocalDate.of(2024, 5, 13),
                startTime = java.time.LocalTime.of(20, 0),
                endTime = java.time.LocalTime.of(22, 0),
                gameCode = "게임코드",
                location = "양재 테니스장",
                court = "A코트",
                currentParticipants = 5,
                maxParticipants = 6,
                status = com.luckydut97.feature_home_activity.domain.model.ActivityStatus.RECRUITING
            ),
            WeeklyActivity(
                id = "3",
                date = java.time.LocalDate.of(2024, 5, 13),
                startTime = java.time.LocalTime.of(20, 0),
                endTime = java.time.LocalTime.of(22, 0),
                gameCode = "게임코드",
                location = "양재 테니스장",
                court = "A코트",
                currentParticipants = 6,
                maxParticipants = 6,
                status = com.luckydut97.feature_home_activity.domain.model.ActivityStatus.FULL
            ),
            WeeklyActivity(
                id = "4",
                date = java.time.LocalDate.of(2024, 5, 13),
                startTime = java.time.LocalTime.of(20, 0),
                endTime = java.time.LocalTime.of(22, 0),
                gameCode = "게임코드",
                location = "양재 테니스장",
                court = "A코트",
                currentParticipants = 5,
                maxParticipants = 6,
                status = com.luckydut97.feature_home_activity.domain.model.ActivityStatus.RECRUITING
            )
        )
    }
}