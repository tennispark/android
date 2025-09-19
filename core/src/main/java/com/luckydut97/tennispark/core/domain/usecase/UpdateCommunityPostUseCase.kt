package com.luckydut97.tennispark.core.domain.usecase

import android.net.Uri
import com.luckydut97.tennispark.core.domain.repository.CommunityRepository

class UpdateCommunityPostUseCase(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(
        postId: Int,
        title: String,
        content: String,
        deleteIndexes: List<Int>,
        newImages: List<Uri>
    ): Result<Unit> {
        return communityRepository.updatePost(postId, title, content, deleteIndexes, newImages)
    }
}
