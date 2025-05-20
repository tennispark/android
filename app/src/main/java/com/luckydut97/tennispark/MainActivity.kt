package com.luckydut97.tennispark

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.luckydut97.tennispark.feature_auth.navigation.AuthNavigation
import com.luckydut97.tennispark.ui.theme.TennisParkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TennisParkTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TennisParkApp()
                }
            }
        }
    }
}

@Composable
fun TennisParkApp() {
    var isLoggedIn by remember { mutableStateOf(false) }

    if (isLoggedIn) {
        // 로그인 상태일 때는 메인 화면 표시 (추후 구현)
        MainScreen()
    } else {
        // 로그인 상태가 아닐 때는 인증 관련 네비게이션 표시
        AuthNavigation(
            onNavigateToMain = {
                isLoggedIn = true
            }
        )
    }
}

@Composable
fun MainScreen() {
    // 메인 화면은 추후 구현 예정
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        // 여기에 메인 화면 컴포넌트 추가
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TennisParkTheme {
        TennisParkApp()
    }
}