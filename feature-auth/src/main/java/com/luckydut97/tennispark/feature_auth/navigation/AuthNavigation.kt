package com.luckydut97.tennispark.feature_auth.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.luckydut97.tennispark.feature_auth.signup.ui.SignupScreen
import com.luckydut97.tennispark.feature_auth.sms.ui.PhoneVerificationScreen
import com.luckydut97.tennispark.feature_auth.splash.ui.SplashScreen

// 인증 관련 화면 경로 정의
sealed class AuthRoute(val route: String) {
    object Splash : AuthRoute("splash")
    object PhoneVerification : AuthRoute("phone_verification")
    object Signup : AuthRoute("signup")
}

/**
 * 인증 관련 화면 간의 네비게이션을 처리하는 컴포넌트
 */
@Composable
fun AuthNavigation(
    navController: NavHostController = rememberNavController(),
    onNavigateToMain: () -> Unit,
    startDestination: String = AuthRoute.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(AuthRoute.Splash.route) {
            SplashScreen(
                onNavigateToLogin = {
                    // 스플래시 화면에서 바로 본인인증 화면으로 이동
                    navController.navigate(AuthRoute.PhoneVerification.route) {
                        popUpTo(AuthRoute.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AuthRoute.PhoneVerification.route) {
            PhoneVerificationScreen(
                onBackClick = {
                    // 뒤로가기 처리 (원래는 스플래시로 돌아가지만, 현재 구현에서는 백 스택 단순화를 위해 닫음)
                    navController.popBackStack()
                },
                onNavigateToSignup = {
                    // 본인인증 완료 후 회원가입 화면으로 이동
                    navController.navigate(AuthRoute.Signup.route) {
                        popUpTo(AuthRoute.PhoneVerification.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AuthRoute.Signup.route) {
            SignupScreen(
                onBackClick = {
                    // 뒤로가기 처리
                    navController.popBackStack()
                },
                onSignupComplete = {
                    // 회원가입 완료 후 메인 화면으로 이동
                    onNavigateToMain()
                }
            )
        }
    }
}