package com.luckydut97.tennispark.core.ui.components.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.tennispark.core.ui.components.animation.PressableComponent
import com.luckydut97.tennispark.core.ui.theme.Pretendard
import com.luckydut97.tennispark.core.utils.CalendarUtils
import java.time.DayOfWeek
import java.time.LocalDate

/**
 * 달력 날짜 셀 컴포넌트
 * 새로운 활성화/비활성화 색상 규칙 적용
 * 모든 날짜 선택 가능, 비활성화 날짜는 알림 표시
 */
@Composable
fun CalendarDateCell(
    date: LocalDate,
    isCurrentMonth: Boolean,
    isSelected: Boolean,
    hasActivity: Boolean = false, // 해당 날짜에 활동이 있는지 여부
    onDateClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val isSunday = date.dayOfWeek == DayOfWeek.SUNDAY
    val isHoliday = CalendarUtils.isKoreanHoliday(date)
    val isInActiveRange = CalendarUtils.isInActiveDateRange(date)

    // 모든 현재 달 날짜 선택 가능
    val isSelectable = isCurrentMonth

    // 선택된 날짜는 원형 배경
    val hasCircleBackground = isSelected

    // 날짜 텍스트 색상 결정
    val textColor = when {
        !isCurrentMonth -> Color.Transparent // 다른 달 날짜는 투명
        hasCircleBackground -> Color.White // 선택된 날짜는 흰색

        // 활성화된 날짜
        isInActiveRange -> {
            when {
                !hasActivity -> Color(0xFFDADADA) // 활성화이지만 활동 없음
                isSunday || isHoliday -> Color(0xFFEF3629) // 활동 있는 일요일/공휴일
                else -> Color(0xFF6F6F6F) // 활동 있는 평일/토요일
            }
        }

        // 비활성화된 날짜 (과거 또는 미래)
        else -> Color(0xFFDADADA)
    }

    // 글자 굵기
    val fontWeight = if (hasCircleBackground) FontWeight.Bold else FontWeight.Normal

    Box(
        modifier = modifier.size(40.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isSelectable) {
            // 모든 현재 달 날짜 클릭 가능
            PressableComponent(
                onClick = { onDateClick(date) },
                scaleDown = 0.85f,
                animationDurationMillis = 150
            ) {
                Box(
                    modifier = Modifier.size(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // 원형 배경 (선택된 날짜)
                    if (hasCircleBackground) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF145F44))
                        )
                    }

                    // 날짜 텍스트
                    Text(
                        text = date.dayOfMonth.toString(),
                        fontSize = 18.sp,
                        fontFamily = Pretendard,
                        fontWeight = fontWeight,
                        color = textColor
                    )
                }
            }
        } else {
            // 다른 달 날짜는 텍스트만 (투명)
            if (isCurrentMonth) {
                Text(
                    text = date.dayOfMonth.toString(),
                    fontSize = 18.sp,
                    fontFamily = Pretendard,
                    fontWeight = fontWeight,
                    color = textColor
                )
            }
        }
    }
}