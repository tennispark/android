package com.luckydut97.tennispark.core.domain.usecase

import android.net.Uri
import com.luckydut97.tennispark.core.domain.repository.CommunityRepository

/**
 * 커뮤니티 게시글 작성 UseCase
 * 비즈니스 로직: 입력 검증, 데이터 정제 등
 */
class CreateCommunityPostUseCase(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(
        title: String,
        content: String,
        images: List<Uri> = emptyList()
    ): Result<Unit> {
        // 입력값 검증 및 정제
        val trimmedTitle = title.trim()
        val trimmedContent = content.trim()

        // 빈 값 검증
        if (trimmedTitle.isEmpty()) {
            return Result.failure(Exception("제목은 필수입니다."))
        }
        if (trimmedContent.isEmpty()) {
            return Result.failure(Exception("내용은 필수입니다."))
        }

        // Repository 호출
        return communityRepository.createPost(trimmedTitle, trimmedContent, images)
    }
}