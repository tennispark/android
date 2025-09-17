package com.luckydut97.tennispark.core.data.mapper

import com.luckydut97.tennispark.core.data.model.CommunityPost as DataCommunityPost
import com.luckydut97.tennispark.core.data.model.CommunityPostDetailResponse
import com.luckydut97.tennispark.core.data.model.CommunityCommentData
import com.luckydut97.tennispark.core.domain.model.CommunityPost as DomainCommunityPost
import com.luckydut97.tennispark.core.domain.model.CommunityComment
import com.luckydut97.tennispark.core.utils.TimeUtils
import java.time.LocalDateTime

/**
 * 커뮤니티 데이터 모델을 도메인 모델로 변환하는 매퍼
 */
object CommunityMapper {

    /**
     * 데이터 CommunityPost를 도메인 CommunityPost로 변환
     * 커뮤니티 홈 API용 - photos 필드가 없음
     */
    fun DataCommunityPost.toDomain(): DomainCommunityPost {
        return DomainCommunityPost(
            id = this.id,
            authorName = this.authorName ?: "", // null 체크
            createdAt = parseCreatedAt(this.createdAt ?: ""),
            title = this.title ?: "", // null 체크
            content = this.content ?: "", // null 체크
            mainImage = this.mainImage,
            photos = emptyMap(), // 홈 API에서는 photos 없음
            likeCount = this.likeCount ?: 0, // null 체크
            commentCount = this.commentCount ?: 0, // null 체크
            viewCount = this.viewCount ?: 0, // null 체크
            likedByMe = this.likedByMe ?: false, // null 체크
            authoredByMe = this.authoredByMe ?: false, // null 체크
            notificationEnabled = null // 홈 API에서는 notificationEnabled 없음
        )
    }

    /**
     * 게시글 상세 응답을 도메인 CommunityPost로 변환
     * 상세 API용 - photos 필드가 있음
     */
    fun CommunityPostDetailResponse.toDomain(): DomainCommunityPost {
        return DomainCommunityPost(
            id = this.id,
            authorName = this.authorName ?: "", // null 체크
            createdAt = parseCreatedAt(this.createdAt ?: ""),
            title = this.title ?: "", // null 체크
            content = this.content ?: "", // null 체크
            mainImage = null, // 상세 응답에는 mainImage가 없고 photos만 있음
            photos = this.photos ?: emptyMap(), // null 체크
            likeCount = this.likeCount ?: 0, // null 체크
            commentCount = this.commentCount ?: 0, // null 체크
            viewCount = this.viewCount ?: 0, // null 체크
            likedByMe = this.likedByMe ?: false, // null 체크
            authoredByMe = this.authoredByMe ?: false, // null 체크
            notificationEnabled = this.notificationEnabled
        )
    }

    /**
     * 댓글 데이터를 도메인 댓글로 변환
     */
    fun CommunityCommentData.toDomain(): CommunityComment {
        return CommunityComment(
            id = this.id,
            authorName = this.authorName ?: "", // null 체크
            createdAt = parseCreatedAt(this.createdAt ?: ""),
            content = this.content ?: "", // null 체크
            photoUrl = this.photoUrl,
            authoredByMe = this.authoredByMe ?: false // null 체크
        )
    }

    /**
     * 데이터 CommunityPost 리스트를 도메인 CommunityPost 리스트로 변환
     */
    fun List<DataCommunityPost>.toDomain(): List<DomainCommunityPost> {
        return this.map { it.toDomain() }
    }

    /**
     * 댓글 데이터 리스트를 도메인 댓글 리스트로 변환
     */
    fun List<CommunityCommentData>.toCommentDomain(): List<CommunityComment> {
        return this.map { it.toDomain() }
    }

    /**
     * ISO 8601 형식의 문자열을 LocalDateTime으로 변환
     * "2025-09-07T02:32:59.122272" -> LocalDateTime
     */
    private fun parseCreatedAt(createdAt: String): LocalDateTime {
        return try {
            if (createdAt.isBlank()) {
                LocalDateTime.now()
            } else {
                TimeUtils.parseIsoDateTime(createdAt)
            }
        } catch (e: Exception) {
            // 파싱 실패 시 현재 시간으로 설정
            LocalDateTime.now()
        }
    }
}