package com.luckydut97.feature_home_activity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckydut97.feature_home_activity.data.repository.WeeklyActivityRepository
import com.luckydut97.feature_home_activity.domain.model.WeeklyActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 주간 활동 ViewModel
 */
class WeeklyActivityViewModel(
    private val repository: WeeklyActivityRepository
) : ViewModel() {

    // Bottom Sheet 표시 상태
    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet.asStateFlow()

    // 활동 목록 상태
    private val _activities = MutableStateFlow<List<WeeklyActivity>>(emptyList())
    val activities: StateFlow<List<WeeklyActivity>> = _activities.asStateFlow()

    // 로딩 상태
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // 에러 상태
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // 선택된 활동 상태
    private val _selectedActivity = MutableStateFlow<WeeklyActivity?>(null)
    val selectedActivity: StateFlow<WeeklyActivity?> = _selectedActivity.asStateFlow()

    // 상세 다이얼로그 표시 상태
    private val _showDetailDialog = MutableStateFlow(false)
    val showDetailDialog: StateFlow<Boolean> = _showDetailDialog.asStateFlow()

    // 완료 다이얼로그 표시 상태
    private val _showCompleteDialog = MutableStateFlow(false)
    val showCompleteDialog: StateFlow<Boolean> = _showCompleteDialog.asStateFlow()

    init {
        // 자동 로드 제거 - 사용자가 버튼 클릭할 때만 로드
        // loadWeeklyActivities()
    }

    /**
     * Bottom Sheet 표시
     */
    fun showWeeklyApplicationSheet() {
        _showBottomSheet.value = true
        loadWeeklyActivities() // 최신 데이터 로드
    }

    /**
     * Bottom Sheet 숨김
     */
    fun hideWeeklyApplicationSheet() {
        _showBottomSheet.value = false
    }

    /**
     * 활동 선택 및 상세 다이얼로그 표시
     */
    fun selectActivityAndShowDetail(activity: WeeklyActivity) {
        _selectedActivity.value = activity
        _showDetailDialog.value = true
    }

    /**
     * 상세 다이얼로그 숨김
     */
    fun hideDetailDialog() {
        _showDetailDialog.value = false
        _selectedActivity.value = null
    }

    /**
     * 완료 다이얼로그 표시
     */
    fun showCompleteDialog() {
        _showCompleteDialog.value = true
    }

    /**
     * 완료 다이얼로그 숨김
     */
    fun hideCompleteDialog() {
        _showCompleteDialog.value = false
        // 모든 다이얼로그 닫기
        hideDetailDialog()
        hideWeeklyApplicationSheet()
    }

    /**
     * 주간 활동 목록 로드
     */
    private fun loadWeeklyActivities() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                repository.getWeeklyActivities().collect { activities ->
                    _activities.value = activities
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "알 수 없는 오류가 발생했습니다."
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * 활동 신청 (실제 API 호출)
     */
    fun applyForActivity(activityId: String) {
        android.util.Log.d(
            "WeeklyActivityViewModel",
            "🔥 applyForActivity 함수 호출됨: activityId=$activityId"
        )

        viewModelScope.launch {
            try {
                android.util.Log.d("WeeklyActivityViewModel", "🔄 viewModelScope.launch 시작")

                // 먼저 해당 활동이 신청 가능한지 확인
                val targetActivity = _activities.value.find { it.id == activityId }
                android.util.Log.d("WeeklyActivityViewModel", "🔍 대상 활동 검색 결과: $targetActivity")

                if (targetActivity == null) {
                    android.util.Log.e("WeeklyActivityViewModel", "❌ 활동을 찾을 수 없습니다: $activityId")
                    _error.value = "활동을 찾을 수 없습니다."
                    return@launch
                }

                android.util.Log.d("WeeklyActivityViewModel", "📊 활동 상태: ${targetActivity.status}")
                android.util.Log.d(
                    "WeeklyActivityViewModel",
                    "🆔 실제 ActivityId: ${targetActivity.actualActivityId}"
                )

                if (targetActivity.actualActivityId == null) {
                    android.util.Log.e(
                        "WeeklyActivityViewModel",
                        "❌ 서버 데이터 오류: actualActivityId가 null"
                    )
                    _error.value = "서버 데이터 오류로 인해 현재 신청할 수 없습니다.\n나중에 다시 시도해주세요."
                    return@launch
                }

                if (targetActivity.status == com.luckydut97.feature_home_activity.domain.model.ActivityStatus.UNAVAILABLE) {
                    android.util.Log.e("WeeklyActivityViewModel", "❌ 신청 불가 상태")
                    _error.value = "현재 신청할 수 없는 활동입니다."
                    return@launch
                }

                android.util.Log.d(
                    "WeeklyActivityViewModel",
                    "🚀 Repository.applyForActivityWithId 호출 시작"
                )
                val result = repository.applyForActivityWithId(targetActivity.actualActivityId!!)
                android.util.Log.d("WeeklyActivityViewModel", "📨 Repository 결과: $result")

                if (result.isSuccess) {
                    android.util.Log.d("WeeklyActivityViewModel", "✅ 신청 성공!")
                    // 신청 성공 시
                    hideDetailDialog() // 상세 다이얼로그 닫기
                    showCompleteDialog() // 완료 다이얼로그 표시
                    loadWeeklyActivities() // 목록 새로고침
                } else {
                    val exception = result.exceptionOrNull()
                    val errorMessage = exception?.message ?: "신청에 실패했습니다."
                    android.util.Log.e("WeeklyActivityViewModel", "❌ 신청 실패: $errorMessage")
                    _error.value = errorMessage
                }
            } catch (e: Exception) {
                android.util.Log.e("WeeklyActivityViewModel", "💥 예외 발생: ${e.message}", e)
                _error.value = e.message ?: "신청 중 오류가 발생했습니다."
            }
        }
    }

    /**
     * 에러 상태 초기화
     */
    fun clearError() {
        _error.value = null
    }
}
