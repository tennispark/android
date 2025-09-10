package com.luckydut97.feature_home_activity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckydut97.tennispark.core.domain.model.WeeklyActivity
import com.luckydut97.tennispark.core.domain.usecase.ApplyForActivityUseCase
import com.luckydut97.tennispark.core.domain.usecase.GetActivitiesUseCase
import com.luckydut97.tennispark.core.utils.CalendarUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

/**
 * 활동 신청 화면 전용 ViewModel
 * 달력 상태 관리 + 날짜별 활동 필터링
 */
class ActivityApplicationViewModel(
    private val getActivitiesUseCase: GetActivitiesUseCase,
    private val applyForActivityUseCase: ApplyForActivityUseCase
) : ViewModel() {

    // 현재 표시 중인 년월
    private val _currentYearMonth = MutableStateFlow(YearMonth.now())
    val currentYearMonth: StateFlow<YearMonth> = _currentYearMonth.asStateFlow()

    // 선택된 날짜 (기본값: 오늘)
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    // 전체 활동 목록
    private val _allActivities = MutableStateFlow<List<WeeklyActivity>>(emptyList())
    val allActivities: StateFlow<List<WeeklyActivity>> = _allActivities.asStateFlow()

    // 선택된 날짜의 활동 목록 (필터링된)
    private val _filteredActivities = MutableStateFlow<List<WeeklyActivity>>(emptyList())
    val filteredActivities: StateFlow<List<WeeklyActivity>> = _filteredActivities.asStateFlow()

    // 로딩 상태
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // 에러 상태
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // 선택된 활동 (상세 다이얼로그용)
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

    init {
        // 선택된 날짜가 변경될 때마다 활동 목록 필터링
        viewModelScope.launch {
            combine(
                _allActivities,
                _selectedDate
            ) { activities, selectedDate ->
                activities.filter { activity ->
                    activity.date == selectedDate
                }
            }.collect { filtered ->
                _filteredActivities.value = filtered
            }
        }

        // 초기 데이터 로드
        loadActivities()
    }

    /**
     * 이전 달로 이동
     */
    fun goToPreviousMonth() {
        _currentYearMonth.value = _currentYearMonth.value.minusMonths(1)
    }

    /**
     * 다음 달로 이동
     */
    fun goToNextMonth() {
        _currentYearMonth.value = _currentYearMonth.value.plusMonths(1)
    }

    /**
     * 날짜 선택
     */
    fun selectDate(date: LocalDate) {
        _selectedDate.value = date

        // 선택된 날짜가 다른 달이면 달력도 이동
        val selectedYearMonth = YearMonth.from(date)
        if (selectedYearMonth != _currentYearMonth.value) {
            _currentYearMonth.value = selectedYearMonth
        }
    }

    /**
     * 활동 목록 로드
     */
    private fun loadActivities() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                getActivitiesUseCase().collect { activities ->
                    _allActivities.value = activities
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
            } finally {
                _isLoading.value = false
            }
        }
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
        _isDuplicateError.value = false
        hideDetailDialog()
    }

    /**
     * 활동 신청
     */
    fun applyForActivity(activityId: String) {
        viewModelScope.launch {
            try {
                val result = applyForActivityUseCase(activityId)

                result.fold(
                    onSuccess = {
                        hideDetailDialog()
                        showCompleteDialog()
                        loadActivities() // 목록 새로고침
                    },
                    onFailure = { exception ->
                        val errorMessage = exception.message ?: "신청에 실패했습니다."
                        if (errorMessage.contains("HTTP_500") ||
                            errorMessage.contains("알 수 없는 오류") ||
                            errorMessage.contains("이미 신청한 활동입니다")
                        ) {
                            _isDuplicateError.value = true
                            hideDetailDialog()
                            showCompleteDialog()
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
                    hideDetailDialog()
                    showCompleteDialog()
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

    /**
     * 활동 목록 새로고침
     */
    fun refreshActivities() {
        loadActivities()
    }
}