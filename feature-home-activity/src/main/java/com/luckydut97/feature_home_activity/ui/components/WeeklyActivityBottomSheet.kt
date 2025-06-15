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
import com.luckydut97.feature_home_activity.viewmodel.WeeklyActivityViewModel
import com.luckydut97.feature_home_activity.R
import com.luckydut97.tennispark.core.ui.theme.Pretendard

/**
 * 주간 활동 신청 Bottom Sheet
 * 전체 크기: fillMaxWidth × 591dp
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyActivityBottomSheet(
    viewModel: WeeklyActivityViewModel,
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    val activities by viewModel.activities.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val showDetailDialog by viewModel.showDetailDialog.collectAsState()
    val selectedActivity by viewModel.selectedActivity.collectAsState()
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
                                    text = "활동 신청",
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
                                text = "원하는 활동을 선택해 주세요.",
                                fontSize = 16.sp,
                                fontFamily = Pretendard,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center,
                                color = Color(0xFF8B9096)
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // 활동 목록 LazyColumn으로 변경
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(442.dp)
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
                                        text = "활동 목록을 불러오는 중...",
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
                                // 활동 목록 표시 - LazyColumn으로 스크롤 가능
                                LazyColumn(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(activities) { activity ->
                                        ActivityItemComponent(
                                            activity = activity,
                                            onActivityClick = { selectedActivity ->
                                                // 변경: 바로 신청하지 않고 상세 다이얼로그 표시
                                                viewModel.selectActivityAndShowDetail(
                                                    selectedActivity
                                                )
                                            }
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

    // 상세 BottomSheet 표시
    if (showDetailDialog && selectedActivity != null) {
        ActivityDetailBottomSheet(
            activity = selectedActivity!!,
            isVisible = showDetailDialog,
            onConfirm = { activityId ->
                viewModel.applyForActivity(activityId)
            },
            onDismiss = {
                viewModel.hideDetailDialog()
            }
        )
    }

    // 완료 BottomSheet 표시
    if (showCompleteDialog) {
        ActivityCompleteBottomSheet(
            isVisible = showCompleteDialog,
            onConfirm = {
                viewModel.hideCompleteDialog()
            }
        )
    }
}
