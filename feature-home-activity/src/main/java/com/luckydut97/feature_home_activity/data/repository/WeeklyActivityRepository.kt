package com.luckydut97.feature_home_activity.data.repository

import com.luckydut97.feature_home_activity.domain.model.WeeklyActivity
import com.luckydut97.feature_home_activity.data.mapper.toWeeklyActivity
import com.luckydut97.tennispark.core.data.repository.WeeklyActivityRepository as CoreWeeklyActivityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * 주간 활동 데이터 Repository 인터페이스
 */
interface WeeklyActivityRepository {

    /**
     * 주간 활동 목록 조회
     */
    suspend fun getWeeklyActivities(): Flow<List<WeeklyActivity>>

    /**
     * 활동 신청 (문자열 ID 버전 - 하위 호환성)
     */
    suspend fun applyForActivity(activityId: String): Result<Unit>

    /**
     * 활동 신청 (실제 서버 ID 버전)
     */
    suspend fun applyForActivityWithId(actualActivityId: Long): Result<Unit>

    /**
     * 활동 신청 취소
     */
    suspend fun cancelActivity(activityId: String): Result<Unit>
}

/**
 * 🔥 실제 API를 호출하는 Repository 구현체 (Core Repository 사용)
 */
class WeeklyActivityRepositoryImpl : WeeklyActivityRepository {

    private val tag = "🔍 FeatureWeeklyActivityRepo"
    private val coreRepository = CoreWeeklyActivityRepository()

