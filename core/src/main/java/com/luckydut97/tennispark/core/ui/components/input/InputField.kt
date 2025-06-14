package com.luckydut97.tennispark.core.ui.components.input

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.tennispark.core.ui.theme.AppColors
import com.luckydut97.tennispark.core.ui.theme.Pretendard

@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    isRequired: Boolean = false,
    placeholder: String = "",
    enabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column(modifier = modifier) {
        if (label.isNotEmpty()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    color = AppColors.TextPrimary,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.Bold
                )
                if (isRequired) {
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "*",
                        fontSize = 14.sp,
                        color = AppColors.Required,
                        fontFamily = Pretendard
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = if (enabled) AppColors.TextPrimary else AppColors.TextSecondary,
                fontFamily = Pretendard
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            visualTransformation = visualTransformation,
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(47.dp)
                        .background(
                            color = if (enabled) Color.White else AppColors.InputDisabledBackground,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .border(1.dp, AppColors.Divider, RoundedCornerShape(4.dp))
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            fontSize = 15.sp,
                            color = if (enabled) AppColors.TextHint else AppColors.TextDisabledPlaceholder,
                            fontFamily = Pretendard
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

@Composable
fun VerificationCodeField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    BasicTextField(
        value = value,
        onValueChange = { if (it.length <= 6) onValueChange(it) },
        enabled = enabled,
        textStyle = TextStyle(
            fontSize = 16.sp,
            color = AppColors.TextPrimary,
            fontFamily = Pretendard
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        modifier = modifier,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(47.dp)
                    .background(Color.White, RoundedCornerShape(4.dp))
                    .border(1.dp, AppColors.Divider, RoundedCornerShape(4.dp))
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                innerTextField()
            }
        }
    )
}

@Composable
fun VerificationCodeFieldWithTimer(
    value: String,
    onValueChange: (String) -> Unit,
    remainingTime: Int,
    isTimerActive: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    BasicTextField(
        value = value,
        onValueChange = { if (it.length <= 6) onValueChange(it) },
        enabled = enabled,
        textStyle = TextStyle(
            fontSize = 16.sp,
            color = AppColors.TextPrimary,
            fontFamily = Pretendard
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        modifier = modifier,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(47.dp)
                    .background(Color.White, RoundedCornerShape(4.dp))
                    .border(1.dp, AppColors.InputDisabledBackground, RoundedCornerShape(6.dp))
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        innerTextField()
                    }
                    
                    if (isTimerActive) {
                        Spacer(modifier = Modifier.width(17.dp))
                        val minutes = remainingTime / 60
                        val seconds = remainingTime % 60
                        Text(
                            text = String.format("%02d:%02d", minutes, seconds),
                            color = Color(0xFF145F44),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
            }
        }
    )
}
