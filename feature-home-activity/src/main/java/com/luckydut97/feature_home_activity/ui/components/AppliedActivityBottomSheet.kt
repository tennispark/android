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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.feature_home_activity.viewmodel.AppliedActivityViewModel
import com.luckydut97.feature_home_activity.R
import com.luckydut97.tennispark.core.ui.theme.Pretendard

/**
 * 활동인증(신청한 활동 목록) Bottom Sheet - 단순 버전
 * 전체 크기: fillMaxWidth × 591dp (기존 WeeklyActivityBottomSheet와 동일)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppliedActivityBottomSheet(
    viewModel: AppliedActivityViewModel,
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    val appliedActivities by viewModel.appliedActivities.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

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
                    .height(591.dp)
            ) {
                // 메인 컨텐츠 박스
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(575.dp)
                        .padding(horizontal = 24.dp)
                ) {
                    Column {
                        // Header Column
                        Column(
                            modifier = Modifier.height(73.dp),
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
                                    text = "활동 인증",
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

                            // 부제목
                            Text(
                                text = "신청한 활동 내역을 확인해 주세요.",
                                fontSize = 16.sp,
                                fontFamily = Pretendard,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center,
                                color = Color(0xFF8B9096)
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // 활동 목록 Column
                        Column(
                            modifier = Modifier
                                .width(367.dp)
                                .height(442.dp)
                        ) {
                            if (isLoading) {
                                // 로딩 상태 표시
                                Box(
                                    modifier = Modifier
                                        .width(367.dp)
                                        .height(442.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "활동 내역을 불러오는 중...",
                                        fontSize = 16.sp,
                                        fontFamily = Pretendard,
                                        color = Color(0xFF8B9096)
                                    )
                                }
                            } else if (appliedActivities.isEmpty()) {
                                // 빈 상태 표시
                                Box(
                                    modifier = Modifier
                                        .width(367.dp)
                                        .height(442.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "신청한 활동이 없습니다",
                                            fontSize = 16.sp,
                                            fontFamily = Pretendard,
                                            color = Color(0xFF8B9096)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "활동을 신청해보세요!",
                                            fontSize = 14.sp,
                                            fontFamily = Pretendard,
                                            color = Color(0xFFAAAAAA)
                                        )
                                    }
                                }
                            } else {
                                // 활동 목록 표시 - LazyColumn으로 스크롤 가능
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(appliedActivities) { appliedActivity ->
                                        AppliedActivityItemComponent(
                                            appliedActivity = appliedActivity
                                        )
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
}