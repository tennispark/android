package com.luckydut97.tennispark.feature_auth.membership.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import com.luckydut97.tennispark.core.data.repository.MembershipRepository
import com.luckydut97.tennispark.core.data.model.MembershipRegistrationRequest

class MembershipRegistrationViewModel : ViewModel() {

    private val tag = "🔍 디버깅: MembershipViewModel"
    private val membershipRepository = MembershipRepository()

    // 멤버십 등록 완료 여부
    private val _isMembershipComplete = MutableStateFlow(false)
    val isMembershipComplete: StateFlow<Boolean> = _isMembershipComplete.asStateFlow()

    // 로딩 상태
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // 에러 메시지
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // 멤버십 유형 (0: 최초 가입, 1: 기존 회원)
    private val _membershipType = MutableStateFlow(-1)
    val membershipType = _membershipType.asStateFlow()

    // 멤버십 가입 이유
    private val _joinReason = MutableStateFlow("")
    val joinReason = _joinReason.asStateFlow()

    // 코트 선택 (0: 개인/개인도전, 1: 빌리, 2: 게임스터디, 3: 초보코트)
    private val _selectedCourt = MutableStateFlow(3) // 기본값: 초보코트
    val selectedCourt = _selectedCourt.asStateFlow()

    // 기간 선택 (0: 7주, 1: 9주, 2: 13주)
    private val _selectedPeriod = MutableStateFlow(-1)
    val selectedPeriod = _selectedPeriod.asStateFlow()

    // 추천인
    private val _referrer = MutableStateFlow("")
    val referrer = _referrer.asStateFlow()

    // 멤버십 활동규정 동의
    private val _agreeToRules = MutableStateFlow(false)
    val agreeToRules = _agreeToRules.asStateFlow()

    // 사진/영상 활용 동의
    private val _agreeToMediaUsage = MutableStateFlow(false)
    val agreeToMediaUsage = _agreeToMediaUsage.asStateFlow()

    // 가입하기 버튼 활성화 여부
    val isSubmitEnabled = combine(
        combine(membershipType, joinReason, selectedCourt) { type, reason, court ->
            type != -1 && reason.isNotEmpty() && court != -1
        },
        combine(selectedPeriod, agreeToRules, agreeToMediaUsage) { period, rules, media ->
            period != -1 && rules && media
        }
    ) { basicConditions, agreementConditions ->
        basicConditions && agreementConditions
    }

    fun updateMembershipType(type: Int) {
        _membershipType.value = type
    }

    fun updateJoinReason(reason: String) {
        if (reason.length <= 100) {
            _joinReason.value = reason
        }
    }

    fun updateSelectedCourt(courtId: Int) {
        _selectedCourt.value = courtId
    }

    fun updateSelectedPeriod(periodId: Int) {
        _selectedPeriod.value = periodId
    }

    fun updateReferrer(referrer: String) {
        _referrer.value = referrer
    }

    fun updateAgreeToRules(agree: Boolean) {
        _agreeToRules.value = agree
    }

    fun updateAgreeToMediaUsage(agree: Boolean) {
        _agreeToMediaUsage.value = agree
    }

    fun submitMembershipRegistration() {
        Log.d(tag, "=== 멤버십 등록 버튼 클릭 ===")
        Log.d(tag, "멤버십 타입: ${_membershipType.value}")
        Log.d(tag, "가입 이유: ${_joinReason.value}")
        Log.d(tag, "선택된 코트: ${_selectedCourt.value}")
        Log.d(tag, "선택된 기간: ${_selectedPeriod.value}")
        Log.d(tag, "추천인: ${_referrer.value}")
        Log.d(tag, "규정 동의: ${_agreeToRules.value}")
        Log.d(tag, "미디어 동의: ${_agreeToMediaUsage.value}")

        viewModelScope.launch {
            try {
                Log.d(tag, "API 호출 준비 중...")
                _isLoading.value = true
                _errorMessage.value = null

                // 멤버십 타입 매핑
                val membershipType = when (_membershipType.value) {
                    0 -> "NEW"
                    1 -> "EXISTING"
                    else -> "NEW"
                }

                // 코트 타입 매핑
                val courtType = when (_selectedCourt.value) {
                    0 -> "GAME_CHALLENGE"
                    1 -> "RALLY"
                    2 -> "STUDY"
                    3 -> "BEGINNER"
                    else -> "BEGINNER"
                }

                // 기간 매핑
                val period = when (_selectedPeriod.value) {
                    0 -> "7WEEKS"
                    1 -> "9WEEKS"
                    2 -> "13WEEKS"
                    else -> "7WEEKS"
                }

                // 추천인 처리 (빈 문자열이면 null로 변환)
                val recommender = if (_referrer.value.isBlank()) null else _referrer.value

                val request = MembershipRegistrationRequest(
                    membershipType = membershipType,
                    reason = _joinReason.value,
                    courtType = courtType,
                    period = period,
                    recommender = recommender
                )

                Log.d(tag, "Repository 호출 시작...")
                val response = membershipRepository.registerMembership(request)
                Log.d(tag, "Repository 호출 완료")

                if (response.success) {
                    Log.d(tag, "멤버십 등록 성공!")
                    _isMembershipComplete.value = true
                } else {
                    val errorMessage = response.error?.message ?: "멤버십 등록에 실패했습니다."
                    Log.e(tag, "멤버십 등록 실패: $errorMessage")
                    _errorMessage.value = errorMessage
                }
            } catch (e: Exception) {
                Log.e(tag, "API 호출 예외 발생: ${e.message}", e)
                _errorMessage.value = "오류가 발생했습니다."
            } finally {
                _isLoading.value = false
                Log.d(tag, "=== 멤버십 등록 처리 완료 ===")
            }
        }
    }

    fun resetMembershipState() {
        _isMembershipComplete.value = false
        _errorMessage.value = null
    }
}
