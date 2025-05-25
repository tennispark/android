package com.luckydut97.tennispark.feature_auth.membership.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine

class MembershipRegistrationViewModel : ViewModel() {

    // 멤버십 등록 완료 여부
    private val _isMembershipComplete = MutableStateFlow(false)
    val isMembershipComplete: StateFlow<Boolean> = _isMembershipComplete.asStateFlow()

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
        // 실제 서버 연동 로직 구현 예정
        _isMembershipComplete.value = true
    }

    fun resetMembershipState() {
        _isMembershipComplete.value = false
    }
}