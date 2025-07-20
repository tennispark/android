package com.luckydut97.tennispark.feature_auth.membership.ui

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
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
fun RefundPolicyScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = Color.White,
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            TopBar(
                title = "환불규정",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "1. 우천 및 설천에 따른 취소 시 쿠폰 차감 없음",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF262626),
                fontFamily = Pretendard,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "2. 우천 또는 설천에 따라 진행이 중단될 시",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF262626),
                fontFamily = Pretendard,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "- 진행타임 50% 초과 이용시 쿠폰 100% 차감",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF262626),
                fontFamily = Pretendard,
                lineHeight = 20.sp,
                modifier = Modifier.padding(start = 8.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "- 진행타임 50% 미만 이용시 쿠폰 50% 차감",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF262626),
                fontFamily = Pretendard,
                lineHeight = 20.sp,
                modifier = Modifier.padding(start = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "3. 정회원 참석 확정 후 취소 시 쿠폰 100% 차감",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF262626),
                fontFamily = Pretendard,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "- 개인사정 (출장, 부고, 교통사고, 코로나 확진 등) 으로 인한 취소 및 환불불가",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF262626),
                fontFamily = Pretendard,
                lineHeight = 20.sp,
                modifier = Modifier.padding(start = 8.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "- 타인 양도불가",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF262626),
                fontFamily = Pretendard,
                lineHeight = 20.sp,
                modifier = Modifier.padding(start = 8.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "- 쿠폰 환불 불가",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF262626),
                fontFamily = Pretendard,
                lineHeight = 20.sp,
                modifier = Modifier.padding(start = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "4. 당일 불참 시 쿠폰 200% 차감",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF262626),
                fontFamily = Pretendard,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun MembershipRegistrationScreen(
    viewModel: MembershipRegistrationViewModel = viewModel(),
    onBackClick: () -> Unit,
    onMembershipComplete: () -> Unit,
    onRefundPolicyClick: () -> Unit = {}
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
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(isMembershipComplete) {
        if (isMembershipComplete) {
            onMembershipComplete()
        }
    }

    // 에러 메시지 표시
    errorMessage?.let { message ->
        LaunchedEffect(message) {
            // TODO: Toast나 SnackBar로 에러 메시지 표시 가능
        }
    }

    Scaffold(
        containerColor = Color.White,
        modifier = Modifier.statusBarsPadding(),
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
                    fontWeight = FontWeight.SemiBold,
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
                fontSize = 11.8.sp,
                color = AppColors.TextSecondary26,
                fontFamily = Pretendard,
                letterSpacing = (-1).sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 멤버십 가입 이유
            InputField(
                value = joinReason,
                onValueChange = {
                    if (it.length <= 100) {
                        viewModel.updateJoinReason(it)
                    }
                },
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
                    fontWeight = FontWeight.SemiBold,
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

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "멤버십 비용은 오프라인 테니스 활동에 실제로 사용되는 실비용입니다.\n(코트 대관료, 물, 볼, 운영비 등 실제 활동에 필요한 비용이며, 코트별로 상이할 수 있습니다.)",
                fontSize = 12.sp,
                color = AppColors.TextSecondary26,
                fontFamily = Pretendard,
                letterSpacing = (-1).sp,
                lineHeight = 15.sp
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
                    fontWeight = FontWeight.SemiBold,
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
                    text = "게임\n/게임도전",
                    isSelected = selectedCourt == 0,
                    onClick = { viewModel.updateSelectedCourt(0) },
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp // 줄 간격 9sp
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
                    fontWeight = FontWeight.SemiBold,
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
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    ) {
                        append("* 친구 추천 혜택")
                    }
                    append("\n추천인 (멤버십 회원만 접수) : 쿠폰 5장 추가\n추천으로 멤버십 가입 시 : 쿠폰 1장 추가")
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF262626),
                fontFamily = Pretendard,
                lineHeight = 20.sp,
                letterSpacing = (-1).sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 멤버십 활동규정 동의
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "멤버십 환불규정에 동의",
                    fontSize = 14.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.Normal,
                    color = AppColors.CaptionColor,
                    letterSpacing = (-1).sp
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "환불규정",
                    fontSize = 14.sp,
                    color = Color(0xFFC4C4C4),
                    fontWeight = FontWeight.Normal,
                    fontFamily = Pretendard,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onRefundPolicyClick() },
                    letterSpacing = (-1).sp
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "예",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Pretendard,
                    color = AppColors.TextPrimary,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.width(4.dp))

                CheckBox(
                    text = "",
                    isChecked = agreeToRules,
                    onCheckedChange = {
                        viewModel.updateAgreeToRules(true)
                    }
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "아니오",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Pretendard,
                    color = AppColors.TextPrimary,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.width(4.dp))

                CheckBox(
                    text = "",
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
                fontSize = 14.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight.Normal,
                color = AppColors.CaptionColor,
                lineHeight = 23.sp,
                letterSpacing = (-1).sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "예",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Pretendard,
                    color = AppColors.TextPrimary,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.width(4.dp))

                CheckBox(
                    text = "",
                    isChecked = agreeToMediaUsage,
                    onCheckedChange = {
                        viewModel.updateAgreeToMediaUsage(true)
                    }
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "아니오",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Pretendard,
                    color = AppColors.TextPrimary,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.width(4.dp))

                CheckBox(
                    text = "",
                    isChecked = !agreeToMediaUsage,
                    onCheckedChange = {
                        viewModel.updateAgreeToMediaUsage(false)
                    }
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 친구 추천 혜택
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    ) {
                        append("* 멤버십 가입 신청 후, 운영자의 안내에 따라 계좌이체로 비용을 납부해 주세요.")
                    }
                    append("\n본 비용은 오프라인 활동을 위한 실비로, 앱 내에서 결제되거나 디지털 콘텐츠 구매에 사용되지 않습니다.")
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF262626),
                fontFamily = Pretendard,
                lineHeight = 20.sp,
                letterSpacing = (-1).sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 로딩 및 에러 상태 표시
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = AppColors.PrimaryVariant
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 에러 메시지 표시
            errorMessage?.let { message ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFFFF2F2),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = message,
                        fontSize = 14.sp,
                        color = Color(0xFFEF3629),
                        fontFamily = Pretendard,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 가입하기 버튼
            ActionButton(
                text = "가입하기",
                onClick = { viewModel.submitMembershipRegistration() },
                enabled = isSubmitEnabled && !isLoading
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
            color = AppColors.TextBrandColor14,
            fontFamily = Pretendard,
            letterSpacing = (-1).sp
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
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        fontFamily = Pretendard
                    )
                    Text(
                        text = "175,000원 (17.5장)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
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
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        fontFamily = Pretendard
                    )
                    Text(
                        text = "245,000원 (24.5장)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
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
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        fontFamily = Pretendard
                    )
                    Text(
                        text = "350,000원 (35장)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
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
            color = AppColors.TextBrandColor14,
            fontFamily = Pretendard,
            letterSpacing = (-1).sp
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
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        fontFamily = Pretendard
                    )
                    Text(
                        text = "195,000원 (19.5장)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
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
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        fontFamily = Pretendard
                    )
                    Text(
                        text = "275,000원 (27.5장)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
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
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        fontFamily = Pretendard
                    )
                    Text(
                        text = "390,000원 (39장)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        fontFamily = Pretendard
                    )
                }
            }
        }
    }
}
