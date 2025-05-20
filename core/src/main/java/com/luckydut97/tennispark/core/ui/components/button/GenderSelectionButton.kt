package com.luckydut97.tennispark.core.ui.components.button

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.tennispark.core.ui.theme.AppColors
import com.luckydut97.tennispark.core.ui.theme.Pretendard

@Composable
fun GenderSelectionButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(47.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(
                width = 1.dp,
                color = if (isSelected) AppColors.Primary else AppColors.Divider,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable(onClick = onClick)
            .let {
                if (isSelected) {
                    it.border(
                        width = 1.dp,
                        color = AppColors.Primary,
                        shape = RoundedCornerShape(4.dp)
                    )
                } else {
                    it
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) AppColors.Primary else AppColors.TextPrimary,
            fontFamily = Pretendard
        )
    }
}