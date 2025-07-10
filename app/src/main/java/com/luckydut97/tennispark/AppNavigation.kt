package com.luckydut97.tennispark

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.luckydut97.feature.attendance.ui.AttendanceScreen
import com.luckydut97.feature_home_shop.ui.ShopDetailScreen
import com.luckydut97.feature_home_shop.data.model.ShopItem
import com.luckydut97.tennispark.core.ui.components.navigation.BottomNavigationBar
import com.luckydut97.tennispark.core.ui.components.navigation.BottomNavigationItem
import com.luckydut97.feature_home.main.ui.HomeScreen
import com.luckydut97.feature_home_shop.ui.ShopScreen
import com.luckydut97.feature_myinfo.navigation.MyInfoNavigation
import com.luckydut97.tennispark.feature_auth.navigation.AuthNavigation

/**
 * 탭 순서에 따른 슬라이드 방향 결정
 */
fun getTabOrder(route: String): Int {
    return when {
        route == BottomNavigationItem.HOME.route -> 0
        route == BottomNavigationItem.SHOP.route -> 1
        route.startsWith("shop_detail") -> 1 // 상품 상세 화면도 상품구매 탭으로 처리
        route == BottomNavigationItem.PROFILE.route -> 2
        // 내정보 탭의 하위 화면들
        route == "myinfo" || route == "settings" || route == "notice" ||
                route == "app_settings" || route == "faq" || route == "terms" ||
                route == "version_info" || route == "withdrawal" -> 2
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

        // 멤버십 등록 화면 (오른쪽에서 왼쪽으로 슬라이드)
        composable(
            "membership",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            }
        ) {
            com.luckydut97.tennispark.feature_auth.membership.ui.MembershipRegistrationScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onMembershipComplete = {
                    navController.navigate("main") {
                        popUpTo("membership") { inclusive = true }
                    }
                },
                onRefundPolicyClick = {
                    navController.navigate("refund_policy")
                }
            )
        }

        // 환불규정 화면 (오른쪽에서 왼쪽으로 슬라이드)
        composable(
            "refund_policy",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            }
        ) {
            com.luckydut97.tennispark.feature_auth.membership.ui.RefundPolicyScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // 출석체크 화면
        composable(
            "attendance",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            }
        ) {
            android.util.Log.d("출석체크 디버깅", "AttendanceScreen Composable 생성됨")
            com.luckydut97.feature.attendance.ui.AttendanceScreen(
                onBackClick = {
                    android.util.Log.d("출석체크 디버깅", "출석체크 뒤로가기 클릭됨")
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
                    android.util.Log.d("출석체크 디버깅", "출석체크 완료됨")
                    navController.popBackStack()
                }
            )
        }

    }
}

/**
 * 바텀 네비게이션 포함된 메인 화면
 */
