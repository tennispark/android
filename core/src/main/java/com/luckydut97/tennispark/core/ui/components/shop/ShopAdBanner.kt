package com.luckydut97.tennispark.core.ui.components.shop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * 상품 광고 배너 컴포넌트
 * 크기: fillMaxWidth × 67dp
 */
@Composable
fun ShopAdBanner(
    totalPages: Int = 3,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { totalPages })

    // 5초마다 자동 넘김
    LaunchedEffect(key1 = Unit) {
        while (true) {
            delay(5000)
            if (pagerState.currentPage < totalPages - 1) {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            } else {
                pagerState.animateScrollToPage(0)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(134.dp)
    ) {
        // 광고 페이저
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            // 광고 내용 (임시로 회색 배경)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(134.dp)
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                // 나중에 실제 광고 이미지나 컨텐츠로 교체
            }
        }

        // 하단 중앙의 페이지 인디케이터
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(totalPages) { index ->
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(
                            color = if (index == pagerState.currentPage) Color.Black else Color.Gray,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}