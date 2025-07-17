package com.luckydut97.tennispark.core.ui.components.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.tennispark.core.ui.theme.AppColors.ButtonGreen
import com.luckydut97.tennispark.core.ui.theme.Pretendard

@Composable
fun BasicButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    applyPadding: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(47.dp)
            .let { if (applyPadding) it.padding(horizontal = 17.dp) else it },
        colors = ButtonDefaults.buttonColors(
            containerColor = ButtonGreen
        ),
        shape = RoundedCornerShape(8.dp),
        enabled = enabled
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = Pretendard
        )
    }
}