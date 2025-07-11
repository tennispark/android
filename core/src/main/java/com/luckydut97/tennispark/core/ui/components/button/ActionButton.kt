package com.luckydut97.tennispark.core.ui.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.tennispark.core.ui.theme.AppColors
import com.luckydut97.tennispark.core.ui.components.animation.PressableComponent

@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = AppColors.ButtonGreen,
    contentColor: Color = Color.White
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(47.dp),
        enabled = enabled,
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled) backgroundColor else AppColors.DisabledButton,
            contentColor = contentColor,
            disabledContainerColor = AppColors.DisabledButton,
            disabledContentColor = Color.White
        ),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = if (enabled) Color.White else AppColors.UnselectedText,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
fun SmallActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = AppColors.ButtonGreen,
    contentColor: Color = Color.White,
    borderColor: Color? = null,
    textColor: Color? = null,
    fontSize: Int = 16,
    fontWeight: FontWeight = FontWeight.SemiBold
) {
    val finalTextColor = textColor ?: if (enabled) {
        contentColor
    } else {
        AppColors.UnselectedText
    }

    if (borderColor != null && backgroundColor == Color.White) {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier.height(47.dp),
            enabled = enabled,
            shape = RoundedCornerShape(6.dp),
            border = BorderStroke(1.dp, borderColor),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = backgroundColor,
                contentColor = contentColor
            ),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Text(
                text = text,
                fontSize = fontSize.sp,
                color = finalTextColor,
                fontWeight = fontWeight,
                maxLines = 1
            )
        }
    } else {
        Button(
            onClick = onClick,
            modifier = modifier.height(47.dp),
            enabled = enabled,
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (enabled) backgroundColor else AppColors.DisabledButton,
                contentColor = contentColor,
                disabledContainerColor = AppColors.DisabledButton,
                disabledContentColor = Color.White
            ),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Text(
                text = text,
                fontSize = fontSize.sp,
                color = finalTextColor,
                fontWeight = fontWeight,
                maxLines = 1
            )
        }
    }
}
