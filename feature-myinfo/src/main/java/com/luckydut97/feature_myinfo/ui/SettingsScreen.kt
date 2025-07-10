package com.luckydut97.feature_myinfo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.luckydut97.tennispark.core.ui.components.navigation.TopBar
import com.luckydut97.feature_myinfo.ui.components.SettingMenuItem
import com.luckydut97.feature_myinfo.ui.components.SettingDivider
import com.luckydut97.feature_myinfo.ui.components.WithdrawalDialog
import com.luckydut97.feature_myinfo.ui.components.LogoutDialog
import com.luckydut97.feature_myinfo.viewmodel.MyInfoViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luckydut97.tennispark.core.utils.launchUrl
import android.util.Log

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
    onLogoutComplete: () -> Unit = {},
    viewModel: MyInfoViewModel = viewModel() // 🔍 디버깅: 기본값 제공하되 외부에서 전달 가능
) {
    val context = LocalContext.current
    var showWithdrawalDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    Log.d("🔍 SettingsScreen", "🔍 디버깅: SettingsScreen 컴포즈됨 - viewModel: $viewModel")

    // 🔍 디버깅: SettingsScreen에서 중복 LaunchedEffect 제거
    // MyInfoNavigation에서 처리하므로 여기서는 제거
    // val isWithdrawn by viewModel.isWithdrawn.collectAsState()
    // val isLoggedOut by viewModel.isLoggedOut.collectAsState()

    // LaunchedEffect(isWithdrawn) {
    //     if (isWithdrawn) {
    //         viewModel.resetWithdrawState()
    //         onLogoutComplete()
    //     }
    // }
    // LaunchedEffect(isLoggedOut) {
    //     if (isLoggedOut) {
    //         viewModel.resetLogoutState()
    //         onLogoutComplete()
    //     }
    // }

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
                onClick = {
                    context.launchUrl("https://cafe.naver.com/f-e/cafes/30925469/menus/1?viewType=L")
                }
            )

            SettingMenuItem(
                title = "앱 설정",
                onClick = onAppSettingsClick
            )

            /*SettingMenuItem(
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
            )*/
            SettingDivider()

            SettingMenuItem(
                title = "로그아웃",
                onClick = { showLogoutDialog = true }
            )

            SettingMenuItem(
                title = "회원 탈퇴",
                onClick = { showWithdrawalDialog = true }
            )
        }
    }

    if (showLogoutDialog) {
        LogoutDialog(
            onDismiss = { showLogoutDialog = false },
            onConfirm = {
                showLogoutDialog = false
                Log.d("🔍 SettingsScreen", "🔍 디버깅: 로그아웃 확인 - viewModel.logout() 직접 호출")
                viewModel.logout()
            }
        )
    }
    if (showWithdrawalDialog) {
        WithdrawalDialog(
            onDismiss = { showWithdrawalDialog = false },
            onConfirm = {
                showWithdrawalDialog = false
                Log.d("🔍 SettingsScreen", "🔍 디버깅: 회원탈퇴 확인 - viewModel.withdraw() 직접 호출")
                viewModel.withdraw()
            }
        )
    }
}
