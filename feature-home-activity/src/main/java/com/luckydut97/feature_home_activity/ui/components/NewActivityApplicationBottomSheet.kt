package com.luckydut97.feature_home_activity.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import com.luckydut97.tennispark.core.domain.model.WeeklyActivity
import com.luckydut97.tennispark.core.ui.theme.Pretendard

/**
 * 고정형 드래그 가능한 바텀시트
 * 간단하고 안정적인 구현
 */
@Composable
fun NewActivityApplicationBottomSheet(
    activities: List<WeeklyActivity>,
    isLoading: Boolean,
    error: String?,
    onActivityClick: (WeeklyActivity) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    // 활동 개수별 고정 높이 설정 (상단 여백 10dp 추가)
    val targetHeight = when (activities.size) {
        0 -> 158.dp // 148 + 10
        1 -> 158.dp // 148 + 10
        2 -> 240.dp // 230 + 10
        3 -> 322.dp // 312 + 10
        4 -> 404.dp // 394 + 10
        else -> 488.dp // 478 + 10 (5개 이상, 최대 + 스크롤)
    }

    val maxHeight = 488.dp

    // 드래그 상태 관리
    var currentHeight by remember { mutableStateOf(targetHeight) }

    // 활동 개수가 변경될 때마다 목표 높이로 자동 업데이트
    if (currentHeight != targetHeight) {
        currentHeight = targetHeight
    }

    val animatedHeight by animateDpAsState(
        targetValue = currentHeight,
        animationSpec = tween(durationMillis = 350),
        label = "bottomSheetHeight"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(animatedHeight)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .background(
                color = Color(0xFFF2FAF4),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // 드래그 가능한 헤더 영역
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragEnd = {
                                // 드래그 종료 시 스냅
                                val halfWay = (targetHeight.value + maxHeight.value) / 2
                                currentHeight = if (currentHeight.value < halfWay) {
                                    targetHeight
                                } else {
                                    maxHeight
                                }
                            }
                        ) { _, dragAmount ->
                            val newHeightValue = currentHeight.value - (dragAmount.y / 2)
                            currentHeight = when {
                                newHeightValue < targetHeight.value -> targetHeight
                                newHeightValue > maxHeight.value -> maxHeight
                                else -> newHeightValue.dp
                            }
                        }
                    }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                // 드래그 핸들
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .background(
                            color = Color(0xFFDDDDDD),
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }

            // 컨텐츠 영역
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "활동 목록을 불러오는 중...",
                                fontSize = 16.sp,
                                fontFamily = Pretendard,
                                color = Color(0xFF8B9096)
                            )
                        }
                    }

                    error != null -> {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = error,
                                fontSize = 16.sp,
                                fontFamily = Pretendard,
                                color = Color(0xFFEF3629),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    activities.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "선택 가능한 활동이 없습니다.",
                                fontSize = 16.sp,
                                fontFamily = Pretendard,
                                color = Color(0xFF8B9096),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    else -> {
                        Column {
                            // 상단 여백
                            Spacer(modifier = Modifier.height(10.dp))

                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 36.dp),
                                verticalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                items(activities) { activity ->
                                    NewActivityItemComponent(
                                        activity = activity,
                                        onActivityClick = onActivityClick
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}