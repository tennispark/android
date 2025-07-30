package com.luckydut97.tennispark.core.data.repository

import android.util.Log
import com.luckydut97.tennispark.core.data.model.ActivityImageListResponse
import com.luckydut97.tennispark.core.data.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ActivityImageRepository {
    suspend fun getActivityImages(): Flow<List<String>>
}

class ActivityImageRepositoryImpl(
    private val apiService: ApiService
) : ActivityImageRepository {

    private val tag = "🔍 디버깅: ActivityImageRepository"

    override suspend fun getActivityImages(): Flow<List<String>> = flow {
        Log.d(tag, "[getActivityImages] called")

        try {
            val response = apiService.getActivityImages()
            Log.d(
                tag,
                "[getActivityImages] API response: isSuccessful=${response.isSuccessful}, code=${response.code()}"
            )

            if (response.isSuccessful && response.body()?.success == true) {
                val imageResponse = response.body()?.response
                val images = imageResponse?.images ?: emptyList()
                val totalCount = imageResponse?.totalCount ?: 0

                Log.d(
                    tag,
                    "[getActivityImages] success - totalCount: $totalCount, actual images: ${images.size}"
                )
                images.forEachIndexed { index, imageUrl ->
                    Log.d(tag, "[getActivityImages] image[$index]: $imageUrl")
                }

                emit(images)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(
                    tag,
                    "[getActivityImages] API failed - code=${response.code()}, errorBody=$errorBody"
                )
                emit(emptyList())
            }
        } catch (e: Exception) {
            Log.e(tag, "[getActivityImages] Exception: ${e.message}", e)
            emit(emptyList())
        }
    }
}