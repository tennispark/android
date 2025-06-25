package com.luckydut97.feature_home_shop.data.repository

import com.luckydut97.feature_home_shop.data.model.ShopItem
import com.luckydut97.feature_home_shop.data.mapper.toShopItem
import com.luckydut97.tennispark.core.data.repository.ShopRepository as CoreShopRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * 실제 core ShopRepository를 사용해서 앱 도메인 ShopItem 리스트를 반환하는 Wrapper.
 */
class ShopRepositoryImpl {
    private val coreRepo = CoreShopRepository()

    fun getShopItems(): Flow<List<ShopItem>> = flow {
        val apiResponse = coreRepo.getShopProducts()
        val products = apiResponse.response?.products?.map { it.toShopItem() } ?: emptyList()
        emit(products)
    }

    suspend fun getUserPoints(): Int {
        // TODO: 실제 포인트 연동 필요시 core PointRepository 등 활용해 구현
        return 0
    }
}
