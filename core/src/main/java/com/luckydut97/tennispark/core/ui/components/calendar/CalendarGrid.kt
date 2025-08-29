package com.luckydut97.tennispark.core.ui.components.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.luckydut97.tennispark.core.utils.CalendarDateInfo
import java.time.LocalDate

/**
 * 달력 그리드 컴포넌트
 * 날짜들을 7열로 배치하고 28dp 간격 적용
 */
@Composable
fun CalendarGrid(
    calendarData: List<CalendarDateInfo>,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(28.dp), // 세로 간격
        horizontalArrangement = Arrangement.spacedBy(0.dp) // 가로는 균등 분배
    ) {
        items(calendarData) { dateInfo ->
            CalendarDateCell(
                date = dateInfo.date,
                isCurrentMonth = dateInfo.isCurrentMonth,
                isSelected = selectedDate == dateInfo.date,
                onDateClick = { date ->
                    onDateSelected(date)
                }
            )
        }
    }
}