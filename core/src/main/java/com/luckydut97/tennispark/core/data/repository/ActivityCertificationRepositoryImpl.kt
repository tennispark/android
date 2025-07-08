package com.luckydut97.tennispark.core.data.repository

import com.luckydut97.tennispark.core.data.model.ApiResponse
import com.luckydut97.tennispark.core.data.network.ApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ActivityCertificationRepositoryImpl(
    private val apiService: ApiService
) : ActivityCertificationRepository {

    override suspend fun certifyActivity(
        activityId: Long,
        images: List<File>,
        content: String?
    ): Result<ApiResponse<Any>> {
        return try {
            // 첫 번째 이미지만 사용 (API 스펙에 맞춤)
            val file = images.firstOrNull() ?: return Result.failure(Exception("업로드할 이미지가 없습니다"))

            // 이미지 파일을 MultipartBody.Part로 변환
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)

            val response = apiService.certifyActivity(imagePart)

            if (response.isSuccessful) {
                response.body()?.let { body ->
                    Result.success(body)
                } ?: Result.failure(Exception("응답이 없습니다"))
            } else {
                Result.failure(Exception("활동 인증에 실패했습니다: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}