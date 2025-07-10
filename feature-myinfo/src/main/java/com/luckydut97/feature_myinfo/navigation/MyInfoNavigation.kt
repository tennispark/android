package com.luckydut97.feature_myinfo.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.luckydut97.feature_myinfo.ui.MyInfoScreen
import com.luckydut97.feature_myinfo.ui.NoticeScreen
import com.luckydut97.feature_myinfo.ui.FaqScreen
import com.luckydut97.feature_myinfo.ui.SettingsScreen
import com.luckydut97.feature_myinfo.ui.AppSettingsScreen
import com.luckydut97.feature_myinfo.viewmodel.MyInfoViewModel
import android.util.Log
import kotlinx.coroutines.delay

/**
 * feature-myinfo 모듈 내부 네비게이션
 * MyInfoScreen -> SettingsScreen -> 공지사항/FAQ 등
 */
@Composable
fun MyInfoNavigation(
    onBackClick: () -> Unit = {},
    onLogoutComplete: () -> Unit = {}, // 로그아웃 완료 시 인증 화면으로 이동
    navController: NavHostController = rememberNavController(),
    viewModel: MyInfoViewModel = viewModel()
) {
    val tag = "🔍 MyInfoNavigation"

    // 로그아웃 상태 감지
    val isLoggedOut by viewModel.isLoggedOut.collectAsState()
    val isWithdrawn by viewModel.isWithdrawn.collectAsState()

    Log.d(
        tag,
        "🔍 디버깅: MyInfoNavigation 리컴포지션 - isLoggedOut: $isLoggedOut, isWithdrawn: $isWithdrawn"
    )

    // 로그아웃 완료 시 인증 화면으로 이동
    LaunchedEffect(isLoggedOut) {
        Log.d(tag, "🔍 디버깅: LaunchedEffect(isLoggedOut) 트리거됨 - isLoggedOut: $isLoggedOut")
        if (isLoggedOut) {
            Log.d(tag, "🔍 디버깅: ✅ 로그아웃 완료 감지 - onLogoutComplete() 호출 예정")
            try {
                onLogoutComplete()
                Log.d(tag, "🔍 디버깅: onLogoutComplete() 호출 완료")
            } catch (e: Exception) {
                Log.e(tag, "🔍 디버깅: onLogoutComplete() 호출 실패: ${e.message}", e)
            }
            // 네비게이션 완료 후 상태 초기화
            Log.d(tag, "🔍 디버깅: 200ms 대기 시작")
            delay(200) // 네비게이션 완료 대기 (좀 더 길게)
            Log.d(tag, "🔍 디버깅: 200ms 대기 완료, resetLogoutState() 호출")
            viewModel.resetLogoutState()
            Log.d(tag, "🔍 디버깅: ✅ 로그아웃 상태 초기화 완료")
        } else {
            Log.d(tag, "🔍 디버깅: isLoggedOut이 false여서 onLogoutComplete() 호출하지 않음")
        }
    }

    // 회원 탈퇴 완료 시 인증 화면으로 이동
    LaunchedEffect(isWithdrawn) {
        Log.d(tag, "🔍 디버깅: LaunchedEffect(isWithdrawn) 트리거됨 - isWithdrawn: $isWithdrawn")
        if (isWithdrawn) {
            Log.d(tag, "🔍 디버깅: ✅ 회원 탈퇴 완료 감지 - onLogoutComplete() 호출 예정")
            try {
                onLogoutComplete()
                Log.d(tag, "🔍 디버깅: onLogoutComplete() 호출 완료")
            } catch (e: Exception) {
                Log.e(tag, "🔍 디버깅: onLogoutComplete() 호출 실패: ${e.message}", e)
            }
            // 네비게이션 완료 후 상태 초기화
            Log.d(tag, "🔍 디버깅: 200ms 대기 시작")
            delay(200) // 네비게이션 완료 대기 (좀 더 길게)
            Log.d(tag, "🔍 디버깅: 200ms 대기 완료, resetWithdrawState() 호출")
            viewModel.resetWithdrawState()
            Log.d(tag, "🔍 디버깅: ✅ 회원 탈퇴 상태 초기화 완료")
        } else {
            Log.d(tag, "🔍 디버깅: isWithdrawn이 false여서 onLogoutComplete() 호출하지 않음")
        }
    }

    NavHost(
        navController = navController,
        startDestination = "myinfo"
    ) {
        // 내 정보 메인 화면
        composable("myinfo") {
            MyInfoScreen(
                onSettingsClick = {
                    navController.navigate("settings")
                },
                viewModel = viewModel
            )
        }

        // 설정 화면
        composable(
            "settings",
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(300))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300))
            }
        ) {
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
                viewModel = viewModel // 🔍 디버깅: 동일한 ViewModel 인스턴스 전달
            )
        }

        // 공지사항 화면
        composable(
            "notice",
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(300))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300))
            }
        ) {
            NoticeScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // 앱 설정 화면 
        composable(
            "app_settings",
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(300))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300))
            }
        ) {
            AppSettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // FAQ 화면
        composable(
            "faq",
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(300))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300))
            }
        ) {
            FaqScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // 이용약관 화면 (TODO: 향후 구현)
        composable(
            "terms",
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(300))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300))
            }
        ) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // 버전 정보 화면 (TODO: 향후 구현)
        composable(
            "version_info",
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(300))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300))
            }
        ) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
