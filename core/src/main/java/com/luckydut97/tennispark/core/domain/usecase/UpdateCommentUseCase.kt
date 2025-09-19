package com.luckydut97.tennispark.core.domain.usecase

import android.net.Uri
import com.luckydut97.tennispark.core.domain.repository.CommunityRepository

/**
 * 댓글 수정 UseCase
 */
class UpdateCommentUseCase(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(
        postId: Int,
        commentId: Int,
        content: String,
        deletePhoto: Boolean,
        newImageUri: Uri?
    ): Result<Unit> {
        val trimmedContent = content.trim()
        if (trimmedContent.isEmpty()) {
            return Result.failure(Exception("내용은 필수입니다."))
        }
        return communityRepository.updateComment(postId, commentId, trimmedContent, deletePhoto, newImageUri)
    }
}
