package com.luckydut97.feature_myinfo.ui

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.feature_myinfo.viewmodel.ActivityHistoryViewModel
import com.luckydut97.feature_myinfo.ui.components.ActivityApplicationItemComponent
import com.luckydut97.feature_myinfo.ui.components.GroupDividerComponent
import com.luckydut97.tennispark.core.R as CoreR
import com.luckydut97.tennispark.core.ui.theme.Pretendard
import com.luckydut97.tennispark.core.ui.components.navigation.TopBar

/**
 * 나의 활동신청 내역 화면
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityHistoryScreen(
    viewModel: ActivityHistoryViewModel,
    onNavigateBack: () -> Unit
) {
    val groupedApplications by viewModel.groupedApplications.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    BackHandler {
        onNavigateBack()
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "나의 활동신청 내역",
                onBackClick = onNavigateBack
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    // 로딩 상태
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "활동 신청 내역을 불러오는 중...",
                            fontSize = 16.sp,
                            fontFamily = Pretendard,
                            color = Color(0xFF8B9096),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                error != null -> {
                    // 에러 상태
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = error ?: "오류가 발생했습니다.",
                                fontSize = 16.sp,
                                fontFamily = Pretendard,
                                color = Color(0xFFEF3629),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "다시 시도해주세요.",
                                fontSize = 14.sp,
                                fontFamily = Pretendard,
                                color = Color(0xFF8B9096),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                groupedApplications.isEmpty() -> {
                    // 빈 상태
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "신청하신 활동 내역이 없습니다.",
                                fontSize = 16.sp,
                                fontFamily = Pretendard,
                                color = Color(0xFF8B9096),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                else -> {
                    // 데이터 표시
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // 날짜별 그룹으로 렌더링
                        groupedApplications.entries.forEachIndexed { groupIndex, (date, applications) ->
                            // 그룹 구분선 (첫 번째 그룹이 아닌 경우)
                            if (groupIndex > 0) {
                                item(key = "group_divider_$groupIndex") {
                                    GroupDividerComponent()
                                }
                            }

                            // 날짜 텍스트
                            item(key = "date_text_$date") {
                                Text(
                                    text = "${date.monthValue.toString().padStart(2, '0')}월 ${
                                        date.dayOfMonth.toString().padStart(2, '0')
                                    }일",
                                    fontSize = 13.sp,
                                    fontFamily = Pretendard,
                                    fontWeight = FontWeight.Normal,
                                    color = Color(0xFF939393),
                                    modifier = Modifier.padding(
                                        horizontal = 18.dp,
                                        vertical = 17.dp
                                    )
                                )
                            }

                            // 해당 날짜의 활동 신청 내역들
                            items(
                                items = applications,
                                key = { application -> "application_${application.id}" }
                            ) { application ->
                                ActivityApplicationItemComponent(
                                    application = application,
                                    modifier = Modifier.padding(horizontal = 18.dp)
                                )

                                // 17dp 여백 (마지막 아이템이 아닌 경우)
                                if (application != applications.last()) {
                                    Spacer(modifier = Modifier.height(17.dp))
                                }
                            }
                        }

                        // 하단 여백
                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
    }
}