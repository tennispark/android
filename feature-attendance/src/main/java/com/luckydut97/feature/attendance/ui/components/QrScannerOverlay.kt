package com.luckydut97.feature.attendance.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun QrScannerOverlay(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            drawOverlay()
        }
    }
}

private fun DrawScope.drawOverlay() {
    val canvasWidth = size.width
    val canvasHeight = size.height
    val scanAreaSize = minOf(canvasWidth, canvasHeight) * 0.7f
    val cornerRadius = 20.dp.toPx()

    val scanAreaLeft = (canvasWidth - scanAreaSize) / 2
    val scanAreaTop = (canvasHeight - scanAreaSize) / 2

    // 어두운 오버레이 그리기
    drawRect(
        color = Color.Black.copy(alpha = 0.6f),
        size = size
    )

    // 스캔 영역 투명하게 만들기
    drawRoundRect(
        color = Color.Transparent,
        topLeft = Offset(scanAreaLeft, scanAreaTop),
        size = Size(scanAreaSize, scanAreaSize),
        cornerRadius = CornerRadius(cornerRadius),
        blendMode = BlendMode.Clear
    )

    // 스캔 영역 테두리 그리기
    drawRoundRect(
        color = Color.White,
        topLeft = Offset(scanAreaLeft, scanAreaTop),
        size = Size(scanAreaSize, scanAreaSize),
        cornerRadius = CornerRadius(cornerRadius),
        style = Stroke(width = 3.dp.toPx())
    )
}
