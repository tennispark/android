package com.luckydut97.tennispark.core.data.repository

import android.util.Log
import com.luckydut97.tennispark.core.data.model.Advertisement
import com.luckydut97.tennispark.core.data.model.AdPosition
import com.luckydut97.tennispark.core.data.model.ApiResponse
import com.luckydut97.tennispark.core.data.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface AdBannerRepository {
    suspend fun getAdvertisements(position: AdPosition): Flow<List<Advertisement>>
}

class AdBannerRepositoryImpl(
    private val apiService: ApiService
) : AdBannerRepository {

    private val tag = "🔍 디버깅: AdBannerRepository"

    override suspend fun getAdvertisements(position: AdPosition): Flow<List<Advertisement>> = flow {
        Log.d(tag, "[getAdvertisements] called with position: ${position.value}")

        try {
            val response = apiService.getAdvertisements(position.value)
            Log.d(
                tag,
                "[getAdvertisements] API response: isSuccessful=${response.isSuccessful}, code=${response.code()}"
            )

            if (response.isSuccessful && response.body()?.success == true) {
                val advertisements = response.body()?.response?.advertisements ?: emptyList()
                Log.d(
                    tag,
                    "[getAdvertisements] success - received ${advertisements.size} advertisements"
                )
                advertisements.forEachIndexed { index, ad ->
                    Log.d(
                        tag,
                        "[getAdvertisements] ad[$index]: id=${ad.id}, imageUrl=${ad.imageUrl}, linkUrl=${ad.linkUrl}"
                    )
                }
                emit(advertisements)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(
                    tag,
                    "[getAdvertisements] API failed - code=${response.code()}, errorBody=$errorBody"
                )
                emit(emptyList())
            }
        } catch (e: Exception) {
            Log.e(tag, "[getAdvertisements] Exception: ${e.message}", e)
            emit(emptyList())
        }
    }
}