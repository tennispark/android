package com.luckydut97.feature_home_activity.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckydut97.tennispark.core.domain.model.Academy
import com.luckydut97.tennispark.core.domain.usecase.GetAcademiesUseCase
import com.luckydut97.tennispark.core.domain.usecase.ApplyForAcademyUseCase
import com.luckydut97.tennispark.core.data.model.Advertisement
import com.luckydut97.tennispark.core.data.model.AdPosition
import com.luckydut97.tennispark.core.data.network.NetworkModule
import com.luckydut97.tennispark.core.data.repository.AdBannerRepository
import com.luckydut97.tennispark.core.data.repository.AdBannerRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 아카데미 신청을 위한 ViewModel (Clean Architecture)
 * UseCase 기반으로 리팩토링됨
 */
class AcademyApplicationViewModel(
    private val getAcademiesUseCase: GetAcademiesUseCase,
    private val applyForAcademyUseCase: ApplyForAcademyUseCase
) : ViewModel() {

    private val tag = "🔍 디버깅: AcademyApplicationViewModel"

    // AdBanner Repository
    private val adBannerRepository: AdBannerRepository by lazy {
        AdBannerRepositoryImpl(NetworkModule.apiService)
    }

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

    // 광고 배너 관련 - ACTIVITY position
    private val _activityAdvertisements = MutableStateFlow<List<Advertisement>>(emptyList())
    val activityAdvertisements: StateFlow<List<Advertisement>> =
        _activityAdvertisements.asStateFlow()

    private val _isLoadingAds = MutableStateFlow(false)
    val isLoadingAds: StateFlow<Boolean> = _isLoadingAds.asStateFlow()

    init {
        Log.d(tag, "[init] AcademyApplicationViewModel initialized")
        loadAcademies()
        loadActivityAdvertisements()
    }

    /**
     * 아카데미 목록 로드 (UseCase 사용)
     */
    private fun loadAcademies() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                getAcademiesUseCase().collect { academyList ->
                    _academies.value = academyList
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "알 수 없는 오류가 발생했습니다."
                _isLoading.value = false
            }
        }
    }

    /**
     * ACTIVITY position 광고 배너 로드
     */
    private fun loadActivityAdvertisements() {
        Log.d(tag, "[loadActivityAdvertisements] called")
        viewModelScope.launch {
            _isLoadingAds.value = true
            try {
                adBannerRepository.getAdvertisements(AdPosition.ACTIVITY)
                    .collect { advertisements ->
                        Log.d(
                            tag,
                            "[loadActivityAdvertisements] received ${advertisements.size} advertisements"
                        )
                        _activityAdvertisements.value = advertisements
                    }
            } catch (e: Exception) {
                Log.e(tag, "[loadActivityAdvertisements] Exception: ${e.message}", e)
                _activityAdvertisements.value = emptyList()
            } finally {
                _isLoadingAds.value = false
            }
        }
    }

    /**
     * 아카데미 신청 바텀시트 표시
     */
    fun showAcademyApplicationSheet() {
        _showBottomSheet.value = true
        loadAcademies() // 바텀시트 열 때마다 최신 데이터 로드
    }

    /**
     * 아카데미 신청 바텀시트 숨기기
     */
    fun hideAcademyApplicationSheet() {
        _showBottomSheet.value = false
    }

    /**
     * 아카데미 선택 및 상세 다이얼로그 표시
     */
    fun selectAcademyAndShowDetail(academy: Academy) {
        _selectedAcademy.value = academy
        _showDetailDialog.value = true
    }

    /**
     * 상세 다이얼로그 숨기기
     */
    fun hideDetailDialog() {
        _showDetailDialog.value = false
        _selectedAcademy.value = null
    }

    /**
     * 아카데미 신청 (UseCase 사용)
     */
    fun applyForAcademy(academyId: String) {
        viewModelScope.launch {
            try {
                val result = applyForAcademyUseCase(academyId)

                result.fold(
                    onSuccess = { message ->
                        _showDetailDialog.value = false
                        _showCompleteDialog.value = true
                        loadAcademies() // 신청 후 목록 새로고침
                    },
                    onFailure = { exception ->
                        val errorMessage = exception.message ?: "신청 중 오류가 발생했습니다."
                        if (errorMessage.contains("HTTP_500") ||
                            errorMessage.contains("알 수 없는 오류") ||
                            errorMessage.contains("이미 신청한 아카데미입니다")
                        ) {
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
                val errorMessage = e.message ?: "신청 중 오류가 발생했습니다."
                if (errorMessage.contains("HTTP_500") ||
                    errorMessage.contains("알 수 없는 오류") ||
                    errorMessage.contains("이미 신청한 아카데미입니다")
                ) {
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
        _showCompleteDialog.value = false
        _isDuplicateError.value = false // 중복 에러 상태 초기화
    }

    /**
     * 에러 메시지 초기화
     */
    fun clearError() {
        _error.value = null
    }

    /**
     * 광고 배너 새로고침
     */
    fun refreshAdvertisements() {
        Log.d(tag, "[refreshAdvertisements] called")
        loadActivityAdvertisements()
    }
}
