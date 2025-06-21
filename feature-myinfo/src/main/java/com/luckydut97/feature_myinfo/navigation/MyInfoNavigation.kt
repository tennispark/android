package com.luckydut97.feature_myinfo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.luckydut97.feature_myinfo.ui.MyInfoScreen
import com.luckydut97.feature_myinfo.ui.SettingsScreen

/**
 * feature-myinfo 모듈 내부 네비게이션
 * MyInfoScreen -> SettingsScreen -> 공지사항/FAQ 등
 */
@Composable
fun MyInfoNavigation(
    onBackClick: () -> Unit = {},
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "myinfo"
    ) {
        // 내 정보 메인 화면
        composable("myinfo") {
            MyInfoScreen(
                onBackClick = onBackClick,
                onSettingsClick = {
                    navController.navigate("settings")
                }
            )
        }

        // 설정 화면
        composable("settings") {
            SettingsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onNoticeClick = {
                    navController.navigate("notice")
                },
                onAppSettingsClick = {
                    navController.navigate("app_settings")
                },
                onFaqClick = {
                    navController.navigate("faq")
                },
                onTermsClick = {
                    navController.navigate("terms")
                },
                onVersionInfoClick = {
                    navController.navigate("version_info")
                },
                onLogoutClick = {
                    navController.navigate("logout")
                },
                onWithdrawalClick = {
                    navController.navigate("withdrawal")
                }
            )
        }

        // 공지사항 화면 (TODO: 향후 구현)
        composable("notice") {
            // NoticeScreen()
            // 임시 화면
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // 앱 설정 화면 (TODO: 향후 구현)
        composable("app_settings") {
            // AppSettingsScreen()
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // FAQ 화면 (TODO: 향후 구현)
        composable("faq") {
            // FaqScreen()
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // 이용약관 화면 (TODO: 향후 구현)
        composable("terms") {
            // TermsScreen()
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // 버전 정보 화면 (TODO: 향후 구현)
        composable("version_info") {
            // VersionInfoScreen()
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // 로그아웃 화면 (TODO: 향후 구현)
        composable("logout") {
            // LogoutScreen()
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // 회원 탈퇴 화면 (TODO: 향후 구현)
        composable("withdrawal") {
            // WithdrawalScreen()
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
