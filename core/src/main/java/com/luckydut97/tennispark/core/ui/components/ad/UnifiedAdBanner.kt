package com.luckydut97.tennispark.core.ui.components.ad

import android.app.Activity
import android.content.Context
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.luckydut97.tennispark.core.data.model.AdBannerData
import com.luckydut97.tennispark.core.ui.components.animation.PressableComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.luckydut97.tennispark.core.utils.launchUrl

/**
 * 앱 내 공통 사용 광고배너 (자동 스크롤, 인디케이터 & 클릭시 외부 URL)
 */
@Composable
fun UnifiedAdBanner(
    bannerList: List<AdBannerData>,
    modifier: Modifier = Modifier
) {
    if (bannerList.isEmpty()) return
    val pagerState = rememberPagerState(pageCount = { bannerList.size })
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // 5초마다 자동 넘김
    LaunchedEffect(key1 = Unit) {
        while (true) {
            delay(5000)
            if (pagerState.currentPage < bannerList.size - 1) {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            } else {
                pagerState.animateScrollToPage(0)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 17.dp) // 패딩 통일
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                val banner = bannerList[page]
                PressableComponent(
                    onClick = {
                        context.launchUrl(banner.url)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = banner.imageRes),
                        contentDescription = "광고배너_${page + 1}",
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.FillWidth
                    )
                }
            }
            // 인디케이터
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(bannerList.size) { index ->
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
}
