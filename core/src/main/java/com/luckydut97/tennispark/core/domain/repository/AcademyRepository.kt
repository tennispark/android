package com.luckydut97.tennispark.core.domain.repository

import com.luckydut97.tennispark.core.domain.model.Academy
import kotlinx.coroutines.flow.Flow

/**
 * 아카데미 도메인 Repository 인터페이스 (Clean Architecture)
 */
interface AcademyRepository {

    /**
     * 아카데미 목록 조회
     */
    suspend fun getAcademies(): Flow<List<Academy>>

    /**
     * 아카데미 신청
     */
    suspend fun applyForAcademy(academyId: Long): Result<String>
}