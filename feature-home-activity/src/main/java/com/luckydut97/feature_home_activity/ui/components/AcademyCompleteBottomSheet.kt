package com.luckydut97.feature_home_activity.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.tennispark.core.ui.theme.Pretendard

/**
 * 아카데미 신청 완료 Bottom Sheet
 * 크기: fillMaxWidth × 253dp
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcademyCompleteBottomSheet(
    isVisible: Boolean,
    onConfirm: () -> Unit
) {
    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onConfirm,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(253.dp)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // 제목
                Text(
                    text = "신청 완료",
                    fontSize = 20.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 설명 텍스트
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "아카데미 신청이 완료되었습니다.",
                        fontSize = 16.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "입금계좌 정보를 안내드리겠습니다.",
                        fontSize = 16.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // 확인 버튼
                Button(
                    onClick = onConfirm,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF145F44)
                    )
                ) {
                    Text(
                        text = "확인",
                        fontSize = 16.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(36.dp))
            }
        }
    }
}