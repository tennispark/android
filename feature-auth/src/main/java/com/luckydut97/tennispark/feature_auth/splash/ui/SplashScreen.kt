package com.luckydut97.tennispark.feature_auth.splash.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luckydut97.tennispark.feature.auth.R
import com.luckydut97.tennispark.feature_auth.splash.viewmodel.SplashViewModel
import com.luckydut97.tennispark.feature_auth.splash.viewmodel.AuthState

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    val viewModel: SplashViewModel = viewModel()
    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> onNavigateToMain()
            is AuthState.Unauthenticated -> onNavigateToLogin()
            is AuthState.Loading -> { /* 로딩 상태 - 스플래시 화면 유지 */
            }
        }
    }
    
    SplashContent()
}

@Composable
fun SplashContent() {
    // 디자인에 맞는 보라색 배경
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF08432E))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 로고 이미지
            Image(
                painter = painterResource(id = R.drawable.ic_app_logo),
                contentDescription = "Tennis Park Logo",
                modifier = Modifier.size(268.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // 텍스트
            Text(
                text = "",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashContent()
}
