//클린아키텍쳐 리팩토링 O
package com.luckydut97.tennispark.core.data.repository

import com.luckydut97.tennispark.core.domain.repository.AcademyRepository
import com.luckydut97.tennispark.core.domain.model.Academy
import com.luckydut97.tennispark.core.data.mapper.toAcademy
import com.luckydut97.tennispark.core.data.network.ApiService
import com.luckydut97.tennispark.core.data.model.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * 아카데미 Repository 구현체 (Clean Architecture)
 * 기존 AcademyRepository 로직을 래핑 - Flow 문제 해결
 */
class AcademyRepositoryImpl(
    private val apiService: ApiService
) : AcademyRepository {

    private val tag = "🔍 CoreAcademyRepository"

    // 기존 Core Repository 사용 (래핑 방식)
    private val coreRepository = com.luckydut97.tennispark.core.data.repository.AcademyRepository()

    override suspend fun getAcademies(): Flow<List<Academy>> = flow {
        android.util.Log.d(tag, "getAcademies() called - using wrapper approach")
        try {
            // 기존 방식: Core Repository 래핑
            val response = coreRepository.getAcademies()
            android.util.Log.d(tag, "Core API response: success=${response.success}")

            if (response.success) {
                val academyListResponse = response.response
                if (academyListResponse != null) {
                    val academies = academyListResponse.academies.map { it.toAcademy() }
                    android.util.Log.d(tag, "Successfully mapped ${academies.size} academies")
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
            android.util.Log.e(tag, "Exception in getAcademies: ${e.message}", e)
            throw e
        }
    }

    override suspend fun applyForAcademy(academyId: Long): Result<String> {
        return try {
            android.util.Log.d(tag, "applyForAcademy called with id: $academyId")

            // 기존 방식: Core Repository 래핑
            val response = coreRepository.applyForAcademy(academyId)

            if (response.success) {
                Result.success("아카데미 신청이 완료되었습니다.")
            } else {
                val errorMessage = response.error?.message ?: "아카데미 신청에 실패했습니다."
                val statusCode = response.error?.status

                android.util.Log.d(
                    tag,
                    "applyForAcademy error - statusCode: $statusCode, errorMessage: $errorMessage"
                )

                // 서버에서 온 메시지를 우선 사용하고, 없을 때만 상태 코드로 판단
                val specificErrorMessage = if (errorMessage != "아카데미 신청에 실패했습니다.") {
                    // 중복 신청 관련 메시지인 경우 아카데미용으로 변환
                    if (errorMessage.contains("이미") || errorMessage.contains("중복")) {
                        android.util.Log.d(tag, "Duplicate error detected: $errorMessage")
                        // 서버에서 "이미 신청한 활동입니다"로 보내므로 아카데미용으로 변환
                        val convertedMessage = errorMessage.replace("활동", "아카데미")
                        android.util.Log.d(tag, "Converted message: $convertedMessage")
                        convertedMessage
                    } else {
                        errorMessage // 서버 메시지 우선 사용
                    }
                } else {
                    when (statusCode) {
                        400 -> {
                            android.util.Log.d(tag, "400 error - treating as duplicate")
                            "이미 신청한 아카데미입니다." // HTTP_500 제거하고 직접 메시지 사용
                        }
                        401 -> "인증이 필요합니다. 다시 로그인해주세요."
                        404 -> "해당 아카데미를 찾을 수 없습니다."
                        409 -> {
                            android.util.Log.d(tag, "409 error - treating as duplicate")
                            "이미 신청한 아카데미입니다." // HTTP_500 제거하고 직접 메시지 사용
                        }
                        500 -> "HTTP_500: $errorMessage" // 활동 신청과 동일하게 500 에러 명시
                        else -> errorMessage
                    }
                }

                android.util.Log.d(tag, "Final error message: $specificErrorMessage")

                Result.failure(Exception(specificErrorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}