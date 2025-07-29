package com.luckydut97.feature_home.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckydut97.tennispark.core.data.model.Advertisement
import com.luckydut97.tennispark.core.data.model.AdPosition
import com.luckydut97.tennispark.core.data.network.NetworkModule
import com.luckydut97.tennispark.core.data.repository.AdBannerRepository
import com.luckydut97.tennispark.core.data.repository.AdBannerRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val tag = "🔍 디버깅: HomeViewModel"

    // AdBanner Repository
    private val adBannerRepository: AdBannerRepository by lazy {
        AdBannerRepositoryImpl(NetworkModule.apiService)
    }

    // 이벤트 페이지 관련
    private val _currentEventPage = MutableStateFlow(0)
    val currentEventPage: StateFlow<Int> = _currentEventPage.asStateFlow()

    private val _totalEventPages = MutableStateFlow(3)
    val totalEventPages: StateFlow<Int> = _totalEventPages.asStateFlow()

    // 광고 배너 관련
    private val _mainAdvertisements = MutableStateFlow<List<Advertisement>>(emptyList())
    val mainAdvertisements: StateFlow<List<Advertisement>> = _mainAdvertisements.asStateFlow()

    private val _isLoadingAds = MutableStateFlow(false)
    val isLoadingAds: StateFlow<Boolean> = _isLoadingAds.asStateFlow()

    init {
        Log.d(tag, "[init] HomeViewModel initialized")
        loadMainAdvertisements()
    }

    // 이벤트 페이지 업데이트
    fun nextEventPage() {
        if (_currentEventPage.value < _totalEventPages.value - 1) {
            _currentEventPage.value = _currentEventPage.value + 1
        }
    }

    fun prevEventPage() {
        if (_currentEventPage.value > 0) {
            _currentEventPage.value = _currentEventPage.value - 1
        }
    }

    // 이벤트 페이지 설정
    fun setEventPage(page: Int) {
        if (page in 0 until _totalEventPages.value) {
            _currentEventPage.value = page
        }
    }

    // 메인 광고 배너 로드
    private fun loadMainAdvertisements() {
        Log.d(tag, "[loadMainAdvertisements] called")
        viewModelScope.launch {
            _isLoadingAds.value = true
            try {
                adBannerRepository.getAdvertisements(AdPosition.MAIN).collect { advertisements ->
                    Log.d(
                        tag,
                        "[loadMainAdvertisements] received ${advertisements.size} advertisements"
                    )
                    _mainAdvertisements.value = advertisements
                }
            } catch (e: Exception) {
                Log.e(tag, "[loadMainAdvertisements] Exception: ${e.message}", e)
                _mainAdvertisements.value = emptyList()
            } finally {
                _isLoadingAds.value = false
            }
        }
    }

    // 광고 배너 새로고침
    fun refreshAdvertisements() {
        Log.d(tag, "[refreshAdvertisements] called")
        loadMainAdvertisements()
    }
}