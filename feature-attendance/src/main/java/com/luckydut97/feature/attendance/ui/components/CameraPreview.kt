package com.luckydut97.feature.attendance.ui.components

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.luckydut97.tennispark.core.ui.theme.AppColors
import kotlinx.coroutines.delay

@Composable
fun CameraPreview(
    onQrCodeScanned: (String) -> Unit,
    modifier: Modifier = Modifier,
    onPermissionGranted: (Boolean) -> Unit = {},
    onPermissionDenied: () -> Unit = {}
) {
    val tag = "🔍 디버깅: CameraPreview"

    val context = LocalContext.current
    
    // 초기 권한 상태 확인
    val initialPermission = remember {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        hasPermission
    }
    
    var hasCameraPermission by remember { mutableStateOf(initialPermission) }

    // 권한 요청 launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (!isGranted) {
            onPermissionDenied()
        }
    }

    // 권한 상태가 변경될 때마다 콜백 호출
    LaunchedEffect(hasCameraPermission) {
        onPermissionGranted(hasCameraPermission)
    }

    // 초기 권한 요청
    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            delay(300) // UI 안정화를 위한 지연
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
                CameraContent(
                    onQrCodeScanned = { qrCode ->
                        onQrCodeScanned(qrCode)
                    }
                )
            }
            else -> {
                CircularProgressIndicator(
                    color = AppColors.Primary
                )
            }
        }
    }
}

@Composable
private fun CameraContent(
    onQrCodeScanned: (String) -> Unit
) {
    val tag = "🔍 디버깅: CameraContent"

    // 실제 카메라 프리뷰
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 🔥 실제 QR 스캔 카메라 뷰 (MLKit 사용)
        QrScannerCameraView(
            modifier = Modifier.fillMaxSize(),
            onQrCodeDetected = { qrCode ->
                onQrCodeScanned(qrCode)
            }
        )
    }
}