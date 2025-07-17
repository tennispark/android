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
import com.luckydut97.tennispark.core.ui.theme.AppColors.CaptionColor
import com.luckydut97.tennispark.core.utils.rememberImagePickerLauncher
import com.luckydut97.tennispark.core.utils.rememberPermissionHelper
import com.luckydut97.tennispark.feature.home.R
import com.luckydut97.tennispark.core.ui.theme.Pretendard
import com.luckydut97.tennispark.core.ui.components.button.BasicButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoUploadBottomSheet(
    viewModel: PhotoUploadViewModel,
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val selectedImageUri by viewModel.selectedImageUri.collectAsState()
    val showConfirmDialog by viewModel.showConfirmDialog.collectAsState()
    val isUploading by viewModel.isUploading.collectAsState()
    val uploadSuccess by viewModel.uploadSuccess.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val imagePickerLauncher = rememberImagePickerLauncher { uri ->
        viewModel.onImageSelected(uri)
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
                    color = CaptionColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 업로드 버튼
                BasicButton(
                    text = "사진 업로드",
                    onClick = {
                        requestPermission()
                    },
                    enabled = !isUploading,
                    applyPadding = false
                )

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

    // 사진 업로드 확인 다이얼로그
    if (showConfirmDialog) {
        PhotoUploadConfirmDialog(
            selectedImageUri = selectedImageUri,
            onDismiss = {
                viewModel.onConfirmDialogDismiss()
            },
            onConfirm = {
                viewModel.onConfirmUpload(context)
            }
        )
    }
}