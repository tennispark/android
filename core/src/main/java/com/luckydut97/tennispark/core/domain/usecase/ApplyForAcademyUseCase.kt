package com.luckydut97.tennispark.core.domain.usecase

import com.luckydut97.tennispark.core.domain.model.AcademyStatus
import com.luckydut97.tennispark.core.domain.repository.AcademyRepository
import kotlinx.coroutines.flow.first

/**
 * 아카데미 신청 UseCase
 * 비즈니스 로직: 신청 가능성 검증, 상태 확인 등
 */
class ApplyForAcademyUseCase(
    private val academyRepository: AcademyRepository
) {
    suspend operator fun invoke(academyId: String): Result<String> {
        return try {
            // 1. 현재 아카데미 목록에서 해당 아카데미 찾기
            val academies = academyRepository.getAcademies().first()
            val targetAcademy = academies.find { it.id == academyId }

            // 2. 비즈니스 규칙 검증
            when {
                targetAcademy == null -> {
                    Result.failure(Exception("아카데미를 찾을 수 없습니다."))
                }

                targetAcademy.actualAcademyId == null -> {
                    Result.failure(Exception("잘못된 아카데미 ID입니다."))
                }

                targetAcademy.status == AcademyStatus.FULL -> {
                    Result.failure(Exception("이미 마감된 아카데미입니다."))
                }

                else -> {
                    // 3. 실제 신청 처리
                    academyRepository.applyForAcademy(targetAcademy.actualAcademyId!!)
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}