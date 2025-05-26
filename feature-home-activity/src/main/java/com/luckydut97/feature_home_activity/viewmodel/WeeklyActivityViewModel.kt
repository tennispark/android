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
        loadWeeklyActivities()
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
     * 활동 신청 (API 호출 준비)
     */
    fun applyForActivity(activityId: String) {
        viewModelScope.launch {
            try {
                // TODO: 서버 API 준비되면 실제 구현
                val result = repository.applyForActivity(activityId)

                if (result.isSuccess) {
                    // 신청 성공 시
                    hideDetailDialog() // 상세 다이얼로그 닫기
                    showCompleteDialog() // 완료 다이얼로그 표시
                    loadWeeklyActivities() // 목록 새로고침
                } else {
                    _error.value = "신청에 실패했습니다."
                }
            } catch (e: Exception) {
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