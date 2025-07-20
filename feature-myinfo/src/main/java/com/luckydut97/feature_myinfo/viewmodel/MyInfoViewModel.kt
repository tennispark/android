package com.luckydut97.feature_myinfo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckydut97.tennispark.core.data.model.PointHistoryItem
import com.luckydut97.tennispark.core.data.model.MemberInfoResponse
import com.luckydut97.tennispark.core.data.model.GameRecord
import com.luckydut97.tennispark.core.data.repository.PointRepository
import com.luckydut97.tennispark.core.data.repository.AuthRepository
import com.luckydut97.tennispark.core.data.repository.AuthRepositoryImpl
import com.luckydut97.tennispark.core.data.storage.TokenManagerImpl
import com.luckydut97.tennispark.core.data.network.NetworkModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyInfoViewModel(
    private val pointRepository: PointRepository = PointRepository()
) : ViewModel() {

    private val tag = "🔍 디버깅: MyInfoViewModel"

    // AuthRepository 초기화
    private val authRepository: AuthRepository by lazy {
        val tokenManager = TokenManagerImpl(NetworkModule.getContext()!!)
        AuthRepositoryImpl(NetworkModule.apiService, tokenManager)
    }

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

    // 회원정보 상태 추가
    private val _memberInfo = MutableStateFlow<MemberInfoResponse?>(null)
    val memberInfo: StateFlow<MemberInfoResponse?> = _memberInfo.asStateFlow()

    // 로그아웃 성공 상태
    private val _isLoggedOut = MutableStateFlow(false)
    val isLoggedOut: StateFlow<Boolean> = _isLoggedOut.asStateFlow()

    // 회원 탈퇴 성공 상태
    private val _isWithdrawn = MutableStateFlow(false)
    val isWithdrawn: StateFlow<Boolean> = _isWithdrawn.asStateFlow()

    init {
        refreshAllData()
    }

    /**
     * 모든 포인트 데이터 새로고침
     */
    fun refreshAllData() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                // 1. 내 포인트 조회
                val pointsResponse = pointRepository.getMyPoints()
                if (pointsResponse.success) {
                    val responseData = pointsResponse.response
                    if (responseData != null) {
                        _points.value = responseData.points
                    } else {
                        _errorMessage.value = "포인트 조회 실패: 응답 데이터가 없습니다"
                    }
                } else {
                    _errorMessage.value = pointsResponse.error?.message ?: "포인트 조회 실패"
                }

                // 2. 포인트 내역 조회
                val historiesResponse = pointRepository.getPointHistories()
                if (historiesResponse.success) {
                    val responseData = historiesResponse.response
                    if (responseData != null) {
                        // 날짜 기준으로 최신순 정렬 (내림차순)
                        val sortedHistories = responseData.histories.sortedByDescending { it.date }
                        _histories.value = sortedHistories
                    } else {
                        _errorMessage.value = "포인트 내역 조회 실패: 응답 데이터가 없습니다"
                    }
                } else {
                    _errorMessage.value = historiesResponse.error?.message ?: "포인트 내역 조회 실패"
                }

                // 3. 회원정보 조회 추가
                val memberInfoResponse = pointRepository.getMemberInfo()
                if (memberInfoResponse.success) {
                    val responseData = memberInfoResponse.response
                    if (responseData != null) {
                        _memberInfo.value = responseData
                    } else {
                        _errorMessage.value = "회원정보 조회 실패: 응답 데이터가 없습니다"
                    }
                } else {
                    _errorMessage.value = memberInfoResponse.error?.message ?: "회원정보 조회 실패"
                }

            } catch (e: Exception) {
                _errorMessage.value = "데이터 조회 중 오류가 발생했습니다."
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * QR 이벤트 처리 (출석, 포인트 적립 등)
     */
    fun processQrEvent(eventUrl: String) {

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val response = pointRepository.postQrEvent(eventUrl)
                if (response.success) {
                    // QR 이벤트 성공 후 포인트 데이터 새로고침
                    refreshAllData()
                } else {
                    _errorMessage.value = response.error?.message ?: "QR 이벤트 처리 실패"
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _errorMessage.value = "QR 이벤트 처리 중 오류가 발생했습니다."
                _isLoading.value = false
            }
        }
    }

    /**
     * 로그아웃 처리
     */
    fun logout() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val response = authRepository.logout()

                if (response.success) {
                    _isLoggedOut.value = true
                } else {
                    // API 실패해도 로컬에서는 토큰이 삭제되었으므로 로그아웃 성공으로 처리
                    _isLoggedOut.value = true
                }
            } catch (e: Exception) {
                // 예외 발생해도 로컬에서는 토큰이 삭제되었으므로 로그아웃 성공으로 처리
                _isLoggedOut.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * 회원 탈퇴 처리
     */
    fun withdraw() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val response = authRepository.withdraw()

                if (response.success) {
                    _isWithdrawn.value = true
                } else {
                    _errorMessage.value = response.error?.message ?: "회원 탈퇴 실패"
                }
            } catch (e: Exception) {
                _errorMessage.value = "회원 탈퇴 중 오류가 발생했습니다."
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * 로그아웃 상태 초기화 (화면 이동 후)
     */
    fun resetLogoutState() {
        _isLoggedOut.value = false
    }

    /**
     * 회원 탈퇴 상태 초기화 (화면 이동 후)
     */
    fun resetWithdrawState() {
        _isWithdrawn.value = false
    }

    /**
     * 에러 메시지 초기화
     */
    fun clearError() {
        _errorMessage.value = null
    }
}
