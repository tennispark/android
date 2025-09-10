package com.luckydut97.tennispark.core.ui.components.calendar

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.luckydut97.tennispark.core.utils.CalendarDateInfo
import java.time.LocalDate

/**
 * 달력 그리드 컴포넌트
 * 4주차, 5주차, 6주차 무조건 284dp 고정 높이
 * Box + offset을 사용한 절대 위치 배치로 정확한 간격 제어
 */
@Composable
fun CalendarGrid(
    calendarData: List<CalendarDateInfo>,
    selectedDate: LocalDate?,
    activitiesDateSet: Set<LocalDate> = emptySet(), // 활동이 있는 날짜들
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val weekCount = calendarData.size / 7
    val weeks = calendarData.chunked(7)

    // 고정값
    val fixedCalendarHeight = 274.dp
    val cellHeight = 40.dp

    Log.d("🔍 CalendarGrid", "=== 달력 디버깅 시작 ===")
    Log.d("🔍 CalendarGrid", "calendarData.size = ${calendarData.size}")
    Log.d("🔍 CalendarGrid", "weekCount = $weekCount")
    Log.d("🔍 CalendarGrid", "weeks.size = ${weeks.size}")
    Log.d("🔍 CalendarGrid", "fixedCalendarHeight = $fixedCalendarHeight")
    Log.d("🔍 CalendarGrid", "cellHeight = $cellHeight")

    // 동적 간격 계산
    val dynamicSpacing = remember(weekCount) {
        val totalCellHeight = weekCount * cellHeight.value
        val availableSpacingHeight = fixedCalendarHeight.value - totalCellHeight
        val spacingCount = weekCount - 1

        Log.d("🔍 CalendarGrid", "--- 간격 계산 ---")
        Log.d(
            "🔍 CalendarGrid",
            "totalCellHeight = $weekCount × ${cellHeight.value} = $totalCellHeight"
        )
        Log.d(
            "🔍 CalendarGrid",
            "availableSpacingHeight = ${fixedCalendarHeight.value} - $totalCellHeight = $availableSpacingHeight"
        )
        Log.d("🔍 CalendarGrid", "spacingCount = $weekCount - 1 = $spacingCount")

        val spacing = if (spacingCount > 0) {
            (availableSpacingHeight / spacingCount).dp
        } else {
            0.dp
        }

        Log.d(
            "🔍 CalendarGrid",
            "dynamicSpacing = $availableSpacingHeight ÷ $spacingCount = $spacing"
        )
        Log.d("🔍 CalendarGrid", "--- 간격 계산 완료 ---")

        spacing
    }

    // 284dp 고정 컨테이너 + 절대 위치 배치
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(fixedCalendarHeight) // 강제 284dp
    ) {
        Log.d("🔍 CalendarGrid", "--- 주차별 위치 계산 ---")

        weeks.forEachIndexed { weekIndex, weekDates ->
            // 각 주차의 정확한 Y 위치 계산
            val yOffset = (weekIndex * (cellHeight.value + dynamicSpacing.value)).dp

            Log.d(
                "🔍 CalendarGrid",
                "Week $weekIndex: yOffset = $weekIndex × (${cellHeight.value} + ${dynamicSpacing.value}) = ${yOffset}"
            )
            Log.d("🔍 CalendarGrid", "Week $weekIndex: weekDates.size = ${weekDates.size}")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cellHeight)
                    .offset(y = yOffset), // 절대 위치 지정
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                weekDates.forEach { dateInfo ->
                    CalendarDateCell(
                        date = dateInfo.date,
                        isCurrentMonth = dateInfo.isCurrentMonth,
                        isSelected = selectedDate == dateInfo.date,
                        hasActivity = activitiesDateSet.contains(dateInfo.date), // 활동이 있는지 표시
                        onDateClick = onDateSelected
                    )
                }
            }
        }

        // 최종 검증 계산
        val lastWeekIndex = weeks.size - 1
        val finalYOffset = lastWeekIndex * (cellHeight.value + dynamicSpacing.value)
        val totalUsedHeight = finalYOffset + cellHeight.value

        Log.d("🔍 CalendarGrid", "--- 최종 검증 ---")
        Log.d("🔍 CalendarGrid", "lastWeekIndex = $lastWeekIndex")
        Log.d(
            "🔍 CalendarGrid",
            "finalYOffset = $lastWeekIndex × (${cellHeight.value} + ${dynamicSpacing.value}) = $finalYOffset"
        )
        Log.d(
            "🔍 CalendarGrid",
            "totalUsedHeight = $finalYOffset + ${cellHeight.value} = $totalUsedHeight"
        )
        Log.d("🔍 CalendarGrid", "fixedCalendarHeight = ${fixedCalendarHeight.value}")
        Log.d("🔍 CalendarGrid", "차이 = ${fixedCalendarHeight.value - totalUsedHeight}")
        Log.d("🔍 CalendarGrid", "=== 달력 디버깅 완료 ===")
    }
}