package com.luckydut97.feature.attendance.data.repository

import com.luckydut97.feature.attendance.domain.model.AttendanceResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AttendanceRepository {
    suspend fun checkInWithQrCode(qrCode: String): Flow<AttendanceResult> = flow {
        // 실제 구현에서는 API 호출
        delay(1000) // 네트워크 요청 시뮬레이션

        if (qrCode.startsWith("TENNIS_PARK_")) {
            emit(
                AttendanceResult(
                    success = true,
                    message = "출석체크가 완료되었습니다!",
                    points = 100,
                    attendanceTime = "2024-01-14 10:30"
                )
            )
        } else {
            emit(
                AttendanceResult(
                    success = false,
                    message = "유효하지 않은 QR 코드입니다.",
                    points = null,
                    attendanceTime = null
                )
            )
        }
    }
}