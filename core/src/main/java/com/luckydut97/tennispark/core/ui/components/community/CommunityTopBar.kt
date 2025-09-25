package com.luckydut97.tennispark.core.ui.components.community

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.luckydut97.tennispark.core.R
import com.luckydut97.tennispark.core.data.network.NetworkModule
import com.luckydut97.tennispark.core.data.repository.NotificationRepositoryImpl
import com.luckydut97.tennispark.core.domain.usecase.GetUnreadCountUseCase
import com.luckydut97.tennispark.core.fcm.MyFirebaseMessagingService
import com.luckydut97.tennispark.core.ui.theme.Pretendard
import com.luckydut97.tennispark.core.utils.BadgeCountManager
import kotlinx.coroutines.launch

/**
 * 커뮤니티 상단바 컴포넌트
 */
@Composable
fun CommunityTopBar(
    onSearchClick: () -> Unit,
    onAlarmClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val getUnreadCountUseCase = remember {
        val repository = NotificationRepositoryImpl(NetworkModule.apiService)
        GetUnreadCountUseCase(repository)
    }

    var notificationCount by remember { mutableStateOf(BadgeCountManager.getBadgeCount()) }

    val refreshBadgeCount = remember {
        {
            coroutineScope.launch {
                try {
                    val count = getUnreadCountUseCase()
                    notificationCount = count
                    BadgeCountManager.setBadgeCount(count)
                } catch (e: Exception) {
                    notificationCount = 0
                    BadgeCountManager.setBadgeCount(0)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        refreshBadgeCount()
    }

    DisposableEffect(context) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == MyFirebaseMessagingService.ACTION_NOTIFICATION_RECEIVED) {
                    refreshBadgeCount()
                }
            }
        }

        val intentFilter = IntentFilter(MyFirebaseMessagingService.ACTION_NOTIFICATION_RECEIVED)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(receiver, intentFilter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            context.registerReceiver(receiver, intentFilter)
        }

        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 커뮤니티 텍스트
        Text(
            text = "커뮤니티",
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            letterSpacing = (-0.5).sp
        )

        // 검색 및 알림 아이콘
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onSearchClick,
                modifier = Modifier.size(27.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "검색",
                    tint = Color.Black,
                    modifier = Modifier.size(27.dp)
                )
            }

            IconButton(
                onClick = onAlarmClick,
                modifier = Modifier.size(27.dp)
            ) {
                Box(
                    modifier = Modifier.size(27.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_alarm),
                        contentDescription = "알림",
                        tint = Color.Black,
                        modifier = Modifier.size(27.dp)
                    )

                    if (notificationCount > 0) {
                        val badgeText = when {
                            notificationCount <= 99 -> notificationCount.toString()
                            else -> "99+"
                        }
                        val badgeWidth = when (badgeText.length) {
                            1 -> 11.8.dp
                            2 -> 17.dp
                            else -> 21.dp
                        }

                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = (-1).dp, y = 5.dp)
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
                                lineHeight = 7.8.sp,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}
