package com.luckydut97.tennispark.core.domain.usecase

import com.luckydut97.tennispark.core.domain.model.ActivityStatus
import com.luckydut97.tennispark.core.domain.repository.ActivityRepository
import kotlinx.coroutines.flow.first

/**
 * 활동 신청 UseCase
 * 비즈니스 로직: 신청 가능성 검증, 상태 확인 등
 */
class ApplyForActivityUseCase(
    private val activityRepository: ActivityRepository
) {
    suspend operator fun invoke(activityId: String): Result<Unit> {
        return try {
            // 1. 현재 활동 목록에서 해당 활동 찾기
            val activities = activityRepository.getWeeklyActivities().first()
            val targetActivity = activities.find { it.id == activityId }

            // 2. 비즈니스 규칙 검증
            when {
                targetActivity == null -> {
                    Result.failure(Exception("활동을 찾을 수 없습니다."))
                }

                targetActivity.actualActivityId == null -> {
                    Result.failure(Exception("서버 데이터 오류로 인해 현재 신청할 수 없습니다.\n나중에 다시 시도해주세요."))
                }

                targetActivity.status == ActivityStatus.FULL -> {
                    Result.failure(Exception("이미 모집이 완료된 활동입니다."))
                }

                targetActivity.status == ActivityStatus.UNAVAILABLE -> {
                    Result.failure(Exception("현재 신청할 수 없는 활동입니다."))
                }

                else -> {
                    // 3. 실제 신청 처리
                    activityRepository.applyForActivity(targetActivity.actualActivityId!!)
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}