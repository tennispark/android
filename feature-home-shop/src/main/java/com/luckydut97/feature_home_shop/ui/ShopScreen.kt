package com.luckydut97.feature_home_shop.ui

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luckydut97.feature_home_shop.viewmodel.ShopViewModel
import com.luckydut97.tennispark.core.R
import com.luckydut97.tennispark.core.shop.data.repository.MockShopRepository
import com.luckydut97.tennispark.core.ui.components.navigation.TopBar
import com.luckydut97.tennispark.core.ui.components.shop.ShopAdBanner
import com.luckydut97.tennispark.core.ui.components.shop.ShopItemComponent
import com.luckydut97.tennispark.core.ui.theme.Pretendard

/**
 * 마이살래 화면
 */
@Composable
fun ShopScreen(
    onBackClick: () -> Unit = {},
    viewModel: ShopViewModel = viewModel { ShopViewModel(MockShopRepository()) } // Hilt 등 DI 적용 전 임시
) {
    val shopItems by viewModel.shopItems.collectAsState()
    val userPoints by viewModel.userPoints.collectAsState()
    // val isLoading by viewModel.isLoading.collectAsState() // 필요시 사용

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBar(
                title = "마이살래", // 이미지 요청대로 "마이살래"
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 광고 배너
            ShopAdBanner(totalPages = 3) // 3개 광고로 설정

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
                    ShopItemComponent(item = item)
                }

                // 마지막 아이템 아래에 추가 여백 (스크롤 영역 확보)
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}
