package com.luckydut97.feature_home.main.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
    // 이벤트 페이지 관련
    private val _currentEventPage = MutableStateFlow(0)
    val currentEventPage: StateFlow<Int> = _currentEventPage.asStateFlow()

    private val _totalEventPages = MutableStateFlow(2)
    val totalEventPages: StateFlow<Int> = _totalEventPages.asStateFlow()

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
}