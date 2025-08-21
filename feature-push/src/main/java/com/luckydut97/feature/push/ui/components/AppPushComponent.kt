package com.luckydut97.feature.push.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.tennispark.core.ui.components.animation.PressableComponent
import com.luckydut97.tennispark.core.ui.theme.Pretendard
import com.luckydut97.feature.push.R

/**
 * 푸시 알림 데이터 클래스
 */
data class PushNotification(
    val id: String,
    val type: PushNotificationType,
    val title: String,
    val content: String,
    val timeText: String,
    val isExpanded: Boolean = false // 더보기 확장 상태 추가
)

/**
 * 푸시 알림 타입
 */
enum class PushNotificationType {
    MATCH,      // 매칭 안내 - ic_push_match
    APPROVE,    // 승인 - ic_push_approve  
    DENY,       // 거부 - ic_push_deny
    RECRUIT     // 모집 - ic_push_recruit
}

/**
 * 푸시 알림 아이템 컴포넌트
 *
 * @param notification 알림 데이터
 * @param isSelected 선택 상태 (외부에서 관리)
 * @param onItemClick 아이템 클릭 콜백
 * @param onMoreClick 더보기 클릭 콜백
 */
@Composable
fun PushNotificationItem(
    notification: PushNotification,
    isSelected: Boolean = false, // 외부에서 선택 상태 관리
    onItemClick: (String) -> Unit = {},
    onMoreClick: (String) -> Unit = {}
) {
    val iconRes = when (notification.type) {
        PushNotificationType.MATCH -> R.drawable.ic_push_match
        PushNotificationType.APPROVE -> R.drawable.ic_push_approve
        PushNotificationType.DENY -> R.drawable.ic_push_deny
        PushNotificationType.RECRUIT -> R.drawable.ic_push_recruit
    }

    // 내용이 2줄 이상인지 확인
    val isMultiLine = notification.content.length > 30
    // 더보기 확장 상태에 따른 높이 결정
    val containerHeight = when {
        notification.isExpanded -> null // 확장시 높이 제한 없음
        isMultiLine -> 140.dp // 2줄 + 더보기 버튼
        else -> 114.dp // 1줄, 더보기 버튼
    }

    // 텍스트를 2줄까지와 나머지로 분리
    val displayText = notification.content
    val previewText = if (isMultiLine && !notification.isExpanded) {
        // 2줄에 해당하는 텍스트만 표시 (대략적인 계산)
        if (displayText.length > 60) {
            displayText.take(60) + "..."
        } else {
            displayText
        }
    } else {
        displayText
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .let { modifier ->
                if (containerHeight != null) {
                    modifier.height(containerHeight)
                } else {
                    modifier
                }
            }
            .background(
                color = if (isSelected) Color(0xFFF2FAF4) else Color.White,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onItemClick(notification.id) }
            .padding(horizontal = 18.dp)
            .padding(vertical = 16.dp), // 고정 세로 패딩
        contentAlignment = Alignment.TopStart // 상단 시작점 고정
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top // 아이콘이 상단에 붙도록
        ) {
            // 아이콘 (상단 정렬)
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = notification.title,
                modifier = Modifier
                    .size(18.dp)
                    .offset(y = 4.dp) // 아이콘을 2dp만큼 아래로 이동
            )

            Spacer(modifier = Modifier.width(8.dp))

            // 내용 영역 (타이틀, 실제내용, 더보기 버튼)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // 첫 번째 줄: 알림 타이틀 + 시간 (고정)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        fontSize = 13.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF939393),
                        letterSpacing = (-0.5).sp,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = notification.timeText,
                        fontSize = 13.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF939393),
                        letterSpacing = (-0.5).sp
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                // 두 번째 영역: 실제 내용 (고정 + 확장)
                if (isMultiLine && !notification.isExpanded) {
                    // 축약된 내용 (2줄) - 고정
                    Text(
                        text = previewText,
                        fontSize = 18.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF6F6F6F),
                        letterSpacing = (-0.5).sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 26.sp
                    )
                } else {
                    // 전체 내용 또는 1줄 내용
                    Text(
                        text = displayText,
                        fontSize = 18.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF6F6F6F),
                        letterSpacing = (-0.5).sp,
                        maxLines = if (notification.isExpanded) Int.MAX_VALUE else 1,
                        overflow = if (notification.isExpanded) TextOverflow.Visible else TextOverflow.Ellipsis,
                        lineHeight = 26.sp
                    )
                }

                // 더보기 버튼 (모든 아이템에 표시) - 고정 위치
                Spacer(modifier = Modifier.height(5.dp))

                PressableComponent(
                    onClick = {
                        // 2줄 이상일 때만 실제 동작
                        if (isMultiLine) {
                            onMoreClick(notification.id)
                        }
                    }
                ) {
                    Text(
                        text = if (notification.isExpanded) "접기" else "더보기",
                        fontSize = 13.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF359170),
                        letterSpacing = (-0.5).sp,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}