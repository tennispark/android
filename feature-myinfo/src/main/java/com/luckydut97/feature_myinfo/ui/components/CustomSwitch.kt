package com.luckydut97.feature_myinfo.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.luckydut97.tennispark.core.ui.theme.AppColors.AssistiveColor
import com.luckydut97.tennispark.core.ui.theme.AppColors.ButtonGreen

@Composable
fun CustomSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val trackWidth = 55.dp
    val trackHeight = 32.dp
    val thumbSize = 24.dp
    val thumbOffsetEnd = trackWidth - thumbSize - 4.dp // 양쪽 여백 4dp로 맞춤
    val thumbOffset = if (checked) thumbOffsetEnd else 4.dp // 4dp는 시작 여백
    val animatedOffset by animateDpAsState(targetValue = thumbOffset, label = "SwitchThumbOffset")

    val trackColor = if (checked) ButtonGreen else AssistiveColor

    Box(
        modifier = modifier
            .width(trackWidth)
            .height(trackHeight)
            .clip(RoundedCornerShape(16.dp))
            .background(trackColor)
            .clickable(
                enabled = enabled,
                onClick = { if (enabled) onCheckedChange(!checked) }
            )
            .semantics { contentDescription = if (checked) "On" else "Off" },
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = animatedOffset)
                .size(thumbSize)
                .clip(CircleShape)
                .background(Color.White)
        )
    }
}
