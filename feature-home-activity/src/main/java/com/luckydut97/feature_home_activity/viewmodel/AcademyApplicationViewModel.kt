package com.luckydut97.feature_home_activity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckydut97.feature_home_activity.data.model.Academy
import com.luckydut97.feature_home_activity.data.repository.AcademyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

/**
 * 아카데미 신청을 위한 ViewModel
 */
class AcademyApplicationViewModel(
    private val repository: AcademyRepository
) : ViewModel() {

    private val tag = "🔍 디버깅: AcademyApplicationViewModel"

    // 아카데미 목록
    private val _academies = MutableStateFlow<List<Academy>>(emptyList())
    val academies: StateFlow<List<Academy>> = _academies.asStateFlow()

    // 로딩 상태
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // 에러 상태
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // 바텀시트 표시 상태
    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet.asStateFlow()

    // 상세 다이얼로그 표시 상태
    private val _showDetailDialog = MutableStateFlow(false)
    val showDetailDialog: StateFlow<Boolean> = _showDetailDialog.asStateFlow()

    // 완료 다이얼로그 표시 상태
    private val _showCompleteDialog = MutableStateFlow(false)
    val showCompleteDialog: StateFlow<Boolean> = _showCompleteDialog.asStateFlow()

    // 중복 신청 에러 상태
    private val _isDuplicateError = MutableStateFlow(false)
    val isDuplicateError: StateFlow<Boolean> = _isDuplicateError.asStateFlow()

    // 선택된 아카데미
    private val _selectedAcademy = MutableStateFlow<Academy?>(null)
    val selectedAcademy: StateFlow<Academy?> = _selectedAcademy.asStateFlow()

    init {
        Log.d(tag, "AcademyApplicationViewModel 초기화")
        loadAcademies()
    }

    /**
     * 아카데미 목록 로드
     */
    private fun loadAcademies() {
        Log.d(tag, "=== 아카데미 목록 로드 시작 ===")
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                repository.getAcademies().collect { academyList ->
                    _academies.value = academyList
                    _isLoading.value = false
                    Log.d(tag, "✅ 아카데미 목록 로드 성공: ${academyList.size}개")
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "알 수 없는 오류가 발생했습니다."
                _isLoading.value = false
                Log.e(tag, "❌ 아카데미 목록 로드 실패: ${e.message}", e)
            }
        }
    }

    /**
     * 아카데미 신청 바텀시트 표시
     */
    fun showAcademyApplicationSheet() {
        Log.d(tag, "아카데미 신청 바텀시트 표시")
        _showBottomSheet.value = true
        loadAcademies() // 바텀시트 열 때마다 최신 데이터 로드
    }

    /**
     * 아카데미 신청 바텀시트 숨기기
     */
    fun hideAcademyApplicationSheet() {
        Log.d(tag, "아카데미 신청 바텀시트 숨기기")
        _showBottomSheet.value = false
    }

    /**
     * 아카데미 선택 및 상세 다이얼로그 표시
     */
    fun selectAcademyAndShowDetail(academy: Academy) {
        Log.d(tag, "아카데미 선택: ${academy.id}")
        _selectedAcademy.value = academy
        _showDetailDialog.value = true
    }

    /**
     * 상세 다이얼로그 숨기기
     */
    fun hideDetailDialog() {
        Log.d(tag, "상세 다이얼로그 숨기기")
        _showDetailDialog.value = false
        _selectedAcademy.value = null
    }

    /**
     * 아카데미 신청
     */
    fun applyForAcademy(academyId: String) {
        Log.d(tag, "=== 아카데미 신청 시작: $academyId ===")
        viewModelScope.launch {
            try {
                val result = repository.applyForAcademy(academyId)
                result.fold(
                    onSuccess = { message ->
                        Log.d(tag, "✅ 아카데미 신청 성공: $message")
                        _showDetailDialog.value = false
                        _showCompleteDialog.value = true
                        loadAcademies() // 신청 후 목록 새로고침
                    },
                    onFailure = { exception ->
                        Log.e(tag, "❌ 아카데미 신청 실패: ${exception.message}")
                        val errorMessage = exception.message ?: "신청 중 오류가 발생했습니다."
                        if (errorMessage.contains("서버 오류가 발생했습니다") || errorMessage.contains("알 수 없는 오류")) {
                            Log.d(tag, "🔄 서버 오류 감지 - 중복 신청으로 처리")
                            _isDuplicateError.value = true
                            _showDetailDialog.value = false
                            _showCompleteDialog.value = true
                        } else {
                            _error.value = errorMessage
                            _showDetailDialog.value = false
                        }
                    }
                )
            } catch (e: Exception) {
                Log.e(tag, "❌ 아카데미 신청 예외: ${e.message}", e)
                val errorMessage = e.message ?: "신청 중 오류가 발생했습니다."
                if (errorMessage.contains("서버 오류가 발생했습니다") || errorMessage.contains("알 수 없는 오류")) {
                    Log.d(tag, "🔄 서버 오류 감지 - 중복 신청으로 처리")
                    _isDuplicateError.value = true
                    _showDetailDialog.value = false
                    _showCompleteDialog.value = true
                } else {
                    _error.value = errorMessage
                    _showDetailDialog.value = false
                }
            }
        }
    }

    /**
     * 완료 다이얼로그 숨기기
     */
    fun hideCompleteDialog() {
        Log.d(tag, "완료 다이얼로그 숨기기")
        _showCompleteDialog.value = false
        _isDuplicateError.value = false // 중복 에러 상태 초기화
    }

    /**
     * 에러 메시지 초기화
     */
    fun clearError() {
        _error.value = null
    }
}
