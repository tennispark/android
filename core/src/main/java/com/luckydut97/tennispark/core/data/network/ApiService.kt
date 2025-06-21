package com.luckydut97.tennispark.core.data.network

import com.luckydut97.tennispark.core.data.model.ApiResponse
import com.luckydut97.tennispark.core.data.model.MemberRegistrationRequest
import com.luckydut97.tennispark.core.data.model.MemberRegistrationResponse
import com.luckydut97.tennispark.core.data.model.PhoneVerificationRequest
import com.luckydut97.tennispark.core.data.model.PhoneVerificationCodeRequest
import com.luckydut97.tennispark.core.data.model.PhoneVerificationResponse
import com.luckydut97.tennispark.core.data.model.TokenResponse
import com.luckydut97.tennispark.core.data.model.ActivityListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Path

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
}
