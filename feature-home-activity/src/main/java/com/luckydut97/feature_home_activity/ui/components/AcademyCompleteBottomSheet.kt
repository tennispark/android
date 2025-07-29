package com.luckydut97.feature_home_activity.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.tennispark.core.ui.theme.Pretendard
import com.luckydut97.tennispark.core.ui.components.ad.UnifiedAdBannerNoPaddingApi
import com.luckydut97.feature_home_activity.viewmodel.AcademyApplicationViewModel

/**
 * 아카데미 신청 완료 Bottom Sheet
 * 크기: fillMaxWidth × 383dp (광고 배너 추가로 높이 증가)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcademyCompleteBottomSheet(
    isVisible: Boolean,
    isDuplicateError: Boolean = false, // 중복 신청 에러 여부
    academyApplicationViewModel: AcademyApplicationViewModel, // ViewModel 추가
    onConfirm: () -> Unit
) {
    val tag = "🔍 디버깅: AcademyCompleteBottomSheet"

    val activityAdvertisements by academyApplicationViewModel.activityAdvertisements.collectAsState()
    val isLoadingAds by academyApplicationViewModel.isLoadingAds.collectAsState()

    Log.d(
        tag,
        "[AcademyCompleteBottomSheet] isVisible: $isVisible, advertisements: ${activityAdvertisements.size}, isLoadingAds: $isLoadingAds"
    )

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onConfirm,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isDuplicateError) 332.dp else 332.dp)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(2.dp))

                // 제목
                Text(
                    text = if (isDuplicateError) "신청 실패" else "신청 완료",
                    fontSize = 20.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 설명 텍스트
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isDuplicateError) "이미 신청한 아카데미입니다." else "아카데미 신청이 완료되었습니다.",
                        fontSize = 16.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )

                    if (!isDuplicateError) {
                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "입금계좌 정보를 안내드리겠습니다.",
                            fontSize = 16.sp,
                            fontFamily = Pretendard,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "다른 일정의 아카데미로 추가 신청해주세요.",
                            fontSize = 16.sp,
                            fontFamily = Pretendard,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))

                // 광고 배너 - API 기반으로 변경
                if (activityAdvertisements.isNotEmpty()) {
                    Log.d(
                        tag,
                        "[AcademyCompleteBottomSheet] showing ${activityAdvertisements.size} advertisements"
                    )
                    UnifiedAdBannerNoPaddingApi(
                        advertisements = activityAdvertisements
                    )
                } else if (!isLoadingAds) {
                    Log.d(tag, "[AcademyCompleteBottomSheet] no advertisements available")
                    // 광고가 없으면 높이 조정을 위한 Spacer
                    Spacer(modifier = Modifier.height(60.dp))
                }

                Spacer(modifier = Modifier.weight(1f))

                // 확인 버튼
                Button(
                    onClick = onConfirm,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF145F44)
                    )
                ) {
                    Text(
                        text = "확인",
                        fontSize = 16.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}