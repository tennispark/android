package com.luckydut97.tennispark

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.luckydut97.tennispark.core.ui.theme.TennisParkTheme

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
    // 앱 네비게이션 사용
    AppNavigation(isLoggedIn = false) // 기본값은 로그인 안된 상태로 시작
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TennisParkTheme {
        TennisParkApp()
    }
}