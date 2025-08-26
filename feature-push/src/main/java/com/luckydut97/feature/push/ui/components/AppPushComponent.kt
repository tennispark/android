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
import com.luckydut97.tennispark.core.domain.model.PushNotification
import com.luckydut97.tennispark.core.domain.model.NotificationType

/**
 * 푸시 알림 아이템 컴포넌트
 *
 * @param notification 알림 데이터 (Core 도메인 모델 사용)
 * @param onItemClick 아이템 클릭 콜백 (사용하지 않음)
 * @param onMoreClick 더보기 클릭 콜백
 */
@Composable
fun PushNotificationItem(
    notification: PushNotification,
    onItemClick: (String) -> Unit = {},
    onMoreClick: (String) -> Unit = {}
) {
    // 서버 알림 타입에 따른 아이콘 매핑
    val iconRes = when (notification.type) {
        NotificationType.MATCHING_GUIDE -> R.drawable.ic_push_match      // 매칭 안내
        NotificationType.ACTIVITY_GUIDE -> R.drawable.ic_push_approve    // 활동 안내 (승인/거부)
        NotificationType.ANNOUNCEMENT -> R.drawable.ic_push_recruit      // 공지/모집
        NotificationType.COMMUNITY -> R.drawable.ic_push_match          // 커뮤니티 (임시)
        NotificationType.ETC -> R.drawable.ic_push_match               // 기타 (임시)
    }

    // 더보기 확장 상태에 따른 높이 결정
    val containerHeight = when {
        notification.isExpanded -> null // 확장시 높이 제한 없음
        else -> 140.dp // 기본 높이 (2줄 + 더보기 버튼 공간)
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
                // 신규 알림인 경우에만 연한 초록색 배경, 나머지는 흰색
                color = if (notification.isNew) Color(0xFFF2FAF4) else Color.White,
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
                    .offset(y = 4.dp) // 아이콘을 4dp만큼 아래로 이동
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
                        text = notification.title, // Core 모델의 title 사용
                        fontSize = 13.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF939393),
                        letterSpacing = (-0.5).sp,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = notification.relativeTimeText, // Core 모델의 상대시간 사용
                        fontSize = 13.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF939393),
                        letterSpacing = (-0.5).sp
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                // 두 번째 영역: 실제 내용 (접힌 상태: 2줄까지, 확장 상태: 전체)
                Text(
                    text = notification.content,
                    fontSize = 18.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF6F6F6F),
                    letterSpacing = (-0.5).sp,
                    maxLines = if (notification.isExpanded) Int.MAX_VALUE else 2,
                    overflow = if (notification.isExpanded) TextOverflow.Visible else TextOverflow.Ellipsis,
                    lineHeight = 26.sp
                )

                // 모든 알림에 더보기 버튼 표시
                Spacer(modifier = Modifier.height(5.dp))

                PressableComponent(
                    onClick = {
                        onMoreClick(notification.id)
                    }
                ) {
                    Text(
                        text = if (notification.isExpanded) "접기" else "더보기",
                        fontSize = 13.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF359170),
                        letterSpacing = (-0.5).sp
                    )
                }
            }
        }
    }
}