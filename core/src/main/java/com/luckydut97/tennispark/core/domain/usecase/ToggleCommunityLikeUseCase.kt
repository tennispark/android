package com.luckydut97.tennispark.core.domain.usecase

import com.luckydut97.tennispark.core.domain.repository.CommunityRepository

/**
 * 커뮤니티 게시글 좋아요 토글 UseCase
 * 비즈니스 로직: 좋아요 상태 검증 등
 */
class ToggleCommunityLikeUseCase(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(postId: Int): Result<Unit> {
        return try {
            communityRepository.toggleLike(postId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}