package com.luckydut97.feature_home.main.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.tennispark.feature.home.R
import com.luckydut97.tennispark.core.ui.theme.Pretendard
import kotlinx.coroutines.delay

@Composable
fun EventSection(
    totalPages: Int,
    onMembershipClick: () -> Unit,
    onAcademyClick: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { totalPages })

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
            .padding(top = 28.dp, bottom = 20.dp)
    ) {
        Text(
            text = "이벤트",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Pretendard,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            when (page) {
                0 -> EventCard(
                    iconRes = R.drawable.ic_member,
                    title = "멤버십 등록하기",
                    subtitle = "5월 정기 멤버십 등록",
                    pageIndicator = "${page + 1} / $totalPages",
                    onClick = onMembershipClick
                )
                1 -> EventCard(
                    iconRes = R.drawable.ic_tennis,
                    title = "아카데미 등록하기",
                    subtitle = "5월 아카데미 등록",
                    pageIndicator = "${page + 1} / $totalPages",
                    onClick = onAcademyClick
                )
            }
        }
    }
}

@Composable
fun EventCard(
    iconRes: Int,
    title: String,
    subtitle: String,
    pageIndicator: String,  // "1/2" 또는 "2/2" 형태로 전달
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        onClick = onClick
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 17.dp, top = 16.dp, end = 17.dp, bottom = 11.dp)  // 아래 여백만 8dp로 설정
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // 아이콘
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(55.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // 제목과 부제목을 포함하는 컬럼
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // 제목과 화살표 한 행에 배치
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 제목
                        Text(
                            text = title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Pretendard,
                            color = Color.Black,
                            modifier = Modifier.weight(1f)
                        )

                        // 화살표
                        Image(
                            painter = painterResource(id = R.drawable.ic_arrow_right),
                            contentDescription = "Arrow Right",
                            modifier = Modifier.size(width = 7.dp, height = 14.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // 부제목
                    Text(
                        text = subtitle,
                        fontSize = 15.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF8B9096)
                    )
                }
            }

            // 페이지 인디케이터 행
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(17.dp)
                    .padding(top = 0.dp)
            ) {
                // 페이지 인디케이터 - 정확한 크기로 지정
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(width = 40.dp, height = 17.dp)
                        .background(
                            color = Color(0xFFBBBBBB),
                            shape = RoundedCornerShape(70)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = pageIndicator,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        fontFamily = Pretendard,
                        // 텍스트가 확실히
                        modifier = Modifier
                            .wrapContentHeight(Alignment.CenterVertically)
                            .offset(y = (-0.5).dp), // 미세 조정을 위한 offset 추가
                        textAlign = TextAlign.Center,
                        // 텍스트 lineHeight 설정으로 수직 중앙 정렬 개선
                        lineHeight = 10.sp
                    )
                }
            }
        }
    }
}
