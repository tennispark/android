package com.luckydut97.tennispark.core.data.repository

import android.util.Log
import com.luckydut97.tennispark.core.data.model.ApiResponse
import com.luckydut97.tennispark.core.data.model.ShopProductListResponse
import com.luckydut97.tennispark.core.data.network.NetworkModule

class ShopRepository {
    private val tag = "🔍 디버깅: ShopRepository"
    private val apiService = NetworkModule.apiService

    suspend fun getShopProducts(): ApiResponse<ShopProductListResponse> {
        Log.d(tag, "=== 상품 리스트 조회 API 호출 시작 ===")
        Log.d(tag, "Endpoint: GET /api/members/points/products")
        Log.d(tag, "HTTP Method: GET")

        return try {
            val response = apiService.getShopProducts()
            Log.d(tag, "📡 API 응답 수신: code=${response.code()} message=${response.message()}")
            if (response.isSuccessful) {
                val body = response.body()
                Log.d(tag, "✅ 상품 리스트 조회 성공: $body")
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
                Log.e(tag, "❌ 상품 리스트 조회 실패: ${response.code()} $errorBody")
                ApiResponse(
                    success = false,
                    response = null,
                    error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                        status = response.code(),
                        message = when (response.code()) {
                            401 -> "인증이 되지 않았습니다."
                            else -> "서버 오류가 발생했습니다."
                        }
                    )
                )
            }
        } catch (e: Exception) {
            Log.e(tag, "💥 네트워크 예외 발생: ${e.message}", e)
            ApiResponse(
                success = false,
                response = null,
                error = com.luckydut97.tennispark.core.data.model.ErrorResponse(
                    status = 0,
                    message = "네트워크 오류: ${e.message}"
                )
            )
        } finally {
            Log.d(tag, "=== 상품 리스트 조회 API 호출 완료 ===")
        }
    }
}
