package com.luckydut97.tennispark.feature_auth.signup.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luckydut97.tennispark.feature_auth.signup.ui.components.JoinPathButton
import com.luckydut97.tennispark.core.ui.components.button.ActionButton
import com.luckydut97.tennispark.core.ui.components.selection.CheckBox
import com.luckydut97.tennispark.feature_auth.signup.ui.components.GenderSelectionButton
import com.luckydut97.tennispark.core.ui.components.input.InputField
import com.luckydut97.tennispark.core.ui.components.navigation.TopBar
import com.luckydut97.tennispark.core.ui.theme.AppColors
import com.luckydut97.tennispark.core.ui.theme.Pretendard
import com.luckydut97.tennispark.feature_auth.signup.viewmodel.SignupViewModel

@Composable
fun SignupScreen(
    viewModel: SignupViewModel = viewModel(),
    onBackClick: () -> Unit,
    onSignupComplete: () -> Unit
) {
    val name by viewModel.name.collectAsState()
    val isMale by viewModel.isMale.collectAsState()
    val experience by viewModel.experience.collectAsState()
    val birthYear by viewModel.birthYear.collectAsState()
    val joinPath by viewModel.joinPath.collectAsState()
    val referrer by viewModel.referrer.collectAsState()
    val instagramId by viewModel.instagramId.collectAsState()
    val agreeAll by viewModel.agreeAll.collectAsState()
    val agreeTerms by viewModel.agreeTerms.collectAsState()
    val agreePrivacy by viewModel.agreePrivacy.collectAsState()
    val agreeFourteen by viewModel.agreeFourteen.collectAsState()
    val agreeInstagram by viewModel.agreeInstagram.collectAsState()
    val agreeKakaoChannel by viewModel.agreeKakaoChannel.collectAsState()
    val isSignupComplete by viewModel.isSignupComplete.collectAsState()

    if (isSignupComplete) {
        onSignupComplete()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
    ) {
        TopBar(
            title = "회원가입",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // 개인정보 입력
            Text(
                text = "개인정보 입력",
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontFamily = Pretendard
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 이름
            InputField(
                value = name,
                onValueChange = { viewModel.updateName(it) },
                label = "이름",
                isRequired = true,
                placeholder = "이름을 입력해주세요.",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 성별 선택
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "성별 선택",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextPrimary,
                    fontFamily = Pretendard
                )

                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    text = "*",
                    fontSize = 14.sp,
                    color = AppColors.Required,
                    fontFamily = Pretendard
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                GenderSelectionButton(
                    text = "남자",
                    isSelected = isMale,
                    onClick = { viewModel.updateGender(true) },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(6.dp))

                GenderSelectionButton(
                    text = "여자",
                    isSelected = !isMale,
                    onClick = { viewModel.updateGender(false) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 구력
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "구력",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextPrimary,
                    fontFamily = Pretendard
                )

                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    text = "*",
                    fontSize = 14.sp,
                    color = AppColors.Required,
                    fontFamily = Pretendard
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                InputField(
                    value = experience,
                    onValueChange = { viewModel.updateExperience(it) },
                    label = "",
                    placeholder = "ex) 15",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.width(118.dp).height(47.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "개월",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF262626),
                    fontFamily = Pretendard
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 나이 (탄생년도)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "나이 (탄생년도)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextPrimary,
                    fontFamily = Pretendard
                )

                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    text = "*",
                    fontSize = 14.sp,
                    color = AppColors.Required,
                    fontFamily = Pretendard
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                InputField(
                    value = birthYear,
                    onValueChange = { viewModel.updateBirthYear(it) },
                    label = "",
                    placeholder = "ex) 1990",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.width(118.dp).height(47.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "년도",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF262626),
                    fontFamily = Pretendard
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 가입경로
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "가입경로 (택1)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextPrimary,
                    fontFamily = Pretendard
                )

                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    text = "*",
                    fontSize = 14.sp,
                    color = AppColors.Required,
                    fontFamily = Pretendard
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                JoinPathButton(
                    text = "인스타",
                    isSelected = joinPath == 0,
                    onClick = { viewModel.updateJoinPath(0) },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(6.dp))

                JoinPathButton(
                    text = "네이버 검색",
                    isSelected = joinPath == 1,
                    onClick = { viewModel.updateJoinPath(1) },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(6.dp))

                JoinPathButton(
                    text = "친구 추천",
                    isSelected = joinPath == 2,
                    onClick = { viewModel.updateJoinPath(2) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 추천인
            InputField(
                value = referrer,
                onValueChange = { viewModel.updateReferrer(it) },
                label = "추천인",
                placeholder = "ex) 추천인 이름 + 연락처 끝 4자리",
                enabled = joinPath == 2, // 친구 추천(2)을 선택했을 때만 활성화
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 인스타그램 ID
            InputField(
                value = instagramId,
                onValueChange = { viewModel.updateInstagramId(it) },
                label = "인스타그램 ID",
                isRequired = true,
                placeholder = "ID를 입력해주세요.",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 약관 동의
            Text(
                text = "약관 동의",
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontFamily = Pretendard,
                modifier = Modifier.padding(start = 0.dp, end = 20.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 전체 동의
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp)
            ) {
                CheckBox(
                    text = "",
                    isChecked = agreeAll,
                    onCheckedChange = { checked -> viewModel.setAgreeAll(checked) }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "전체 동의합니다.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.CaptionColor,
                    fontFamily = Pretendard,
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            val underlineColor = Color(0xFFABABAB)
            val rowPaddingStart = 20.dp + 16.dp

            // 하위 약관 3줄
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(start = rowPaddingStart, end = 20.dp)
            ) {
                CheckBox(
                    text = "",
                    isChecked = agreeTerms,
                    onCheckedChange = { checked -> viewModel.setAgreeTerms(checked) }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "이용약관에 동의 합니다. (필수)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = AppColors.TextPrimary,
                    fontFamily = Pretendard
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "내용보기",
                    fontSize = 12.sp,
                    color = underlineColor,
                    fontWeight = FontWeight.Normal,
                    fontFamily = Pretendard,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { /* TODO: 상세 팝업 */ }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(start = rowPaddingStart, end = 20.dp)
            ) {
                CheckBox(
                    text = "",
                    isChecked = agreePrivacy,
                    onCheckedChange = { checked -> viewModel.setAgreePrivacy(checked) }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "개인정보 수집 및 이용에 동의합니다. (필수)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = AppColors.TextPrimary,
                    fontFamily = Pretendard
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "내용보기",
                    fontSize = 12.sp,
                    color = underlineColor,
                    fontWeight = FontWeight.Normal,
                    fontFamily = Pretendard,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { /* TODO: 상세 팝업 */ }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(start = rowPaddingStart, end = 20.dp)
            ) {
                CheckBox(
                    text = "",
                    isChecked = agreeFourteen,
                    onCheckedChange = { checked -> viewModel.setAgreeFourteen(checked) }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "만 14세 이상입니다. (필수)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = AppColors.TextPrimary,
                    fontFamily = Pretendard
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 서비스 이용 조건
            Text(
                text = "서비스 이용 조건",
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontFamily = Pretendard,
                modifier = Modifier.padding(start = 0.dp, end = 20.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 인스타/카카오 Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(start = rowPaddingStart, end = 20.dp)
            ) {
                CheckBox(
                    text = "",
                    isChecked = agreeInstagram,
                    onCheckedChange = { checked -> viewModel.setAgreeInstagram(checked) }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "테니스파크 인스타 계정을 팔로우 하셨습니까? (필수)",
                    fontSize = 14.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.Normal,
                    color = AppColors.TextPrimary
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(start = rowPaddingStart, end = 20.dp)
            ) {
                CheckBox(
                    text = "",
                    isChecked = agreeKakaoChannel,
                    onCheckedChange = { checked -> viewModel.setAgreeKakaoChannel(checked) }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "테니스파크 카카오톡 채널을 추가 하셨습니까? (필수)",
                    fontSize = 14.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.Normal,
                    color = AppColors.TextPrimary
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "* 테니스파크의 다양하고 유용한 정보를 확인하실 수 있습니다.",
                fontSize = 12.sp,
                fontFamily = Pretendard,
                color = AppColors.TextTertiary,
                modifier = Modifier.padding(start = rowPaddingStart, end = 20.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 가입하기 버튼
            ActionButton(
                text = "가입하기",
                onClick = { viewModel.signup() },
                enabled = name.isNotEmpty() && experience.isNotEmpty() && birthYear.isNotEmpty() && instagramId.isNotEmpty() && joinPath != -1,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
