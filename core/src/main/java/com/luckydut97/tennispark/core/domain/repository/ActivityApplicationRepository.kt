package com.luckydut97.tennispark.core.domain.repository

import com.luckydut97.tennispark.core.domain.model.ActivityApplication
import kotlinx.coroutines.flow.Flow

/**
 * 활동 신청 내역 도메인 Repository 인터페이스 (Clean Architecture)
 */
interface ActivityApplicationRepository {

    /**
     * 나의 활동 신청 내역 조회
     * 신청날짜 내림차순으로 정렬됨
     */
    suspend fun getMyActivityApplications(): Flow<List<ActivityApplication>>
}