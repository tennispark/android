package com.luckydut97.tennispark.core.shop.data.model

/**
 * 상품 데이터 모델
 */
data class ShopItem(
    val id: String,
    val brandName: String,
    val productName: String,
    val price: Int,
    val imageUrl: String? = null
)