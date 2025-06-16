package com.luckydut97.feature_home_activity.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.feature_home_activity.viewmodel.AcademyApplicationViewModel
import com.luckydut97.feature_home_activity.R
import com.luckydut97.tennispark.core.ui.theme.Pretendard

/**
 * 아카데미 신청 Bottom Sheet
 * 전체 크기: fillMaxWidth × 651dp (활동 신청보다 60dp 높음 - 비용 안내 텍스트 추가)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcademyApplicationBottomSheet(
    viewModel: AcademyApplicationViewModel,
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    val academies by viewModel.academies.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val showDetailDialog by viewModel.showDetailDialog.collectAsState()
    val selectedAcademy by viewModel.selectedAcademy.collectAsState()
    val showCompleteDialog by viewModel.showCompleteDialog.collectAsState()

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(651.dp)
            ) {
                // 메인 컨텐츠 박스
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(615.dp)
                        .padding(horizontal = 24.dp)
                ) {
                    Column {
                        // Header Column
                        Column(
                            modifier = Modifier.height(133.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // 제목과 닫기 버튼 Row
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(39.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "아카데미 신청",
                                    fontSize = 19.sp,
                                    fontFamily = Pretendard,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black,
                                    textAlign = TextAlign.Center
                                )

                                IconButton(
                                    onClick = onDismiss,
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .size(24.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_close),
                                        contentDescription = "닫기",
                                        modifier = Modifier.size(15.dp),
                                        tint = Color.Black
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // 부제목 및 안내문
                            val captionColor = Color(0xFF555555)
                            Text(
                                text = "원하는 활동을 선택해 주세요.",
                                fontSize = 14.sp,
                                fontFamily = Pretendard,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center,
                                color = captionColor
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            // 비용 안내(Bold + Caption Color)
                            Text(
                                text = buildAnnotatedString {
                                    append("비용은 ")
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("총 240,000원")
                                    }
                                    append("입니다.")
                                },
                                fontSize = 14.sp,
                                fontFamily = Pretendard,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center,
                                color = captionColor
                            )

                            

                            Text(
                                text = "신청 후 입금계좌 정보를 안내 드리겠습니다.",
                                fontSize = 14.sp,
                                fontFamily = Pretendard,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center,
                                color = captionColor
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // 아카데미 목록 Column
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(442.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            if (isLoading) {
                                // 로딩 상태 표시
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(442.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "아카데미 목록을 불러오는 중...",
                                        fontSize = 16.sp,
                                        fontFamily = Pretendard,
                                        color = Color(0xFF8B9096)
                                    )
                                }
                            } else if (error != null) {
                                // 에러 상태 표시
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(422.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "오류가 발생했습니다",
                                            fontSize = 16.sp,
                                            fontFamily = Pretendard,
                                            color = Color(0xFFEF3629)
                                        )
                                        Text(
                                            text = error ?: "",
                                            fontSize = 14.sp,
                                            fontFamily = Pretendard,
                                            color = Color(0xFF8B9096)
                                        )
                                    }
                                }
                            } else {
                                // 아카데미 목록 표시 (최대 4개)
                                academies.take(4).forEach { academy ->
                                    AcademyItemComponent(
                                        academy = academy,
                                        onAcademyClick = { selectedAcademy ->
                                            viewModel.selectAcademyAndShowDetail(selectedAcademy)
                                        }
                                    )
                                }

                                // 빈 공간 채우기 (4개 미만일 경우)
                                if (academies.size < 4) {
                                    repeat(4 - academies.size) {
                                        Spacer(modifier = Modifier.height(96.5.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                // 하단 빈 박스 (36dp 높이)
                Spacer(modifier = Modifier.height(36.dp))
            }
        }
    }

    // 상세 BottomSheet 표시
    if (showDetailDialog && selectedAcademy != null) {
        AcademyDetailBottomSheet(
            academy = selectedAcademy!!,
            isVisible = showDetailDialog,
            onConfirm = { academyId ->
                viewModel.applyForAcademy(academyId)
            },
            onDismiss = {
                viewModel.hideDetailDialog()
            }
        )
    }

    // 완료 BottomSheet 표시
    if (showCompleteDialog) {
        AcademyCompleteBottomSheet(
            isVisible = showCompleteDialog,
            onConfirm = {
                viewModel.hideCompleteDialog()
            }
        )
    }
}