@Composable
fun MainScreenWithBottomNav(
    mainNavController: NavHostController
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: BottomNavigationItem.HOME.route

    // 상품 상세 화면에서도 상품구매 탭이 활성화되도록 라우트 매핑
    val bottomNavRoute = when {
        currentRoute.startsWith("shop_detail") -> BottomNavigationItem.SHOP.route
        // 내정보 하위 화면들에서도 내정보 탭 활성화
        currentRoute == "myinfo" || currentRoute == "settings" || currentRoute == "notice" ||
                currentRoute == "app_settings" || currentRoute == "faq" || currentRoute == "terms" ||
                currentRoute == "version_info" || currentRoute == "withdrawal" -> BottomNavigationItem.PROFILE.route
        else -> currentRoute
    }

    // 이전 라우트 추적을 위한 상태
    var previousRoute by remember { mutableStateOf(BottomNavigationItem.HOME.route) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(
                currentRoute = bottomNavRoute, // 매핑된 라우트 사용
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
                    val initialRoute = initialState.destination.route ?: ""
                    val initialOrder = getTabOrder(initialRoute)
                    val targetOrder = getTabOrder(BottomNavigationItem.HOME.route)

                    if (initialOrder > targetOrder) {
                        // 오른쪽에서 왼쪽으로 (상품구매/내정보 -> 홈)
                        slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = tween(300)
                        )
                    } else {
                        // 왼쪽에서 오른쪽으로 (처음 진입)
                        slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(300)
                        )
                    }
                },
                exitTransition = {
                    val targetRoute = targetState.destination.route ?: ""
                    val initialOrder = getTabOrder(BottomNavigationItem.HOME.route)
                    val targetOrder = getTabOrder(targetRoute)

                    if (targetOrder > initialOrder) {
                        // 왼쪽으로 나가기 (홈 -> 상품구매/내정보)
                        slideOutHorizontally(
                            targetOffsetX = { -it },
                            animationSpec = tween(300)
                        )
                    } else {
                        // 오른쪽으로 나가기
                        slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(300)
                        )
                    }
                }
            ) {
                HomeScreen(
                    onMembershipClick = {
                        android.util.Log.d(
                            "출석체크 디버깅",
                            "멤버십 클릭 - mainNavController.navigate(membership)"
                        )
                        mainNavController.navigate("membership")
                    },
                    onAttendanceClick = {
                        android.util.Log.d(
                            "출석체크 디버깅",
                            "출석체크 클릭 - mainNavController.navigate(attendance) 호출"
                        )
                        try {
                            mainNavController.navigate("attendance")
                            android.util.Log.d("출석체크 디버깅", "출석체크 네비게이션 성공")
                        } catch (e: Exception) {
                            android.util.Log.e("출석체크 디버깅", "출석체크 네비게이션 실패: ${e.message}")
                        }
                    }
                )
            }

            // 상품 구매 화면
            composable(
                BottomNavigationItem.SHOP.route,
                enterTransition = {
                    val initialRoute = initialState.destination.route ?: ""
                    val initialOrder = getTabOrder(initialRoute)
                    val targetOrder = getTabOrder(BottomNavigationItem.SHOP.route)

                    if (initialOrder > targetOrder) {
                        // 오른쪽에서 왼쪽으로 (내정보 -> 상품구매)
                        slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = tween(300)
                        )
                    } else {
                        // 왼쪽에서 오른쪽으로 (홈 -> 상품구매)
                        slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(300)
                        )
                    }
                },
                exitTransition = {
                    val targetRoute = targetState.destination.route ?: ""
                    val initialOrder = getTabOrder(BottomNavigationItem.SHOP.route)
                    val targetOrder = getTabOrder(targetRoute)

                    if (targetOrder > initialOrder) {
                        // 왼쪽으로 나가기 (상품구매 -> 내정보)
                        slideOutHorizontally(
                            targetOffsetX = { -it },
                            animationSpec = tween(300)
                        )
                    } else {
                        // 오른쪽으로 나가기 (상품구매 -> 홈)
                        slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(300)
                        )
                    }
                }
            ) {
                ShopScreen(
                    onBackClick = {
                        navController.navigate(BottomNavigationItem.HOME.route) {
                            popUpTo(BottomNavigationItem.HOME.route) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    },
                    onItemClick = { item ->
                        val encodedImageUrl = item.imageUrl?.let {
                            java.net.URLEncoder.encode(it, "UTF-8")
                        } ?: ""
                        navController.navigate(
                            "shop_detail/${item.id}/${item.brandName}/${item.productName}/${item.price}/$encodedImageUrl"
                        )
                    }
                )
            }

            // 상품 상세 화면
            composable(
                "shop_detail/{productId}/{brandName}/{productName}/{price}/{imageUrl}",
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(300)
                    )
                },
                exitTransition = {
                    val targetRoute = targetState.destination.route ?: ""
                    val currentOrder = getTabOrder("shop_detail")
                    val targetOrder = getTabOrder(targetRoute)

                    if (targetOrder > currentOrder) {
                        // 왼쪽으로 나가기 (상품구매 -> 내정보)
                        slideOutHorizontally(
                            targetOffsetX = { -it },
                            animationSpec = tween(300)
                        )
                    } else {
                        // 오른쪽으로 나가기 (상품구매 -> 홈 또는 뒤로가기)
                        slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(300)
                        )
                    }
                },
                popEnterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(300)
                    )
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(300)
                    )
                }
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId") ?: ""
                val brandName = backStackEntry.arguments?.getString("brandName") ?: ""
                val productName = backStackEntry.arguments?.getString("productName") ?: ""
                val price = backStackEntry.arguments?.getString("price")?.toIntOrNull() ?: 0
                val imageUrl = backStackEntry.arguments?.getString("imageUrl")?.let {
                    java.net.URLDecoder.decode(it, "UTF-8")
                }

                val shopItem = ShopItem(
                    id = productId,
                    brandName = brandName,
                    productName = productName,
                    price = price,
                    imageUrl = imageUrl
                )

                ShopDetailScreen(
                    item = shopItem,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            // 내 정보 화면
            composable(
                BottomNavigationItem.PROFILE.route,
                enterTransition = {
                    val initialRoute = initialState.destination.route ?: ""
                    val initialOrder = getTabOrder(initialRoute)
                    val targetOrder = getTabOrder(BottomNavigationItem.PROFILE.route)

                    if (initialOrder < targetOrder) {
                        // 왼쪽에서 오른쪽으로 (홈/상품구매 -> 내정보)
                        slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(300)
                        )
                    } else {
                        // 오른쪽에서 왼쪽으로 (처음 진입)
                        slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = tween(300)
                        )
                    }
                },
                exitTransition = {
                    val targetRoute = targetState.destination.route ?: ""
                    val initialOrder = getTabOrder(BottomNavigationItem.PROFILE.route)
                    val targetOrder = getTabOrder(targetRoute)

                    if (targetOrder < initialOrder) {
                        // 오른쪽으로 나가기 (내정보 -> 홈/상품구매)
                        slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(300)
                        )
                    } else {
                        // 왼쪽으로 나가기
                        slideOutHorizontally(
                            targetOffsetX = { -it },
                            animationSpec = tween(300)
                        )
                    }
                }
            ) {
                MyInfoNavigation(
                    onBackClick = {
                        navController.navigate(BottomNavigationItem.HOME.route) {
                            popUpTo(BottomNavigationItem.HOME.route) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    },
                    onLogoutComplete = {
                        android.util.Log.d(
                            "🔍 AppNavigation",
                            "🔍 디버깅: === onLogoutComplete 콜백 시작 ==="
                        )
                        android.util.Log.d(
                            "🔍 AppNavigation",
                            "🔍 디버깅: 현재 스레드: ${Thread.currentThread().name}"
                        )
                        android.util.Log.d("🔍 AppNavigation", "🔍 디버깅: mainNavController 상태 확인")
                        android.util.Log.d(
                            "🔍 AppNavigation",
                            "🔍 디버깅: mainNavController.currentDestination: ${mainNavController.currentDestination?.route}"
                        )

                        try {
                            android.util.Log.d(
                                "🔍 AppNavigation",
                                "🔍 디버깅: mainNavController.navigate(\"auth\") 호출 시작"
                            )
                            // 로그아웃 완료 시 인증 화면으로 이동
                            mainNavController.navigate("auth") {
                                popUpTo("main") { inclusive = true }
                            }
                            android.util.Log.d("🔍 AppNavigation", "🔍 디버깅: ✅ 인증 화면으로 네비게이션 성공")
                            android.util.Log.d(
                                "🔍 AppNavigation",
                                "🔍 디버깅: 새로운 destination: ${mainNavController.currentDestination?.route}"
                            )
                        } catch (e: Exception) {
                            android.util.Log.e(
                                "🔍 AppNavigation",
                                "🔍 디버깅: ❌ 인증 화면으로 네비게이션 실패: ${e.message}",
                                e
                            )
                            android.util.Log.e(
                                "🔍 AppNavigation",
                                "🔍 디버깅: 예외 타입: ${e.javaClass.simpleName}"
                            )
                        }
                        android.util.Log.d(
                            "🔍 AppNavigation",
                            "🔍 디버깅: === onLogoutComplete 콜백 완료 ==="
                        )
                    }
                )
            }
        }
    }
}
