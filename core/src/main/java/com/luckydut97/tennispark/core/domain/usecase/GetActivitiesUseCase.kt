package com.luckydut97.tennispark.core.domain.usecase

import com.luckydut97.tennispark.core.domain.model.WeeklyActivity
import com.luckydut97.tennispark.core.domain.repository.ActivityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * 활동 목록 조회 UseCase
 * 비즈니스 로직: 신청 가능한 활동만 필터링, 정렬 등
 */
class GetActivitiesUseCase(
    private val activityRepository: ActivityRepository
) {
    suspend operator fun invoke(): Flow<List<WeeklyActivity>> {
        return activityRepository.getWeeklyActivities()
            .map { activities ->
                // 비즈니스 규칙 적용
                activities
                    .filter { it.actualActivityId != null } // 신청 가능한 활동만
                    .sortedWith(
                        compareBy<WeeklyActivity> { it.date }
                            .thenBy { it.startTime }
                    )
            }
    }
}