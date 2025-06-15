package com.luckydut97.tennispark.feature_auth.signup.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.TextUnit
import com.luckydut97.tennispark.core.ui.theme.AppColors
import com.luckydut97.tennispark.core.ui.theme.AppColors.AssistiveColor
import com.luckydut97.tennispark.core.ui.theme.AppColors.InputDisabledBackground
import com.luckydut97.tennispark.core.ui.theme.Pretendard

@Composable
fun JoinPathButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center, // 기본값은 중앙 정렬
    lineHeight: TextUnit? = null // 줄 간격 파라미터 추가
) {
    Box(
        modifier = modifier
            .height(47.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) AppColors.Primary else Color.White)
            .border(
                width = 1.dp,
                color = if (isSelected) AppColors.Primary else InputDisabledBackground,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 15.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.White else AssistiveColor,
            fontFamily = Pretendard,
            textAlign = textAlign, // 파라미터로 받은 정렬 적용
            lineHeight = lineHeight ?: 15.sp // 줄 간격 적용
        )
    }
}
