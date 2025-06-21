package com.luckydut97.feature_myinfo.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.luckydut97.tennispark.core.ui.components.navigation.TopBar
import com.luckydut97.feature_myinfo.ui.components.SettingMenuItem
import com.luckydut97.feature_myinfo.ui.components.SettingDivider

/**
 * 설정 화면
 */
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit = {},
    onNoticeClick: () -> Unit = {},
    onAppSettingsClick: () -> Unit = {},
    onFaqClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onVersionInfoClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onWithdrawalClick: () -> Unit = {}
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBar(
                title = "설정",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(vertical = 15.dp)
        ) {
            SettingMenuItem(
                title = "공지사항",
                onClick = onNoticeClick
            )

            SettingMenuItem(
                title = "앱 설정",
                onClick = onAppSettingsClick
            )

            SettingMenuItem(
                title = "FAQ",
                onClick = onFaqClick
            )
            SettingDivider()

            SettingMenuItem(
                title = "이용약관",
                onClick = onTermsClick
            )

            SettingMenuItem(
                title = "버전 정보",
                onClick = onVersionInfoClick
            )
            SettingDivider()

            SettingMenuItem(
                title = "로그아웃",
                onClick = onLogoutClick
            )

            SettingMenuItem(
                title = "회원 탈퇴",
                onClick = onWithdrawalClick
            )
        }
    }
}
