package com.luckydut97.tennispark.feature_auth.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.luckydut97.tennispark.feature_auth.verification.ui.PhoneVerificationScreen
import com.luckydut97.tennispark.feature_auth.signup.ui.SignupScreen

// 인증 관련 라우트 정의
object AuthRoute {
    const val PHONE_VERIFICATION = "phone_verification"
    const val SIGNUP = "signup"
}

@Composable
fun AuthNavigation(
    navController: NavHostController = rememberNavController(),
    onNavigateToMain: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = AuthRoute.PHONE_VERIFICATION
    ) {
        // 휴대폰 인증 화면
        composable(
            AuthRoute.PHONE_VERIFICATION,
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
            PhoneVerificationScreen(
                onBackClick = {
                    // 뒤로 가기 시 앱 종료 또는 다른 처리
                },
                onNavigateToSignup = { phoneNumber ->
                    navController.navigate("${AuthRoute.SIGNUP}/$phoneNumber")
                },
                onNavigateToMain = {
                    // 기존 회원 로그인 완료 시 메인 화면으로 이동
                    onNavigateToMain()
                }
            )
        }

        // 회원가입 화면 (전화번호 파라미터 추가)
        composable(
            "${AuthRoute.SIGNUP}/{phoneNumber}",
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
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            SignupScreen(
                phoneNumber = phoneNumber,
                onBackClick = {
                    navController.popBackStack()
                },
                onSignupComplete = {
                    // 회원가입 완료 시 메인 화면으로 이동
                    onNavigateToMain()
                }
            )
        }
    }
}
