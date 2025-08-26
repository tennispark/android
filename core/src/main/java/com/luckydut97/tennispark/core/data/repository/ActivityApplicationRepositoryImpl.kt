package com.luckydut97.tennispark.core.data.repository

import android.util.Log
import com.luckydut97.tennispark.core.domain.repository.ActivityApplicationRepository
import com.luckydut97.tennispark.core.domain.model.ActivityApplication
import com.luckydut97.tennispark.core.data.mapper.toActivityApplication
import com.luckydut97.tennispark.core.data.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * 활동 신청 내역 Repository 구현체 (Clean Architecture)
 */
class ActivityApplicationRepositoryImpl(
    private val apiService: ApiService
) : ActivityApplicationRepository {

    private val tag = "🔍 ActivityApplicationRepo"

    override suspend fun getMyActivityApplications(): Flow<List<ActivityApplication>> = flow {
        Log.d(tag, "[getMyActivityApplications] called")

        try {
            val response = apiService.getActivityApplications()
            Log.d(
                tag,
                "[getMyActivityApplications] API response: isSuccessful=${response.isSuccessful}, code=${response.code()}"
            )

            if (response.isSuccessful && response.body()?.success == true) {
                val applicationListResponse = response.body()?.response

                if (applicationListResponse != null) {
                    val applications =
                        applicationListResponse.applications.map { it.toActivityApplication() }
                    Log.d(
                        tag,
                        "[getMyActivityApplications] Successfully mapped ${applications.size} applications"
                    )

                    // 이미 서버에서 신청날짜 내림차순으로 정렬되어 온다고 했으므로 그대로 emit
                    emit(applications)
                } else {
                    Log.w(tag, "[getMyActivityApplications] Response data is null")
                    throw Exception("활동 신청 내역 데이터가 없습니다.")
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(
                    tag,
                    "[getMyActivityApplications] API failed - code=${response.code()}, errorBody=$errorBody"
                )

                val errorMessage = when (response.code()) {
                    401 -> "인증이 되지 않았습니다."
                    else -> "활동 신청 내역을 가져올 수 없습니다."
                }
                throw Exception(errorMessage)
            }
        } catch (e: Exception) {
            Log.e(tag, "[getMyActivityApplications] Exception: ${e.message}", e)
            throw e
        }
    }
}