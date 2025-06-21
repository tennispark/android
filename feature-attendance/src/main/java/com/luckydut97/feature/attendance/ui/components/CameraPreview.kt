package com.luckydut97.feature.attendance.ui.components

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
    val tag = "🔍 디버깅: CameraPreview"
    Log.d(tag, "CameraPreview Composable 호출됨")

    val context = LocalContext.current
    
    // 초기 권한 상태 확인
    val initialPermission = remember {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        Log.d(tag, "초기 카메라 권한 상태: $hasPermission")
        hasPermission
    }
    
    var hasCameraPermission by remember { mutableStateOf(initialPermission) }
    var hasRequestedPermission by remember { mutableStateOf(false) }
    
    // 권한 상태가 변경될 때마다 콜백 호출
    LaunchedEffect(hasCameraPermission) {
        Log.d(tag, "카메라 권한 상태 변경됨: $hasCameraPermission")
        onPermissionGranted(hasCameraPermission)
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        Log.d(tag, "권한 요청 결과: $isGranted")
        hasCameraPermission = isGranted
        hasRequestedPermission = true
    }
    
    // 권한이 없고 아직 요청하지 않았으면 바로 요청
    LaunchedEffect(hasCameraPermission, hasRequestedPermission) {
        if (!hasCameraPermission && !hasRequestedPermission) {
            Log.d(tag, "카메라 권한 자동 요청 시작")
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
                Log.d(tag, "카메라 권한 있음 - CameraContent 표시")
                // 카메라 권한이 있는 경우
                CameraContent(
                    onQrCodeScanned = { qrCode ->
                        Log.d(tag, "🎯 QR 코드 스캔됨: $qrCode")
                        onQrCodeScanned(qrCode)
                    }
                )
            }
            !hasRequestedPermission -> {
                Log.d(tag, "권한 요청 중...")
                // 권한 요청 중
                CircularProgressIndicator(
                    color = AppColors.Primary
                )
            }
            else -> {
                Log.d(tag, "카메라 권한 거부됨")
                // 권한이 거부된 경우
                PermissionDeniedContent()
            }
        }
    }
}

@Composable
private fun CameraContent(
    onQrCodeScanned: (String) -> Unit
) {
    val tag = "🔍 디버깅: CameraContent"
    Log.d(tag, "CameraContent 표시됨")

    // 실제 카메라 프리뷰
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 🔥 실제 QR 스캔 카메라 뷰 (MLKit 사용)
        QrScannerCameraView(
            modifier = Modifier.fillMaxSize(),
            onQrCodeDetected = { qrCode ->
                Log.d(tag, "📷 실제 카메라에서 QR 인식: $qrCode")
                onQrCodeScanned(qrCode)
            }
        )
    }
}

@Composable
private fun PermissionDeniedContent() {
    val tag = "🔍 디버깅: PermissionDenied"
    Log.d(tag, "권한 거부 화면 표시")

    Card(
        modifier = Modifier
            .padding(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "카메라 권한이 필요합니다",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "QR 코드를 스캔하려면\n카메라 권한을 허용해주세요",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}
