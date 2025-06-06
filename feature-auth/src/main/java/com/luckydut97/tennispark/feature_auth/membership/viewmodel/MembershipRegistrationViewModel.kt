package com.luckydut97.tennispark.feature_auth.membership.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import com.luckydut97.tennispark.core.data.network.MembershipRepository
import com.luckydut97.tennispark.core.data.model.MemberRegistrationRequest

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

                // TODO: 실제 사용자 정보를 가져와야 함 (이름, 전화번호, 성별, 테니스 경력 등)
                val request = MemberRegistrationRequest(
                    phoneNumber = "01012345678", // TODO: 실제 전화번호
                    name = "홍길동", // TODO: 실제 이름
                    gender = "MAN", // TODO: 실제 성별
                    tennisCareer = _joinReason.value, // 가입 이유를 테니스 경력으로 사용
                    year = 2025,
                    registrationSource = "INSTAGRAM",
                    recommender = _referrer.value,
                    instagramId = ""
                )

                Log.d(tag, "Repository 호출 시작...")
                val response = membershipRepository.registerMember(request)
                Log.d(tag, "Repository 호출 완료")

                if (response.success) {
                    Log.d(tag, "멤버십 등록 성공!")
                    _isMembershipComplete.value = true
                } else {
                    Log.e(tag, "디버깅: 멤버십 등록 실패: ${response.error}")
                    _errorMessage.value = response.error ?: "멤버십 등록에 실패했습니다."
                }
            } catch (e: Exception) {
                Log.e(tag, "디버깅: API 호출 예외 발생: ${e.message}", e)
                _errorMessage.value = "네트워크 오류가 발생했습니다: ${e.message}"
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
