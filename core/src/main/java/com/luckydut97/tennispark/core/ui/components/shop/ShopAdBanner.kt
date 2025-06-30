package com.luckydut97.tennispark.core.ui.components.shop

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 상품 광고 배너 컴포넌트
 * 외부에서 광고 이미지 리스트를 전달받아 사용
 */
@Composable
fun ShopAdBanner(
    adImages: List<Int> = emptyList(), // 광고 이미지 리소스 ID 리스트
    modifier: Modifier = Modifier
) {
    // 빈 리스트인 경우 기본 회색 배경으로 처리
    if (adImages.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 67.dp)
                    .background(Color(0xFFE0E0E0), RoundedCornerShape(10.dp))
            )
        }
        return
    }

    val pagerState = rememberPagerState(pageCount = { adImages.size })
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // 5초마다 자동 넘김
    LaunchedEffect(key1 = Unit) {
        while (true) {
            delay(5000)
            if (pagerState.currentPage < adImages.size - 1) {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            } else {
                pagerState.animateScrollToPage(0)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp) // 좌우 여백 18dp 유지
    ) {
        // 광고 페이저를 감싸는 Box에 clip 적용
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp)) // 메인화면과 동일한 라운드 적용
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                Image(
                    painter = painterResource(id = adImages[page]),
                    contentDescription = "Advertisement ${page + 1}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "클릭되었습니다.",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        },
                    contentScale = ContentScale.FillWidth // 반응형: 너비에 맞춰 사진 사이즈 조정
                )
            }

            // 하단 중앙의 페이지 인디케이터
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(adImages.size) { index ->
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

        // 스낵바 호스트
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
