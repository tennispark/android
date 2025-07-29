package com.luckydut97.feature_home_shop.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luckydut97.feature_home_shop.viewmodel.ShopViewModel
import com.luckydut97.feature_home_shop.data.model.ShopItem
import com.luckydut97.feature_home_shop.data.repository.ShopRepositoryImpl
import com.luckydut97.tennispark.core.ui.components.navigation.TopBar
import com.luckydut97.tennispark.core.ui.components.ad.UnifiedAdBannerApi
import com.luckydut97.tennispark.core.data.model.Advertisement
import com.luckydut97.tennispark.core.data.model.AdPosition
import com.luckydut97.tennispark.core.data.network.NetworkModule
import com.luckydut97.tennispark.core.data.repository.AdBannerRepositoryImpl
import com.luckydut97.feature_home_shop.ui.components.ShopItemComponent
import com.luckydut97.tennispark.core.ui.theme.Pretendard
import com.luckydut97.feature_home_shop.R
import com.luckydut97.tennispark.core.ui.components.navigation.NoArrowTopBar
import com.luckydut97.feature_home_shop.ui.ShopPurchaseState

/**
 * 마이살래 화면
 */
@Composable
fun ShopScreen(
    onBackClick: () -> Unit = {},
    onItemClick: (ShopItem) -> Unit = {},
    viewModel: ShopViewModel = viewModel { ShopViewModel(shopRepository = ShopRepositoryImpl()) } // 실제 API 사용
) {
    val tag = "🔍 디버깅: ShopScreen"

    val shopItems by viewModel.shopItems.collectAsState()
    val userPoints by viewModel.userPoints.collectAsState()
    // val isLoading by viewModel.isLoading.collectAsState() // 필요시 사용

    // 광고 배너 상태 - PURCHASE position
    var advertisements by remember { mutableStateOf<List<Advertisement>>(emptyList()) }
    var isLoadingAds by remember { mutableStateOf(false) }

    val adBannerRepository = remember {
        AdBannerRepositoryImpl(NetworkModule.apiService)
    }

    // Refresh user points when ShopScreen becomes visible again
    LaunchedEffect(Unit) {
        viewModel.refreshUserPoints()

        // PURCHASE position 광고 로드
        Log.d(tag, "[ShopScreen] loading PURCHASE advertisements")
        isLoadingAds = true
        try {
            adBannerRepository.getAdvertisements(AdPosition.PURCHASE).collect { ads ->
                Log.d(tag, "[ShopScreen] received ${ads.size} PURCHASE advertisements")
                advertisements = ads
            }
        } catch (e: Exception) {
            Log.e(tag, "[ShopScreen] Exception: ${e.message}", e)
            advertisements = emptyList()
        } finally {
            isLoadingAds = false
        }
    }

    // Monitor purchase completion and refresh points
    LaunchedEffect(ShopPurchaseState.isPurchaseCompleted) {
        if (ShopPurchaseState.isPurchaseCompleted) {
            viewModel.refreshUserPoints()
            ShopPurchaseState.isPurchaseCompleted = false // Reset state
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            NoArrowTopBar(
                title = "마이살래" // 이미지 요청대로 "마이살래"

            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 광고 배너 - API 기반으로 변경
            if (advertisements.isNotEmpty()) {
                Log.d(tag, "[ShopScreen] showing ${advertisements.size} PURCHASE advertisements")
                UnifiedAdBannerApi(advertisements = advertisements)
            } else if (!isLoadingAds) {
                Log.d(tag, "[ShopScreen] no PURCHASE advertisements available")
                // 광고가 없으면 높이 조정을 위한 Spacer
                Spacer(modifier = Modifier.height(60.dp))
            }

            // 내 포인트 섹션
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 17.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "내 포인트",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Pretendard,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.width(8.dp))

                // 코인 아이콘 (ic_coin_black)
                Icon(
                    painter = painterResource(id = com.luckydut97.tennispark.core.R.drawable.ic_coin_black),
                    contentDescription = "포인트",
                    modifier = Modifier.size(16.dp),
                    tint = Color.Unspecified // 원본 색상 유지
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = String.format("%,d", userPoints), // ViewModel에서 가져온 포인트
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Pretendard,
                    color = Color.Black
                )

                Text(
                    text = "P",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Pretendard,
                    color = Color.Black
                )
            }

            // 상품 목록
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 17.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                // 첫 번째 아이템 위에 추가 여백 (디자인상 필요 없어 보이면 0.dp)
                item {
                    Spacer(modifier = Modifier.height(0.dp))
                }

                items(shopItems) { item ->
                    ShopItemComponent(
                        item = item,
                        onItemClick = { onItemClick(item) }
                    )
                }

                // 마지막 아이템 아래에 추가 여백 (스크롤 영역 확보)
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}
