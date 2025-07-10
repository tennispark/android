package com.luckydut97.feature_myinfo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luckydut97.tennispark.core.ui.components.navigation.TopBar
import com.luckydut97.tennispark.core.ui.theme.Pretendard
import com.luckydut97.feature_myinfo.viewmodel.AppSettingsViewModel
import com.luckydut97.feature_myinfo.ui.components.CustomSwitch

@Composable
fun AppSettingsScreen(
    onBackClick: () -> Unit = {}
) {
    val viewModel: AppSettingsViewModel = viewModel()
    val adPushEnabled by viewModel.adPushEnabled.collectAsState()
    val infoPushEnabled by viewModel.infoPushEnabled.collectAsState()

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBar(title = "앱 설정", onBackClick = onBackClick)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 24.dp, start = 18.dp, end = 18.dp),
            verticalArrangement = Arrangement.Top,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    /*text = "광고성 푸시 알림 수신 설정",*/
                    text = "푸시 알림 수신 설정",
                    fontSize = 16.sp,
                    fontFamily = Pretendard,
                    color = Color(0xFF222222)
                )
                CustomSwitch(
                    checked = adPushEnabled,
                    onCheckedChange = viewModel::setAdPushEnabled
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            /*Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "정보성 푸시 알림 수신 설정",
                    fontSize = 16.sp,
                    fontFamily = Pretendard,
                    color = Color(0xFF222222)
                )
                CustomSwitch(
                    checked = infoPushEnabled,
                    onCheckedChange = viewModel::setInfoPushEnabled
                )
            }*/
        }
    }
}
