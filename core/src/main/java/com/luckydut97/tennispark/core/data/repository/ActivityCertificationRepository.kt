package com.luckydut97.tennispark.core.data.repository

import com.luckydut97.tennispark.core.data.model.ApiResponse
import java.io.File

interface ActivityCertificationRepository {
    suspend fun certifyActivity(
        activityId: Long,
        images: List<File>,
        content: String? = null
    ): Result<ApiResponse<Any>>
}