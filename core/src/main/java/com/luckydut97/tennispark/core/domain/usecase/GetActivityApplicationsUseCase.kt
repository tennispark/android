package com.luckydut97.tennispark.core.domain.usecase

import com.luckydut97.tennispark.core.domain.model.ActivityApplication
import com.luckydut97.tennispark.core.domain.repository.ActivityApplicationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

/**
 * 활동 신청 내역 조회 UseCase
 * 비즈니스 로직: 날짜별 그룹핑, 정렬 등
 */
class GetActivityApplicationsUseCase(
    private val activityApplicationRepository: ActivityApplicationRepository
) {
    suspend operator fun invoke(): Flow<Map<LocalDate, List<ActivityApplication>>> {
        return activityApplicationRepository.getMyActivityApplications()
            .map { applications ->
                // 비즈니스 규칙: 신청날짜별로 그룹핑 (이미 내림차순 정렬되어 옴)
                applications.groupBy { it.applicationDate }
                    .toSortedMap(reverseOrder()) // 신청날짜 내림차순 유지
            }
    }

    /**
     * 플랫(flat) 리스트로 가져오기 (그룹핑 없이)
     */
    suspend fun getFlat(): Flow<List<ActivityApplication>> {
        return activityApplicationRepository.getMyActivityApplications()
    }
}