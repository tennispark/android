package com.luckydut97.tennispark

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.luckydut97.feature_home.main.ui.HomeScreen
import com.luckydut97.tennispark.feature_auth.navigation.AuthNavigation
import com.luckydut97.tennispark.core.ui.components.navigation.BottomNavigationBar
import com.luckydut97.tennispark.core.ui.components.navigation.BottomNavigationItem

/**
 * 앱 전체의 메인 네비게이션을 처리하는 컴포넌트
 */
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    isLoggedIn: Boolean = false
) {
    var startDestination by remember { mutableStateOf(if (isLoggedIn) "main" else "auth") }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // 인증 관련 화면들 (로그인, 회원가입 등)
        composable("auth") {
            AuthNavigation(
                onNavigateToMain = {
                    // 로그인/회원가입 완료 시 메인 화면으로 이동
                    navController.navigate("main") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }

        // 메인 화면 (바텀 네비게이션이 있는 화면들)
        composable("main") {
            MainScreenWithBottomNav()
        }
    }
}

/**
 * 바텀 네비게이션이 포함된 메인 화면
 */
@Composable
fun MainScreenWithBottomNav() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: BottomNavigationItem.HOME.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onItemClick = { route ->
                    navController.navigate(route) {
                        // 바텀 네비게이션 클릭 시 백스택 관리
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = BottomNavigationItem.HOME.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            // 홈 화면
            composable(BottomNavigationItem.HOME.route) {
                HomeScreen()
            }

            // 상품 구매 화면
            composable(BottomNavigationItem.SHOP.route) {
                ShopScreen()
            }

            // 내 정보 화면
            composable(BottomNavigationItem.PROFILE.route) {
                ProfileScreen()
            }
        }
    }
}

/**
 * 상품 구매 화면 (임시)
 */
@Composable
fun ShopScreen() {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        androidx.compose.material3.Text(
            text = "상품 구매 화면",
            fontSize = 20.sp,
            fontFamily = com.luckydut97.tennispark.core.ui.theme.Pretendard
        )
    }
}

/**
 * 내 정보 화면 (임시)
 */
@Composable
fun ProfileScreen() {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        androidx.compose.material3.Text(
            text = "내 정보 화면",
            fontSize = 20.sp,
            fontFamily = com.luckydut97.tennispark.core.ui.theme.Pretendard
        )
    }
}