package com.luckydut97.tennispark.core.data.network

import android.content.Context
import com.google.gson.GsonBuilder
import com.luckydut97.tennispark.core.data.storage.TokenManager
import com.luckydut97.tennispark.core.data.storage.TokenManagerImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object  NetworkModule {
    private const val BASE_URL = "https://tennis-park.store/"
    private val tag = "🔍 디버깅: NetworkModule"

    private var appContext: Context? = null

    fun initialize(context: Context) {
        appContext = context.applicationContext
    }

    fun getContext(): Context? {
        return appContext
    }

    private val loggingInterceptor = HttpLoggingInterceptor { message ->
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient: OkHttpClient by lazy {

        val builder = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)

        // AuthInterceptor 추가
        if (appContext != null) {
            val tokenManager = TokenManagerImpl(appContext!!)
            val authInterceptor = AuthInterceptor(tokenManager)
            builder.addInterceptor(authInterceptor)
        } else {
        }

        val client = builder
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        client
    }

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit: Retrofit by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        retrofit
    }

    val apiService: ApiService by lazy {
        val service = retrofit.create(ApiService::class.java)
        service
    }
}
