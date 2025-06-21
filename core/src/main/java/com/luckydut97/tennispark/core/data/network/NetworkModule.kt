package com.luckydut97.tennispark.core.data.network

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.luckydut97.tennispark.core.data.storage.TokenManager
import com.luckydut97.tennispark.core.data.storage.TokenManagerImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {
    private const val BASE_URL = "https://tennis-park.store/"
    private val tag = "🔍 디버깅: NetworkModule"

    private var appContext: Context? = null

    fun initialize(context: Context) {
        appContext = context.applicationContext
        Log.d(tag, "=== NetworkModule 초기화 시작 ===")
        Log.d(tag, "디버깅: BASE_URL = $BASE_URL")
        Log.d(tag, "🔧 AuthInterceptor 초기화 시작...")

        // AuthInterceptor 초기화 확인
        val tokenManager = TokenManagerImpl(appContext!!)
        val authInterceptor = AuthInterceptor(tokenManager)
        Log.d(tag, "✅ AuthInterceptor 생성 완료")

        Log.d(tag, "=== NetworkModule 초기화 완료 ===")
    }

    fun getContext(): Context? {
        return appContext
    }

    private val loggingInterceptor = HttpLoggingInterceptor { message ->
        Log.d(tag, "HTTP: $message")
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient: OkHttpClient by lazy {
        Log.d(tag, "🔧 OkHttpClient 초기화 시작...")
        Log.d(tag, "  appContext 상태: ${appContext != null}")

        val builder = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)

        // 🔥 AuthInterceptor 추가 (Context 확인 후)
        if (appContext != null) {
            val tokenManager = TokenManagerImpl(appContext!!)
            val authInterceptor = AuthInterceptor(tokenManager)
            builder.addInterceptor(authInterceptor)
            Log.d(tag, "✅ AuthInterceptor 추가 완료!")
        } else {
            Log.e(tag, "❌ appContext가 null - AuthInterceptor 추가 실패!")
        }

        val client = builder
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        Log.d(tag, "✅ OkHttpClient 초기화 완료 (Interceptor 개수: ${client.interceptors.size})")
        client
    }

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit: Retrofit by lazy {
        Log.d(tag, "🔧 Retrofit 초기화 시작...")
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        Log.d(tag, "✅ Retrofit 초기화 완료")
        retrofit
    }

    val apiService: ApiService by lazy {
        Log.d(tag, "🔧 ApiService 초기화 시작...")
        val service = retrofit.create(ApiService::class.java)
        Log.d(tag, "✅ ApiService 초기화 완료")
        service
    }
}
