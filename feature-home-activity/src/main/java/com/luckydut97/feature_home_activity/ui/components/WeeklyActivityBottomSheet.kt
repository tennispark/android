package com.luckydut97.feature_home_activity.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.feature_home_activity.domain.model.WeeklyActivity
import com.luckydut97.feature_home_activity.viewmodel.WeeklyActivityViewModel
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource
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
                // 메인 컨텐츠 박스 (fillMaxWidth × 555dp)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(555.dp)
                        .padding(horizontal = 24.dp)
                ) {
                    Column {
                        // Header Column (63dp 높이)
                        Column(
                            modifier = Modifier.height(63.dp)
                        ) {
                            // 제목과 닫기 버튼 Row (39dp 높이)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(39.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "활동 신청",
                                    fontSize = 19.sp,
                                    fontFamily = Pretendard,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.weight(1f)
                                )

                                IconButton(
                                    onClick = onDismiss,
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_close), // 닫기 아이콘 필요
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
                                color = Color(0xFF8B9096)
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // 활동 목록 Column (367×422dp)
                        Column(
                            modifier = Modifier
                                .width(367.dp)
                                .height(422.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            if (isLoading) {
                                // 로딩 상태 표시
                                Box(
                                    modifier = Modifier
                                        .width(367.dp)
                                        .height(422.dp),
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
                                        .width(367.dp)
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
                                // 활동 목록 표시 (최대 4개)
                                activities.take(4).forEach { activity ->
                                    ActivityItemComponent(
                                        activity = activity,
                                        onActivityClick = { selectedActivity ->
                                            viewModel.applyForActivity(selectedActivity.id)
                                        }
                                    )
                                }

                                // 빈 공간 채우기 (4개 미만일 경우)
                                if (activities.size < 4) {
                                    repeat(4 - activities.size) {
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
}