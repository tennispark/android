package com.luckydut97.feature.attendance.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luckydut97.tennispark.core.ui.components.navigation.TopBar
import com.luckydut97.tennispark.core.ui.theme.AppColors
import com.luckydut97.feature.attendance.ui.components.CameraPreview
import com.luckydut97.feature.attendance.ui.components.QrScannerOverlay
import com.luckydut97.feature.attendance.viewmodel.AttendanceViewModel

@Composable
fun AttendanceScreen(
    onBackClick: () -> Unit,
    onAttendanceComplete: () -> Unit = {},
    viewModel: AttendanceViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // 카메라 권한 상태를 저장할 변수 추가
    var hasCameraPermission by remember { mutableStateOf(false) }
    var permissionChecked by remember { mutableStateOf(false) }
    var hasNavigatedBack by remember { mutableStateOf(false) }

    // 디버깅 로그
    Log.d("카메라 디버깅:", "AttendanceScreen Composable called")
    Log.d("카메라 디버깅:", "AttendanceScreen - uiState: $uiState")
    
    // 권한이 거부되었을 때 뒤로가기 (한 번만 실행)
    LaunchedEffect(hasCameraPermission, permissionChecked) {
        if (permissionChecked && !hasCameraPermission && !hasNavigatedBack) {
            Log.d("카메라 디버깅:", "AttendanceScreen - Permission denied, going back")
            hasNavigatedBack = true
            onBackClick()
        }
    }

    Scaffold(
        containerColor = Color.Black, // 배경색을 검은색으로 변경
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            TopBar(
                title = "출석체크",
                onBackClick = {
                    Log.d("카메라 디버깅:", "AttendanceScreen - TopBar back clicked")
                    onBackClick()
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black) // 명시적으로 검은색 배경
        ) {
            // 카메라 프리뷰
            Log.d("카메라 디버깅:", "AttendanceScreen - Calling CameraPreview")
            CameraPreview(
                onQrCodeScanned = { qrCode ->
                    Log.d("카메라 디버깅:", "AttendanceScreen - QR Code scanned: $qrCode")
                    viewModel.processQrCode(qrCode)
                },
                onPermissionGranted = { isGranted ->
                    Log.d("카메라 디버깅:", "AttendanceScreen - Permission granted: $isGranted")
                    hasCameraPermission = isGranted
                    permissionChecked = true
                }
            )

            // QR 스캐너 오버레이 - 카메라 권한이 있을 때만 표시
            if (hasCameraPermission) {
                Log.d("카메라 디버깅:", "AttendanceScreen - Showing QrScannerOverlay")
                QrScannerOverlay()
            }

            // 로딩 표시
            if (uiState.isLoading) {
                Log.d("카메라 디버깅:", "AttendanceScreen - Showing loading indicator")
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = AppColors.Primary
                    )
                }
            }
        }
    }

    // QR 코드 처리 결과 확인
    LaunchedEffect(uiState.showSuccessDialog) {
        Log.d(
            "카메라 디버깅:",
            "AttendanceScreen - LaunchedEffect - showSuccessDialog: ${uiState.showSuccessDialog}"
        )
        if (uiState.showSuccessDialog) {
            // 성공 시 잠시 후 화면 닫기
            kotlinx.coroutines.delay(1500)
            onAttendanceComplete()
        }
    }
}
