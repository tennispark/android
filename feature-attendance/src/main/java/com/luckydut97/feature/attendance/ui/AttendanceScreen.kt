package com.luckydut97.feature.attendance.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luckydut97.tennispark.core.ui.components.navigation.TopBar
import com.luckydut97.tennispark.core.ui.theme.AppColors
import com.luckydut97.feature.attendance.ui.components.CameraPreview
import com.luckydut97.feature.attendance.ui.components.QrScannerOverlay
import com.luckydut97.feature.attendance.viewmodel.AttendanceViewModel
import com.luckydut97.tennispark.core.ui.theme.Pretendard

@Composable
fun AttendanceScreen(
    onBackClick: () -> Unit,
    onAttendanceComplete: () -> Unit = {},
    viewModel: AttendanceViewModel = viewModel()
) {
    val tag = "🔍 디버깅: AttendanceScreen"

    val uiState by viewModel.uiState.collectAsState()
    
    // 카메라 권한 상태를 저장할 변수 추가
    var hasCameraPermission by remember { mutableStateOf(false) }
    var permissionChecked by remember { mutableStateOf(false) }

    // UI 상태 로깅
    
    Scaffold(
        containerColor = Color.Black, // 배경색을 검은색으로 변경
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            TopBar(
                title = "출석체크",
                onBackClick = {
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
            CameraPreview(
                onQrCodeScanned = { qrCode ->
                    viewModel.processQrCode(qrCode)
                },
                onPermissionGranted = { isGranted ->
                    hasCameraPermission = isGranted
                    permissionChecked = true
                },
                onPermissionDenied = {
                    onBackClick()
                }
            )

            // QR 스캐너 오버레이 - 카메라 권한이 있을 때만 표시
            if (hasCameraPermission) {
                QrScannerOverlay()
            }

            // 로딩 표시
            if (uiState.isLoading) {
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

            // 에러 메시지 표시
            uiState.errorMessage?.let { errorMessage ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.padding(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (errorMessage.contains("이미 출석 체크된")) "알림" else "오류",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = errorMessage,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    viewModel.clearError()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AppColors.Primary
                                ),
                                shape = RoundedCornerShape(5.dp)
                            ) {
                                Text("확인")
                            }
                        }
                    }
                }
            }

            // 성공 메시지 표시
            if (uiState.showSuccessDialog) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.padding(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "성공!",
                                style = MaterialTheme.typography.headlineSmall,
                                color = AppColors.Primary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = uiState.successMessage,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    viewModel.dismissSuccessDialog()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AppColors.Primary
                                ),
                                shape = RoundedCornerShape(5.dp)
                            ) {
                                Text("확인")
                            }
                        }
                    }
                }
            }
        }
    }

    // QR 코드 처리 결과 확인
    LaunchedEffect(uiState.showSuccessDialog) {
        if (uiState.showSuccessDialog) {
            // 성공 시 잠시 후 화면 닫기
            kotlinx.coroutines.delay(1500)
            onAttendanceComplete()
        }
    }
}
