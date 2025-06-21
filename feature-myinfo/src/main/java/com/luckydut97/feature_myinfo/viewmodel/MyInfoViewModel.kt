package com.luckydut97.feature_myinfo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckydut97.tennispark.core.data.model.PointHistoryItem
import com.luckydut97.tennispark.core.data.repository.PointRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

class MyInfoViewModel(
    private val pointRepository: PointRepository = PointRepository()
) : ViewModel() {

    private val tag = "🔍 디버깅: MyInfoViewModel"

    // 포인트 잔액
    private val _points = MutableStateFlow(0)
    val points: StateFlow<Int> = _points.asStateFlow()

    // 포인트 내역
    private val _histories = MutableStateFlow<List<PointHistoryItem>>(emptyList())
    val histories: StateFlow<List<PointHistoryItem>> = _histories.asStateFlow()

    // 로딩 상태
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // 에러 메시지
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        Log.d(tag, "MyInfoViewModel 초기화")
        refreshAllData()
    }

    /**
     * 모든 포인트 데이터 새로고침
     */
    fun refreshAllData() {
        Log.d(tag, "=== 모든 포인트 데이터 새로고침 시작 ===")
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                // 1. 내 포인트 조회
                Log.d(tag, "1. 내 포인트 조회 시작")
                val pointsResponse = pointRepository.getMyPoints()
                if (pointsResponse.success) {
                    val responseData = pointsResponse.response
                    if (responseData != null) {
                        _points.value = responseData.points
                        Log.d(tag, "✅ 포인트 조회 성공: ${responseData.points}P")
                    } else {
                        Log.e(tag, "❌ 포인트 조회 실패: 응답 데이터가 null")
                        _errorMessage.value = "포인트 조회 실패: 응답 데이터가 없습니다"
                    }
                } else {
                    Log.e(tag, "❌ 포인트 조회 실패: ${pointsResponse.error?.message}")
                    _errorMessage.value = pointsResponse.error?.message ?: "포인트 조회 실패"
                }

                // 2. 포인트 내역 조회
                Log.d(tag, "2. 포인트 내역 조회 시작")
                val historiesResponse = pointRepository.getPointHistories()
                if (historiesResponse.success) {
                    val responseData = historiesResponse.response
                    if (responseData != null) {
                        _histories.value = responseData.histories
                        Log.d(tag, "✅ 포인트 내역 조회 성공: ${responseData.histories.size}개")
                    } else {
                        Log.e(tag, "❌ 포인트 내역 조회 실패: 응답 데이터가 null")
                        _errorMessage.value = "포인트 내역 조회 실패: 응답 데이터가 없습니다"
                    }
                } else {
                    Log.e(tag, "❌ 포인트 내역 조회 실패: ${historiesResponse.error?.message}")
                    _errorMessage.value = historiesResponse.error?.message ?: "포인트 내역 조회 실패"
                }

            } catch (e: Exception) {
                Log.e(tag, "🔥 데이터 새로고침 예외: ${e.message}", e)
                _errorMessage.value = "데이터 조회 중 오류가 발생했습니다."
            } finally {
                _isLoading.value = false
                Log.d(tag, "=== 모든 포인트 데이터 새로고침 완료 ===")
            }
        }
    }

    /**
     * QR 이벤트 처리 (출석, 포인트 적립 등)
     */
    fun processQrEvent(eventUrl: String) {
        Log.d(tag, "=== QR 이벤트 처리 시작 ===")
        Log.d(tag, "Event URL: $eventUrl")

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val response = pointRepository.postQrEvent(eventUrl)
                if (response.success) {
                    Log.d(tag, "✅ QR 이벤트 처리 성공!")
                    // QR 이벤트 성공 후 포인트 데이터 새로고침
                    refreshAllData()
                } else {
                    Log.e(tag, "❌ QR 이벤트 처리 실패: ${response.error?.message}")
                    _errorMessage.value = response.error?.message ?: "QR 이벤트 처리 실패"
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                Log.e(tag, "🔥 QR 이벤트 처리 예외: ${e.message}", e)
                _errorMessage.value = "QR 이벤트 처리 중 오류가 발생했습니다."
                _isLoading.value = false
            }
        }
    }

    /**
     * 에러 메시지 초기화
     */
    fun clearError() {
        _errorMessage.value = null
    }
}
