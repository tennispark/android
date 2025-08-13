package com.luckydut97.feature_myinfo.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.tennispark.core.R
import com.luckydut97.tennispark.core.ui.components.navigation.TopBar
import com.luckydut97.tennispark.core.ui.theme.Pretendard

/**
 * 더미 포인트 내역 데이터
 */
data class DummyPointHistory(
    val date: String,
    val title: String,
    val point: Int,
    val type: String // "EARNED" | "USED"
)

/**
 * 날짜별 그룹핑된 포인트 내역
 */
data class GroupedPointHistory(
    val date: String,
    val histories: List<DummyPointHistory>
)

/**
 * 나의 포인트 내역 화면
 */
@Composable
fun PointHistoryScreen(
    onBackClick: () -> Unit = {}
) {
    // 더미 데이터
    val dummyHistories = listOf(
        DummyPointHistory("04월 25일", "핸드그립 구매", -7500, "USED"),
        DummyPointHistory("04월 25일", "출석체크", 500, "EARNED"),
        DummyPointHistory("04월 24일", "핸드그립 구매", -7500, "USED"),
        DummyPointHistory("04월 24일", "출석체크", 500, "EARNED"),
        DummyPointHistory("04월 24일", "출석체크", 500, "EARNED"),
        DummyPointHistory("04월 24일", "핸드그립 구매", -7500, "USED"),
        DummyPointHistory("04월 24일", "출석체크", 500, "EARNED"),
        DummyPointHistory("04월 23일", "핸드그립 구매", -7500, "USED"),
        DummyPointHistory("04월 23일", "출석체크", 500, "EARNED")
    )

    // 날짜별 그룹핑
    val groupedHistories = dummyHistories.groupBy { it.date }.map { (date, histories) ->
        GroupedPointHistory(date, histories)
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBar(
                title = "나의 포인트 내역",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 상단 포인트 박스 영역
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 18.dp)
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    // 전체 영역 높이 133dp
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(133.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // 내부 박스 높이 103dp, 그라데이션 배경 (방향 반대)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(103.dp)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFF095F40), // 0%
                                            Color(0xFF1A835D)  // 100%
                                        ),
                                        start = androidx.compose.ui.geometry.Offset(
                                            Float.POSITIVE_INFINITY,
                                            Float.POSITIVE_INFINITY
                                        ),
                                        end = androidx.compose.ui.geometry.Offset(0f, 0f)
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(20.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center
                            ) {
                                // 포인트 아이콘 + "내 포인트" Row
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_coin_black),
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = Color.White
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Text(
                                        text = "내 포인트",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Normal,
                                        fontFamily = Pretendard,
                                        color = Color.White
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                // 포인트 금액
                                Text(
                                    text = "24,000P",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = Pretendard,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }

            // 적립/사용 내역 타이틀
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 18.dp)
                ) {
                    Text(
                        text = "적립/사용 내역",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Pretendard,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            // 날짜별 그룹핑된 포인트 내역
            items(groupedHistories.size) { index ->
                val group = groupedHistories[index]

                // 날짜 헤더
                Text(
                    text = group.date,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = Pretendard,
                    color = Color(0xFF939393),
                    modifier = Modifier.padding(horizontal = 18.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 해당 날짜의 포인트 내역들
                group.histories.forEach { history ->
                    DummyPointHistoryItem(
                        history = history,
                        showDate = false, // 날짜는 헤더에서 표시하므로 아이템에서는 숨김
                        modifier = Modifier.padding(horizontal = 18.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // 마지막 그룹이 아니면 구분선 추가
                if (index < groupedHistories.size - 1) {
                    Spacer(modifier = Modifier.height(4.dp)) // 위쪽 24dp 여백 (20dp + 4dp = 24dp)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp)
                            .height(1.dp)
                            .background(Color(0xFFF0F0F0))
                    )

                    Spacer(modifier = Modifier.height(24.dp)) // 아래쪽 24dp 여백
                }
            }

            // 하단 여백
            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

/**
 * 더미 포인트 내역 아이템 컴포넌트
 */
@Composable
fun DummyPointHistoryItem(
    history: DummyPointHistory,
    showDate: Boolean = true,
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
            // 날짜는 showDate가 true일 때만 표시 (기존 마이페이지에서 사용하는 경우)
            if (showDate) {
                Text(
                    text = history.date,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = Pretendard,
                    color = Color(0xFF959595),
                    modifier = Modifier.width(65.dp)
                )

                Spacer(modifier = Modifier.width(17.dp))
            }

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
                "${String.format("%,d", history.point)}P"
            },
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = Pretendard,
            color = if (history.type == "EARNED") Color(0xFF145F44) else Color(0xFFEF3629)
        )
    }
}