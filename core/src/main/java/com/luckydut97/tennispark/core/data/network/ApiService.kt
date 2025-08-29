package com.luckydut97.tennispark.core.data.network

import com.luckydut97.tennispark.core.data.model.AcademyListResponse
import com.luckydut97.tennispark.core.data.model.ApiResponse
import com.luckydut97.tennispark.core.data.model.ActivityApplicationListResponse
import com.luckydut97.tennispark.core.data.model.MemberRegistrationRequest
import com.luckydut97.tennispark.core.data.model.MemberRegistrationResponse
import com.luckydut97.tennispark.core.data.model.PhoneVerificationRequest
import com.luckydut97.tennispark.core.data.model.PhoneVerificationCodeRequest
import com.luckydut97.tennispark.core.data.model.PhoneVerificationResponse
import com.luckydut97.tennispark.core.data.model.TokenResponse
import com.luckydut97.tennispark.core.data.model.ActivityListResponse
import com.luckydut97.tennispark.core.data.model.MemberInfoResponse
import com.luckydut97.tennispark.core.data.model.MyPointResponse
import com.luckydut97.tennispark.core.data.model.PointHistoryListResponse
import com.luckydut97.tennispark.core.data.model.MatchRecordResponse
import com.luckydut97.tennispark.core.data.model.MembershipRegistrationRequest
import com.luckydut97.tennispark.core.data.model.QrPurchaseResponse
import com.luckydut97.tennispark.core.data.model.ShopProductListResponse
import com.luckydut97.tennispark.core.data.model.UpdateFcmTokenRequest
import com.luckydut97.tennispark.core.data.model.AdvertisementListResponse
import com.luckydut97.tennispark.core.data.model.ActivityImageListResponse
import com.luckydut97.tennispark.core.data.model.NotificationListResponse
import com.luckydut97.tennispark.core.data.model.UnreadCountResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
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

    // 아카데미 관련 API
    @GET("api/members/activities/academies")
    suspend fun getAcademies(): Response<ApiResponse<AcademyListResponse>>

    @POST("api/members/activities/academies/{academyId}/apply")
    suspend fun applyForAcademy(@Path("academyId") academyId: Long): Response<ApiResponse<Any>>

    // 회원정보 조회 API
    @GET("api/members/name/me")
    suspend fun getMemberInfo(): Response<ApiResponse<MemberInfoResponse>>

    // 포인트 관련 API
    @GET("api/members/points/me")
    suspend fun getMyPoints(): Response<ApiResponse<MyPointResponse>>

    @GET("api/members/points/me/history")
    suspend fun getPointHistories(): Response<ApiResponse<PointHistoryListResponse>>

    // 매치 기록 API
    @GET("api/members/matches/me")
    suspend fun getMatchRecords(): Response<ApiResponse<MatchRecordResponse>>

    // 멤버십 관련 API
    @POST("api/members/memberships")
    suspend fun registerMembership(@Body request: MembershipRegistrationRequest): Response<ApiResponse<Any>>

    // QR 이벤트 처리 API (동적 URL) - Body 없는 POST
    @POST
    suspend fun postQrEvent(
        @Url eventUrl: String
    ): Response<ApiResponse<Any>>

    @GET("api/members/points/products")
    suspend fun getShopProducts(): Response<ApiResponse<ShopProductListResponse>>

    @POST("api/members/points/products/{productId}/purchases/qr")
    suspend fun purchaseProductWithQr(@Path("productId") productId: Long): Response<ApiResponse<QrPurchaseResponse>>

    // 활동 인증 API (Activity Certification)
    @Multipart
    @POST("api/members/activities/certifications")
    suspend fun certifyActivity(
        @Part image: MultipartBody.Part
    ): Response<ApiResponse<Any>>

    // FCM 토큰 업데이트 API
    @POST("api/members/fcm-token")
    suspend fun updateFcmToken(@Body request: UpdateFcmTokenRequest): Response<ApiResponse<Any>>

    // 로그아웃 API
    @POST("api/members/auth/logout")
    suspend fun logout(): Response<ApiResponse<Any>>

    // 회원 탈퇴 API
    @DELETE("api/members/me")
    suspend fun withdraw(): Response<ApiResponse<Any>>

    // 광고 배너 API
    @GET("api/members/advertisements")
    suspend fun getAdvertisements(@Query("position") position: String): Response<ApiResponse<AdvertisementListResponse>>

    // 활동 이미지 API
    @GET("api/members/activities/images")
    suspend fun getActivityImages(): Response<ApiResponse<ActivityImageListResponse>>

    // 활동 신청 내역 조회 API
    @GET("api/members/activities/applications/me")
    suspend fun getActivityApplications(): Response<ApiResponse<ActivityApplicationListResponse>>

    // 알림 목록 조회 API
    @GET("api/members/notifications/me")
    suspend fun getNotifications(): Response<ApiResponse<NotificationListResponse>>

    // 미읽은 알림 수 조회 API
    @GET("api/members/notifications/unread-count")
    suspend fun getUnreadNotificationCount(): Response<ApiResponse<UnreadCountResponse>>

    // 알림 전체 읽음 처리 API
    @PATCH("api/members/notifications/read")
    suspend fun markAllNotificationsAsRead(): Response<ApiResponse<Any>>
}
