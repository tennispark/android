package com.luckydut97.tennispark.core.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 공통 이미지 제거 버튼. 원형 배경 위에 X 문자를 표시한다.
 */
@Composable
fun RemoveImageButton(
    modifier: Modifier = Modifier,
    size: Dp = DEFAULT_REMOVE_BUTTON_SIZE,
    backgroundColor: Color = Color.Black.copy(alpha = 0.5f),
    contentColor: Color = Color.White,
    onClick: () -> Unit
) {
    val density = LocalDensity.current
    val textSize = remember(size) {
        with(density) { (size * 0.55f).toSp() }
    }

    Box(
        modifier = modifier
            .size(size)
            .background(backgroundColor, CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.Text(
            text = "×",
            color = contentColor,
            fontSize = textSize,
            fontWeight = FontWeight.Bold
        )
    }
}

private val DEFAULT_REMOVE_BUTTON_SIZE = 54.dp
