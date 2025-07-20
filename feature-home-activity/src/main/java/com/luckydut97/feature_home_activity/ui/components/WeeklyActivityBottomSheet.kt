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
    val isDuplicateError by viewModel.isDuplicateError.collectAsState()

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
                    .height(if (activities.isEmpty() && !isLoading && error == null) 178.dp else 591.dp)
            ) {
                // 메인 컨텐츠 박스
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (activities.isEmpty() && !isLoading && error == null) 142.dp else 575.dp)
                        .padding(horizontal = 24.dp)
                ) {
                    Column {
                        // Header Column - 조건부 높이
                        Column(
                            modifier = Modifier.height(if (activities.isEmpty() && !isLoading && error == null) 39.dp else 73.dp),
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

                            // 부제목 - 목록이 있을 때만 표시
                            if (!(activities.isEmpty() && !isLoading && error == null)) {
                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "원하는 활동을 선택해 주세요.",
                                    fontSize = 16.sp,
                                    fontFamily = Pretendard,
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.Center,
                                    color = Color(0xFF8B9096)
                                )
                            }
                        }

                        // 목록이 있을 때만 여백 추가
                        if (!(activities.isEmpty() && !isLoading && error == null)) {
                            Spacer(modifier = Modifier.height(18.dp))
                        }

                        // 활동 목록 영역
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(if (activities.isEmpty() && !isLoading && error == null) 85.dp else 442.dp)
                        ) {
                            if (isLoading) {
                                // 로딩 상태 표시
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(if (activities.isEmpty() && !isLoading && error == null) 85.dp else 442.dp),
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
                                        .height(if (activities.isEmpty() && !isLoading && error == null) 85.dp else 422.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = error ?: "오류가 발생했습니다.",
                                        fontSize = 16.sp,
                                        fontFamily = Pretendard,
                                        color = Color(0xFFEF3629),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            } else if (activities.isEmpty()) {
                                // 빈 상태 표시 (0개 조회)
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(if (activities.isEmpty() && !isLoading && error == null) 85.dp else 422.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "현재 신청할 수 있는 활동이 없습니다.\n매주 금요일에 새로운 활동 모집이 진행됩니다.",
                                        fontSize = 16.sp,
                                        fontFamily = Pretendard,
                                        color = Color(0xFF8B9096),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            } else {
                                // 활동 목록 표시 - LazyColumn으로 스크롤 가능
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth(),
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
                if (!(activities.isEmpty() && !isLoading && error == null)) {
                    Spacer(modifier = Modifier.height(36.dp))
                }
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
            isDuplicateError = isDuplicateError,
            onConfirm = {
                viewModel.hideCompleteDialog()
            }
        )
    }
}
