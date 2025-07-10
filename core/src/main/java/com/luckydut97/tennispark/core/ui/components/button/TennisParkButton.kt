package com.luckydut97.tennispark.core.ui.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.tennispark.core.ui.theme.AppColors
import com.luckydut97.tennispark.core.ui.theme.Pretendard

/**
 * 테니스파크 공통 버튼 컴포넌트
 * 다이얼로그 및 기타 용도로 사용
 */
@Composable
fun TennisParkButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = AppColors.ButtonGreen,
    contentColor: Color = Color.White,
    borderColor: Color = AppColors.ButtonGreen,
    borderWidth: Dp = 1.dp,
    fontWeight: FontWeight = FontWeight.Bold
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(47.dp)
            .border(
                width = borderWidth,
                color = if (enabled) borderColor else borderColor.copy(alpha = 0.5f),
                shape = RoundedCornerShape(6.dp)
            ),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContainerColor = backgroundColor.copy(alpha = 0.5f),
            disabledContentColor = contentColor.copy(alpha = 0.5f)
        )
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = fontWeight,
            fontFamily = Pretendard,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * 취소 버튼 (흰색 배경, 회색 테두리, 검은색 텍스트, Normal 폰트)
 */
@Composable
fun TennisParkCancelButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    borderColor: Color = Color(0xFFCECECE),
    borderWidth: Dp = 1.dp
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(47.dp)
            .border(
                width = borderWidth,
                color = if (enabled) borderColor else borderColor.copy(alpha = 0.5f),
                shape = RoundedCornerShape(6.dp)
            ),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.White.copy(alpha = 0.5f),
            disabledContentColor = Color.Black.copy(alpha = 0.5f)
        )
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = Pretendard,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

// export 누락될 가능성 방지 - 파일 맨 아래에 export 확정
@Composable
fun TennisParkConfirmButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    TennisParkButton(
        text = text,
        onClick = onClick,
        modifier = modifier.height(47.dp),
        enabled = enabled,
        backgroundColor = AppColors.ButtonGreen,
        contentColor = Color.White,
        borderColor = AppColors.ButtonGreen,
        borderWidth = 1.dp,
        fontWeight = FontWeight.Bold,
    )
}