package com.luckydut97.feature_home.main.ui.components

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
import com.luckydut97.tennispark.core.ui.theme.Pretendard
import com.luckydut97.tennispark.feature.home.R
import kotlinx.coroutines.delay

@Composable
fun WeeklyPhotoSection() {
    val totalPages = 3 // 3개 이미지가 있다고 가정
    val pagerState = rememberPagerState(pageCount = { totalPages })

    // 자동 스크롤 (선택사항)
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 17.dp)
            .padding(top = 20.dp, bottom = 50.dp) // 바텀 네비게이션을 위한 여백
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
            // HorizontalPager로 이미지 슬라이드
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                // 정사각형 이미지
                Image(
                    painter = painterResource(id = R.drawable.test_activity_img),
                    contentDescription = "Weekly Activity Photo ${page + 1}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // 페이지 인디케이터
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
                    text = "${pagerState.currentPage + 1} / $totalPages",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    fontFamily = Pretendard,
                    modifier = Modifier
                        .wrapContentHeight(Alignment.CenterVertically)
                        .offset(y = (-0.5).dp), // EventSection과 동일한 미세 조정
                    textAlign = TextAlign.Center,
                    lineHeight = 10.sp
                )
            }
        }
    }
}
