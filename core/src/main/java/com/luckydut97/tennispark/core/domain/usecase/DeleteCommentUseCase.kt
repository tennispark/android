package com.luckydut97.tennispark.core.domain.usecase

import com.luckydut97.tennispark.core.domain.repository.CommunityRepository

/**
 * 댓글 삭제 UseCase
 */
class DeleteCommentUseCase(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(postId: Int, commentId: Int): Result<Unit> {
        return communityRepository.deleteComment(postId, commentId)
    }
}
