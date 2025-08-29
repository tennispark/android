package com.luckydut97.tennispark.core.ui.components.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.luckydut97.tennispark.core.ui.theme.Pretendard

/**
 * 달력 요일 헤더 컴포넌트
 * 일월화수목금토 표시
 */
@Composable
fun CalendarWeekHeader(
    modifier: Modifier = Modifier
) {
    val weekDays = listOf("일", "월", "화", "수", "목", "금", "토")

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        weekDays.forEach { dayOfWeek ->
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = dayOfWeek,
                    fontSize = 18.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF8B9096) // 기존 AssistiveColor 사용
                )
            }
        }
    }
}