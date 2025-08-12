package com.luckydut97.feature_home.main.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.luckydut97.tennispark.core.ui.theme.Pretendard
import com.luckydut97.feature_home.main.viewmodel.HomeViewModel
import kotlinx.coroutines.delay

@Composable
fun WeeklyPhotoSection(
    homeViewModel: HomeViewModel = viewModel()
) {
    val tag = "🔍 디버깅: WeeklyPhotoSection"

    val activityImages by homeViewModel.activityImages.collectAsState()
    val isLoadingImages by homeViewModel.isLoadingImages.collectAsState()
    val totalPages by homeViewModel.totalActivityImagePages.collectAsState()

    Log.d(
        tag,
        "[WeeklyPhotoSection] rendering - images: ${activityImages.size}, isLoading: $isLoadingImages, totalPages: $totalPages"
    )

    // 무한 스크롤을 위한 매우 큰 페이지 수 설정
    val infinitePageCount = Int.MAX_VALUE
    val pagerState = rememberPagerState(
        initialPage = if (activityImages.isNotEmpty()) {
            infinitePageCount / 2 - (infinitePageCount / 2) % activityImages.size
        } else 0,
        pageCount = { infinitePageCount }
    )

    // 자동 스크롤 (이미지가 있을 때만, 개선된 버전)
    LaunchedEffect(activityImages.size) {
        if (activityImages.isNotEmpty()) {
            while (true) {
                delay(5000)
                // 스크롤 중이 아닐 때만 자동 넘김 (사용자 드래그 중단 방지)
                if (!pagerState.isScrollInProgress) {
                    try {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    } catch (e: Exception) {
                        // 애니메이션 충돌 시 무시
                        Log.w(tag, "[WeeklyPhotoSection] Animation conflict ignored: ${e.message}")
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 17.dp)
            .padding(top = 20.dp, bottom = 10.dp) // 바텀 패딩을 50dp에서 10dp로 변경
    ) {
        Text(
            text = "이번주 활동 사진",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Pretendard,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // 사진 컨테이너
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f) // 정사각형 비율
                .clip(RoundedCornerShape(10.dp)) // 컨테이너 전체에 clip 적용
        ) {
            if (activityImages.isNotEmpty()) {
                // HorizontalPager로 이미지 슬라이드 (무한 스크롤)
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    // 모듈로 연산으로 실제 이미지 인덱스 계산
                    val imageIndex = page % activityImages.size
                    val imageUrl = activityImages[imageIndex]
                    Log.d(
                        tag,
                        "[WeeklyPhotoSection] showing image[$imageIndex] (page: $page): $imageUrl"
                    )

                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Weekly Activity Photo ${imageIndex + 1}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        onSuccess = {
                            Log.d(tag, "[WeeklyPhotoSection] image loaded successfully: $imageUrl")
                        },
                        onError = {
                            Log.e(tag, "[WeeklyPhotoSection] image load failed: $imageUrl")
                        }
                    )
                }

                // 페이지 인디케이터 (이미지가 2개 이상일 때만 표시, 무한 스크롤 대응)
                if (activityImages.size > 1) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 22.dp)
                            .size(width = 40.dp, height = 17.dp)
                            .background(
                                color = Color(0x4D000000), // 검정색 30% 투명도
                                shape = RoundedCornerShape(70)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${(pagerState.currentPage % activityImages.size) + 1} / ${activityImages.size}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White,
                            fontFamily = Pretendard,
                            modifier = Modifier
                                .wrapContentHeight(Alignment.CenterVertically)
                                .offset(y = (-0.5).dp), // 미세 조정
                            textAlign = TextAlign.Center,
                            lineHeight = 10.sp
                        )
                    }
                }
            } else if (!isLoadingImages) {
                // 이미지가 없을 때 빈 상태 표시
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "아직 활동 사진이 없습니다.",
                        fontSize = 16.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF8B9096),
                        textAlign = TextAlign.Center
                    )
                }
                Log.d(tag, "[WeeklyPhotoSection] no images available")
            } else {
                // 로딩 상태 표시
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "활동 사진을 불러오는 중...",
                        fontSize = 16.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF8B9096),
                        textAlign = TextAlign.Center
                    )
                }
                Log.d(tag, "[WeeklyPhotoSection] loading images...")
            }
        }
    }
}
