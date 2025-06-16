package com.luckydut97.feature_home_activity.data.repository

import com.luckydut97.feature_home_activity.data.model.Academy
import com.luckydut97.feature_home_activity.data.model.AcademyStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * 아카데미 데이터를 위한 Mock Repository
 */
class MockAcademyRepository {

    /**
     * 아카데미 목록을 가져오는 함수
     */
    fun getAcademies(): Flow<List<Academy>> = flow {
        // 네트워크 지연 시뮬레이션
        delay(1000)
        
        emit(mockAcademies)
    }

    /**
     * 특정 아카데미에 신청하는 함수
     */
    suspend fun applyForAcademy(academyId: String): Result<String> {
        // 네트워크 지연 시뮬레이션
        delay(1500)
        
        val academy = mockAcademies.find { it.id == academyId }
        
        return if (academy != null && academy.status == AcademyStatus.AVAILABLE) {
            Result.success("아카데미 신청이 완료되었습니다.")
        } else {
            Result.failure(Exception("이미 마감된 아카데미입니다."))
        }
    }

    companion object {
        private val mockAcademies = listOf(
            Academy(
                id = "academy_1",
                date = "05.13 (화)",
                time = "20:00 ~ 22:00",
                court = "게임코트",
                location = "양재 테니스코트",
                activityType = "A클래스",
                currentParticipants = 1,
                maxParticipants = 6,
                status = AcademyStatus.AVAILABLE
            ),
            Academy(
                id = "academy_2",
                date = "05.13 (화)",
                time = "20:00 ~ 22:00",
                court = "게임코트",
                location = "양재 테니스코트",
                activityType = "A클래스",
                currentParticipants = 5,
                maxParticipants = 6,
                status = AcademyStatus.FULL
            ),
            Academy(
                id = "academy_3",
                date = "05.13 (화)",
                time = "20:00 ~ 22:00",
                court = "게임코트",
                location = "양재 테니스코트",
                activityType = "A클래스",
                currentParticipants = 5,
                maxParticipants = 6,
                status = AcademyStatus.FULL
            )
        )
    }
}
