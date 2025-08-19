package com.luckydut97.feature.push.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.tennispark.core.ui.components.navigation.TopBar
import com.luckydut97.tennispark.core.ui.theme.Pretendard

/**
 * 푸시 알림 화면
 */
@Composable
fun AppPushScreen(
    onBackClick: () -> Unit = {}
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // 임시 콘텐츠
            Text(
                text = "알림 목록이 표시됩니다!",
                fontSize = 16.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF666666)
            )
        }
    }
}
