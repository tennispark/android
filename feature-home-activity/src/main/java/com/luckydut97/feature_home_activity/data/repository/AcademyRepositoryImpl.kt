package com.luckydut97.feature_home_activity.data.repository

import com.luckydut97.feature_home_activity.data.model.Academy
import com.luckydut97.feature_home_activity.data.mapper.toAcademy
import com.luckydut97.tennispark.core.data.repository.AcademyRepository as CoreAcademyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * 아카데미 데이터 Repository 인터페이스
 */
interface AcademyRepository {

    /**
     * 아카데미 목록 조회
     */
    suspend fun getAcademies(): Flow<List<Academy>>

    /**
     * 아카데미 신청 (문자열 ID 버전 - 하위 호환성)
     */
    suspend fun applyForAcademy(academyId: String): Result<String>

    /**
     * 아카데미 신청 (실제 서버 ID 버전)
     */
    suspend fun applyForAcademyWithId(actualAcademyId: Long): Result<String>
}

/**
 * 🔥 실제 API를 호출하는 Repository 구현체 (Core Repository 사용)
 */
class AcademyRepositoryImpl : AcademyRepository {

    private val tag = "🔍 FeatureAcademyRepo"
    private val coreRepository = CoreAcademyRepository()

    override suspend fun getAcademies(): Flow<List<Academy>> = flow {
        try {

            val response = coreRepository.getAcademies()

            if (response.success) {
                val academyListResponse = response.response
                if (academyListResponse != null) {
                    val academies = academyListResponse.academies.map { it.toAcademy() }
                    emit(academies)
                } else {
                    throw Exception("아카데미 목록 데이터가 없습니다.")
                }
            } else {
                val errorMessage = response.error?.message ?: "아카데미 목록을 가져올 수 없습니다."

                // 서버에서 온 메시지를 우선 사용하고, 없을 때만 상태 코드로 판단
                val specificErrorMessage = if (errorMessage != "아카데미 목록을 가져올 수 없습니다.") {
                    errorMessage // 서버 메시지 우선 사용
                } else {
                    when {
                        errorMessage.contains("서버 오류") -> "서버 오류가 발생했습니다."
                        errorMessage.contains("인증") -> "인증이 되지 않았습니다."
                        errorMessage.contains("네트워크") -> "네트워크 오류가 발생했습니다."
                        else -> "오류가 발생했습니다."
                    }
                }

                throw Exception(specificErrorMessage)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun applyForAcademy(academyId: String): Result<String> {
        return try {

            // String ID를 Long으로 변환
            val actualId = academyId.toLongOrNull()
            if (actualId == null) {
                return Result.failure(Exception("잘못된 아카데미 ID입니다."))
            }

            val response = coreRepository.applyForAcademy(actualId)

            if (response.success) {
                Result.success("아카데미 신청이 완료되었습니다.")
            } else {
                val errorMessage = response.error?.message ?: "아카데미 신청에 실패했습니다."

                // 서버에서 온 메시지를 우선 사용하고, 없을 때만 상태 코드로 판단
                val specificErrorMessage = if (errorMessage != "아카데미 신청에 실패했습니다.") {
                    errorMessage // 서버 메시지 우선 사용
                } else {
                    when (response.error?.status) {
                        401 -> "인증이 필요합니다. 다시 로그인해주세요."
                        404 -> "해당 아카데미를 찾을 수 없습니다."
                        else -> errorMessage
                    }
                }

                Result.failure(Exception(specificErrorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun applyForAcademyWithId(actualAcademyId: Long): Result<String> {
        return try {

            val response = coreRepository.applyForAcademy(actualAcademyId)

            if (response.success) {
                Result.success("아카데미 신청이 완료되었습니다.")
            } else {
                val errorMessage = response.error?.message ?: "아카데미 신청에 실패했습니다."

                // 서버에서 온 메시지를 우선 사용하고, 없을 때만 상태 코드로 판단
                val specificErrorMessage = if (errorMessage != "아카데미 신청에 실패했습니다.") {
                    errorMessage // 서버 메시지 우선 사용
                } else {
                    when (response.error?.status) {
                        401 -> "인증이 필요합니다. 다시 로그인해주세요."
                        404 -> "해당 아카데미를 찾을 수 없습니다."
                        else -> errorMessage
                    }
                }

                Result.failure(Exception(specificErrorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
