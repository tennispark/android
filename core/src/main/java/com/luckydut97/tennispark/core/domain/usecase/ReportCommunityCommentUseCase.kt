package com.luckydut97.tennispark.core.domain.usecase

import com.luckydut97.tennispark.core.domain.repository.CommunityRepository

class ReportCommunityCommentUseCase(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(postId: Int, commentId: Int, reason: String): Result<Unit> {
        return communityRepository.reportComment(postId, commentId, reason)
    }
}
