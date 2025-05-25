package com.luckydut97.tennispark.feature_auth.signup.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SignupViewModel : ViewModel() {
    // 회원가입 완료 여부
    private val _isSignupComplete = MutableStateFlow(false)
    val isSignupComplete: StateFlow<Boolean> = _isSignupComplete.asStateFlow()

    // 이름
    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    // 성별 (남자=true, 여자=false)
    private val _isMale = MutableStateFlow(true)
    val isMale = _isMale.asStateFlow()

    // 경력
    private val _experience = MutableStateFlow("")
    val experience = _experience.asStateFlow()

    // 생년
    private val _birthYear = MutableStateFlow("")
    val birthYear = _birthYear.asStateFlow()

    // 가입 경로 (0=인스타, 1=네이버 검색, 2=친구 추천, -1=미선택)
    private val _joinPath = MutableStateFlow(-1) // 초기값을 -1로 변경
    val joinPath = _joinPath.asStateFlow()

    // 추천인
    private val _referrer = MutableStateFlow("")
    val referrer = _referrer.asStateFlow()

    // 인스타그램 ID
    private val _instagramId = MutableStateFlow("")
    val instagramId = _instagramId.asStateFlow()

    // 테니스파크 계정 생성 동의
    private val _agreeToTerms = MutableStateFlow(false)
    val agreeToTerms = _agreeToTerms.asStateFlow()

    // 테니스파크 카카오톡 채널 추가 여부
    private val _agreeToKakaoChannel = MutableStateFlow(false)
    val agreeToKakaoChannel = _agreeToKakaoChannel.asStateFlow()

    fun updateName(name: String) {
        _name.value = name
    }

    fun updateGender(isMale: Boolean) {
        _isMale.value = isMale
    }

    fun updateExperience(experience: String) {
        _experience.value = experience
    }

    fun updateBirthYear(year: String) {
        _birthYear.value = year
    }

    fun updateJoinPath(pathIndex: Int) {
        _joinPath.value = pathIndex
    }

    fun updateReferrer(referrer: String) {
        _referrer.value = referrer
    }

    fun updateInstagramId(id: String) {
        _instagramId.value = id
    }

    fun updateAgreeToTerms(agree: Boolean) {
        _agreeToTerms.value = agree
    }

    fun updateAgreeToKakaoChannel(agree: Boolean) {
        _agreeToKakaoChannel.value = agree
    }

    fun signup() {
        // 서버 회원가입 로직은 나중에 구현
        // 임시 구현: 이름, 성별, 경력, 생년이 모두 입력되었고 약관에 동의했으면 회원가입 완료
        val isValid = _name.value.isNotEmpty() &&
                _experience.value.isNotEmpty() &&
                _birthYear.value.isNotEmpty() &&
                _agreeToTerms.value

        if (isValid) {
            _isSignupComplete.value = true
        }
    }
}