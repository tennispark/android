package com.luckydut97.tennispark.core.ui.components.ad

import android.app.Activity
import android.content.Context
import android.util.Log
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
import coil.compose.AsyncImage
import com.luckydut97.tennispark.core.data.model.AdBannerData
import com.luckydut97.tennispark.core.data.model.Advertisement
import com.luckydut97.tennispark.core.ui.components.animation.PressableComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.luckydut97.tennispark.core.utils.launchUrl

/**
 * 앱 내 공통 사용 광고배너 (자동 스크롤, 인디케이터 & 클릭시 외부 URL)
 * - 기존 로컬 리소스 지원 (AdBannerData)
 */
@Composable
fun UnifiedAdBanner(
    bannerList: List<AdBannerData>,
    modifier: Modifier = Modifier
) {
    if (bannerList.isEmpty()) return

    // 무한 스크롤을 위한 매우 큰 페이지 수 설정
    val infinitePageCount = Int.MAX_VALUE
    val pagerState = rememberPagerState(
        initialPage = infinitePageCount / 2 - (infinitePageCount / 2) % bannerList.size, // 첫 번째 배너부터 시작
        pageCount = { infinitePageCount }
    )
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val tag = "🔍 디버깅: UnifiedAdBanner"
    Log.d(tag, "[UnifiedAdBanner] rendering with ${bannerList.size} local banners")

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
                val bannerIndex = page % bannerList.size
                val banner = bannerList[bannerIndex]
                PressableComponent(
                    onClick = {
                        Log.d(tag, "[UnifiedAdBanner] banner clicked: url=${banner.url}")
                        context.launchUrl(banner.url)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = banner.imageRes),
                        contentDescription = "광고배너_${bannerIndex + 1}",
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
                                color = if (index == pagerState.currentPage % bannerList.size) Color.Black else Color.Gray,
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }

    // 간단한 자동 스크롤 (5초마다, 사용자 드래그 중에는 건너뛰기)
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            // 스크롤 중이 아닐 때만 자동 넘김
            if (!pagerState.isScrollInProgress) {
                try {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                } catch (e: Exception) {
                    // 애니메이션 충돌 시 무시
                }
            }
        }
    }
}

/**
 * 앱 내 공통 사용 광고배너 - API 기반 버전
 * (자동 스크롤, 인디케이터 & 클릭시 외부 URL)
 */
@Composable
fun UnifiedAdBannerApi(
    advertisements: List<Advertisement>,
    modifier: Modifier = Modifier
) {
    if (advertisements.isEmpty()) return

    // 무한 스크롤을 위한 매우 큰 페이지 수 설정
    val infinitePageCount = Int.MAX_VALUE
    val pagerState = rememberPagerState(
        initialPage = infinitePageCount / 2 - (infinitePageCount / 2) % advertisements.size,
        pageCount = { infinitePageCount }
    )
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val tag = "🔍 디버깅: UnifiedAdBannerApi"
    Log.d(tag, "[UnifiedAdBannerApi] rendering with ${advertisements.size} API banners")

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 17.dp)
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
                val adIndex = page % advertisements.size
                val advertisement = advertisements[adIndex]
                PressableComponent(
                    onClick = {
                        Log.d(
                            tag,
                            "[UnifiedAdBannerApi] ad clicked: id=${advertisement.id}, linkUrl=${advertisement.linkUrl}"
                        )
                        context.launchUrl(advertisement.linkUrl)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AsyncImage(
                        model = advertisement.imageUrl,
                        contentDescription = "광고배너_${advertisement.id}",
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.FillWidth,
                        onSuccess = {
                            Log.d(
                                tag,
                                "[UnifiedAdBannerApi] image loaded successfully: ${advertisement.imageUrl}"
                            )
                        },
                        onError = {
                            Log.e(
                                tag,
                                "[UnifiedAdBannerApi] image load failed: ${advertisement.imageUrl}"
                            )
                        }
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
                repeat(advertisements.size) { index ->
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(
                                color = if (index == pagerState.currentPage % advertisements.size) Color.Black else Color.Gray,
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }

    // 자동 스크롤
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            if (!pagerState.isScrollInProgress) {
                try {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                } catch (e: Exception) {
                    // 애니메이션 충돌 시 무시
                }
            }
        }
    }
}
