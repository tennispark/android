package com.luckydut97.feature_myinfo.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckydut97.tennispark.core.domain.model.ActivityApplication
import com.luckydut97.tennispark.core.domain.usecase.GetActivityApplicationsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * 활동 신청 내역 ViewModel (Clean Architecture)
 * UseCase 기반
 */
class ActivityHistoryViewModel(
    private val getActivityApplicationsUseCase: GetActivityApplicationsUseCase
) : ViewModel() {

    private val tag = "🔍 ActivityHistoryViewModel"

    // 날짜별 그룹핑된 활동 신청 내역
    private val _groupedApplications =
        MutableStateFlow<Map<LocalDate, List<ActivityApplication>>>(emptyMap())
    val groupedApplications: StateFlow<Map<LocalDate, List<ActivityApplication>>> =
        _groupedApplications.asStateFlow()

    // 로딩 상태
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // 에러 상태
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        Log.d(tag, "[init] ActivityHistoryViewModel initialized")
        loadActivityApplications()
    }

    /**
     * 활동 신청 내역 로드
     */
    fun loadActivityApplications() {
        Log.d(tag, "[loadActivityApplications] called")
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                getActivityApplicationsUseCase().collect { groupedApplications ->
                    Log.d(
                        tag,
                        "[loadActivityApplications] received ${groupedApplications.size} date groups"
                    )
                    _groupedApplications.value = groupedApplications
                }
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("인증") == true -> "인증이 되지 않았습니다. 다시 로그인해주세요."
                    e.message?.contains("네트워크") == true -> "네트워크 연결을 확인해주세요."
                    else -> e.message ?: "활동 신청 내역을 불러올 수 없습니다."
                }
                _error.value = errorMessage
                Log.e(tag, "[loadActivityApplications] error: ${e.message}", e)
            } finally {
                _isLoading.value = false
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
     * 데이터 새로고침
     */
    fun refresh() {
        Log.d(tag, "[refresh] called")
        loadActivityApplications()
    }
}