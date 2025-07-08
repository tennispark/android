package com.luckydut97.tennispark

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.luckydut97.tennispark.core.data.network.NetworkModule
import com.luckydut97.tennispark.core.fcm.FcmTokenManager
import com.luckydut97.tennispark.core.ui.theme.TennisParkTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val fcmTokenManager = FcmTokenManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 다크모드 비활성화
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // NetworkModule 초기화
        NetworkModule.initialize(this)

        // FCM 토큰 즉시 확인 (디버깅용)
        checkFcmToken()

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

    /**
     * FCM 토큰을 확인하고 로그로 출력 (디버깅용)
     */
    private fun checkFcmToken() {
        Log.d("🔥 MainActivity", "=== FCM 토큰 확인 시작 ===")

        lifecycleScope.launch {
            try {
                val fcmToken = fcmTokenManager.getFcmToken()

                if (fcmToken != null) {
                    Log.d("🔥 MainActivity", "✅ FCM 토큰 발급 성공!")
                    Log.d("🔥 MainActivity", "📱 FCM 토큰: $fcmToken")
                    Log.d("🔥 MainActivity", "📏 토큰 길이: ${fcmToken.length}")

                    // 토큰 유효성 검사
                    if (fcmTokenManager.isValidFcmToken(fcmToken)) {
                        Log.d("🔥 MainActivity", "✅ FCM 토큰이 유효합니다")
                    } else {
                        Log.w("🔥 MainActivity", "⚠️ FCM 토큰이 유효하지 않습니다")
                    }
                } else {
                    Log.e("🔥 MainActivity", "❌ FCM 토큰 발급 실패")
                }
            } catch (e: Exception) {
                Log.e("🔥 MainActivity", "🔥 FCM 토큰 확인 중 예외: ${e.message}", e)
            }
        }

        Log.d("🔥 MainActivity", "=== FCM 토큰 확인 완료 ===")
    }
}

@Composable
fun TennisParkApp() {
    // 앱 네비게이션 사용
    AppNavigation()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TennisParkTheme {
        TennisParkApp()
    }
}
