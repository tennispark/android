package com.luckydut97.feature_home_shop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckydut97.feature_home_shop.data.model.ShopItem
import com.luckydut97.feature_home_shop.data.repository.MockShopRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShopViewModel(
    private val repository: MockShopRepository
) : ViewModel() {

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
    }

    private fun loadShopData() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                repository.getShopItems().collect { items ->
                    _shopItems.value = items
                }
                _userPoints.value = repository.getUserPoints()
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message ?: "알 수 없는 오류가 발생했습니다."
                _isLoading.value = false
            }
        }
    }

    fun updateAdPage(page: Int) {
        _currentAdPage.value = page
    }

    fun refreshData() {
        loadShopData()
    }
}
