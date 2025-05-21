package com.luckydut97.tennispark

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.luckydut97.feature_home.main.ui.HomeScreen
import com.luckydut97.tennispark.feature_auth.navigation.AuthNavigation
import com.luckydut97.tennispark.feature_auth.navigation.AuthRoute
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

        // 메인 화면 (홈, 상품 구매, 내 정보 등)
        composable("main") {
            MainNavigation(navController)
        }
    }
}

/**
 * 메인 화면 내부의 네비게이션을 처리하는 컴포넌트
 */
@Composable
fun MainNavigation(
    navController: NavHostController = rememberNavController()
) {
    val mainNavController = rememberNavController()

    NavHost(
        navController = mainNavController,
        startDestination = BottomNavigationItem.HOME.route
    ) {
        // 홈 화면
        composable(BottomNavigationItem.HOME.route) {
            HomeScreen(
                onNavigateToRoute = { route ->
                    mainNavController.navigate(route) {
                        // 바텀 내비게이션 항목 간 이동 시 백 스택을 복잡하게 쌓지 않도록 설정
                        popUpTo(mainNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        // 상품 구매 화면 (추후 구현 예정)
        composable(BottomNavigationItem.SHOP.route) {
            // ShopScreen()
        }

        // 내 정보 화면 (추후 구현 예정)
        composable(BottomNavigationItem.PROFILE.route) {
            // ProfileScreen()
        }
    }
}