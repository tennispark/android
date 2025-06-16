package com.luckydut97.feature.attendance.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.tennispark.core.ui.theme.Pretendard
import com.luckydut97.tennispark.core.ui.theme.AppColors

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

        // 안내 텍스트
        Text(
            text = "QR 코드를 사각형 안에 맞춰주세요",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = Pretendard,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 100.dp)
        )
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

    // 코너 강조 표시
    val cornerLength = 30.dp.toPx()
    val cornerStroke = 5.dp.toPx()

    // 왼쪽 상단
    drawLine(
        color = AppColors.Primary,
        start = Offset(scanAreaLeft, scanAreaTop + cornerRadius),
        end = Offset(scanAreaLeft, scanAreaTop + cornerLength),
        strokeWidth = cornerStroke
    )
    drawLine(
        color = AppColors.Primary,
        start = Offset(scanAreaLeft + cornerRadius, scanAreaTop),
        end = Offset(scanAreaLeft + cornerLength, scanAreaTop),
        strokeWidth = cornerStroke
    )

    // 오른쪽 상단
    drawLine(
        color = AppColors.Primary,
        start = Offset(scanAreaLeft + scanAreaSize, scanAreaTop + cornerRadius),
        end = Offset(scanAreaLeft + scanAreaSize, scanAreaTop + cornerLength),
        strokeWidth = cornerStroke
    )
    drawLine(
        color = AppColors.Primary,
        start = Offset(scanAreaLeft + scanAreaSize - cornerRadius, scanAreaTop),
        end = Offset(scanAreaLeft + scanAreaSize - cornerLength, scanAreaTop),
        strokeWidth = cornerStroke
    )

    // 왼쪽 하단
    drawLine(
        color = AppColors.Primary,
        start = Offset(scanAreaLeft, scanAreaTop + scanAreaSize - cornerRadius),
        end = Offset(scanAreaLeft, scanAreaTop + scanAreaSize - cornerLength),
        strokeWidth = cornerStroke
    )
    drawLine(
        color = AppColors.Primary,
        start = Offset(scanAreaLeft + cornerRadius, scanAreaTop + scanAreaSize),
        end = Offset(scanAreaLeft + cornerLength, scanAreaTop + scanAreaSize),
        strokeWidth = cornerStroke
    )

    // 오른쪽 하단
    drawLine(
        color = AppColors.Primary,
        start = Offset(scanAreaLeft + scanAreaSize, scanAreaTop + scanAreaSize - cornerRadius),
        end = Offset(scanAreaLeft + scanAreaSize, scanAreaTop + scanAreaSize - cornerLength),
        strokeWidth = cornerStroke
    )
    drawLine(
        color = AppColors.Primary,
        start = Offset(scanAreaLeft + scanAreaSize - cornerRadius, scanAreaTop + scanAreaSize),
        end = Offset(scanAreaLeft + scanAreaSize - cornerLength, scanAreaTop + scanAreaSize),
        strokeWidth = cornerStroke
    )
}