    override suspend fun getWeeklyActivities(): Flow<List<WeeklyActivity>> = flow {
        try {

            val response = coreRepository.getActivities()

            if (response.success) {
                val activityListResponse = response.response
                if (activityListResponse != null) {
                    val activities = activityListResponse.activities.map { it.toWeeklyActivity() }
                    emit(activities)
                } else {
                    throw Exception("활동 목록 데이터가 없습니다.")
                }
            } else {
                val errorMessage = response.error?.message ?: "활동 목록을 가져올 수 없습니다."

                // 깔끔한 에러 메시지 제공
                val cleanErrorMessage = when {
                    errorMessage.contains("서버 오류") -> "서버 오류가 발생했습니다."
                    errorMessage.contains("인증") -> "인증이 되지 않았습니다."
                    errorMessage.contains("네트워크") -> "네트워크 오류가 발생했습니다."
                    else -> "오류가 발생했습니다."
                }

                throw Exception(cleanErrorMessage)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun applyForActivity(activityId: String): Result<Unit> {
        return try {

            // ViewModel에서 이미 유효성 검사를 완료했으므로 바로 API 호출
            // activityId가 해시코드 형태라면 실제 서버 ID가 필요
            // 현재는 임시로 1을 사용하지만, 실제로는 ViewModel에서 actualActivityId를 전달받아야 함
            val actualId = 1L // 임시 수정: 실제로는 매개변수로 받아야 함

            val response = coreRepository.applyForActivity(actualId)

            if (response.success) {
                Result.success(Unit)
            } else {
                val errorMessage = response.error?.message ?: "활동 신청에 실패했습니다."
                val statusCode = response.error?.status

                // 서버에서 온 메시지를 우선 사용하고, 없을 때만 상태 코드로 판단
                val specificErrorMessage = if (errorMessage != "활동 신청에 실패했습니다.") {
                    errorMessage // 서버 메시지 우선 사용
                } else {
                    when (statusCode) {
                        401 -> "인증이 필요합니다. 다시 로그인해주세요."
                        404 -> "해당 활동을 찾을 수 없습니다."
                        500 -> "HTTP_500: $errorMessage" // 500 에러임을 명시
                        else -> errorMessage
                    }
                }

                Result.failure(Exception(specificErrorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun applyForActivityWithId(actualActivityId: Long): Result<Unit> {
        return try {

            val response = coreRepository.applyForActivity(actualActivityId)

            if (response.success) {
                Result.success(Unit)
            } else {
                val errorMessage = response.error?.message ?: "활동 신청에 실패했습니다."
                val statusCode = response.error?.status

                // 서버에서 온 메시지를 우선 사용하고, 없을 때만 상태 코드로 판단
                val specificErrorMessage = if (errorMessage != "활동 신청에 실패했습니다.") {
                    errorMessage // 서버 메시지 우선 사용
                } else {
                    when (statusCode) {
                        401 -> "인증이 필요합니다. 다시 로그인해주세요."
                        404 -> "해당 활동을 찾을 수 없습니다."
                        500 -> "HTTP_500: $errorMessage" // 500 에러임을 명시
                        else -> errorMessage
                    }
                }

                Result.failure(Exception(specificErrorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun cancelActivity(activityId: String): Result<Unit> {
        return Result.success(Unit)
    }

    private fun getMockActivities(): List<WeeklyActivity> {
        return listOf(
            WeeklyActivity(
                id = "1",
                date = java.time.LocalDate.of(2024, 5, 13),
                startTime = java.time.LocalTime.of(20, 0),
                endTime = java.time.LocalTime.of(22, 0),
                gameCode = "게임코트",
                location = "양재 테니스장",
                court = "A코트",
                currentParticipants = 1,
                maxParticipants = 6,
                status = com.luckydut97.feature_home_activity.domain.model.ActivityStatus.RECRUITING,
                actualActivityId = 123456
            ),
            WeeklyActivity(
                id = "2",
                date = java.time.LocalDate.of(2024, 5, 13),
                startTime = java.time.LocalTime.of(20, 0),
                endTime = java.time.LocalTime.of(22, 0),
                gameCode = "게임코트",
                location = "양재 테니스장",
                court = "A코트",
                currentParticipants = 5,
                maxParticipants = 6,
                status = com.luckydut97.feature_home_activity.domain.model.ActivityStatus.RECRUITING,
                actualActivityId = 123457
            ),
            WeeklyActivity(
                id = "3",
                date = java.time.LocalDate.of(2024, 5, 13),
                startTime = java.time.LocalTime.of(20, 0),
                endTime = java.time.LocalTime.of(22, 0),
                gameCode = "게임코트",
                location = "양재 테니스장",
                court = "A코트",
                currentParticipants = 6,
                maxParticipants = 6,
                status = com.luckydut97.feature_home_activity.domain.model.ActivityStatus.FULL,
                actualActivityId = 123458
            ),
            WeeklyActivity(
                id = "4",
                date = java.time.LocalDate.of(2024, 5, 13),
                startTime = java.time.LocalTime.of(20, 0),
                endTime = java.time.LocalTime.of(22, 0),
                gameCode = "게임코트",
                location = "양재 테니스장",
                court = "A코트",
                currentParticipants = 5,
                maxParticipants = 6,
                status = com.luckydut97.feature_home_activity.domain.model.ActivityStatus.RECRUITING,
                actualActivityId = 123459
            )
        )
    }
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

    override suspend fun applyForActivityWithId(actualActivityId: Long): Result<Unit> {
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
                gameCode = "게임코트",
                location = "양재 테니스장",
                court = "A코트",
                currentParticipants = 1,
                maxParticipants = 6,
                status = com.luckydut97.feature_home_activity.domain.model.ActivityStatus.RECRUITING,
                actualActivityId = 123456
            ),
            WeeklyActivity(
                id = "2",
                date = java.time.LocalDate.of(2024, 5, 13),
                startTime = java.time.LocalTime.of(20, 0),
                endTime = java.time.LocalTime.of(22, 0),
                gameCode = "게임코트",
                location = "양재 테니스장",
                court = "A코트",
                currentParticipants = 5,
                maxParticipants = 6,
                status = com.luckydut97.feature_home_activity.domain.model.ActivityStatus.RECRUITING,
                actualActivityId = 123457
            ),
            WeeklyActivity(
                id = "3",
                date = java.time.LocalDate.of(2024, 5, 13),
                startTime = java.time.LocalTime.of(20, 0),
                endTime = java.time.LocalTime.of(22, 0),
                gameCode = "게임코트",
                location = "양재 테니스장",
                court = "A코트",
                currentParticipants = 6,
                maxParticipants = 6,
                status = com.luckydut97.feature_home_activity.domain.model.ActivityStatus.FULL,
                actualActivityId = 123458
            ),
            WeeklyActivity(
                id = "4",
                date = java.time.LocalDate.of(2024, 5, 13),
                startTime = java.time.LocalTime.of(20, 0),
                endTime = java.time.LocalTime.of(22, 0),
                gameCode = "게임코트",
                location = "양재 테니스장",
                court = "A코트",
                currentParticipants = 5,
                maxParticipants = 6,
                status = com.luckydut97.feature_home_activity.domain.model.ActivityStatus.RECRUITING,
                actualActivityId = 123459
            )
        )
    }
}
