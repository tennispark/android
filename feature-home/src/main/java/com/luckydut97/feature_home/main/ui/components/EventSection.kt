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
import androidx.compose.runtime.remember
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
import com.luckydut97.tennispark.core.ui.components.animation.PressableComponent
import kotlinx.coroutines.delay
import java.time.ZonedDateTime
import java.time.ZoneId

@Composable
fun EventSection(
    totalPages: Int,
    onMembershipClick: () -> Unit,
    onAcademyClick: () -> Unit,
    onCourtIntroClick: () -> Unit,
    onLeagueClick: () -> Unit
) {
    // 무한 스크롤을 위한 매우 큰 페이지 수 설정
    val infinitePageCount = Int.MAX_VALUE
    val pagerState = rememberPagerState(
        initialPage = infinitePageCount / 2 - (infinitePageCount / 2) % totalPages, // 첫 번째 페이지부터 시작
        pageCount = { infinitePageCount }
    )

    val currentMonth = remember {
        ZonedDateTime.now(ZoneId.of("Asia/Seoul")).monthValue
    }

    LaunchedEffect(key1 = Unit) {
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
            val pageIndex = page % totalPages // 실제 페이지 인덱스 계산
            when (pageIndex) {
                0 -> EventCard(
                    iconRes = R.drawable.ic_member,
                    title = "멤버십 등록하기",
                    subtitle = "${(currentMonth % 12) + 1}월 정기 멤버십 등록",
                    pageIndicator = "${pageIndex + 1} / $totalPages",
                    onClick = onMembershipClick
                )
                1 -> EventCard(
                    iconRes = R.drawable.ic_tennis,
                    title = "아카데미 등록하기",
                    subtitle = "아카데미 바로 신청하기",
                    pageIndicator = "${pageIndex + 1} / $totalPages",
                    onClick = onAcademyClick
                )
                2 -> EventCard(
                    iconRes = R.drawable.ic_tennis,
                    title = "활동 코트 소개",
                    subtitle = "각 코트별 소개",
                    pageIndicator = "${pageIndex + 1} / $totalPages",
                    onClick = onCourtIntroClick
                )
                3 -> EventCard(
                    iconRes = R.drawable.ic_league,
                    title = "행사 참여",
                    subtitle = "팀 리그전 참여하기",
                    pageIndicator = "${pageIndex + 1} / $totalPages",
                    onClick = onLeagueClick
                )
                else -> EventCard(
                    iconRes = R.drawable.ic_member,
                    title = "멤버십 등록하기",
                    subtitle = "${(currentMonth % 12) + 1}월 정기 멤버십 등록",
                    pageIndicator = "${pageIndex + 1} / $totalPages",
                    onClick = onMembershipClick
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
    PressableComponent(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 17.dp,
                        top = 16.dp,
                        end = 17.dp,
                        bottom = 11.dp
                    )  // 아래 여백만 8dp로 설정
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
}
