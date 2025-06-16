package com.luckydut97.feature.attendance.domain.model

data class AttendanceResult(
    val success: Boolean,
    val message: String,
    val points: Int? = null,
    val attendanceTime: String? = null
)