//클린아키텍쳐 리팩토링 O
package com.luckydut97.tennispark.core.data.repository

import com.luckydut97.tennispark.core.domain.repository.ActivityRepository
import com.luckydut97.tennispark.core.domain.model.WeeklyActivity
import com.luckydut97.tennispark.core.domain.model.AppliedActivity
import com.luckydut97.tennispark.core.domain.model.AppliedActivityStatus
import com.luckydut97.tennispark.core.data.mapper.toWeeklyActivity
import com.luckydut97.tennispark.core.data.network.ApiService
import com.luckydut97.tennispark.core.data.model.ApiResponse
import com.luckydut97.tennispark.core.data.model.ActivityListResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate
import java.time.LocalTime

/**
 * 활동 Repository 구현체 (Clean Architecture)
 * 기존 WeeklyActivityRepository 로직을 통합 - Flow 문제 해결
 */
class ActivityRepositoryImpl(
    private val apiService: ApiService
) : ActivityRepository {

    private val tag = "🔍 CoreActivityRepository"

    // 기존 Core Repository 사용 (래핑 방식)
    private val coreRepository =
        com.luckydut97.tennispark.core.data.repository.WeeklyActivityRepository()

    override suspend fun getWeeklyActivities(): Flow<List<WeeklyActivity>> = flow {
        android.util.Log.d(tag, "getWeeklyActivities() called - using wrapper approach")
        try {
            // 기존 방식: Core Repository 래핑
            val response = coreRepository.getActivities()
            android.util.Log.d(tag, "Core API response: success=${response.success}")

            if (response.success) {
                val activityListResponse = response.response
                if (activityListResponse != null) {
                    val activities = activityListResponse.activities.map { it.toWeeklyActivity() }
                    android.util.Log.d(tag, "Successfully mapped ${activities.size} activities")
                    emit(activities)
                } else {
                    throw Exception("활동 목록 데이터가 없습니다.")
                }
            } else {
                val errorMessage = response.error?.message ?: "활동 목록을 가져올 수 없습니다."
                val cleanErrorMessage = when {
                    errorMessage.contains("서버 오류") -> "서버 오류가 발생했습니다."
                    errorMessage.contains("인증") -> "인증이 되지 않았습니다."
                    errorMessage.contains("네트워크") -> "네트워크 오류가 발생했습니다."
                    else -> "오류가 발생했습니다."
                }
                throw Exception(cleanErrorMessage)
            }
        } catch (e: Exception) {
            android.util.Log.e(tag, "Exception in getWeeklyActivities: ${e.message}", e)
            throw e
        }
    }

    override suspend fun applyForActivity(activityId: Long): Result<Unit> {
        return try {
            android.util.Log.d(tag, "applyForActivity called with id: $activityId")

            // 기존 방식: Core Repository 래핑
            val response = coreRepository.applyForActivity(activityId)

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

    override suspend fun getAppliedActivities(): Flow<List<AppliedActivity>> {
        // TODO: 실제 API 구현 시 교체
        return flowOf(getMockAppliedActivities())
    }

    override suspend fun cancelActivity(activityId: String): Result<Unit> {
        // TODO: 실제 API 구현
        return Result.success(Unit)
    }

    /**
     * Mock 데이터 (실제 API 구현 전까지 사용)
     */
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
                date = LocalDate.of(2024, 5, 15),
                startTime = LocalTime.of(18, 0),
                endTime = LocalTime.of(20, 0),
                gameCode = "게임코트",
                location = "강남 테니스장",
                court = "B코트",
                applicationDate = LocalDate.of(2024, 5, 8),
                status = AppliedActivityStatus.APPLIED
            )
        )
    }
}