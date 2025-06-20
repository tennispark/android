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

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .apply {
            if (appContext != null) {
                val tokenManager = TokenManagerImpl(appContext!!)
                val authInterceptor = AuthInterceptor(tokenManager)
                addInterceptor(authInterceptor)
            }
        }
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
