package com.luckydut97.feature_community.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.luckydut97.tennispark.core.ui.theme.AppColors
import com.luckydut97.tennispark.core.ui.theme.Pretendard

@Composable
fun ReportDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var reason by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .size(width = 345.dp, height = 377.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .padding(horizontal = 18.dp, vertical = 24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "이 콘텐츠를 신고하시겠습니까?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = (-0.5).sp,
                    color = AppColors.CaptionColor,
                    fontFamily = Pretendard
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "운영진이 검토 후 규정 위반 시\n삭제 및 필요한 조치를 진행합니다.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = (-0.5).sp,
                    color = AppColors.CaptionColor,
                    fontFamily = Pretendard
                )

                Spacer(modifier = Modifier.height(18.dp))

                Box(
                    modifier = Modifier
                        .width(309.dp)
                        .height(95.dp)
                        .border(1.dp, Color(0xFF8B9096), RoundedCornerShape(6.dp))
                        .padding(12.dp)
                ) {
                    BasicTextField(
                        value = reason,
                        onValueChange = { reason = it },
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState),
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF202020),
                            fontFamily = Pretendard,
                            letterSpacing = (-0.5).sp,
                            lineHeight = 22.sp
                        ),
                        cursorBrush = SolidColor(Color(0xFF1C7756))
                    )

                    if (reason.isEmpty()) {
                        Text(
                            text = "신고 사유를 작성해주세요",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF8B9096),
                            fontFamily = Pretendard,
                            letterSpacing = (-0.5).sp,
                            modifier = Modifier.align(Alignment.TopStart)
                        )
                    }
                }

                LaunchedEffect(reason) {
                    if (scrollState.maxValue > 0) {
                        scrollState.animateScrollTo(scrollState.maxValue)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                TextButton(
                    onClick = {
                        onConfirm(reason)
                    },
                    modifier = Modifier
                        .width(309.dp)
                        .height(45.dp)
                        .border(1.dp, Color(0x00145F44), RoundedCornerShape(6.dp)),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = Color(0xFF145F44),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "확인",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Pretendard
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .width(309.dp)
                        .height(45.dp)
                        .border(1.dp, Color(0xFFCECECE), RoundedCornerShape(6.dp)),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF000000)
                    ),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "취소",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Pretendard
                    )
                }
            }
        }
    }
}
