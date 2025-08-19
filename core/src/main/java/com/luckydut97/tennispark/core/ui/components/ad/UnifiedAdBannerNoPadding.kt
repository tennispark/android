package com.luckydut97.tennispark.core.ui.components.ad

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.luckydut97.tennispark.core.data.model.AdBannerData
import com.luckydut97.tennispark.core.data.model.Advertisement
import com.luckydut97.tennispark.core.ui.components.animation.PressableComponent
import com.luckydut97.tennispark.core.ui.theme.Pretendard
import com.luckydut97.tennispark.core.utils.launchUrl
import kotlinx.coroutines.delay

/**
 * 패딩이 없는 광고 배너 - 기존 로컬 리소스용
 * (Bottom Sheet 등 좁은 공간에서 사용)
 */
@Composable
fun UnifiedAdBannerNoPadding(
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

    val tag = "🔍 디버깅: UnifiedAdBannerNoPadding"
    Log.d(tag, "[UnifiedAdBannerNoPadding] rendering with ${bannerList.size} local banners")

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) { page ->
            val bannerIndex = page % bannerList.size
            val banner = bannerList[bannerIndex]
            PressableComponent(
                onClick = {
                    Log.d(tag, "[UnifiedAdBannerNoPadding] banner clicked: url=${banner.url}")
                    context.launchUrl(banner.url)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = banner.imageRes),
                    contentDescription = "광고배너_${bannerIndex + 1}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.FillWidth
                )
            }
        }

        // 인디케이터 - 숫자형 단순화 (예: "1 / 3")
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-12).dp, y = (-12).dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp, 17.dp)
                    .background(Color(0x4D000000), RoundedCornerShape(70.dp))
                    .clip(RoundedCornerShape(70.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${pagerState.currentPage % bannerList.size + 1} / ${bannerList.size}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontFamily = Pretendard,
                    lineHeight = 10.sp,
                    modifier = Modifier
                        .wrapContentHeight()
                        .offset(y = (-0.5).dp)
                )
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

/**
 * 패딩이 없는 광고 배너 - API 기반 버전
 * (Bottom Sheet 등 좁은 공간에서 사용)
 */
@Composable
fun UnifiedAdBannerNoPaddingApi(
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

    val tag = "🔍 디버깅: UnifiedAdBannerNoPaddingApi"
    Log.d(tag, "[UnifiedAdBannerNoPaddingApi] rendering with ${advertisements.size} API banners")

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) { page ->
            val adIndex = page % advertisements.size
            val advertisement = advertisements[adIndex]
            PressableComponent(
                onClick = {
                    Log.d(
                        tag,
                        "[UnifiedAdBannerNoPaddingApi] ad clicked: id=${advertisement.id}, linkUrl=${advertisement.linkUrl}"
                    )
                    context.launchUrl(advertisement.linkUrl)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = advertisement.imageUrl,
                    contentDescription = "광고배너_${advertisement.id}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.FillWidth,
                    onSuccess = {
                        Log.d(
                            tag,
                            "[UnifiedAdBannerNoPaddingApi] image loaded successfully: ${advertisement.imageUrl}"
                        )
                    },
                    onError = {
                        Log.e(
                            tag,
                            "[UnifiedAdBannerNoPaddingApi] image load failed: ${advertisement.imageUrl}"
                        )
                    }
                )
            }
        }

        // 인디케이터 - 숫자형 단순화 (예: "1 / 3")
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-12).dp, y = (-12).dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp, 17.dp)
                    .background(Color(0x4D000000), RoundedCornerShape(70.dp))
                    .clip(RoundedCornerShape(70.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${pagerState.currentPage % advertisements.size + 1} / ${advertisements.size}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontFamily = Pretendard,
                    lineHeight = 10.sp,
                    modifier = Modifier
                        .wrapContentHeight()
                        .offset(y = (-0.5).dp)
                )
            }
        }
    }

    // 자동 스크롤
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