package com.luckydut97.tennispark.core.domain.usecase

import com.luckydut97.tennispark.core.domain.model.CommunityPost
import com.luckydut97.tennispark.core.domain.repository.CommunityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchCommunityPostsUseCase(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(
        keyword: String,
        page: Int = 0,
        size: Int = 20
    ): Flow<Pair<List<CommunityPost>, Boolean>> {
        return communityRepository.searchCommunityPosts(keyword, page, size)
            .map { (posts, hasNext) ->
                Pair(posts.sortedByDescending { it.createdAt }, hasNext)
            }
    }
}
