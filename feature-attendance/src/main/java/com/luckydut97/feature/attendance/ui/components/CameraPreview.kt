package com.luckydut97.feature.attendance.ui.components

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.luckydut97.tennispark.core.ui.theme.AppColors
import kotlinx.coroutines.delay

@Composable
fun CameraPreview(
    onQrCodeScanned: (String) -> Unit,
    modifier: Modifier = Modifier,
    onPermissionGranted: (Boolean) -> Unit = {}
) {
    Log.d("카메라 디버깅:", "CameraPreview Composable called")

    val context = LocalContext.current
    
    // 초기 권한 상태 확인
    val initialPermission = remember {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    var hasCameraPermission by remember { mutableStateOf(initialPermission) }
    var hasRequestedPermission by remember { mutableStateOf(false) }
    
    // 권한 상태가 변경될 때마다 콜백 호출
    LaunchedEffect(hasCameraPermission) {
        Log.d("카메라 디버깅:", "CameraPreview - Permission state changed: $hasCameraPermission")
        onPermissionGranted(hasCameraPermission)
    }

    Log.d("카메라 디버깅:", "CameraPreview - hasCameraPermission: $hasCameraPermission")

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        Log.d("카메라 디버깅:", "CameraPreview - Permission result: $isGranted")
        hasCameraPermission = isGranted
        hasRequestedPermission = true
    }
    
    // 권한이 없고 아직 요청하지 않았으면 바로 요청
    LaunchedEffect(hasCameraPermission, hasRequestedPermission) {
        if (!hasCameraPermission && !hasRequestedPermission) {
            Log.d("카메라 디버깅:", "CameraPreview - Auto requesting permission")
            delay(100) // 짧은 지연으로 UI가 준비될 시간 제공
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        when {
            hasCameraPermission -> {
                Log.d("카메라 디버깅:", "CameraPreview - Showing CameraContent")
                // 카메라 권한이 있는 경우
                CameraContent(onQrCodeScanned = onQrCodeScanned)
            }
            !hasRequestedPermission -> {
                // 권한 요청 중
                CircularProgressIndicator(
                    color = AppColors.Primary
                )
            }
            else -> {
                // 권한이 거부된 경우 (이미 AttendanceScreen에서 처리됨)
                Log.d("카메라 디버깅:", "CameraPreview - Permission denied")
            }
        }
    }
}

@Composable
private fun CameraContent(
    onQrCodeScanned: (String) -> Unit
) {
    Log.d("카메라 디버깅:", "CameraContent Composable called")
    var showSimulationButton by remember { mutableStateOf(true) }

    // 실제 카메라 프리뷰
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 실제 카메라 뷰
        SimpleCameraView(
            modifier = Modifier.fillMaxSize()
        )

        // 테스트용 시뮬레이션 버튼 (오버레이로 표시)
        if (showSimulationButton) {
            Button(
                onClick = {
                    Log.d("카메라 디버깅:", "CameraContent - Simulation button clicked")
                    showSimulationButton = false
                    // 테스트용 QR 코드 값 전송
                    onQrCodeScanned("TEST_QR_CODE_2024_ATTENDANCE")
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 100.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.Primary
                )
            ) {
                Text("QR 코드 인식 시뮬레이션")
            }
        }
    }

    // 버튼을 다시 표시하기 위한 지연
    LaunchedEffect(showSimulationButton) {
        if (!showSimulationButton) {
            Log.d("카메라 디버깅:", "CameraContent - Waiting 3 seconds to show button again")
            delay(3000) // 3초 후 버튼 다시 표시
            showSimulationButton = true
        }
    }
}
