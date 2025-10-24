package com.luckydut97.feature.push.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luckydut97.tennispark.core.ui.components.navigation.TopBar
import com.luckydut97.feature.push.ui.components.PushNotificationItem
import com.luckydut97.feature.push.viewmodel.AppPushViewModel
import com.luckydut97.tennispark.core.ui.theme.Pretendard

/**
 * 푸시 알림 화면 (실제 API 연동)
 * 신규 알림은 연한 초록색 배경으로 표시
 */
@Composable
fun AppPushScreen(
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val viewModel: AppPushViewModel = viewModel()

    // ViewModel 상태 수집
    val notifications by viewModel.notifications.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val expandedNotificationIds by viewModel.expandedNotificationIds.collectAsState()

    // 화면 진입 시 초기화 및 서버 기반 읽음 처리
    LaunchedEffect(Unit) {
        viewModel.initializeWithContext(context)
        viewModel.markAllAsRead() // 🔥 모든 알림 읽음 처리 (서버 기반)
    }

    BackHandler {
        onBackClick()
    }

    // 최상위 레벨 화면이므로 직접 시스템바 처리 필요
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding() // MainActivity의 setDecorFitsSystemWindows(true) 때문에 필요
    ) {
        Scaffold(
            containerColor = Color.White,
            topBar = {
                TopBar(
                    title = "새로운 알림",
                    onBackClick = onBackClick
                )
            }
        ) { paddingValues ->
            when {
                isLoading -> {
                    // 로딩 상태
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "알림을 불러오는 중...",
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
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
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

                notifications.isEmpty() -> {
                    // 빈 상태
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "새로운 알림이 없습니다.",
                            fontSize = 16.sp,
                            fontFamily = Pretendard,
                            color = Color(0xFF8B9096),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                else -> {
                    // 알림 목록 표시
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(horizontal = 0.dp)
                    ) {
                        items(notifications, key = { it.id }) { notification ->
                            // 확장 상태를 ViewModel에서 가져와서 notification에 반영
                            val isExpanded = expandedNotificationIds.contains(notification.id)
                            val displayNotification = notification.copy(isExpanded = isExpanded)

                            PushNotificationItem(
                                notification = displayNotification,
                                onItemClick = { /* 클릭 이벤트 사용하지 않음 */ },
                                onMoreClick = { id ->
                                    viewModel.toggleNotificationExpansion(id)
                                }
                            )
                        }

                        // 하단 여백 추가
                        item {
                            Spacer(modifier = Modifier.height(40.dp))
                        }
                    }
                }
            }
        }
    }
}
