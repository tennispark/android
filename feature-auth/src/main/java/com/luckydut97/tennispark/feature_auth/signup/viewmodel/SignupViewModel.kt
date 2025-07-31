package com.luckydut97.tennispark.feature_auth.signup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.luckydut97.tennispark.core.data.repository.MembershipRepository
import com.luckydut97.tennispark.core.data.model.MemberRegistrationRequest
import com.luckydut97.tennispark.core.data.model.ErrorResponse
import com.luckydut97.tennispark.core.data.storage.TokenManager
import com.luckydut97.tennispark.core.data.storage.TokenManagerImpl
import com.luckydut97.tennispark.core.data.network.NetworkModule
import com.luckydut97.tennispark.core.fcm.FcmTokenManager

class SignupViewModel : ViewModel() {

    private val tag = "🔍 디버깅: SignupViewModel"
    private val membershipRepository = MembershipRepository()
    private val fcmTokenManager = FcmTokenManager()

    // TokenManager 인스턴스 생성
    private val tokenManager: TokenManager by lazy {
        val context = NetworkModule.getContext()
        if (context != null) {
            TokenManagerImpl(context)
        } else {
            throw IllegalStateException("NetworkModule not initialized")
        }
    }

    // 휴대폰 번호 (인증 화면에서 전달받을 예정)
    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber = _phoneNumber.asStateFlow()

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

    // 약관 관련 상태들
    private val _agreeAll = MutableStateFlow(false)
    val agreeAll = _agreeAll.asStateFlow()

    private val _agreeTerms = MutableStateFlow(false)         // 이용약관
    val agreeTerms = _agreeTerms.asStateFlow()
    private val _agreePrivacy = MutableStateFlow(false)       // 개인정보
    val agreePrivacy = _agreePrivacy.asStateFlow()
    private val _agreeFourteen = MutableStateFlow(false)      // 만 14세 이상
    val agreeFourteen = _agreeFourteen.asStateFlow()

    private val _agreeInstagram = MutableStateFlow(false)     // 인스타 팔로우
    val agreeInstagram = _agreeInstagram.asStateFlow()
    private val _agreeKakaoChannel = MutableStateFlow(false)  // 카카오 추가
    val agreeKakaoChannel = _agreeKakaoChannel.asStateFlow()

    // 로딩 상태
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // 에러 메시지
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun setPhoneNumber(phoneNumber: String) {
        _phoneNumber.value = phoneNumber
    }

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

    fun setAgreeAll(checked: Boolean) {
        _agreeAll.value = checked
        _agreeTerms.value = checked
        _agreePrivacy.value = checked
        _agreeFourteen.value = checked
    }

    fun setAgreeTerms(checked: Boolean) {
        _agreeTerms.value = checked
        _agreeAll.value = _agreeTerms.value && _agreePrivacy.value && _agreeFourteen.value
    }

    fun setAgreePrivacy(checked: Boolean) {
        _agreePrivacy.value = checked
        _agreeAll.value = _agreeTerms.value && _agreePrivacy.value && _agreeFourteen.value
    }

    fun setAgreeFourteen(checked: Boolean) {
        _agreeFourteen.value = checked
        _agreeAll.value = _agreeTerms.value && _agreePrivacy.value && _agreeFourteen.value
    }

    fun setAgreeInstagram(checked: Boolean) {
        _agreeInstagram.value = checked
    }

    fun setAgreeKakaoChannel(checked: Boolean) {
        _agreeKakaoChannel.value = checked
    }

    fun signup() {

        val isValid = _name.value.isNotEmpty() &&
                _experience.value.isNotEmpty() &&
                _birthYear.value.isNotEmpty() &&
                _instagramId.value.isNotEmpty() &&
                _joinPath.value != -1 &&
                (_joinPath.value != 2 || _referrer.value.isNotEmpty()) &&
                _agreeTerms.value &&
                _agreePrivacy.value &&
                _agreeFourteen.value

        if (isValid) {
            viewModelScope.launch {
                try {
                    _isLoading.value = true
                    _errorMessage.value = null

                    // FCM 토큰 가져오기
                    val fcmToken = fcmTokenManager.getFcmToken()

                    if (fcmToken != null) {
                    } else {
                    }

                    val registrationSource = when (_joinPath.value) {
                        0 -> "INSTAGRAM"
                        1 -> "NAVER_SEARCH"
                        2 -> "FRIEND_RECOMMENDATION"
                        else -> "INSTAGRAM"
                    }

                    val request = MemberRegistrationRequest(
                        phoneNumber = _phoneNumber.value,
                        name = _name.value,
                        gender = if (_isMale.value) "MAN" else "WOMAN",
                        tennisCareer = _experience.value,
                        year = _birthYear.value.toIntOrNull() ?: 2000,
                        registrationSource = registrationSource,
                        recommender = if (_joinPath.value == 2 && _referrer.value.isNotEmpty()) _referrer.value else null,
                        instagramId = _instagramId.value,
                        fcmToken = fcmToken // FCM 토큰 추가
                    )

                    val response = membershipRepository.registerMember(request)

                    if (response.success) {
                        val registrationResponse = response.response

                        if (registrationResponse != null) {

                            // 토큰 저장
                            tokenManager.saveTokens(
                                registrationResponse.accessToken,
                                registrationResponse.refreshToken
                            )
                        }

                        _isSignupComplete.value = true
                    } else {
                        val errorMessage = response.error?.message ?: "회원가입에 실패했습니다."

                        // 중복 휴대폰 번호 에러 체크
                        if (errorMessage.contains("휴대폰") || errorMessage.contains("중복") || errorMessage.contains(
                                "이미"
                            )
                        ) {
                            _errorMessage.value = "이미 가입된 휴대폰 번호입니다. 휴대폰 인증을 통해 로그인해주세요."
                        } else {
                            _errorMessage.value = errorMessage
                        }
                    }
                } catch (e: Exception) {
                    _errorMessage.value = "네트워크 오류가 발생했습니다: ${e.message}"
                } finally {
                    _isLoading.value = false
                }
            }
        } else {
            _errorMessage.value = "필수 항목을 모두 입력해주세요."
        }
    }
}
