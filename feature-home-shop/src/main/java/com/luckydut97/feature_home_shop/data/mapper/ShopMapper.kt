package com.luckydut97.feature_home_shop.data.mapper

import com.luckydut97.tennispark.core.data.model.ShopProductResponse
import com.luckydut97.feature_home_shop.data.model.ShopItem

fun ShopProductResponse.toShopItem(): ShopItem {
    return ShopItem(
        id = productId.toString(),
        brandName = brand,
        productName = name,
        price = price,
        imageUrl = imageUrl
    )
}