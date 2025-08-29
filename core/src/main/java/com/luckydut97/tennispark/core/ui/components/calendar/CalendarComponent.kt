package com.luckydut97.tennispark.core.ui.components.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.luckydut97.tennispark.core.utils.CalendarUtils
import java.time.LocalDate
import java.time.YearMonth

/**
 * 메인 달력 컴포넌트
 * 헤더, 요일, 구분선, 날짜 그리드를 모두 포함
 */
@Composable
fun CalendarComponent(
    currentYearMonth: YearMonth,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 달력 데이터 생성
    val calendarData = remember(currentYearMonth) {
        CalendarUtils.generateCalendarData(currentYearMonth)
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // 달력 헤더 (년월 + 화살표)
        CalendarHeader(
            currentYearMonth = currentYearMonth,
            onPreviousMonth = onPreviousMonth,
            onNextMonth = onNextMonth
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 요일 헤더
        CalendarWeekHeader()

        Spacer(modifier = Modifier.height(9.dp))

        // 구분선 (BackgroundColor, 1dp 높이)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFFF4F6F8)) // BackgroundColor 추정값
        )

        Spacer(modifier = Modifier.height(9.dp))

        // 달력 그리드
        CalendarGrid(
            calendarData = calendarData,
            selectedDate = selectedDate,
            onDateSelected = onDateSelected
        )
    }
}