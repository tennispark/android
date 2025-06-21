package com.luckydut97.tennispark

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import android.util.Log
import com.luckydut97.feature_home_shop.data.model.ShopItem
import com.luckydut97.tennispark.core.ui.components.navigation.BottomNavigationBar
import com.luckydut97.tennispark.core.ui.components.navigation.BottomNavigationItem
import com.luckydut97.feature_home.main.ui.HomeScreen
import com.luckydut97.feature_home_shop.ui.ShopScreen
import com.luckydut97.feature_home_shop.ui.ShopDetailScreen
import com.luckydut97.feature_myinfo.ui.MyInfoScreen
import com.luckydut97.tennispark.feature_auth.navigation.AuthNavigation
import com.luckydut97.tennispark.feature_auth.membership.ui.MembershipRegistrationScreen
import com.luckydut97.feature.attendance.ui.AttendanceScreen

/**
 * 앱 전체의 메인 네비게이션을 처리하는 컴포넌트
 */
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // 스플래시 화면
        composable("splash") {
            com.luckydut97.tennispark.feature_auth.splash.ui.SplashScreen(
                onNavigateToLogin = {
                    navController.navigate("auth") {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                onNavigateToMain = {
                    navController.navigate("main") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        // 🔥 테스트용 화면 추가
        composable("dev_test") {
            MainScreenWithBottomNav(navController)
        }

        // 인증 관련 화면들 (로그인, 회원가입 등)
        composable("auth") {
            AuthNavigation(
                onNavigateToMain = {
                    navController.navigate("main") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }

        // 메인 화면 (바텀 네비게이션 포함)
        composable("main") {
            MainScreenWithBottomNav(navController)
        }

        // 상품 상세 화면 (바텀 네비게이션 없음)
        composable("shop_detail/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            val mockItem = ShopItem(
                id = productId ?: "",
                brandName = "Wilson",
                productName = "오버그립",
                price = 4500
            )
            ShopDetailScreen(
                item = mockItem,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // 멤버십 등록 화면
        composable("membership") {
            MembershipRegistrationScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onMembershipComplete = {
                    navController.navigate("main") {
                        popUpTo("membership") { inclusive = true }
                    }
                }
            )
        }

        // 출석체크 화면
        composable("attendance") {
            AttendanceScreen(
                onBackClick = {
                    val canGoBack = navController.previousBackStackEntry != null
                    if (canGoBack) {
                        navController.popBackStack()
                    } else {
                        navController.navigate("main") {
                            popUpTo("main") { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
                onAttendanceComplete = {
                    navController.popBackStack()
                }
            )
        }
    }
}

/**
 * 바텀 네비게이션이 포함된 메인 화면
 */
@Composable
fun MainScreenWithBottomNav(
    mainNavController: NavHostController
) {
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
                HomeScreen(
                    onMembershipClick = {
                        mainNavController.navigate("membership")
                    },
                    onAttendanceClick = {
                        mainNavController.navigate("attendance")
                    }
                )
            }

            // 상품 구매 화면
            composable(BottomNavigationItem.SHOP.route) {
                ShopScreen(
                    onItemClick = { item ->
                        mainNavController.navigate("shop_detail/${item.id}")
                    }
                )
            }

            // 내 정보 화면
            composable(BottomNavigationItem.PROFILE.route) {
                Log.d("🔍 디버깅: Navigation", "MyInfoScreen 네비게이션 - 새로운 인스턴스 생성")
                MyInfoScreen()
            }
        }
    }
}
