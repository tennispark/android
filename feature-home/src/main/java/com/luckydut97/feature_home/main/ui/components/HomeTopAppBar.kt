package com.luckydut97.feature_home.main.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.tennispark.feature.home.R
import com.luckydut97.tennispark.core.ui.components.animation.PressableComponent
import com.luckydut97.tennispark.core.ui.theme.Pretendard
import com.luckydut97.tennispark.core.utils.NotificationBadgeManager

@Composable
fun HomeTopAppBar(
    onNotificationClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    val context = LocalContext.current
    val badgeManager = remember { NotificationBadgeManager.getInstance(context) }

    // 🔥 StateFlow 기반 실시간 배지 감지
    val notificationCount by badgeManager.badgeCount.collectAsState(initial = 0)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(Color(0xFFF4F6F8))
            .padding(horizontal = 17.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // 로고
        Image(
            painter = painterResource(id = R.drawable.ic_typo_logo),
            contentDescription = "Tennis Park Logo",
            modifier = Modifier
                .width(128.dp)
                .height(30.42.dp)
        )

        // 알림 및 검색 버튼
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 알림 아이콘과 배지를 감싸는 Box
            PressableComponent(
                onClick = onNotificationClick,
                modifier = Modifier.size(27.dp)
            ) {
                Box(
                    modifier = Modifier.size(27.dp)
                ) {
                    // 알림 아이콘
                    Image(
                        painter = painterResource(id = R.drawable.ic_alarm),
                        contentDescription = "Notifications",
                        modifier = Modifier.size(27.dp)
                    )

                    // 알림 배지 (개수가 0보다 클 때만 표시)
                    if (notificationCount > 0) {
                        val badgeText = when {
                            notificationCount <= 99 -> notificationCount.toString()
                            else -> "99+"
                        }
                        val badgeWidth = when {
                            badgeText.length == 1 -> 8.dp // 한 자리: 원형
                            badgeText.length == 2 -> 12.dp // 두 자리
                            else -> 16.dp // 세 자리 이상
                        }

                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = (-3).dp, y = 3.dp) // 종 안쪽 오른쪽 위에 위치
                                .size(width = badgeWidth, height = 8.dp)
                                .background(
                                    color = Color(0xFFFA8451),
                                    shape = RoundedCornerShape(4.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = badgeText,
                                fontSize = 6.sp,
                                fontFamily = Pretendard,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                letterSpacing = (-0.5).sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 6.sp, // 폰트 크기와 동일하게 설정
                                maxLines = 1,
                                modifier = Modifier.offset(x = (-0.5).dp, y = (-0.5).dp)
                            )
                        }
                    }
                }
            }

            /*Spacer(modifier = Modifier.width(9.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "Search",
                modifier = Modifier
                    .size(27.dp)
                    .clickable(onClick = onSearchClick)
            )*/
        }
    }
}
