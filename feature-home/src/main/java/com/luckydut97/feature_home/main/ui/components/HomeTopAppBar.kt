package com.luckydut97.feature_home.main.ui.components

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.luckydut97.tennispark.core.domain.usecase.GetUnreadCountUseCase
import com.luckydut97.tennispark.core.data.repository.NotificationRepositoryImpl
import com.luckydut97.tennispark.core.data.network.NetworkModule
import com.luckydut97.tennispark.core.fcm.MyFirebaseMessagingService
import kotlinx.coroutines.launch

@Composable
fun HomeTopAppBar(
    onNotificationClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // 서버 기반 미읽은 알림 수 조회
    val getUnreadCountUseCase = remember {
        val repository = NotificationRepositoryImpl(NetworkModule.apiService)
        GetUnreadCountUseCase(repository)
    }

    var notificationCount by remember { mutableStateOf(0) }

    // 서버에서 배지 수를 조회하는 함수
    val refreshBadgeCount = {
        coroutineScope.launch {
            try {
                val count = getUnreadCountUseCase()
                notificationCount = count
                // 🔥 다른 화면에서 사용할 수 있도록 배지 수 저장
                com.luckydut97.tennispark.core.utils.BadgeCountManager.setBadgeCount(count)
            } catch (e: Exception) {
                notificationCount = 0 // 실패 시 0 표시
                com.luckydut97.tennispark.core.utils.BadgeCountManager.setBadgeCount(0)
            }
        }
    }

    // 화면 진입 시마다 서버에서 배지 수 조회
    LaunchedEffect(Unit) {
        refreshBadgeCount()
    }

    // FCM 브로드캐스트 수신 설정
    DisposableEffect(context) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == MyFirebaseMessagingService.ACTION_NOTIFICATION_RECEIVED) {
                    // FCM 알림 수신 시 배지 수 갱신
                    refreshBadgeCount()
                }
            }
        }

        val intentFilter = IntentFilter(MyFirebaseMessagingService.ACTION_NOTIFICATION_RECEIVED)
        context.registerReceiver(receiver, intentFilter)

        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

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
                            badgeText.length == 1 -> 11.8.dp // 한 자리: 11.8dp
                            badgeText.length == 2 -> 17.dp   // 두 자리: 17dp
                            else -> 21.dp                    // 세 자리 이상: 21dp
                        }

                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = (-1).dp, y = 5.dp) // 배지를 아래로 더 내림
                                .size(width = badgeWidth, height = 10.4.dp)
                                .background(
                                    color = Color(0xFFFA8451),
                                    shape = RoundedCornerShape(6.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = badgeText,
                                fontSize = 7.8.sp,
                                fontFamily = Pretendard,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                letterSpacing = (-0.5).sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 7.8.sp, // 폰트 크기와 동일하게 설정
                                maxLines = 1,
                                modifier = Modifier.offset(x = (-0.2).dp, y = (-0.2).dp)
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
