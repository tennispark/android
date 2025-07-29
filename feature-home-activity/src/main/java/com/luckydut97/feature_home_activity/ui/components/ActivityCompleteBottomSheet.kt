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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.tennispark.core.ui.theme.Pretendard
import com.luckydut97.tennispark.core.ui.components.ad.UnifiedAdBannerNoPaddingApi
import com.luckydut97.tennispark.core.data.model.Advertisement
import com.luckydut97.tennispark.core.data.model.AdPosition
import com.luckydut97.tennispark.core.data.network.NetworkModule
import com.luckydut97.tennispark.core.data.repository.AdBannerRepositoryImpl

/**
 * 활동 신청 완료 Bottom Sheet
 * 크기: fillMaxWidth × 352dp (광고 배너 추가로 높이 증가)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityCompleteBottomSheet(
    isVisible: Boolean,
    isDuplicateError: Boolean = false, // 중복 신청 에러 여부
    onConfirm: () -> Unit
) {
    val tag = "🔍 디버깅: ActivityCompleteBottomSheet"

    var advertisements by remember { mutableStateOf<List<Advertisement>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    val adBannerRepository = remember {
        AdBannerRepositoryImpl(NetworkModule.apiService)
    }

    LaunchedEffect(Unit) {
        Log.d(tag, "[ActivityCompleteBottomSheet] loading ACTIVITY advertisements")
        isLoading = true
        try {
            adBannerRepository.getAdvertisements(AdPosition.ACTIVITY).collect { ads ->
                Log.d(
                    tag,
                    "[ActivityCompleteBottomSheet] received ${ads.size} ACTIVITY advertisements"
                )
                advertisements = ads
            }
        } catch (e: Exception) {
            Log.e(tag, "[ActivityCompleteBottomSheet] Exception: ${e.message}", e)
            advertisements = emptyList()
        } finally {
            isLoading = false
        }
    }

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
                    .height(if (isDuplicateError) 352.dp else 352.dp)
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
                        text = if (isDuplicateError) "이미 신청한 활동입니다." else "활동 신청이 완료되었습니다.",
                        fontSize = 16.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )

                    if (!isDuplicateError) {
                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "선별 과정을 거쳐 선정된 분들께 개별 연락드리겠습니다.",
                            fontSize = 16.sp,
                            fontFamily = Pretendard,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "다른 활동으로 추가 신청해주세요.",
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
                if (advertisements.isNotEmpty()) {
                    Log.d(
                        tag,
                        "[ActivityCompleteBottomSheet] showing ${advertisements.size} advertisements"
                    )
                    UnifiedAdBannerNoPaddingApi(
                        advertisements = advertisements
                    )
                } else if (!isLoading) {
                    Log.d(tag, "[ActivityCompleteBottomSheet] no advertisements available")
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