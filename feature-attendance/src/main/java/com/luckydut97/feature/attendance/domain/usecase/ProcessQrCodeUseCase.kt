package com.luckydut97.feature.attendance.domain.usecase

import com.luckydut97.feature.attendance.data.repository.AttendanceRepository
import com.luckydut97.feature.attendance.domain.model.AttendanceResult
import kotlinx.coroutines.flow.Flow

class ProcessQrCodeUseCase(
    private val repository: AttendanceRepository
) {
    suspend operator fun invoke(qrCode: String): Flow<AttendanceResult> {
        return repository.checkInWithQrCode(qrCode)
    }
}
