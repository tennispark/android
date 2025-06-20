package com.luckydut97.tennispark

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.luckydut97.tennispark.core.ui.theme.TennisParkTheme
import com.luckydut97.tennispark.core.data.network.NetworkModule

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 다크모드 비활성화
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // NetworkModule 초기화
        NetworkModule.initialize(this)

        // 시스템 UI 설정
        WindowCompat.setDecorFitsSystemWindows(window, true)

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
    AppNavigation() // isLoggedIn 파라미터 제거 (스플래시에서 처리)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TennisParkTheme {
        TennisParkApp()
    }
}
