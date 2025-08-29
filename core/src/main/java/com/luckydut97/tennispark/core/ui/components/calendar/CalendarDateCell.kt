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
 * 이번주+다음주 선택 가능, 오늘 날짜 시각적 구분, 정확한 색상 규칙 적용
 */
@Composable
fun CalendarDateCell(
    date: LocalDate,
    isCurrentMonth: Boolean,
    isSelected: Boolean,
    onDateClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val isInSelectableRange = CalendarUtils.isInSelectableRange(date)
    val isSunday = date.dayOfWeek == DayOfWeek.SUNDAY
    val isHoliday = CalendarUtils.isKoreanHoliday(date)
    val isToday = date == LocalDate.now()

    // 선택 가능 여부: 현재 달 + 선택 가능한 범위(이번주+다음주)
    val isSelectable = isCurrentMonth && isInSelectableRange

    // 배경 원이 있는지 여부 (선택된 날짜만)
    val hasCircleBackground = isSelected && isInSelectableRange

    // 날짜 텍스트 색상 결정
    val textColor = when {
        !isCurrentMonth -> Color.Transparent // 다른 달 날짜는 투명
        hasCircleBackground -> Color.White // 원형 배경이 있으면 흰색
        !isInSelectableRange -> Color(0xFFDADADA) // 선택 범위가 아닌 날짜
        isSunday || isHoliday -> Color(0xFFEF3629) // 선택 가능한 일요일/공휴일
        else -> Color(0xFF6F6F6F) // 선택 가능한 평일/토요일
    }

    // 글자 굵기
    val fontWeight = if (hasCircleBackground) FontWeight.Bold else FontWeight.Normal

    Box(
        modifier = modifier.size(40.dp), // 40x40 크기
        contentAlignment = Alignment.Center
    ) {
        if (isSelectable) {
            // 선택 가능한 날짜만 클릭 가능
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
            // 선택 불가능한 날짜는 그냥 텍스트만
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