package com.luckydut97.tennispark.core.domain.usecase

import com.luckydut97.tennispark.core.domain.repository.CommunityRepository

class TogglePostNotificationUseCase(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(postId: Int): Result<Boolean> {
        return communityRepository.togglePostNotification(postId)
    }
}
