package com.luckydut97.tennispark.core.data.repository

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.luckydut97.tennispark.core.data.mapper.CommunityMapper.toDomain
import com.luckydut97.tennispark.core.data.mapper.CommunityMapper.toCommentDomain
import com.luckydut97.tennispark.core.data.model.CommunityPostCreateRequest
import com.luckydut97.tennispark.core.data.model.CommunityCommentCreateRequest
import com.luckydut97.tennispark.core.data.model.CommunityCommentUpdateRequest
import com.luckydut97.tennispark.core.data.network.ApiService
import com.luckydut97.tennispark.core.domain.model.CommunityPost
import com.luckydut97.tennispark.core.domain.model.CommunityComment
import com.luckydut97.tennispark.core.domain.model.CommunityPostUpdateRequest
import com.luckydut97.tennispark.core.domain.repository.CommunityRepository
import com.luckydut97.tennispark.core.data.model.CommunityReportRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

class CommunityRepositoryImpl(
    private val apiService: ApiService,
    private val context: Context? = null
) : CommunityRepository {

    override suspend fun getCommunityPosts(
        page: Int,
        size: Int
    ): Flow<Pair<List<CommunityPost>, Boolean>> = flow {
        try {
            val response = apiService.getCommunityPosts(page, size)

            if (response.isSuccessful && response.body()?.success == true) {
                val homeResponse = response.body()?.response
                val posts = homeResponse?.content ?: emptyList()
                val hasNext = homeResponse?.hasNext ?: false
                emit(posts.toDomain() to hasNext)
            } else {
                val errorMessage = response.body()?.error?.message ?: "게시글을 불러올 수 없습니다."
                throw Exception(errorMessage)
            }
        } catch (e: Exception) {
            throw Exception("네트워크 오류가 발생했습니다: ${e.message}")
        }
    }

    override suspend fun getPostDetail(postId: Int): Flow<CommunityPost> = flow {
        try {
            val response = apiService.getCommunityPostDetail(postId)

            if (response.isSuccessful && response.body()?.success == true) {
                val postDetail = response.body()?.response ?: throw Exception("게시글 정보를 찾을 수 없습니다.")
                emit(postDetail.toDomain())
            } else {
                val errorMessage = response.body()?.error?.message ?: "게시글을 불러올 수 없습니다."
                throw Exception(errorMessage)
            }
        } catch (e: Exception) {
            throw Exception("네트워크 오류가 발생했습니다: ${e.message}")
        }
    }

    override suspend fun toggleLike(postId: Int): Result<Unit> {
        return try {
            val response = apiService.toggleCommunityLike(postId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                val message = response.body()?.error?.message ?: "좋아요 처리에 실패했습니다."
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Result.failure(Exception("네트워크 오류가 발생했습니다: ${e.message}"))
        }
    }

    override suspend fun getPostComments(postId: Int): Flow<List<CommunityComment>> = flow {
        try {
            val response = apiService.getCommunityPostComments(postId)
            if (response.isSuccessful) {
                val comments = response.body()?.comments ?: emptyList()
                emit(comments.toCommentDomain())
            } else {
                throw Exception("댓글을 불러올 수 없습니다. 상태코드: ${response.code()}")
            }
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun createComment(
        postId: Int,
        content: String,
        imageUri: Uri?
    ): Result<Unit> {
        return try {
            if (content.isBlank()) {
                return Result.failure(Exception("내용은 필수입니다."))
            }
            if (content.length > 3000) {
                return Result.failure(Exception("내용은 3,000자 이하여야 합니다."))
            }

            val commentRequest = CommunityCommentCreateRequest(content = content)
            val jsonBody = Gson().toJson(commentRequest).toRequestBody("application/json".toMediaTypeOrNull())

            var imagePart: MultipartBody.Part? = null
            if (imageUri != null) {
                context?.let { ctx ->
                    runCatching {
                        ctx.contentResolver.openInputStream(imageUri)?.use { stream ->
                            val tempFile = File(ctx.cacheDir, "temp_comment_${System.currentTimeMillis()}.jpg")
                            FileOutputStream(tempFile).use { out -> stream.copyTo(out) }
                            imagePart = MultipartBody.Part.createFormData(
                                "photo",
                                tempFile.name,
                                tempFile.asRequestBody("image/*".toMediaTypeOrNull())
                            )
                        }
                    }
                }
            }

            val response = apiService.createCommunityComment(postId, jsonBody, imagePart)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                val errorMessage = response.body()?.error?.message ?: "댓글 작성에 실패했습니다."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("네트워크 오류가 발생했습니다: ${e.message}"))
        }
    }

    override suspend fun updateComment(
        postId: Int,
        commentId: Int,
        content: String,
        deletePhoto: Boolean,
        newImageUri: Uri?
    ): Result<Unit> {
        return try {
            if (content.isBlank()) {
                return Result.failure(Exception("내용은 필수입니다."))
            }
            if (content.length > 3000) {
                return Result.failure(Exception("내용은 3,000자 이하여야 합니다."))
            }

            val updateRequest = CommunityCommentUpdateRequest(
                content = content,
                deletePhoto = deletePhoto
            )
            val jsonBody = Gson().toJson(updateRequest).toRequestBody("application/json".toMediaTypeOrNull())

            val imagePart = createSingleImagePart(newImageUri)

            val response = apiService.updateCommunityComment(postId, commentId, jsonBody, imagePart)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                val errorMessage = response.body()?.error?.message ?: "댓글 수정에 실패했습니다."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("네트워크 오류가 발생했습니다: ${e.message}"))
        }
    }

    override suspend fun deleteComment(postId: Int, commentId: Int): Result<Unit> {
        return try {
            val response = apiService.deleteCommunityComment(postId, commentId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                val errorMessage = response.body()?.error?.message ?: "댓글 삭제에 실패했습니다."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("네트워크 오류가 발생했습니다: ${e.message}"))
        }
    }

    override suspend fun reportPost(postId: Int, reason: String): Result<Unit> {
        return try {
            val trimmed = reason.trim()
            if (trimmed.isEmpty()) {
                return Result.failure(Exception("신고 사유는 필수입니다."))
            }
            if (trimmed.length > 1000) {
                return Result.failure(Exception("신고 사유는 1,000자 이하여야 합니다."))
            }

            val request = CommunityReportRequest(trimmed)
            val response = apiService.reportCommunityPost(postId, request)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                val errorMessage = response.body()?.error?.message ?: "신고 처리에 실패했습니다."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("네트워크 오류가 발생했습니다: ${e.message}"))
        }
    }

    override suspend fun reportComment(postId: Int, commentId: Int, reason: String): Result<Unit> {
        return try {
            val trimmed = reason.trim()
            if (trimmed.isEmpty()) {
                return Result.failure(Exception("신고 사유는 필수입니다."))
            }
            if (trimmed.length > 1000) {
                return Result.failure(Exception("신고 사유는 1,000자 이하여야 합니다."))
            }

            val request = CommunityReportRequest(trimmed)
            val response = apiService.reportCommunityComment(postId, commentId, request)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                val errorMessage = response.body()?.error?.message ?: "신고 처리에 실패했습니다."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("네트워크 오류가 발생했습니다: ${e.message}"))
        }
    }

    override suspend fun togglePostNotification(postId: Int): Result<Boolean> {
        return try {
            val response = apiService.toggleCommunityPostNotification(postId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.response?.notificationEnabled ?: false)
            } else {
                val errorMessage = response.body()?.error?.message ?: "알림 설정을 변경할 수 없습니다."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("네트워크 오류가 발생했습니다: ${e.message}"))
        }
    }

    override suspend fun createPost(
        title: String,
        content: String,
        images: List<Uri>
    ): Result<Unit> {
        return try {
            if (title.isBlank()) return Result.failure(Exception("제목은 필수입니다."))
            if (content.isBlank()) return Result.failure(Exception("내용은 필수입니다."))
            if (title.length > 100) return Result.failure(Exception("제목은 100자 이하여야 합니다."))
            if (content.length > 3000) return Result.failure(Exception("내용은 3,000자 이하여야 합니다."))
            if (images.size > 3) return Result.failure(Exception("사진은 최대 3장까지 업로드할 수 있습니다."))

            val postRequest = CommunityPostCreateRequest(title = title, content = content)
            val jsonBody = Gson().toJson(postRequest).toRequestBody("application/json".toMediaTypeOrNull())

            val imageParts = buildMultipartList(images)

            val response = apiService.createCommunityPost(jsonBody, imageParts)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                val errorMessage = response.body()?.error?.message ?: "게시글 작성에 실패했습니다."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("네트워크 오류가 발생했습니다: ${e.message}"))
        }
    }

    override suspend fun updatePost(
        postId: Int,
        title: String,
        content: String,
        deleteIndexes: List<Int>,
        newImages: List<Uri>
    ): Result<Unit> {
        return try {
            if (title.isBlank()) return Result.failure(Exception("제목은 필수입니다."))
            if (content.isBlank()) return Result.failure(Exception("내용은 필수입니다."))
            if (title.length > 100) return Result.failure(Exception("제목은 100자 이하여야 합니다."))
            if (content.length > 3000) return Result.failure(Exception("내용은 3,000자 이하여야 합니다."))
            if (newImages.size > 3) return Result.failure(Exception("사진은 최대 3장까지 업로드할 수 있습니다."))

            val updateRequest = CommunityPostUpdateRequest(
                title = title,
                content = content,
                deleteList = deleteIndexes
            )
            val jsonBody = Gson().toJson(updateRequest).toRequestBody("application/json".toMediaTypeOrNull())

            val imageParts = buildMultipartList(newImages)

            val response = apiService.updateCommunityPost(postId, jsonBody, imageParts)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                val errorMessage = response.body()?.error?.message ?: "게시글 수정에 실패했습니다."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("네트워크 오류가 발생했습니다: ${e.message}"))
        }
    }

    override suspend fun deletePost(postId: Int): Result<Unit> {
        return try {
            val response = apiService.deleteCommunityPost(postId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                val errorMessage = response.body()?.error?.message ?: "게시글 삭제에 실패했습니다."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("네트워크 오류가 발생했습니다: ${e.message}"))
        }
    }

    private fun buildMultipartList(images: List<Uri>): List<MultipartBody.Part> {
        if (images.isEmpty()) return emptyList()
        val ctx = context ?: return emptyList()
        val parts = mutableListOf<MultipartBody.Part>()

        images.forEachIndexed { index, uri ->
            runCatching {
                ctx.contentResolver.openInputStream(uri)?.use { stream ->
                    val tempFile = File(ctx.cacheDir, "temp_post_image_${System.currentTimeMillis()}_$index.jpg")
                    FileOutputStream(tempFile).use { out -> stream.copyTo(out) }
                    val requestBody = tempFile.asRequestBody("image/*".toMediaTypeOrNull())
                    parts += MultipartBody.Part.createFormData("photos", tempFile.name, requestBody)
                }
            }
        }

        return parts
    }

    private fun createSingleImagePart(uri: Uri?): MultipartBody.Part? {
        if (uri == null) return null
        val ctx = context ?: return null

        var part: MultipartBody.Part? = null
        runCatching {
            ctx.contentResolver.openInputStream(uri)?.use { stream ->
                val tempFile = File(ctx.cacheDir, "temp_comment_update_${System.currentTimeMillis()}.jpg")
                FileOutputStream(tempFile).use { out -> stream.copyTo(out) }
                val requestBody = tempFile.asRequestBody("image/*".toMediaTypeOrNull())
                part = MultipartBody.Part.createFormData("photo", tempFile.name, requestBody)
            }
        }
        return part
    }
}
