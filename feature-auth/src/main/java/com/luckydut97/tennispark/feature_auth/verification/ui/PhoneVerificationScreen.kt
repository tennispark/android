package com.luckydut97.tennispark.feature_auth.verification.ui

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.math.min
import com.luckydut97.tennispark.core.ui.components.button.ActionButton
import com.luckydut97.tennispark.core.ui.components.input.InputField
import com.luckydut97.tennispark.core.ui.components.button.SmallActionButton
import com.luckydut97.tennispark.core.ui.components.navigation.TopBar
import com.luckydut97.tennispark.core.ui.components.input.VerificationCodeFieldWithTimer
import com.luckydut97.tennispark.core.ui.theme.AppColors
import com.luckydut97.tennispark.feature_auth.verification.viewmodel.PhoneVerificationViewModel

@Composable
fun PhoneVerificationScreen(
    viewModel: PhoneVerificationViewModel = viewModel(),
    onBackClick: () -> Unit,
    onNavigateToSignup: () -> Unit
) {
    val phoneNumber by viewModel.phoneNumber.collectAsState()
    val verificationCode by viewModel.verificationCode.collectAsState()
    val isVerificationRequested by viewModel.isVerificationRequested.collectAsState()
    val isVerified by viewModel.isVerified.collectAsState()
    val remainingTime by viewModel.remainingTime.collectAsState()
    val isTimerActive by viewModel.isTimerActive.collectAsState()
    val isNextButtonEnabled by viewModel.isNextButtonEnabled.collectAsState()
    val navigateToSignup by viewModel.navigateToSignup.collectAsState()
    val resendCooldownTime by viewModel.resendCooldownTime.collectAsState()

    // 반응형 디자인을 위한 스케일 팩터 계산
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val widthRatio = screenWidth / 402.dp
    val heightRatio = screenHeight / 874.dp
    val scaleFactor = min(widthRatio, heightRatio)

    LaunchedEffect(navigateToSignup) {
        if (navigateToSignup) {
            viewModel.navigateToSignupComplete()
            onNavigateToSignup()
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBar(
                title = "휴대폰 본인인증",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = (17 * scaleFactor).dp)
            ) {
                Spacer(modifier = Modifier.height((40 * scaleFactor).dp))

                // 안내 메시지
                Text(
                    text = "문자로 전송된\n인증번호를 입력해 주세요.",
                    fontSize = (26 * scaleFactor).sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    lineHeight = (34 * scaleFactor).sp,
                    modifier = Modifier.height((68 * scaleFactor).dp)
                )

                Spacer(modifier = Modifier.height((17 * scaleFactor).dp))

                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    InputField(
                        value = phoneNumber,
                        onValueChange = { viewModel.updatePhoneNumber(it) },
                        label = "휴대폰 번호",
                        isRequired = true,
                        placeholder = "휴대폰 번호를 입력해주세요.",
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done,
                        enabled = !isVerificationRequested,
                        width = (261 * scaleFactor).toInt(),
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width((12 * scaleFactor).dp))

                    SmallActionButton(
                        text = "인증 받기",
                        onClick = { viewModel.requestVerification() },
                        enabled = phoneNumber.isNotEmpty() && !isVerificationRequested,
                        modifier = Modifier.width((95 * scaleFactor).dp)
                    )
                }

                // 인증번호 입력 (인증 요청 시 표시)
                if (isVerificationRequested) {
                    Spacer(modifier = Modifier.height((16 * scaleFactor).dp))

                    Column {
                        // 인증번호 입력 필드와 타이머가 포함된 Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            // 인증번호 입력 필드 (내부에 타이머 포함)
                            VerificationCodeFieldWithTimer(
                                value = verificationCode,
                                onValueChange = { viewModel.updateVerificationCode(it) },
                                remainingTime = remainingTime,
                                isTimerActive = isTimerActive,
                                width = (207 * scaleFactor).toInt(),
                                scaleFactor = scaleFactor
                            )

                            Spacer(modifier = Modifier.width((12 * scaleFactor).dp))

                            // 재전송 버튼
                            SmallActionButton(
                                text = if (resendCooldownTime > 0) "${resendCooldownTime}초" else "재전송",
                                onClick = { viewModel.resendCode() },
                                backgroundColor = Color.White,
                                contentColor = Color.Black,
                                borderColor = Color.Gray,
                                enabled = resendCooldownTime <= 0,
                                modifier = Modifier.width((78 * scaleFactor).dp)
                            )

                            Spacer(modifier = Modifier.width((12 * scaleFactor).dp))

                            // 확인 버튼
                            SmallActionButton(
                                text = "확인",
                                onClick = { viewModel.verifyCode() },
                                enabled = verificationCode.length == 6 && isTimerActive,
                                backgroundColor = Color(0xFF145F44),
                                contentColor = Color.White,
                                modifier = Modifier.width((64 * scaleFactor).dp)
                            )
                        }
                    }
                }
            }

            // 하단 버튼
            ActionButton(
                text = "다음",
                onClick = { viewModel.verifyCode() },
                enabled = isNextButtonEnabled,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding((17 * scaleFactor).dp)
                    .width((402 * scaleFactor).dp)
            )
        }
    }
}
