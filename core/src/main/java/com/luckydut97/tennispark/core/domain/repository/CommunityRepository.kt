package com.luckydut97.tennispark.core.domain.repository

import com.luckydut97.tennispark.core.domain.model.CommunityPost
import com.luckydut97.tennispark.core.domain.model.CommunityComment
import android.net.Uri
import kotlinx.coroutines.flow.Flow

/**
 * 커뮤니티 도메인 Repository 인터페이스 (Clean Architecture)
 */
interface CommunityRepository {

    /**
     * 커뮤니티 홈 게시글 목록 조회
     * @param page 페이지 번호 (0부터 시작)
     * @param size 페이지 크기
     * @return 게시글 목록과 다음 페이지 존재 여부
     */
    suspend fun getCommunityPosts(
        page: Int = 0,
        size: Int = 20
    ): Flow<Pair<List<CommunityPost>, Boolean>>

    /**
     * 게시글 좋아요 토글
     */
    suspend fun toggleLike(postId: Int): Result<Unit>

    /**
     * 게시글 상세 조회
     */
    suspend fun getPostDetail(postId: Int): Flow<CommunityPost>

    /**
     * 게시글 댓글 목록 조회
     */
    suspend fun getPostComments(postId: Int): Flow<List<CommunityComment>>

    /**
     * 게시글 댓글 작성
     *
     * @param postId 게시글 ID
     * @param content 댓글 내용
     * @param imageUri 첨부 이미지 (선택, 최대 1장)
     * @return 성공 시 Unit, 실패 시 예외 결과
     */
    suspend fun createComment(
        postId: Int,
        content: String,
        imageUri: Uri? = null
    ): Result<Unit>

    /**
     * 게시글 댓글 수정
     */
    suspend fun updateComment(
        postId: Int,
        commentId: Int,
        content: String,
        deletePhoto: Boolean,
        newImageUri: Uri? = null
    ): Result<Unit>

    /**
     * 게시글 댓글 삭제
     */
    suspend fun deleteComment(
        postId: Int,
        commentId: Int
    ): Result<Unit>

    /**
     * 게시글 신고
     */
    suspend fun reportPost(
        postId: Int,
        reason: String
    ): Result<Unit>

    /**
     * 게시글 댓글 신고
     */
    suspend fun reportComment(
        postId: Int,
        commentId: Int,
        reason: String
    ): Result<Unit>

    /**
     * 게시글 알림 토글
     */
    suspend fun togglePostNotification(postId: Int): Result<Boolean>

    /**
     * 게시글 작성
     *
     * @param title 게시글 제목
     * @param content 게시글 내용
     * @param images 첨부할 이미지 목록 (최대 3장)
     * @return 성공 시 Unit, 실패 시 예외 결과
     */
    suspend fun createPost(
        title: String,
        content: String,
        images: List<Uri> = emptyList()
    ): Result<Unit>

    /**
     * 게시글 수정
     *
     * @param postId 게시글 ID
     * @param title 게시글 제목
     * @param content 게시글 내용
     * @param deleteIndexes 삭제할 기존 사진 인덱스 목록
     * @param newImages 새로 첨부할 이미지 목록 (최대 3장)
     */
    suspend fun updatePost(
        postId: Int,
        title: String,
        content: String,
        deleteIndexes: List<Int> = emptyList(),
        newImages: List<Uri> = emptyList()
    ): Result<Unit>

    /**
     * 게시글 삭제
     */
    suspend fun deletePost(postId: Int): Result<Unit>
}
