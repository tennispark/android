package com.luckydut97.tennispark.core.domain.repository

import com.luckydut97.tennispark.core.domain.model.WeeklyActivity
import com.luckydut97.tennispark.core.domain.model.AppliedActivity
import kotlinx.coroutines.flow.Flow

/**
 * 활동 도메인 Repository 인터페이스 (Clean Architecture)
 */
interface ActivityRepository {

    /**
     * 주간 활동 목록 조회
     */
    suspend fun getWeeklyActivities(): Flow<List<WeeklyActivity>>

    /**
     * 활동 신청
     */
    suspend fun applyForActivity(activityId: Long): Result<Unit>

    /**
     * 신청한 활동 목록 조회
     */
    suspend fun getAppliedActivities(): Flow<List<AppliedActivity>>

    /**
     * 활동 신청 취소
     */
    suspend fun cancelActivity(activityId: String): Result<Unit>
}