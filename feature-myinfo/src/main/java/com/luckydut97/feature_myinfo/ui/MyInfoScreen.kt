package com.luckydut97.feature_myinfo.ui

import android.util.Log
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luckydut97.tennispark.core.R
import com.luckydut97.tennispark.core.ui.components.navigation.TopBar
import com.luckydut97.tennispark.core.ui.theme.Pretendard
import com.luckydut97.tennispark.core.data.model.PointHistoryItem
import com.luckydut97.feature_myinfo.viewmodel.MyInfoViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 내 정보 화면
 */
@Composable
fun MyInfoScreen(
    onBackClick: () -> Unit = {},
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
        Log.d("🔍 디버깅: MyInfoScreen", "화면 진입 - 포인트 데이터 자동 새로고침 시작")
        viewModel.refreshAllData()
    }

    // 광고 배너 관련
    val adBannerPages = 3
    val pagerState = rememberPagerState(pageCount = { adBannerPages })
    
    // 5초마다 자동 스크롤
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            if (pagerState.currentPage < adBannerPages - 1) {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            } else {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(0)
                }
            }
        }
    }
    
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBar(
                title = "회원 정보",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 17.dp)
            ) {
                item {
                    // 회원명
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "$userName 회원님",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Pretendard,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 포인트 및 경기 기록 박스
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .background(
                                color = Color(0xFFF2FAF4),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 24.dp)
                            .clickable { refreshPoints() }, // 포인트 박스 클릭 시 새로고침
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

                    Spacer(modifier = Modifier.height(16.dp))

                    // 광고 배너 (스와이프 가능, 자동 스크롤)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(67.dp)
                            .background(
                                color = Color(0xFFF5F5F5),
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        // 자동 스크롤 및 스와이프 기능 추가
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(67.dp)
                        ) { page ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        color = Color(0xFFF5F5F5),
                                        shape = RoundedCornerShape(8.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "광고 배너 ${page + 1}",
                                    fontSize = 14.sp,
                                    fontFamily = Pretendard,
                                    color = Color.Gray
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
                            repeat(adBannerPages) { index ->
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(
                                            color = if (pagerState.currentPage == index) Color.Black else Color.Gray,
                                            shape = CircleShape
                                        )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(36.dp))

                    // 구분선
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .background(Color(0xFFF5F5F5))
                    )

                    Spacer(modifier = Modifier.height(36.dp))

                    // 나의 포인트 내역 제목
                    Text(
                        text = "나의 포인트 내역",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Pretendard,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(36.dp))
                }

                // 포인트 내역 리스트 (실제 API 데이터 사용)
                if (histories.isEmpty() && !isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "포인트 내역이 없습니다.",
                                fontSize = 16.sp,
                                fontFamily = Pretendard,
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    items(histories) { history ->
                        ApiPointHistoryItem(history = history)
                        Spacer(modifier = Modifier.height(21.dp))
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }

            // 로딩 인디케이터 (주석 처리 - 사용자가 API 호출을 알 필요 없음)
            /*
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFF145F44)
                    )
                }
            }
            */

            // 에러 메시지 표시 (필요시)
            errorMessage?.let { message ->
                LaunchedEffect(message) {
                    // 에러 처리 (예: Toast, SnackBar 등)
                    // 임시로 로그만 출력
                    Log.e("MyInfoScreen", "Error: $message")
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
                color = Color.Black
            )
        }
        
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
