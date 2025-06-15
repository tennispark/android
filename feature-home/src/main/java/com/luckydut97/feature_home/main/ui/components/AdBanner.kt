package com.luckydut97.feature_home.main.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
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
import com.luckydut97.tennispark.feature.home.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AdBanner() {
    val adImages = listOf(
        R.drawable.test_ad_img1,
        R.drawable.test_ad_img2,
        R.drawable.test_ad_img3,
        R.drawable.test_ad_img4
    )

    val pagerState = rememberPagerState(pageCount = { adImages.size })
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // 자동 스크롤
    LaunchedEffect(key1 = Unit) {
        while (true) {
            delay(5000) // 5초마다 자동 스크롤
            if (pagerState.currentPage < adImages.size - 1) {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            } else {
                pagerState.animateScrollToPage(0)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp) // 좌우 여백 18dp씩 (총 36dp)
            .padding(top = 10.dp)
    ) {
        // 페이저를 감싸는 Box에 clip 적용
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f) // 3:1 비율
                .clip(RoundedCornerShape(10.dp)) // 전체 컨테이너에 라운드 적용
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
                        .aspectRatio(3f) // 3:1 비율 유지
                        .clickable {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "클릭되었습니다.",
                                    duration = androidx.compose.material3.SnackbarDuration.Short
                                )
                            }
                        },
                    contentScale = ContentScale.Fit // 이미지가 잘리지 않도록 Fit 사용
                )
            }
        }

        // 스낵바 호스트
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
