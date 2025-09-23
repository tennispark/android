package com.luckydut97.tennispark.core.domain.usecase

import com.luckydut97.tennispark.core.domain.repository.CommunityRepository

class ReportCommunityPostUseCase(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(postId: Int, reason: String): Result<Unit> {
        return communityRepository.reportPost(postId, reason)
    }
}
