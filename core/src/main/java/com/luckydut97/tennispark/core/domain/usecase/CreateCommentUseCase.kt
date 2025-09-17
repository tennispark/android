package com.luckydut97.tennispark.core.domain.usecase

import android.net.Uri
import com.luckydut97.tennispark.core.domain.repository.CommunityRepository

/**
 * 댓글 작성 UseCase
 * 비즈니스 로직: 입력 검증, 데이터 정제 등
 */
class CreateCommentUseCase(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(
        postId: Int,
        content: String,
        imageUri: Uri? = null
    ): Result<Unit> {
        // 입력값 검증 및 정제
        val trimmedContent = content.trim()

        // 빈 값 검증
        if (trimmedContent.isEmpty()) {
            return Result.failure(Exception("내용은 필수입니다."))
        }

        // Repository 호출
        return communityRepository.createComment(postId, trimmedContent, imageUri)
    }
}