package com.luckydut97.tennispark

import android.Manifest
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import android.widget.Toast
import com.luckydut97.tennispark.core.data.network.NetworkModule
import com.luckydut97.tennispark.core.fcm.FcmTokenManager
import com.luckydut97.tennispark.core.fcm.NotificationHelper
import com.luckydut97.tennispark.core.ui.theme.TennisParkTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val fcmTokenManager = FcmTokenManager()

    // 알림 권한 요청 런처
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("🔥 MainActivity", "✅ 알림 권한이 허용되었습니다.")
        } else {
            Log.w("🔥 MainActivity", "⚠️ 알림 권한이 거부되었습니다.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 다크모드 비활성화
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // NetworkModule 초기화
        NetworkModule.initialize(this)

        // Google Play 서비스 체크
        if (GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(this) != ConnectionResult.SUCCESS
        ) {
            Toast.makeText(
                this,
                "Google Play 서비스가 필요합니다. 플레이스토어에서 설치/업데이트 후 재실행하세요.",
                Toast.LENGTH_LONG
            ).show()
        }

        // 알림 채널을 명시적으로 미리 생성합니다.
        NotificationHelper(this)

        // 알림 권한 요청
        requestNotificationPermission()

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
     * 알림 권한 요청 (Android 13+)
     */
    private fun requestNotificationPermission() {
        Log.d("🔥 MainActivity", "🔍 디버깅: === 알림 권한 확인 시작 ===")
        Log.d("🔥 MainActivity", "🔍 디버깅: Android 버전: ${Build.VERSION.SDK_INT}")
        Log.d("🔥 MainActivity", "🔍 디버깅: TIRAMISU 버전: ${Build.VERSION_CODES.TIRAMISU}")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Log.d("🔥 MainActivity", "🔍 디버깅: Android 13+ 감지 - 알림 권한 확인 필요")

            val currentPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            )

            Log.d("🔥 MainActivity", "🔍 디버깅: 현재 권한 상태: $currentPermission")
            Log.d(
                "🔥 MainActivity",
                "🔍 디버깅: PERMISSION_GRANTED: ${PackageManager.PERMISSION_GRANTED}"
            )
            Log.d("🔥 MainActivity", "🔍 디버깅: PERMISSION_DENIED: ${PackageManager.PERMISSION_DENIED}")

            when {
                currentPermission == PackageManager.PERMISSION_GRANTED -> {
                    Log.d("🔥 MainActivity", "✅ 디버깅: 알림 권한이 이미 허용되어 있습니다.")

                    // 추가 권한 상태 확인
                    val notificationManager =
                        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                    val areNotificationsEnabled = notificationManager.areNotificationsEnabled()
                    Log.d("🔥 MainActivity", "🔍 디버깅: 시스템 알림 활성화: $areNotificationsEnabled")
                }
                else -> {
                    Log.d("🔥 MainActivity", "⚠️ 디버깅: 알림 권한이 없습니다.")
                    Log.d("🔥 MainActivity", "📱 디버깅: 알림 권한을 요청합니다.")

                    try {
                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        Log.d("🔥 MainActivity", "🔍 디버깅: 권한 요청 런처 실행 완료")
                    } catch (e: Exception) {
                        Log.e("🔥 MainActivity", "❌ 디버깅: 권한 요청 실패: ${e.message}", e)
                    }
                }
            }
        } else {
            Log.d("🔥 MainActivity", "✅ 디버깅: Android 13 미만 - 알림 권한 요청 불필요")

            // Android 13 미만에서도 알림 상태 확인
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val areNotificationsEnabled = notificationManager.areNotificationsEnabled()
            Log.d("🔥 MainActivity", "🔍 디버깅: 시스템 알림 활성화: $areNotificationsEnabled")
        }

        Log.d("🔥 MainActivity", "🔍 디버깅: === 알림 권한 확인 완료 ===")
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
