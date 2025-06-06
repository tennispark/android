package com.luckydut97.feature_home_shop.data.repository

import com.luckydut97.tennispark.core.data.model.ShopItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * 상품 데이터를 위한 Mock Repository
 */
class MockShopRepository {

    /**
     * 상품 목록을 가져오는 함수
     */
    fun getShopItems(): Flow<List<ShopItem>> = flow {
        // 네트워크 지연 시뮬레이션
        delay(1000)

        emit(mockShopItems)
    }

    /**
     * 사용자 포인트를 가져오는 함수
     */
    suspend fun getUserPoints(): Int {
        delay(500)
        return 12000
    }

    companion object {
        private val mockShopItems = listOf(
            ShopItem(
                id = "1",
                brandName = "브랜드명",
                productName = "테니스 용품 제품명",
                price = 6300
            ),
            ShopItem(
                id = "2",
                brandName = "브랜드명",
                productName = "테니스 용품 제품명",
                price = 6300
            ),
            ShopItem(
                id = "3",
                brandName = "브랜드명",
                productName = "테니스 용품 제품명",
                price = 6300
            ),
            ShopItem(
                id = "4",
                brandName = "브랜드명",
                productName = "테니스 용품 제품명",
                price = 6300
            ),
            ShopItem(
                id = "5",
                brandName = "브랜드명",
                productName = "테니스 용품 제품명",
                price = 6300
            ),
            ShopItem(
                id = "6",
                brandName = "브랜드명",
                productName = "테니스 용품 제품명",
                price = 6300
            ),
            ShopItem(
                id = "7",
                brandName = "브랜드명",
                productName = "테니스 용품 제품명",
                price = 6300
            )
        )
    }
}
