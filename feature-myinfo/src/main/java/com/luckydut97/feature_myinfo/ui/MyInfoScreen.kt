package com.luckydut97.feature_myinfo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luckydut97.tennispark.core.R
import com.luckydut97.tennispark.core.ui.components.navigation.TopBar
import com.luckydut97.tennispark.core.ui.theme.Pretendard
import com.luckydut97.feature_myinfo.viewmodel.MyInfoViewModel
import com.luckydut97.tennispark.core.data.model.PointHistoryItem
import com.luckydut97.tennispark.core.ui.components.navigation.NoArrowTopBar
import com.luckydut97.tennispark.core.ui.components.ad.UnifiedAdBanner
import com.luckydut97.tennispark.core.data.model.unifiedAdBannerList
import com.luckydut97.tennispark.core.ui.components.animation.PressableComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 내 정보 화면
 */
@Composable
fun MyInfoScreen(
    onBackClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    viewModel: MyInfoViewModel = viewModel()
) {
    // ViewModel에서 데이터 구독
    val points by viewModel.points.collectAsState()
    val histories by viewModel.histories.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val memberInfo by viewModel.memberInfo.collectAsState()

    // 포인트 새로고침 핸들러
    val coroutineScope = rememberCoroutineScope()
    val refreshPoints = remember { { viewModel.refreshAllData() } }

    // 실제 API 데이터 또는 기본값 사용
    val userName = memberInfo?.name ?: "로딩 중..."
    val gameRecord = memberInfo?.record ?: com.luckydut97.tennispark.core.data.model.GameRecord(
        wins = 0, draws = 0, losses = 0, score = 0, ranking = 0
    )

    // 🔥 화면 진입 시마다 자동 새로고침
    LaunchedEffect(Unit) {
        viewModel.refreshAllData()
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            NoArrowTopBar(
                title = "회원 정보" // 이미지 요청대로 "마이살래"

            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 상단 고정 영역
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 17.dp)
                ) {
                    // 회원명
                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$userName 회원님",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Pretendard,
                            color = Color.Black
                        )

                        PressableComponent(
                            onClick = onSettingsClick,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = com.luckydut97.feature_myinfo.R.drawable.ic_setting),
                                contentDescription = "설정",
                                modifier = Modifier.size(24.dp),
                                tint = Color.Unspecified
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 포인트 및 경기 기록 박스
                    PressableComponent(
                        onClick = refreshPoints,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                                .background(
                                    color = Color(0xFFF2FAF4),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 24.dp),
                            contentAlignment = Alignment.Center // 중앙 정렬
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center // 수직 중앙 정렬
                            ) {
                                // 내 포인트 Row (실제 API 데이터 사용)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "내 포인트",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                        fontFamily = Pretendard,
                                        color = Color.Black
                                    )

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_coin_black),
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp),
                                            tint = Color.Unspecified
                                        )

                                        Spacer(modifier = Modifier.width(4.dp))

                                        Text(
                                            text = String.format(
                                                "%,d",
                                                points
                                            ), // ViewModel에서 받은 실제 포인트
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            fontFamily = Pretendard,
                                            color = Color.Black
                                        )

                                        Text(
                                            text = "P",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            fontFamily = Pretendard,
                                            color = Color.Black
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // 경기 기록 Row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "경기 기록",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                        fontFamily = Pretendard,
                                        color = Color.Black
                                    )

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${gameRecord.wins}",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            fontFamily = Pretendard,
                                            color = Color(0xFF145F44)
                                        )
                                        Text(
                                            text = "승 ",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            fontFamily = Pretendard,
                                            color = Color(0xFF145F44)
                                        )

                                        Text(
                                            text = "${gameRecord.draws}",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            fontFamily = Pretendard,
                                            color = Color(0xFFCBA439)
                                        )
                                        Text(
                                            text = "무 ",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            fontFamily = Pretendard,
                                            color = Color(0xFFCBA439)
                                        )

                                        Text(
                                            text = "${gameRecord.losses}",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            fontFamily = Pretendard,
                                            color = Color(0xFFEF3629)
                                        )
                                        Text(
                                            text = "패 ",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            fontFamily = Pretendard,
                                            color = Color(0xFFEF3629)
                                        )

                                        Text(
                                            text = "(${gameRecord.score}점/${gameRecord.ranking}위)",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Normal,
                                            fontFamily = Pretendard,
                                            color = Color.Black
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            // 광고 배너
            item {
                UnifiedAdBanner(
                    bannerList = unifiedAdBannerList
                )
            }

            // 여백 및 구분선
            item {
                Spacer(modifier = Modifier.height(36.dp))

                // 구분선 (좌우 여백 없이)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .background(Color(0xFFF5F5F5))
                )
            }

            // 포인트 내역 타이틀
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 17.dp)
                ) {
                    Spacer(modifier = Modifier.height(36.dp))

                    Text(
                        text = "나의 포인트 내역",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Pretendard,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(36.dp))
                }
            }

            // 포인트 내역 영역 (동적 높이)
            item {
                // 포인트 내역 개수에 따른 동적 높이 계산
                val dynamicHeight = if (histories.isEmpty() && !isLoading) {
                    101.dp // 빈 상태일 때 최소 높이
                } else {
                    // 내역이 있을 때: 각 아이템 21dp + 간격 21dp = 42dp per item
                    // 최대 350dp, 최소 150dp
                    val calculatedHeight = (histories.size * 42 + 100).dp
                    minOf(calculatedHeight, 350.dp).coerceAtLeast(150.dp)
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dynamicHeight)
                        .padding(horizontal = 17.dp)
                ) {
                    if (histories.isEmpty() && !isLoading) {
                        // 포인트 내역이 없을 때: 스크롤 없이 단순 표시
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(65.dp)
                                .padding(horizontal = 26.dp)
                                .background(
                                    color = Color(0xFFF5F5F5),
                                    shape = RoundedCornerShape(10.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "현재 보유 중인 포인트가 없습니다.",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                fontFamily = Pretendard,
                                color = Color.Black
                            )
                        }
                    } else {
                        // 포인트 내역이 있을 때: 스크롤 가능
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(histories) { history ->
                                ApiPointHistoryItem(history = history)
                                Spacer(modifier = Modifier.height(21.dp))
                            }

                            // 하단 여백
                            item {
                                Spacer(modifier = Modifier.height(40.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * API 데이터를 사용하는 포인트 내역 아이템 컴포넌트
 */
@Composable
fun ApiPointHistoryItem(
    history: PointHistoryItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(21.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 날짜를 "YYYY.MM.DD"에서 "MM.DD" 형식으로 변환
            val displayDate = if (history.date.length >= 10) {
                history.date.substring(5) // "MM.DD" 부분만 추출
            } else {
                history.date
            }

            Text(
                text = displayDate,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = Pretendard,
                color = Color(0xFF959595),
                modifier = Modifier.width(45.dp)
            )

            Spacer(modifier = Modifier.width(17.dp))

            Text(
                text = history.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = Pretendard,
                color = Color.Black,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = if (history.type == "EARNED") {
                "+${String.format("%,d", history.point)}P"
            } else {
                "-${String.format("%,d", history.point)}P"
            },
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = Pretendard,
            color = if (history.type == "EARNED") Color(0xFF145F44) else Color(0xFFEF3629)
        )
    }
}
