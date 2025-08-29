package com.luckydut97.tennispark.core.utils

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

/**
 * 달력 관련 유틸리티 함수들
 */
object CalendarUtils {

    /**
     * 이번주 범위 계산 (일요일 시작)
     */
    fun getThisWeekRange(): Pair<LocalDate, LocalDate> {
        val today = LocalDate.now()
        val startOfWeek = today.with(DayOfWeek.SUNDAY)
        val endOfWeek = startOfWeek.plusDays(6)
        return startOfWeek to endOfWeek
    }

    /**
     * 선택 가능한 날짜 범위 계산 (이번주 + 다음주)
     * 오늘부터 다음주 일요일까지
     */
    fun getSelectableDateRange(): Pair<LocalDate, LocalDate> {
        val today = LocalDate.now()
        val nextSunday = today.with(DayOfWeek.SUNDAY).plusWeeks(1)
        return today to nextSunday
    }

    /**
     * 특정 날짜가 선택 가능한 범위에 포함되는지 확인
     * (이번주 + 다음주)
     */
    fun isInSelectableRange(date: LocalDate): Boolean {
        val (startDate, endDate) = getSelectableDateRange()
        return !date.isBefore(startDate) && !date.isAfter(endDate)
    }

    /**
     * 특정 날짜가 이번주에 포함되는지 확인
     */
    fun isInThisWeek(date: LocalDate): Boolean {
        val (startOfWeek, endOfWeek) = getThisWeekRange()
        return !date.isBefore(startOfWeek) && !date.isAfter(endOfWeek)
    }

    /**
     * 한국 공휴일 여부 확인 (고정 공휴일만)
     */
    fun isKoreanHoliday(date: LocalDate): Boolean {
        val monthDay = date.format(DateTimeFormatter.ofPattern("MM-dd"))

        // 고정 공휴일
        val fixedHolidays = setOf(
            "01-01", // 신정
            "03-01", // 삼일절
            "05-05", // 어린이날
            "06-06", // 현충일
            "08-15", // 광복절
            "10-03", // 개천절
            "10-09", // 한글날
            "12-25"  // 크리스마스
        )

        if (fixedHolidays.contains(monthDay)) {
            return true
        }

        // 특정 연도의 추가 공휴일 (예시)
        val year = date.year
        val specificHolidays = when (year) {
            2024 -> setOf(
                "02-09", "02-10", "02-11", "02-12", // 설날 연휴
                "04-10", // 국회의원선거일
                "05-06", // 대체휴일
                "09-16", "09-17", "09-18" // 추석 연휴
            )

            2025 -> setOf(
                "01-28", "01-29", "01-30", // 설날 연휴
                "05-05", // 어린이날
                "10-05", "10-06", "10-07" // 추석 연휴 (예상)
            )

            else -> emptySet()
        }

        return specificHolidays.contains(monthDay)
    }

    /**
     * 달력 그리드용 날짜 데이터 생성
     */
    fun generateCalendarData(yearMonth: YearMonth): List<CalendarDateInfo> {
        val firstDayOfMonth = yearMonth.atDay(1)
        val lastDayOfMonth = yearMonth.atEndOfMonth()
        val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // 일요일을 0으로 맞춤

        val calendarData = mutableListOf<CalendarDateInfo>()

        // 이전 달 날짜들
        val prevMonth = yearMonth.minusMonths(1)
        val prevMonthLastDay = prevMonth.atEndOfMonth()
        for (i in firstDayOfWeek - 1 downTo 0) {
            calendarData.add(
                CalendarDateInfo(
                    date = prevMonthLastDay.minusDays(i.toLong()),
                    isCurrentMonth = false
                )
            )
        }

        // 현재 달 날짜들
        var currentDate = firstDayOfMonth
        while (currentDate <= lastDayOfMonth) {
            calendarData.add(
                CalendarDateInfo(
                    date = currentDate,
                    isCurrentMonth = true
                )
            )
            currentDate = currentDate.plusDays(1)
        }

        // 다음 달 날짜들 (6주 완성)
        val nextMonth = yearMonth.plusMonths(1)
        var nextMonthDate = nextMonth.atDay(1)
        while (calendarData.size < 42) { // 6주 * 7일 = 42
            calendarData.add(
                CalendarDateInfo(
                    date = nextMonthDate,
                    isCurrentMonth = false
                )
            )
            nextMonthDate = nextMonthDate.plusDays(1)
        }

        return calendarData
    }

    /**
     * 년월 표시용 포맷팅
     */
    fun formatYearMonth(yearMonth: YearMonth): String {
        return "${yearMonth.year}년 ${yearMonth.monthValue}월"
    }
}

/**
 * 달력 날짜 정보 데이터 클래스
 */
data class CalendarDateInfo(
    val date: LocalDate,
    val isCurrentMonth: Boolean
)