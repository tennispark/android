package com.luckydut97.tennispark.core.domain.usecase

import com.luckydut97.tennispark.core.domain.model.CommunityPost
import com.luckydut97.tennispark.core.domain.repository.CommunityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * 커뮤니티 게시글 목록 조회 UseCase
 * 비즈니스 로직: 정렬, 필터링 등
 */
class GetCommunityPostsUseCase(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(
        page: Int = 0,
        size: Int = 20
    ): Flow<Pair<List<CommunityPost>, Boolean>> {
        return communityRepository.getCommunityPosts(page, size)
            .map { (posts, hasNext) ->
                // 비즈니스 규칙: 최신 순 정렬 (서버에서 이미 정렬되어 오지만 클라이언트에서도 보장)
                Pair(posts.sortedByDescending { it.createdAt }, hasNext)
            }
    }
}