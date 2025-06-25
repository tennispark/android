package com.luckydut97.tennispark.core.data.model

import com.google.gson.annotations.SerializedName

/**
 * 상품 리스트 응답
 */
data class ShopProductListResponse(
    @SerializedName("products")
    val products: List<ShopProductResponse>
)

/**
 * 단일 상품 정보
 */
data class ShopProductResponse(
    @SerializedName("productId")
    val productId: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("brand")
    val brand: String,
    @SerializedName("price")
    val price: Int,
    @SerializedName("imageUrl")
    val imageUrl: String?
)