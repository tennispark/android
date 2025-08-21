package com.luckydut97.tennispark.core.domain.usecase

import com.luckydut97.tennispark.core.domain.model.Academy
import com.luckydut97.tennispark.core.domain.repository.AcademyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * 아카데미 목록 조회 UseCase
 * 비즈니스 로직: 정렬, 필터링 등
 */
class GetAcademiesUseCase(
    private val academyRepository: AcademyRepository
) {
    suspend operator fun invoke(): Flow<List<Academy>> {
        return academyRepository.getAcademies()
            .map { academies ->
                // 비즈니스 규칙 적용: 날짜순 정렬
                academies.sortedBy { it.date }
            }
    }
}