package com.luckydut97.tennispark.feature_auth.sms.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luckydut97.tennispark.core.ui.components.button.ActionButton
import com.luckydut97.tennispark.core.ui.components.input.InputField
import com.luckydut97.tennispark.core.ui.components.button.SmallActionButton
import com.luckydut97.tennispark.core.ui.components.navigation.TopBar
import com.luckydut97.tennispark.core.ui.components.input.VerificationCodeField
import com.luckydut97.tennispark.core.ui.theme.AppColors
import com.luckydut97.tennispark.feature_auth.sms.viewmodel.PhoneVerificationViewModel

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

    LaunchedEffect(navigateToSignup) {
        if (navigateToSignup) {
            viewModel.navigateToSignupComplete()
            onNavigateToSignup()
        }
    }

    Scaffold(
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
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                // 안내 메시지
                Text(
                    text = "문자로 전송된\n인증번호를 입력해 주세요.",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 34.sp,
                    modifier = Modifier.height(68.dp)
                )

                Spacer(modifier = Modifier.height(17.dp))

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
                        modifier = Modifier.weight(1f)
                    )



                    SmallActionButton(
                        text = "인증 받기",
                        onClick = { viewModel.requestVerification() },
                        enabled = phoneNumber.isNotEmpty() && !isVerificationRequested,
                        modifier = Modifier.width(95.dp)
                    )
                }

                // 인증번호 입력 (인증 요청 시 표시)
                if (isVerificationRequested) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        VerificationCodeField(
                            value = verificationCode,
                            onValueChange = { viewModel.updateVerificationCode(it) },
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // 타이머
                        if (isTimerActive) {
                            val minutes = remainingTime / 60
                            val seconds = remainingTime % 60
                            Text(
                                text = String.format("%02d:%02d", minutes, seconds),
                                color = AppColors.Timer,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.width(52.dp),
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        SmallActionButton(
                            text = "재전송",
                            onClick = { viewModel.resendCode() },
                            modifier = Modifier.width(78.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        SmallActionButton(
                            text = "확인",
                            onClick = { viewModel.verifyCode() },
                            enabled = verificationCode.length == 6 && isTimerActive,
                            modifier = Modifier.width(64.dp)
                        )
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
                    .padding(20.dp)
                    .width(402.dp)
            )
        }
    }
}