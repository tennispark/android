package com.luckydut97.tennispark

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.luckydut97.feature_home_shop.data.model.ShopItem
import com.luckydut97.tennispark.core.ui.components.navigation.BottomNavigationBar
import com.luckydut97.tennispark.core.ui.components.navigation.BottomNavigationItem
import com.luckydut97.feature_home.main.ui.HomeScreen
import com.luckydut97.feature_home_shop.ui.ShopScreen
import com.luckydut97.feature_home_shop.ui.ShopDetailScreen
import com.luckydut97.feature_myinfo.ui.MyInfoScreen
import com.luckydut97.tennispark.feature_auth.navigation.AuthNavigation

/**
 * 탭 순서에 따른 슬라이드 방향 결정
 */
fun getTabOrder(route: String): Int {
    return when (route) {
        BottomNavigationItem.HOME.route -> 0
        BottomNavigationItem.SHOP.route -> 1
        BottomNavigationItem.PROFILE.route -> 2
        else -> 0
    }
}

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

        // 상품 상세 화면
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
            com.luckydut97.tennispark.feature_auth.membership.ui.MembershipRegistrationScreen(
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
            com.luckydut97.feature.attendance.ui.AttendanceScreen(
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

    // 이전 라우트 추적을 위한 상태
    var previousRoute by remember { mutableStateOf(BottomNavigationItem.HOME.route) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onItemClick = { route ->
                    previousRoute = currentRoute
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
            composable(
                BottomNavigationItem.HOME.route,
                enterTransition = {
                    val targetOrder = getTabOrder(BottomNavigationItem.HOME.route)
                    val initialOrder = getTabOrder(initialState.destination.route ?: "")
                    if (targetOrder > initialOrder) {
                        // 오른쪽에서 들어옴
                        slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(300)
                        )
                    } else {
                        // 왼쪽에서 들어옴
                        slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = tween(300)
                        )
                    }
                },
                exitTransition = {
                    val currentOrder = getTabOrder(BottomNavigationItem.HOME.route)
                    val targetOrder = getTabOrder(targetState.destination.route ?: "")
                    if (currentOrder > targetOrder) {
                        // 오른쪽으로 나감
                        slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(300)
                        )
                    } else {
                        // 왼쪽으로 나감
                        slideOutHorizontally(
                            targetOffsetX = { -it },
                            animationSpec = tween(300)
                        )
                    }
                }
            ) {
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
            composable(
                BottomNavigationItem.SHOP.route,
                enterTransition = {
                    val targetOrder = getTabOrder(BottomNavigationItem.SHOP.route)
                    val initialOrder = getTabOrder(initialState.destination.route ?: "")
                    if (targetOrder > initialOrder) {
                        // 오른쪽에서 들어옴
                        slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(300)
                        )
                    } else {
                        // 왼쪽에서 들어옴
                        slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = tween(300)
                        )
                    }
                },
                exitTransition = {
                    val currentOrder = getTabOrder(BottomNavigationItem.SHOP.route)
                    val targetOrder = getTabOrder(targetState.destination.route ?: "")
                    if (currentOrder > targetOrder) {
                        // 오른쪽으로 나감
                        slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(300)
                        )
                    } else {
                        // 왼쪽으로 나감
                        slideOutHorizontally(
                            targetOffsetX = { -it },
                            animationSpec = tween(300)
                        )
                    }
                }
            ) {
                ShopScreen(
                    onBackClick = {
                        // 홈으로 이동
                        navController.navigate(BottomNavigationItem.HOME.route) {
                            popUpTo(BottomNavigationItem.HOME.route) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    },
                    onItemClick = { item ->
                        mainNavController.navigate("shop_detail/${item.id}")
                    }
                )
            }

            // 내 정보 화면
            composable(
                BottomNavigationItem.PROFILE.route,
                enterTransition = {
                    val targetOrder = getTabOrder(BottomNavigationItem.PROFILE.route)
                    val initialOrder = getTabOrder(initialState.destination.route ?: "")
                    if (targetOrder > initialOrder) {
                        // 오른쪽에서 들어옴
                        slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(300)
                        )
                    } else {
                        // 왼쪽에서 들어옴
                        slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = tween(300)
                        )
                    }
                },
                exitTransition = {
                    val currentOrder = getTabOrder(BottomNavigationItem.PROFILE.route)
                    val targetOrder = getTabOrder(targetState.destination.route ?: "")
                    if (currentOrder > targetOrder) {
                        // 오른쪽으로 나감
                        slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(300)
                        )
                    } else {
                        // 왼쪽으로 나감
                        slideOutHorizontally(
                            targetOffsetX = { -it },
                            animationSpec = tween(300)
                        )
                    }
                }
            ) {
                Log.d("🔍 디버깅: Navigation", "MyInfoScreen 네비게이션 - 새로운 인스턴스 생성")
                MyInfoScreen(
                    onBackClick = {
                        // 홈으로 이동
                        navController.navigate(BottomNavigationItem.HOME.route) {
                            popUpTo(BottomNavigationItem.HOME.route) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}
