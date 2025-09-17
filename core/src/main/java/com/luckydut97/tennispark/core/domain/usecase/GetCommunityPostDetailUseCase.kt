package com.luckydut97.tennispark.core.domain.usecase

import com.luckydut97.tennispark.core.domain.model.CommunityPost
import com.luckydut97.tennispark.core.domain.repository.CommunityRepository
import kotlinx.coroutines.flow.Flow

/**
 * 커뮤니티 게시글 상세 조회 UseCase
 */
class GetCommunityPostDetailUseCase(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(postId: Int): Flow<CommunityPost> {
        return communityRepository.getPostDetail(postId)
    }
}