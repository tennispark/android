package com.luckydut97.feature_home_activity.data.repository

import com.luckydut97.feature_home_activity.domain.model.AppliedActivity
import com.luckydut97.feature_home_activity.domain.model.AppliedActivityStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate
import java.time.LocalTime

/**
 * 신청한 활동 데이터 Repository 인터페이스
 */
interface AppliedActivityRepository {

    /**
     * 신청한 활동 목록 조회
     */
    suspend fun getAppliedActivities(): Flow<List<AppliedActivity>>
}

/**
 * Mock Repository 구현체 (서버 API 준비 전까지 사용)
 */
class MockAppliedActivityRepository : AppliedActivityRepository {

    override suspend fun getAppliedActivities(): Flow<List<AppliedActivity>> {
        return flowOf(getMockAppliedActivities())
    }

    private fun getMockAppliedActivities(): List<AppliedActivity> {
        return listOf(
            AppliedActivity(
                id = "app_1",
                activityId = "1",
                date = LocalDate.of(2024, 5, 13),
                startTime = LocalTime.of(20, 0),
                endTime = LocalTime.of(22, 0),
                gameCode = "게임코트",
                location = "양재 테니스장",
                court = "A코트",
                applicationDate = LocalDate.of(2024, 5, 10),
                status = AppliedActivityStatus.CONFIRMED
            ),
            AppliedActivity(
                id = "app_2",
                activityId = "2",
                date = LocalDate.of(2024, 5, 13),
                startTime = LocalTime.of(20, 0),
                endTime = LocalTime.of(22, 0),
                gameCode = "게임코트",
                location = "양재 테니스장",
                court = "A코트",
                applicationDate = LocalDate.of(2024, 5, 12),
                status = AppliedActivityStatus.APPLIED
            ),
            AppliedActivity(
                id = "app_3",
                activityId = "3",
                date = LocalDate.of(2024, 5, 15),
                startTime = LocalTime.of(18, 0),
                endTime = LocalTime.of(20, 0),
                gameCode = "게임코트",
                location = "강남 테니스장",
                court = "B코트",
                applicationDate = LocalDate.of(2024, 5, 8),
                status = AppliedActivityStatus.APPLIED
            ),
            AppliedActivity(
                id = "app_4",
                activityId = "4",
                date = LocalDate.of(2024, 5, 18),
                startTime = LocalTime.of(16, 0),
                endTime = LocalTime.of(18, 0),
                gameCode = "게임코트",
                location = "잠실 테니스장",
                court = "D코트",
                applicationDate = LocalDate.of(2024, 5, 13),
                status = AppliedActivityStatus.APPLIED
            ),
            AppliedActivity(
                id = "app_5",
                activityId = "5",
                date = LocalDate.of(2024, 5, 20),
                startTime = LocalTime.of(19, 0),
                endTime = LocalTime.of(21, 0),
                gameCode = "게임코트",
                location = "서초 테니스장",
                court = "C코트",
                applicationDate = LocalDate.of(2024, 5, 14),
                status = AppliedActivityStatus.APPLIED
            ),
            AppliedActivity(
                id = "app_6",
                activityId = "6",
                date = LocalDate.of(2024, 5, 22),
                startTime = LocalTime.of(20, 30),
                endTime = LocalTime.of(22, 30),
                gameCode = "게임코트",
                location = "양재 테니스장",
                court = "A코트",
                applicationDate = LocalDate.of(2024, 5, 15),
                status = AppliedActivityStatus.APPLIED
            )
        )
    }
}