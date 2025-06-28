package com.luckydut97.tennispark.core.data.repository

import android.util.Log
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
        Log.d(tag, "=== 멤버십 등록 API 호출 시작 ===")
        Log.d(tag, "Base URL: https://tennis-park.store/")
        Log.d(tag, "Endpoint: POST /api/members/memberships")
        Log.d(tag, "HTTP Method: POST")
        Log.d(tag, "Content-Type: application/json")

        // 🔥 토큰 상태 확인
        val context = NetworkModule.getContext()
        if (context != null) {
            val tokenManager = TokenManagerImpl(context)
            val accessToken = tokenManager.getAccessToken()
            val refreshToken = tokenManager.getRefreshToken()

            Log.d(tag, "🔑 토큰 상태 확인:")
            Log.d(tag, "  AccessToken 존재: ${accessToken != null}")
            Log.d(tag, "  AccessToken 길이: ${accessToken?.length ?: 0}")
            Log.d(tag, "  RefreshToken 존재: ${refreshToken != null}")

            if (accessToken != null) {
                Log.d(tag, "  AccessToken 앞 20자: ${accessToken.take(20)}...")
                Log.d(tag, "  Authorization 헤더: Bearer $accessToken")
            } else {
                Log.e(tag, "  ❌ AccessToken이 null입니다!")
            }
        } else {
            Log.e(tag, "  ❌ Context가 null입니다!")
        }

        Log.d(tag, "📋 Request Data:")
        Log.d(tag, "  🔹 membershipType: ${request.membershipType}")
        Log.d(tag, "  📝 reason: ${request.reason}")
        Log.d(tag, "  🎾 courtType: ${request.courtType}")
        Log.d(tag, "  📅 period: ${request.period}")
        Log.d(tag, "  🤝 recommender: ${request.recommender}")

        return try {
            Log.d(tag, "🚀 Retrofit API 호출 시작...")

            val response = apiService.registerMembership(request)

            Log.d(tag, "📊 HTTP Status Code: ${response.code()}")
            Log.d(tag, "📝 HTTP Status Message: ${response.message()}")
            Log.d(tag, "📋 Response Headers: ${response.headers()}")

            if (response.isSuccessful) {
                val body = response.body()
                Log.d(tag, "✅ 멤버십 등록 API 호출 성공!")
                Log.d(tag, "📦 Response Body: $body")

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
                Log.e(tag, "❌ 멤버십 등록 API 호출 실패!")
                Log.e(tag, "🔥 Error Code: ${response.code()}")
                Log.e(tag, "🔥 Error Message: ${response.message()}")
                Log.e(tag, "🔥 Error Body: $errorBody")

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
            Log.e(tag, "🔥 네트워크 예외 발생: ${e.message}", e)
            ApiResponse(
                success = false,
                response = null,
                error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                    status = 0,
                    message = "네트워크 오류: ${e.message}"
                )
            )
        } finally {
            Log.d(tag, "=== 멤버십 등록 API 호출 완료 ===")
        }
    }

    suspend fun registerMember(request: MemberRegistrationRequest): ApiResponse<MemberRegistrationResponse> {
        Log.d(tag, "=== 회원가입 API 호출 시작 ===")
        Log.d(tag, "Base URL: $BASE_URL")
        Log.d(tag, "Endpoint: $REGISTER_MEMBER_ENDPOINT")
        Log.d(tag, "Full URL: $BASE_URL$REGISTER_MEMBER_ENDPOINT")
        Log.d(tag, "HTTP Method: POST")
        Log.d(tag, "Content-Type: application/json")

        Log.d(tag, "📋 Request Data:")
        Log.d(tag, "  📱 phoneNumber: ${request.phoneNumber}")
        Log.d(tag, "  👤 name: ${request.name}")
        Log.d(tag, "  🚻 gender: ${request.gender}")
        Log.d(tag, "  🎾 tennisCareer: ${request.tennisCareer}")
        Log.d(tag, "  📅 year: ${request.year}")
        Log.d(tag, "  📍 registrationSource: ${request.registrationSource}")
        Log.d(tag, "  🤝 recommender: ${request.recommender}")
        Log.d(tag, "  📸 instagramId: ${request.instagramId}")

        return try {
            Log.d(tag, "🚀 Retrofit API 호출 시작...")

            val response = apiService.registerMember(request)

            Log.d(tag, "📊 HTTP Status Code: ${response.code()}")
            Log.d(tag, "📝 HTTP Status Message: ${response.message()}")
            Log.d(tag, "📋 Response Headers: ${response.headers()}")

            if (response.isSuccessful) {
                val body = response.body()
                Log.d(tag, "✅ 회원가입 API 호출 성공!")
                Log.d(tag, "📦 Response Body: $body")

                if (body?.success == true && body.response != null) {
                    Log.d(tag, "🔑 AccessToken 발급 성공: ${body.response.accessToken}")
                    Log.d(tag, "🔄 RefreshToken 발급 성공: ${body.response.refreshToken}")
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
                Log.e(tag, "❌ 회원가입 API 호출 실패!")
                Log.e(tag, "🔥 Error Code: ${response.code()}")
                Log.e(tag, "🔥 Error Message: ${response.message()}")
                Log.e(tag, "🔥 Error Body: $errorBody")

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
            Log.e(tag, "🔥 네트워크 예외 발생: ${e.message}", e)
            ApiResponse(
                success = false,
                response = null,
                error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                    status = 0,
                    message = "네트워크 오류: ${e.message}"
                )
            )
        } finally {
            Log.d(tag, "=== 회원가입 API 호출 완료 ===")
        }
    }

    companion object {
        const val BASE_URL = "https://tennis-park.store/"
        const val REGISTER_MEMBER_ENDPOINT = "api/members"
    }
}
