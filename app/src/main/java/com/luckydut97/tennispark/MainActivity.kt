package com.luckydut97.tennispark

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
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
import com.luckydut97.tennispark.core.data.storage.NotificationPreferenceManager
import com.luckydut97.tennispark.core.fcm.FcmTokenManager
import com.luckydut97.tennispark.core.fcm.NotificationHelper
import com.luckydut97.tennispark.core.fcm.MyFirebaseMessagingService
import com.luckydut97.tennispark.core.ui.theme.TennisParkTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val fcmTokenManager = FcmTokenManager()
    private val notificationPreferenceManager by lazy {
        NotificationPreferenceManager(this)
    }

    // 알림 권한 요청 런처
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // 권한이 허용되면 푸시 알림 설정 상태 다시 확인
            checkPushNotificationSettings()
        } else {
            // 권한이 거부되면 푸시 알림 설정을 false로 업데이트
            notificationPreferenceManager.setPushNotificationEnabled(false)
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val configuration = Configuration(newBase?.resources?.configuration)
        // 최대 폰트 스케일 1.3배로 제한
        configuration.fontScale = minOf(configuration.fontScale ?: 1.0f, 1.05f)
        super.attachBaseContext(newBase?.createConfigurationContext(configuration))
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

        // 푸시 알림 설정 확인
        checkPushNotificationSettings()

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
     * 앱이 포그라운드로 돌아올 때 호출됩니다
     */
    override fun onResume() {
        super.onResume()

        // HomeTopAppBar에 배지 갱신 알림 (브로드캐스트)
        sendBadgeRefreshBroadcast()
    }

    /**
     * HomeTopAppBar에 배지 갱신을 알리는 브로드캐스트 전송
     */
    private fun sendBadgeRefreshBroadcast() {
        val intent = Intent(MyFirebaseMessagingService.ACTION_NOTIFICATION_RECEIVED)
        sendBroadcast(intent)
    }

    /**
     * 푸시 알림 설정 상태 확인 및 로그 출력
     */
    private fun checkPushNotificationSettings() {
        val isPushEnabled = notificationPreferenceManager.isPushNotificationEnabled()
        val isFirstTime = notificationPreferenceManager.isFirstTime()

        // 시스템 알림 권한 확인
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val areNotificationsEnabled = notificationManager.areNotificationsEnabled()

        // Android 13+ 권한 확인
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * 알림 권한 요청 (Android 13+)
     */
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val currentPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            )

            when {
                currentPermission == PackageManager.PERMISSION_GRANTED -> {
                    // 추가 권한 상태 확인
                    val notificationManager =
                        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                    val areNotificationsEnabled = notificationManager.areNotificationsEnabled()
                }
                else -> {
                    try {
                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } catch (e: Exception) {
                    }
                }
            }
        } else {
            // Android 13 미만에서도 알림 상태 확인
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val areNotificationsEnabled = notificationManager.areNotificationsEnabled()
        }
    }

    /**
     * FCM 토큰을 확인하고 로그로 출력 (디버깅용)
     */
    private fun checkFcmToken() {
        lifecycleScope.launch {
            try {
                // FCM 토큰 발급 시도
                val fcmToken = fcmTokenManager.getFcmToken()

                if (fcmToken != null) {
                    // 토큰 유효성 검사
                    if (fcmTokenManager.isValidFcmToken(fcmToken)) {
                        // 푸시 알림 설정 확인
                        val isPushEnabled =
                            notificationPreferenceManager.isPushNotificationEnabled()

                        // 실제 서버 전송 여부 결정
                        val tokenToSend = if (isPushEnabled) fcmToken else ""

                        // 서버로 토큰 전송 (로그인 상태일 때만)
                        checkAndSendFcmTokenToServer(tokenToSend)
                    }
                }
            } catch (e: Exception) {
            }
        }
    }

    /**
     * FCM 토큰을 서버로 전송 (로그인 상태 확인 후)
     */
    private fun checkAndSendFcmTokenToServer(fcmToken: String) {
        lifecycleScope.launch {
            try {
                // TokenManager 및 AuthRepository 초기화
                val tokenManager =
                    com.luckydut97.tennispark.core.data.storage.TokenManagerImpl(this@MainActivity)
                val authRepository =
                    com.luckydut97.tennispark.core.data.repository.AuthRepositoryImpl(
                        apiService = NetworkModule.apiService,
                        tokenManager = tokenManager
                    )

                // 로그인 상태 확인
                val isLoggedIn = authRepository.isLoggedIn()

                if (isLoggedIn) {
                    val response = authRepository.updateFcmToken(fcmToken)
                }
            } catch (e: Exception) {
            }
        }
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
