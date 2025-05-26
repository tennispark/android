package com.luckydut97.feature_home_activity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckydut97.feature_home_activity.data.repository.AppliedActivityRepository
import com.luckydut97.feature_home_activity.domain.model.AppliedActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 신청한 활동(활동인증) ViewModel - 단순 버전
 */
class AppliedActivityViewModel(
    private val repository: AppliedActivityRepository
) : ViewModel() {

    // Bottom Sheet 표시 상태
    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet.asStateFlow()

    // 신청한 활동 목록 상태
    private val _appliedActivities = MutableStateFlow<List<AppliedActivity>>(emptyList())
    val appliedActivities: StateFlow<List<AppliedActivity>> = _appliedActivities.asStateFlow()

    // 로딩 상태
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadAppliedActivities()
    }

    /**
     * Bottom Sheet 표시
     */
    fun showAppliedActivitiesSheet() {
        _showBottomSheet.value = true
        loadAppliedActivities() // 최신 데이터 로드
    }

    /**
     * Bottom Sheet 숨김
     */
    fun hideAppliedActivitiesSheet() {
        _showBottomSheet.value = false
    }

    /**
     * 신청한 활동 목록 로드
     */
    private fun loadAppliedActivities() {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                repository.getAppliedActivities().collect { activities ->
                    _appliedActivities.value = activities
                }
            } catch (e: Exception) {
                // 에러 발생 시 빈 리스트로 설정
                _appliedActivities.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * 데이터 새로고침
     */
    fun refresh() {
        loadAppliedActivities()
    }
}