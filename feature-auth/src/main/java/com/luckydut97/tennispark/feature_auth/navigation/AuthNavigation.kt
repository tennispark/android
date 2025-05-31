package com.luckydut97.tennispark.feature_auth.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.luckydut97.tennispark.feature_auth.splash.ui.SplashScreen
import com.luckydut97.tennispark.feature_auth.verification.ui.PhoneVerificationScreen
import com.luckydut97.tennispark.feature_auth.signup.ui.SignupScreen

// 인증 관련 라우트 정의
object AuthRoute {
    const val SPLASH = "splash"
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
        startDestination = AuthRoute.SPLASH
    ) {
        // 스플래시 화면
        composable(AuthRoute.SPLASH) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(AuthRoute.PHONE_VERIFICATION) {
                        popUpTo(AuthRoute.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        // 휴대폰 본인인증 화면
        composable(AuthRoute.PHONE_VERIFICATION) {
            PhoneVerificationScreen(
                onBackClick = {
                    // 뒤로 가기 시 앱 종료 또는 다른 처리
                },
                onNavigateToSignup = {
                    navController.navigate(AuthRoute.SIGNUP)
                }
            )
        }

        // 회원가입 화면
        composable(AuthRoute.SIGNUP) {
            SignupScreen(
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
