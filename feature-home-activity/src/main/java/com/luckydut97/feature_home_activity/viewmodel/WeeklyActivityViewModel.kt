package com.luckydut97.feature_home_activity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckydut97.tennispark.core.domain.model.WeeklyActivity
import com.luckydut97.tennispark.core.domain.usecase.GetActivitiesUseCase
import com.luckydut97.tennispark.core.domain.usecase.ApplyForActivityUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 주간 활동 ViewModel (Clean Architecture)
 * UseCase 기반으로 리팩토링됨
 */
class WeeklyActivityViewModel(
    private val getActivitiesUseCase: GetActivitiesUseCase,
    private val applyForActivityUseCase: ApplyForActivityUseCase
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

    // 중복 신청 에러 상태
    private val _isDuplicateError = MutableStateFlow(false)
    val isDuplicateError: StateFlow<Boolean> = _isDuplicateError.asStateFlow()

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
        _isDuplicateError.value = false // 중복 에러 상태 초기화
        // 모든 다이얼로그 닫기
        hideDetailDialog()
        hideWeeklyApplicationSheet()
    }

    /**
     * 주간 활동 목록 로드 (UseCase 사용)
     */
    private fun loadWeeklyActivities() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                getActivitiesUseCase().collect { activities ->
                    _activities.value = activities
                }
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("flow was aborted") == true ->
                        "데이터 로딩이 중단되었습니다. 다시 시도해주세요."

                    e.message?.contains("인증") == true ->
                        "인증이 되지 않았습니다. 다시 로그인해주세요."

                    e.message?.contains("네트워크") == true ->
                        "네트워크 연결을 확인해주세요."

                    else -> e.message ?: "알 수 없는 오류가 발생했습니다."
                }
                _error.value = errorMessage
                android.util.Log.e(
                    "WeeklyActivityVM",
                    "loadWeeklyActivities error: ${e.message}",
                    e
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * 활동 신청 (UseCase 사용)
     */
    fun applyForActivity(activityId: String) {
        viewModelScope.launch {
            try {
                val result = applyForActivityUseCase(activityId)

                result.fold(
                    onSuccess = {
                        // 신청 성공 시
                        hideDetailDialog() // 상세 다이얼로그 닫기
                        showCompleteDialog() // 완료 다이얼로그 표시
                        loadWeeklyActivities() // 목록 새로고침
                    },
                    onFailure = { exception ->
                        val errorMessage = exception.message ?: "신청에 실패했습니다."
                        if (errorMessage.contains("HTTP_500") ||
                            errorMessage.contains("알 수 없는 오류") ||
                            errorMessage.contains("이미 신청한 활동입니다")
                        ) {
                            _isDuplicateError.value = true
                            hideDetailDialog() // 상세 다이얼로그 닫기
                            showCompleteDialog() // 완료 다이얼로그 표시 (중복 에러 상태로)
                        } else {
                            _error.value = errorMessage
                        }
                    }
                )
            } catch (e: Exception) {
                val errorMessage = e.message ?: "신청 중 오류가 발생했습니다."
                if (errorMessage.contains("HTTP_500") ||
                    errorMessage.contains("알 수 없는 오류") ||
                    errorMessage.contains("이미 신청한 활동입니다")
                ) {
                    _isDuplicateError.value = true
                    hideDetailDialog() // 상세 다이얼로그 닫기
                    showCompleteDialog() // 완료 다이얼로그 표시 (중복 에러 상태로)
                } else {
                    _error.value = errorMessage
                }
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
