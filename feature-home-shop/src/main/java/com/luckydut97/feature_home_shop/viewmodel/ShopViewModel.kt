package com.luckydut97.feature_home_shop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckydut97.feature_home_shop.data.model.ShopItem
import com.luckydut97.feature_home_shop.data.repository.ShopRepositoryImpl
import com.luckydut97.tennispark.core.data.repository.PointRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

class ShopViewModel(
    private val shopRepository: ShopRepositoryImpl,
    private val pointRepository: PointRepository = PointRepository()
) : ViewModel() {

    private val tag = " 디버깅: ShopViewModel"

    private val _shopItems = MutableStateFlow<List<ShopItem>>(emptyList())
    val shopItems: StateFlow<List<ShopItem>> = _shopItems.asStateFlow()

    private val _userPoints = MutableStateFlow(0)
    val userPoints: StateFlow<Int> = _userPoints.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _currentAdPage = MutableStateFlow(0)
    val currentAdPage: StateFlow<Int> = _currentAdPage.asStateFlow()

    private val _totalAdPages = MutableStateFlow(3) // 예시로 3개 광고
    val totalAdPages: StateFlow<Int> = _totalAdPages.asStateFlow()

    init {
        loadShopData()
        loadUserPoints()
    }

    private fun loadShopData() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                Log.d(tag, "상품 목록 로딩 시작")
                shopRepository.getShopItems().collect { items ->
                    _shopItems.value = items
                    Log.d(tag, "상품 목록 로딩 완료: ${items.size}개")
                }
            } catch (e: Exception) {
                Log.e(tag, "상품 목록 로딩 실패: ${e.message}")
                _error.value = e.message ?: "상품 목록을 불러올 수 없습니다."
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadUserPoints() {
        viewModelScope.launch {
            try {
                Log.d(tag, "사용자 포인트 조회 시작")
                val response = pointRepository.getMyPoints()
                val responseData = response.response
                if (response.success && responseData != null) {
                    _userPoints.value = responseData.points
                    Log.d(tag, "사용자 포인트 조회 성공: ${responseData.points}P")
                } else {
                    Log.e(tag, "사용자 포인트 조회 실패: ${response.error?.message}")
                    _error.value = response.error?.message ?: "포인트 조회 실패"
                }
            } catch (e: Exception) {
                Log.e(tag, "사용자 포인트 조회 예외: ${e.message}")
                _error.value = "포인트 조회 중 오류가 발생했습니다."
            }
        }
    }

    fun updateAdPage(page: Int) {
        _currentAdPage.value = page
    }

    fun refreshData() {
        loadShopData()
        loadUserPoints()
    }

    /**
     * 사용자 포인트만 새로고침 (구매 완료 후 호출용)
     */
    fun refreshUserPoints() {
        loadUserPoints()
    }

    /**
     * 화면 재진입 시 포인트 새로고침
     */
    fun onResume() {
        loadUserPoints()
    }
}
