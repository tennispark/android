package com.luckydut97.tennispark.core.data.network

import com.luckydut97.tennispark.core.data.model.ApiResponse
import com.luckydut97.tennispark.core.data.model.MemberRegistrationRequest
import com.luckydut97.tennispark.core.data.model.MemberRegistrationResponse
import com.luckydut97.tennispark.core.data.model.PhoneVerificationRequest
import com.luckydut97.tennispark.core.data.model.PhoneVerificationCodeRequest
import com.luckydut97.tennispark.core.data.model.PhoneVerificationResponse
import com.luckydut97.tennispark.core.data.model.TokenResponse
import com.luckydut97.tennispark.core.data.model.ActivityListResponse
import com.luckydut97.tennispark.core.data.model.MyPointResponse
import com.luckydut97.tennispark.core.data.model.PointHistoryListResponse
import com.luckydut97.tennispark.core.data.model.MemberInfoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface ApiService {
    @POST("api/members")
    suspend fun registerMember(@Body request: MemberRegistrationRequest): Response<ApiResponse<MemberRegistrationResponse>>

    @POST("api/members/auth/phones/code")
    suspend fun requestPhoneVerification(@Body request: PhoneVerificationRequest): Response<ApiResponse<Any>>

    @POST("api/members/auth/phones/code/verify")
    suspend fun verifyPhoneCode(@Body request: PhoneVerificationCodeRequest): Response<ApiResponse<PhoneVerificationResponse>>

    @POST("api/members/auth/token/refresh")
    suspend fun refreshToken(@Header("Refresh-Token") refreshToken: String): Response<ApiResponse<TokenResponse>>

    @GET("api/members/activities")
    suspend fun getActivities(): Response<ApiResponse<ActivityListResponse>>

    @POST("api/members/activities/{activityId}/apply")
    suspend fun applyForActivity(@Path("activityId") activityId: Long): Response<ApiResponse<Any>>

    // 회원정보 조회 API
    @GET("api/members/name/me")
    suspend fun getMemberInfo(): Response<ApiResponse<MemberInfoResponse>>

    // 포인트 관련 API
    @GET("api/members/points/me")
    suspend fun getMyPoints(): Response<ApiResponse<MyPointResponse>>

    @GET("api/members/points/me/history")
    suspend fun getPointHistories(): Response<ApiResponse<PointHistoryListResponse>>

    // QR 이벤트 처리 API (동적 URL) - Body 없는 POST
    @POST
    suspend fun postQrEvent(
        @Url eventUrl: String
    ): Response<ApiResponse<Any>>
}
