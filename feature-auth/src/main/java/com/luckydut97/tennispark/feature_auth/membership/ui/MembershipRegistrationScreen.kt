package com.luckydut97.tennispark.feature_auth.membership.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luckydut97.tennispark.core.ui.components.button.ActionButton
import com.luckydut97.tennispark.core.ui.components.input.InputField
import com.luckydut97.tennispark.core.ui.components.navigation.TopBar
import com.luckydut97.tennispark.core.ui.components.selection.CheckBox
import com.luckydut97.tennispark.core.ui.theme.AppColors
import com.luckydut97.tennispark.core.ui.theme.Pretendard
import com.luckydut97.tennispark.feature_auth.membership.viewmodel.MembershipRegistrationViewModel
import com.luckydut97.tennispark.feature_auth.signup.ui.components.JoinPathButton

@Composable
fun MembershipRegistrationScreen(
    viewModel: MembershipRegistrationViewModel = viewModel(),
    onBackClick: () -> Unit,
    onMembershipComplete: () -> Unit
) {
    val membershipType by viewModel.membershipType.collectAsState()
    val joinReason by viewModel.joinReason.collectAsState()
    val selectedCourt by viewModel.selectedCourt.collectAsState()
    val selectedPeriod by viewModel.selectedPeriod.collectAsState()
    val referrer by viewModel.referrer.collectAsState()
    val agreeToRules by viewModel.agreeToRules.collectAsState()
    val agreeToMediaUsage by viewModel.agreeToMediaUsage.collectAsState()
    val isMembershipComplete by viewModel.isMembershipComplete.collectAsState()
    val isSubmitEnabled by viewModel.isSubmitEnabled.collectAsState(initial = false)

    LaunchedEffect(isMembershipComplete) {
        if (isMembershipComplete) {
            onMembershipComplete()
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBar(
                title = "멤버십 등록",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // 멤버십 선택
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "멤버십",
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
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                JoinPathButton(
                    text = "최초 가입",
                    isSelected = membershipType == 0,
                    onClick = { viewModel.updateMembershipType(0) },
                    modifier = Modifier.weight(1f)
                )
                JoinPathButton(
                    text = "기존 회원",
                    isSelected = membershipType == 1,
                    onClick = { viewModel.updateMembershipType(1) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "* 이전에 멤버십으로 활동하신 회원분들은 기존 정회원으로 신청 부탁드립니다.",
                fontSize = 12.sp,
                color = AppColors.TextSecondary,
                fontFamily = Pretendard
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 멤버십 가입 이유
            InputField(
                value = joinReason,
                onValueChange = { viewModel.updateJoinReason(it) },
                label = "멤버십 가입 이유",
                isRequired = true,
                placeholder = "내용을 입력해주세요 (최대 100자)",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 멤버십 비용 안내
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "멤버십 비용 안내",
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

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "선택하신 코트에 따라 멤버십 비용이 상이합니다.",
                fontSize = 12.sp,
                color = AppColors.TextSecondary,
                fontFamily = Pretendard
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 가격 정보 표시
            PriceInfoSection()

            Spacer(modifier = Modifier.height(20.dp))

            // 코트 선택
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "코트 선택",
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

            // 코트 선택 - 4개 버튼 한 줄로
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                JoinPathButton(
                    text = "게임/\n게임도전",
                    isSelected = selectedCourt == 0,
                    onClick = { viewModel.updateSelectedCourt(0) },
                    modifier = Modifier.weight(1f)
                )
                JoinPathButton(
                    text = "랠리",
                    isSelected = selectedCourt == 1,
                    onClick = { viewModel.updateSelectedCourt(1) },
                    modifier = Modifier.weight(1f)
                )
                JoinPathButton(
                    text = "게임 스터디",
                    isSelected = selectedCourt == 2,
                    onClick = { viewModel.updateSelectedCourt(2) },
                    modifier = Modifier.weight(1f)
                )
                JoinPathButton(
                    text = "초보 코트",
                    isSelected = selectedCourt == 3,
                    onClick = { viewModel.updateSelectedCourt(3) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 기간 선택
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "기간 선택",
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
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                JoinPathButton(
                    text = "7주",
                    isSelected = selectedPeriod == 0,
                    onClick = { viewModel.updateSelectedPeriod(0) },
                    modifier = Modifier.weight(1f)
                )
                JoinPathButton(
                    text = "9주",
                    isSelected = selectedPeriod == 1,
                    onClick = { viewModel.updateSelectedPeriod(1) },
                    modifier = Modifier.weight(1f)
                )
                JoinPathButton(
                    text = "13주",
                    isSelected = selectedPeriod == 2,
                    onClick = { viewModel.updateSelectedPeriod(2) },
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
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 친구 추천 혜택
            Text(
                text = "* 친구 추천 혜택",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary,
                fontFamily = Pretendard
            )
            Text(
                text = "추천인(멤버십 회원만 접수) : 쿠폰 5장 추가",
                fontSize = 11.sp,
                color = Color(0xFF000000),
                fontFamily = Pretendard
            )
            Text(
                text = "추천으로 멤버십 가입 시 : 쿠폰 1장 추가",
                fontSize = 11.sp,
                color = Color(0xFF000000),
                fontFamily = Pretendard
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 멤버십 활동규정 동의
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "멤버십 활동규정에 동의",
                    fontSize = 14.sp,
                    fontFamily = Pretendard,
                    color = AppColors.TextPrimary
                )

                Spacer(modifier = Modifier.weight(1f))

                CheckBox(
                    text = "예",
                    isChecked = agreeToRules,
                    onCheckedChange = {
                        viewModel.updateAgreeToRules(true)
                    }
                )

                Spacer(modifier = Modifier.width(16.dp))

                CheckBox(
                    text = "아니오",
                    isChecked = !agreeToRules,
                    onCheckedChange = {
                        viewModel.updateAgreeToRules(false)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 사진/영상 활용 동의
            Text(
                text = "본인이 촬영된 사진 및 영상은 테니스파크 컨텐츠로 제작되어 인스타그램 @tennispark_official / 네이버 블로그 '테니스파크매거진'에 노출되고 홍보 목적으로 이용될 수 있습니다. 동의하십니까?",
                fontSize = 12.sp,
                fontFamily = Pretendard,
                color = AppColors.TextPrimary,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                CheckBox(
                    text = "예",
                    isChecked = agreeToMediaUsage,
                    onCheckedChange = {
                        viewModel.updateAgreeToMediaUsage(true)
                    }
                )

                Spacer(modifier = Modifier.width(16.dp))

                CheckBox(
                    text = "아니오",
                    isChecked = !agreeToMediaUsage,
                    onCheckedChange = {
                        viewModel.updateAgreeToMediaUsage(false)
                    }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // 가입하기 버튼
            ActionButton(
                text = "가입하기",
                onClick = { viewModel.submitMembershipRegistration() },
                enabled = isSubmitEnabled
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun PriceInfoSection() {
    Column {
        // 첫 번째 섹션
        Text(
            text = "게임 / 게임 도전코트 & 랠리코트",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontFamily = Pretendard
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFF2FAF4),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "7주",
                        fontSize = 12.sp,
                        color = Color.Black,
                        fontFamily = Pretendard
                    )
                    Text(
                        text = "175,000원 (17.5장)",
                        fontSize = 12.sp,
                        color = Color.Black,
                        fontFamily = Pretendard
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "9주",
                        fontSize = 12.sp,
                        color = Color.Black,
                        fontFamily = Pretendard
                    )
                    Text(
                        text = "245,000원 (24.5장)",
                        fontSize = 12.sp,
                        color = Color.Black,
                        fontFamily = Pretendard
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "13주",
                        fontSize = 12.sp,
                        color = Color.Black,
                        fontFamily = Pretendard
                    )
                    Text(
                        text = "350,000원 (35장)",
                        fontSize = 12.sp,
                        color = Color.Black,
                        fontFamily = Pretendard
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 두 번째 섹션
        Text(
            text = "게임스터디 코트 & 초보 코트",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontFamily = Pretendard
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFF2FAF4),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "7주",
                        fontSize = 12.sp,
                        color = Color.Black,
                        fontFamily = Pretendard
                    )
                    Text(
                        text = "195,000원 (19.5장)",
                        fontSize = 12.sp,
                        color = Color.Black,
                        fontFamily = Pretendard
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "9주",
                        fontSize = 12.sp,
                        color = Color.Black,
                        fontFamily = Pretendard
                    )
                    Text(
                        text = "275,000원 (27.5장)",
                        fontSize = 12.sp,
                        color = Color.Black,
                        fontFamily = Pretendard
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "13주",
                        fontSize = 12.sp,
                        color = Color.Black,
                        fontFamily = Pretendard
                    )
                    Text(
                        text = "390,000원 (39장)",
                        fontSize = 12.sp,
                        color = Color.Black,
                        fontFamily = Pretendard
                    )
                }
            }
        }
    }
}
