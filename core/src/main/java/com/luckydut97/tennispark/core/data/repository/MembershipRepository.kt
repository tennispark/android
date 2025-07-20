package com.luckydut97.tennispark.core.data.repository

import com.luckydut97.tennispark.core.data.model.ApiResponse
import com.luckydut97.tennispark.core.data.model.MembershipRegistrationRequest
import com.luckydut97.tennispark.core.data.model.MemberRegistrationRequest
import com.luckydut97.tennispark.core.data.model.MemberRegistrationResponse
import com.luckydut97.tennispark.core.data.network.NetworkModule
import com.luckydut97.tennispark.core.data.storage.TokenManagerImpl

class MembershipRepository {

    private val tag = "🔍 디버깅: MembershipRepository"
    private val apiService = NetworkModule.apiService

    suspend fun registerMembership(request: MembershipRegistrationRequest): ApiResponse<Any> {

        // 🔥 토큰 상태 확인
        val context = NetworkModule.getContext()
        if (context != null) {
            val tokenManager = TokenManagerImpl(context)
            val accessToken = tokenManager.getAccessToken()
            val refreshToken = tokenManager.getRefreshToken()


            if (accessToken != null) {
            } else {
            }
        } else {
        }


        return try {

            val response = apiService.registerMembership(request)


            if (response.isSuccessful) {
                val body = response.body()

                body ?: ApiResponse(
                    success = false,
                    response = null,
                    error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                        status = 500,
                        message = "응답 본문이 비어있습니다."
                    )
                )
            } else {
                val errorBody = response.errorBody()?.string()

                ApiResponse(
                    success = false,
                    response = null,
                    error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                        status = response.code(),
                        message = when (response.code()) {
                            400 -> {
                                // 서버에서 온 실제 메시지 사용
                                if (errorBody?.contains("이미 멤버십에 가입한 회원") == true) {
                                    "이미 멤버십에 가입한 회원입니다."
                                } else {
                                    "필수 항목이 누락되었습니다."
                                }
                            }

                            401 -> "인증이 되지 않았습니다."
                            else -> "서버 오류가 발생했습니다."
                        }
                    )
                )
            }
        } catch (e: Exception) {
            ApiResponse(
                success = false,
                response = null,
                error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                    status = 0,
                    message = "네트워크 오류: ${e.message}"
                )
            )
        } finally {
        }
    }

    suspend fun registerMember(request: MemberRegistrationRequest): ApiResponse<MemberRegistrationResponse> {


        return try {

            val response = apiService.registerMember(request)


            if (response.isSuccessful) {
                val body = response.body()

                if (body?.success == true && body.response != null) {
                }

                body ?: ApiResponse(
                    success = false,
                    response = null,
                    error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                        status = 500,
                        message = "응답 본문이 비어있습니다."
                    )
                )
            } else {
                val errorBody = response.errorBody()?.string()

                ApiResponse(
                    success = false,
                    response = null,
                    error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                        status = response.code(),
                        message = when (response.code()) {
                            400 -> "필수 항목이 누락되었습니다. 모든 필수 정보를 입력해주세요."
                            else -> "서버 오류가 발생했습니다."
                        }
                    )
                )
            }
        } catch (e: Exception) {
            ApiResponse(
                success = false,
                response = null,
                error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                    status = 0,
                    message = "네트워크 오류: ${e.message}"
                )
            )
        } finally {
        }
    }

    companion object {
        const val BASE_URL = "https://tennis-park.store/"
        const val REGISTER_MEMBER_ENDPOINT = "api/members"
    }
}
