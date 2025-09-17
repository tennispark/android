package com.luckydut97.tennispark.core.data.repository

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.luckydut97.tennispark.core.data.mapper.CommunityMapper.toDomain
import com.luckydut97.tennispark.core.data.mapper.CommunityMapper.toCommentDomain
import com.luckydut97.tennispark.core.data.model.ApiResponse
import com.luckydut97.tennispark.core.data.model.CommunityHomeResponse
import com.luckydut97.tennispark.core.data.model.CommunityPostCreateRequest
import com.luckydut97.tennispark.core.data.model.CommunityCommentCreateRequest
import com.luckydut97.tennispark.core.data.network.ApiService
import com.luckydut97.tennispark.core.domain.model.CommunityPost
import com.luckydut97.tennispark.core.domain.model.CommunityComment
import com.luckydut97.tennispark.core.domain.repository.CommunityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream

/**
 * 커뮤니티 Repository 구현체 (Data Layer)
 */
class CommunityRepositoryImpl(
    private val apiService: ApiService,
    private val context: Context? = null
) : CommunityRepository {

    /**
     * 커뮤니티 홈 게시글 목록을 가져온다
     */
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
                emit(Pair(posts.toDomain(), hasNext))
            } else {
                val errorMessage = response.body()?.error?.message ?: "게시글을 불러올 수 없습니다."
                throw Exception(errorMessage)
            }
        } catch (e: Exception) {
            throw Exception("네트워크 오류가 발생했습니다: ${e.message}")
        }
    }

    /**
     * 게시글 상세 조회
     */
    override suspend fun getPostDetail(postId: Int): Flow<CommunityPost> = flow {
        try {
            println("📖 ===== 게시글 상세 조회 시작 =====")
            println("📖 요청 postId: $postId")

            val response = apiService.getCommunityPostDetail(postId)

            println("📖 API 응답 코드: ${response.code()}")
            println("📖 API 응답 성공 여부: ${response.isSuccessful}")

            if (response.isSuccessful && response.body()?.success == true) {
                val postDetail = response.body()?.response
                if (postDetail != null) {
                    println("📖 게시글 상세 정보:")
                    println("📖   - 제목: ${postDetail.title}")
                    println("📖   - 작성자: ${postDetail.authorName}")
                    println("📖   - photos 필드: ${postDetail.photos}")
                    println("📖   - photos 크기: ${postDetail.photos?.size ?: 0}개")

                    postDetail.photos?.forEach { (index, url) ->
                        println("📖   - 사진 $index: $url")
                    }

                    val domainPost = postDetail.toDomain()
                    println("📖   - 변환된 도메인 모델 photos: ${domainPost.photos}")
                    println("📖   - 변환된 도메인 모델 sortedPhotos: ${domainPost.sortedPhotos}")
                    println("📖 ✅ 게시글 상세 조회 성공")

                    emit(domainPost)
                } else {
                    println("📖 ❌ 게시글 정보가 null입니다.")
                    throw Exception("게시글 정보를 찾을 수 없습니다.")
                }
            } else {
                val errorMessage = response.body()?.error?.message ?: "게시글을 불러올 수 없습니다."
                println("📖 ❌ 게시글 상세 조회 실패: $errorMessage")
                throw Exception(errorMessage)
            }
        } catch (e: Exception) {
            println("📖 ❌ 네트워크 오류: ${e.message}")
            throw Exception("네트워크 오류가 발생했습니다: ${e.message}")
        }
    }

    /**
     * 게시글 좋아요 토글 (현재는 로컬에서만 처리, 향후 API 연동 예정)
     */
    override suspend fun toggleLike(postId: Int): Result<Unit> {
        return try {
            // TODO: 실제 좋아요 API 연동 예정
            // val response = apiService.toggleCommunityLike(postId)
            // if (response.isSuccessful && response.body()?.success == true) {
            //     Result.success(Unit)
            // } else {
            //     Result.failure(Exception("좋아요 처리 중 오류가 발생했습니다."))
            // }

            // 임시로 성공 반환
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("네트워크 오류가 발생했습니다: ${e.message}"))
        }
    }

    /**
     * 게시글 댓글 목록 조회
     */
    override suspend fun getPostComments(postId: Int): Flow<List<CommunityComment>> = flow {
        try {
            val response = apiService.getCommunityPostComments(postId)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val comments = body.comments ?: emptyList()
                    emit(comments.toCommentDomain())
                } else {
                    emit(emptyList())
                }
            } else {
                throw Exception("댓글을 불러올 수 없습니다. 상태코드: ${response.code()}")
            }
        } catch (e: Exception) {
            // 댓글 로드 실패 시 빈 리스트 반환 (게시글은 표시되도록)
            emit(emptyList())
        }
    }

    /**
     * 게시글 댓글 작성
     */
    override suspend fun createComment(
        postId: Int,
        content: String,
        imageUri: Uri?
    ): Result<Unit> {
        return try {
            println("💬 ===== 댓글 작성 시작 =====")
            println("💬 게시글 ID: $postId")
            println("💬 댓글 내용 길이: ${content.length}자")
            println("💬 이미지 첨부: ${if (imageUri != null) "있음" else "없음"}")

            // 내용 길이 검증
            if (content.isEmpty()) {
                return Result.failure(Exception("내용은 필수입니다."))
            }
            if (content.length > 3000) {
                return Result.failure(Exception("내용은 3,000자 이하여야 합니다."))
            }

            // JSON 데이터 생성
            val commentRequest = CommunityCommentCreateRequest(content = content)
            val gson = Gson()
            val jsonString = gson.toJson(commentRequest)
            val jsonRequestBody = jsonString.toRequestBody("application/json".toMediaTypeOrNull())

            println("💬 JSON 데이터: $jsonString")

            // 이미지 파일 처리 (선택적)
            var imagePart: MultipartBody.Part? = null

            if (imageUri != null && context != null) {
                try {
                    println("💬 이미지 처리 시작: $imageUri")
                    val inputStream: InputStream? =
                        context.contentResolver.openInputStream(imageUri)
                    inputStream?.let { stream ->
                        // 임시 파일 생성
                        val tempFile = File(
                            context.cacheDir,
                            "temp_comment_image_${System.currentTimeMillis()}.jpg"
                        )
                        val outputStream = FileOutputStream(tempFile)
                        stream.copyTo(outputStream)
                        stream.close()
                        outputStream.close()

                        println("💬 임시 파일 생성: ${tempFile.name}, 크기: ${tempFile.length()} bytes")

                        // MultipartBody.Part 생성
                        val requestFile = tempFile.asRequestBody("image/*".toMediaTypeOrNull())
                        imagePart = MultipartBody.Part.createFormData(
                            "photo",
                            tempFile.name,
                            requestFile
                        )
                        println("💬 이미지 MultipartBody.Part 생성 완료")
                    }
                } catch (e: Exception) {
                    println("💬 ⚠️ 이미지 처리 실패: ${e.message}")
                    // 이미지 처리 실패해도 댓글은 작성 가능
                }
            }

            // API 호출
            println("💬 API 호출 시작...")
            val response = apiService.createCommunityComment(postId, jsonRequestBody, imagePart)

            println("💬 API 응답 코드: ${response.code()}")
            println("💬 API 응답 성공 여부: ${response.isSuccessful}")
            println("💬 API 응답 body: ${response.body()}")

            if (response.isSuccessful && response.body()?.success == true) {
                println("💬 ✅ 댓글 작성 성공!")
                Result.success(Unit)
            } else {
                val errorMessage = response.body()?.error?.message ?: "댓글 작성에 실패했습니다."
                println("💬 ❌ 댓글 작성 실패: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            println("💬 ❌ 네트워크 오류: ${e.message}")
            Result.failure(Exception("네트워크 오류가 발생했습니다: ${e.message}"))
        }
    }

    /**
     * 게시글 작성
     */
    override suspend fun createPost(
        title: String,
        content: String,
        images: List<Uri>
    ): Result<Unit> {
        return try {
            println("📸 ===== 게시글 작성 시작 =====")
            println("📸 제목: $title")
            println("📸 내용 길이: ${content.length}자")
            println("📸 첨부 이미지 수: ${images.size}개")

            // 제목과 내용 길이 검증
            if (title.length > 100) {
                return Result.failure(Exception("제목은 100자 이하여야 합니다."))
            }
            if (content.length > 3000) {
                return Result.failure(Exception("내용은 3,000자 이하여야 합니다."))
            }
            if (images.size > 3) {
                return Result.failure(Exception("사진은 최대 3장까지 업로드할 수 있습니다."))
            }

            // JSON 데이터 생성
            val postRequest = CommunityPostCreateRequest(title = title, content = content)
            val gson = Gson()
            val jsonString = gson.toJson(postRequest)
            val jsonRequestBody = jsonString.toRequestBody("application/json".toMediaTypeOrNull())

            println("📸 JSON 데이터: $jsonString")

            // 이미지 파일들을 MultipartBody.Part로 변환
            val imageParts = mutableListOf<MultipartBody.Part>()

            if (context != null) {
                images.forEachIndexed { index, uri ->
                    try {
                        println("📸 이미지 ${index + 1} 처리 시작: $uri")
                        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                        inputStream?.let { stream ->
                            // 임시 파일 생성
                            val tempFile = File(
                                context.cacheDir,
                                "temp_image_${System.currentTimeMillis()}_$index.jpg"
                            )
                            val outputStream = FileOutputStream(tempFile)
                            stream.copyTo(outputStream)
                            stream.close()
                            outputStream.close()

                            println("📸 임시 파일 생성: ${tempFile.name}, 크기: ${tempFile.length()} bytes")

                            // MultipartBody.Part 생성
                            val requestFile = tempFile.asRequestBody("image/*".toMediaTypeOrNull())
                            val imagePart = MultipartBody.Part.createFormData(
                                "photos",
                                tempFile.name,
                                requestFile
                            )
                            imageParts.add(imagePart)
                            println("📸 이미지 ${index + 1} MultipartBody.Part 생성 완료")
                        }
                    } catch (e: Exception) {
                        // 이미지 처리 실패 시 로그만 남기고 계속 진행
                        println("📸 ❌ 이미지 ${index + 1} 처리 실패: ${e.message}")
                    }
                }
            } else {
                println("📸 ⚠️ Context가 null이라 이미지 처리를 건너뜁니다.")
            }

            println("📸 최종 전송할 이미지 파트 수: ${imageParts.size}개")

            // API 호출
            println("📸 API 호출 시작...")
            val response = apiService.createCommunityPost(jsonRequestBody, imageParts)

            println("📸 API 응답 코드: ${response.code()}")
            println("📸 API 응답 성공 여부: ${response.isSuccessful}")
            println("📸 API 응답 body: ${response.body()}")

            if (response.isSuccessful && response.body()?.success == true) {
                println("📸 ✅ 게시글 작성 성공!")
                Result.success(Unit)
            } else {
                val errorMessage = response.body()?.error?.message ?: "게시글 작성에 실패했습니다."
                println("📸 ❌ 게시글 작성 실패: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            println("📸 ❌ 네트워크 오류: ${e.message}")
            Result.failure(Exception("네트워크 오류가 발생했습니다: ${e.message}"))
        }
    }
}