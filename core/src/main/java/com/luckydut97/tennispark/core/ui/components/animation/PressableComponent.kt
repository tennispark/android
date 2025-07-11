package com.luckydut97.tennispark.core.ui.components.animation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput

/**
 * 토스 앱처럼 눌렸을 때 축소되는 애니메이션을 제공하는 컴포넌트
 * @param onClick 클릭 콜백
 * @param modifier 수정자
 * @param scaleDown 눌렸을 때 축소 비율 (기본값: 0.95f)
 * @param animationDurationMillis 애니메이션 지속 시간 (기본값: 150ms)
 * @param enabled 활성화 여부 (기본값: true)
 * @param content 내용 컴포저블
 */
@Composable
fun PressableComponent(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    scaleDown: Float = 0.95f,
    animationDurationMillis: Int = 150,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    // 애니메이션 효과를 위한 스케일 값
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) scaleDown else 1f,
        animationSpec = tween(
            durationMillis = animationDurationMillis,
            delayMillis = 0
        ),
        label = "pressableScale"
    )

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .pointerInput(enabled) {
                if (enabled) {
                    detectTapGestures(
                        onPress = {
                            isPressed = true
                            tryAwaitRelease() // 손가락을 뗄 때까지 기다림
                            isPressed = false
                        },
                        onTap = {
                            onClick()
                        }
                    )
                }
            }
    ) {
        content()
    }
}