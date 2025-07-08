package com.luckydut97.feature_home.main.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.feature_home.main.viewmodel.PhotoUploadViewModel
import com.luckydut97.tennispark.core.ui.theme.AppColors.ButtonGreen
import com.luckydut97.tennispark.core.utils.rememberImagePickerLauncher
import com.luckydut97.tennispark.core.utils.rememberPermissionHelper
import com.luckydut97.tennispark.feature.home.R
import com.luckydut97.tennispark.core.ui.theme.Pretendard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoUploadBottomSheet(
    viewModel: PhotoUploadViewModel,
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val selectedImageUri by viewModel.selectedImageUri.collectAsState()
    val isUploading by viewModel.isUploading.collectAsState()
    val uploadSuccess by viewModel.uploadSuccess.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val imagePickerLauncher = rememberImagePickerLauncher { uri ->
        viewModel.onImageSelected(uri)
    }

    LaunchedEffect(selectedImageUri) {
        selectedImageUri?.let {
            viewModel.onUploadClick(context)
        }
    }

    val requestPermission = rememberPermissionHelper(
        onPermissionGranted = {
            viewModel.onPermissionGranted()
            imagePickerLauncher.launch("image/*")
        },
        onPermissionDenied = {
            viewModel.onPermissionDenied()
        },
        onPermissionPermanentlyDenied = {
            viewModel.onPermissionPermanentlyDenied()
        }
    )

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(208.dp)
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Header Row - 제목과 닫기 버튼
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(24.dp))

                    Text(
                        text = "활동 인증",
                        fontSize = 18.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(15.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_exit_btn),
                            contentDescription = "닫기",
                            modifier = Modifier.size(15.dp),
                            tint = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 설명문
                Text(
                    text = "인스타그램 스토리 또는 피드 후기를\n업로드할 사진을 선택해 주세요",
                    fontSize = 16.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF8B9096),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))



                // 업로드 섹션
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    // 좌측 박스
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(45.dp)
                            .background(
                                color = Color(0xFFF2FAF4),
                                shape = RoundedCornerShape(
                                    topStart = 8.dp,
                                    bottomStart = 8.dp,
                                    topEnd = 0.dp,
                                    bottomEnd = 0.dp
                                )
                            )
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        when {
                            isUploading -> {
                                Text(
                                    text = "업로드 중...",
                                    fontSize = 15.sp,
                                    fontFamily = Pretendard,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4CAF50)
                                )
                            }
                            selectedImageUri != null -> {
                                Text(
                                    text = "이미지 업로드 완료",//이미지 선택됨
                                    fontSize = 15.sp,
                                    fontFamily = Pretendard,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                            else -> {
                                Text(
                                    text = "사진을 업로드 해주세요",
                                    fontSize = 15.sp,
                                    fontFamily = Pretendard,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }
                    }

                    Button(
                        onClick = {
                            requestPermission()
                        },
                        modifier = Modifier
                            .width(87.dp)
                            .height(45.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ButtonGreen
                        ),
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            bottomStart = 0.dp,
                            topEnd = 8.dp,
                            bottomEnd = 8.dp
                        ),
                        contentPadding = PaddingValues(0.dp),
                        enabled = !isUploading
                    ) {
                        if (isUploading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "업로드",
                                fontSize = 15.sp,
                                fontFamily = Pretendard,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                maxLines = 1
                            )
                        }
                    }
                }

                // 에러 메시지
                errorMessage?.let { message ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = message,
                        fontSize = 14.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFFE57373),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}