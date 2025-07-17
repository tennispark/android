package com.luckydut97.feature_home.main.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.luckydut97.tennispark.core.ui.components.button.TennisParkCancelButton
import com.luckydut97.tennispark.core.ui.components.button.TennisParkConfirmButton
import com.luckydut97.tennispark.core.ui.theme.AppColors
import com.luckydut97.tennispark.core.ui.theme.Pretendard

@Composable
fun PhotoUploadConfirmDialog(
    selectedImageUri: Uri?,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (selectedImageUri != null) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                modifier = Modifier
                    .width(354.dp)
                    .height(360.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 타이틀
                    Text(
                        text = "활동인증",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Pretendard,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 확인 텍스트
                    Text(
                        text = "선택한 사진을 업로드 하시겠습니까?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = Pretendard,
                        color = AppColors.CaptionColor,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.4.sp,
                        letterSpacing = (-1).sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // 선택한 사진 미리보기
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(selectedImageUri),
                            contentDescription = "선택한 사진",
                            modifier = Modifier.size(150.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 버튼 영역
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TennisParkCancelButton(
                            text = "취소",
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f)
                        )
                        TennisParkConfirmButton(
                            text = "확인",
                            onClick = onConfirm,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}