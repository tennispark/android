package com.luckydut97.tennispark.core.data.model

/**
 * 커뮤니티 게시물 데이터 모델 (홈 API용)
 */
data class CommunityPost(
    val id: Int,
    val authorName: String?,
    val createdAt: String?,
    val title: String?,
    val content: String?,
    val mainImage: String?,
    val likeCount: Int?,
    val commentCount: Int?,
    val viewCount: Int?,
    val likedByMe: Boolean?,
    val authoredByMe: Boolean?
)

/**
 * 커뮤니티 게시글 상세 응답 데이터 (상세 API용)
 */
data class CommunityPostDetailResponse(
    val id: Int,
    val authorName: String,
    val createdAt: String,
    val title: String,
    val content: String,
    val photos: Map<String, String> = emptyMap(),
    val likeCount: Int,
    val commentCount: Int,
    val viewCount: Int,
    val likedByMe: Boolean,
    val authoredByMe: Boolean,
    val notificationEnabled: Boolean? = null
)

/**
 * 커뮤니티 댓글 데이터 모델
 */
data class CommunityCommentData(
    val id: Int,
    val authorName: String?,
    val createdAt: String?,
    val content: String?,
    val photoUrl: String?,
    val authoredByMe: Boolean?
)

/**
 * 커뮤니티 댓글 응답 데이터
 */
data class CommunityCommentsResponse(
    val comments: List<CommunityCommentData>?
)

/**
 * 커뮤니티 홈 응답 데이터
 */
data class CommunityHomeResponse(
    val content: List<CommunityPost>,
    val page: Int,
    val size: Int,
    val first: Boolean,
    val last: Boolean,
    val hasNext: Boolean,
    val numberOfElements: Int
)

/**
 * 페이지 정보
 */
data class Pageable(
    val pageNumber: Int,
    val pageSize: Int,
    val sort: List<Sort>,
    val offset: Int,
    val paged: Boolean,
    val unpaged: Boolean
)

/**
 * 정렬 정보
 */
data class Sort(
    val direction: String,
    val property: String,
    val ignoreCase: Boolean,
    val nullHandling: String,
    val ascending: Boolean,
    val descending: Boolean
)

/**
 * 커뮤니티 게시글 작성 요청 모델
 */
data class CommunityPostCreateRequest(
    val title: String,      // 제목 (100자 제한)
    val content: String     // 내용 (3000자 제한)
)

/**
 * 커뮤니티 게시글 작성 응답 모델
 */
data class CommunityPostCreateResponse(
    val postId: Int? = null // 생성된 게시글 ID (성공시 null일 수도 있음)
)

/**
 * 커뮤니티 댓글 작성 요청 모델
 */
data class CommunityCommentCreateRequest(
    val content: String // 내용 (3000자 제한)
)