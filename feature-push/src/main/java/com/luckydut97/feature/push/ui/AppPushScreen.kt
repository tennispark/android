package com.luckydut97.feature.push.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.luckydut97.tennispark.core.ui.components.navigation.TopBar
import com.luckydut97.feature.push.ui.components.PushNotification
import com.luckydut97.feature.push.ui.components.PushNotificationItem
import com.luckydut97.feature.push.ui.components.PushNotificationType

/**
 * 푸시 알림 화면
 */
@Composable
fun AppPushScreen(
    onBackClick: () -> Unit = {}
) {
    // 선택된 아이템 ID 상태 관리
    var selectedItemId by remember { mutableStateOf<String?>(null) }

    // 확장된 아이템들 ID 세트 상태 관리
    var expandedItemIds by remember { mutableStateOf(setOf<String>()) }

    // 샘플 데이터 - 스크롤 테스트를 위해 더 많은 데이터 추가
    val sampleNotifications = listOf(
        PushNotification(
            id = "1",
            type = PushNotificationType.MATCH,
            title = "매칭 안내",
            content = "7월 20일 양재 테니스장 A코트 김00, 이00님과 함께 경기 매칭되었습니다. 활동 10분 전에 꼭 참석해주세요. 2줄이상 활동 10분 전에 꼭 참석해주세요. 2줄이상 활동 10분 전에 꼭 참석해주세요. 2줄이상",
            timeText = "50분 전"
        ),
        PushNotification(
            id = "2",
            type = PushNotificationType.RECRUIT,
            title = "공지",
            content = "7월 3주차 게스트 추가 모집합니다.",
            timeText = "24시간 전"
        ),
        PushNotification(
            id = "3",
            type = PushNotificationType.APPROVE,
            title = "활동 안내",
            content = "활동 신청이 승인되었습니다.",
            timeText = "07월 20일"
        ),
        PushNotification(
            id = "4",
            type = PushNotificationType.DENY,
            title = "활동 안내",
            content = "활동 신청이 거절되었습니다.",
            timeText = "07월 13일"
        ),
        PushNotification(
            id = "5",
            type = PushNotificationType.APPROVE,
            title = "활동 안내",
            content = "활동 신청이 승인되었습니다.",
            timeText = "07월 12일"
        ),
        PushNotification(
            id = "6",
            type = PushNotificationType.MATCH,
            title = "매칭 안내",
            content = "7월 18일 강남 테니스장 B코트에서 매칭이 진행됩니다. 준비물을 미리 챙겨주세요.",
            timeText = "3시간 전"
        ),
        PushNotification(
            id = "7",
            type = PushNotificationType.RECRUIT,
            title = "공지",
            content = "8월 1주차 정기 모임 참가자를 모집합니다.",
            timeText = "5시간 전"
        ),
        PushNotification(
            id = "8",
            type = PushNotificationType.APPROVE,
            title = "활동 안내",
            content = "테니스 레슨 신청이 승인되었습니다.",
            timeText = "07월 10일"
        ),
        PushNotification(
            id = "9",
            type = PushNotificationType.MATCH,
            title = "매칭 안내",
            content = "내일 오전 9시 송파 테니스장에서 복식 경기가 예정되어 있습니다. 시간에 맞춰 참석해주세요.",
            timeText = "12시간 전"
        ),
        PushNotification(
            id = "10",
            type = PushNotificationType.DENY,
            title = "활동 안내",
            content = "레슨 일정 변경 요청이 거절되었습니다.",
            timeText = "07월 08일"
        ),
        PushNotification(
            id = "11",
            type = PushNotificationType.RECRUIT,
            title = "공지",
            content = "여름 테니스 캠프 참가자 모집 중입니다.",
            timeText = "1일 전"
        ),
        PushNotification(
            id = "12",
            type = PushNotificationType.APPROVE,
            title = "활동 안내",
            content = "토너먼트 참가 신청이 승인되었습니다.",
            timeText = "07월 05일"
        )
    )

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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 0.dp)
            ) {
                items(sampleNotifications) { notification ->
                    val currentNotification = notification.copy(
                        isExpanded = expandedItemIds.contains(notification.id)
                    )

                    PushNotificationItem(
                        notification = currentNotification,
                        isSelected = selectedItemId == notification.id,
                        onItemClick = { id ->
                            selectedItemId = if (selectedItemId == id) null else id
                        },
                        onMoreClick = { id ->
                            expandedItemIds = if (expandedItemIds.contains(id)) {
                                expandedItemIds - id // 접기
                            } else {
                                expandedItemIds + id // 확장
                            }
                        }
                    )

                    // 아이템 간 구분선 (선택사항)
                    Spacer(modifier = Modifier.height(1.dp))
                }

                // 하단 여백 추가
                item {
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}
