package com.luckydut97.feature_home_activity.ui.components

import android.util.Log
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import com.luckydut97.tennispark.core.domain.model.WeeklyActivity
import com.luckydut97.tennispark.core.ui.theme.Pretendard
import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars

/**
 * 고정형 드래그 가능한 바텀시트
 * 달력을 가리지 않는 반응형 높이 계산
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
    val density = LocalDensity.current
    val screenHeight = configuration.screenHeightDp.dp

    // 실제 StatusBar 높이 계산 (기기별로 다름)
    val statusBarHeight = with(density) {
        WindowInsets.systemBars.getTop(density).toDp()
    }
    val topBarHeight = 56.dp // TopBar 높이
    val calendarHeaderHeight = 44.dp // CalendarHeader (IconButton 크기)
    val calendarHeaderSpacing = 20.dp // 헤더-요일 간격
    val calendarWeekHeaderHeight = 25.dp // 요일 헤더 ("일월화수목금토")
    val calendarDividerSpacing = 19.dp // 요일-구분선-그리드 간격 (9+1+9)
    val calendarGridHeight = 274.dp // CalendarGrid 고정 높이
    val calendarBottomMargin = 20.dp // 달력 하단 여백 (여유분)

    // 전체 달력 높이 계산 (StatusBar 포함)
    val totalCalendarHeight =
        statusBarHeight + topBarHeight + calendarHeaderHeight + calendarHeaderSpacing +
            calendarWeekHeaderHeight + calendarDividerSpacing +
            calendarGridHeight + calendarBottomMargin

    // 달력을 가리지 않는 최대 높이 계산 (마지막 주차 직후까지)
    val availableHeight = screenHeight - totalCalendarHeight

    // 기본 컴포넌트 높이
    val headerHeight = 30.dp
    val topPadding = 10.dp
    val bottomPadding = 36.dp
    val itemHeight = 79.dp
    val itemSpacing = 8.dp

    // 필수 고정 높이 (헤더 + 패딩들)
    val fixedHeight = headerHeight + topPadding + bottomPadding

    // 활동 개수별 컨텐츠 높이 계산
    val contentHeight = when (activities.size) {
        0 -> 60.dp // 빈 상태 메시지 공간
        1 -> itemHeight
        else -> {
            val totalItemsHeight = (activities.size * itemHeight.value).dp
            val totalSpacingHeight = ((activities.size - 1) * itemSpacing.value).dp
            totalItemsHeight + totalSpacingHeight
        }
    }

    // 이상적인 높이 (고정 + 컨텐츠)
    val idealHeight = fixedHeight + contentHeight

    // 실제 타겟 높이 (달력을 가리지 않도록 제한)
    val targetHeight = when {
        idealHeight <= availableHeight -> idealHeight // 이상적 높이가 가능하면 사용
        activities.size <= 1 -> idealHeight.coerceAtMost(availableHeight) // 1개 이하는 무조건 보여주기
        else -> {
            // 여러 개일 때는 최대 높이 사용 (스크롤로 모든 활동 접근 가능)
            availableHeight
        }
    }

    Log.d("🔍 BottomSheet", "=== 바텀시트 높이 계산 ===")
    Log.d("🔍 BottomSheet", "screenHeight: $screenHeight")
    Log.d("🔍 BottomSheet", "실제 statusBarHeight: $statusBarHeight (동적 계산)")
    Log.d("🔍 BottomSheet", "topBarHeight: $topBarHeight")
    Log.d("🔍 BottomSheet", "totalCalendarHeight: $totalCalendarHeight")
    Log.d("🔍 BottomSheet", "  └─ 실제 statusBarHeight: $statusBarHeight")
    Log.d("🔍 BottomSheet", "  └─ topBarHeight: $topBarHeight")
    Log.d("🔍 BottomSheet", "  └─ calendarHeaderHeight: $calendarHeaderHeight")
    Log.d("🔍 BottomSheet", "  └─ calendarHeaderSpacing: $calendarHeaderSpacing")
    Log.d("🔍 BottomSheet", "  └─ calendarWeekHeaderHeight: $calendarWeekHeaderHeight")
    Log.d("🔍 BottomSheet", "  └─ calendarDividerSpacing: $calendarDividerSpacing")
    Log.d("🔍 BottomSheet", "  └─ calendarGridHeight: $calendarGridHeight")
    Log.d("🔍 BottomSheet", "  └─ calendarBottomMargin: $calendarBottomMargin")
    Log.d("🔍 BottomSheet", "availableHeight: $availableHeight")
    Log.d("🔍 BottomSheet", "activities.size: ${activities.size}")
    Log.d("🔍 BottomSheet", "idealHeight: $idealHeight")
    Log.d("🔍 BottomSheet", "targetHeight: $targetHeight")
    Log.d("🔍 BottomSheet", "=== 계산 완료 ===")
    Log.d("🔍 BottomSheet", "📱 기기별 차이 - statusBar 높이가 핵심!")

    // 드래그 상태 관리 - 드래그 상태는 영구 유지, 높이는 LaunchedEffect에서 관리
    var currentHeight by remember { mutableStateOf(targetHeight) }
    var userHasDragged by remember { mutableStateOf(false) }

    // 최소/최대 높이 제한 (반응형)
    val minHeight = (fixedHeight + 60.dp).coerceAtMost(availableHeight * 0.3f) // 최소 30%
    val maxHeight = availableHeight.coerceAtMost(screenHeight * 0.7f) // 최대 70%

    // 활동 목록이 변경될 때마다 높이 재조정 - 개선된 로직
    LaunchedEffect(activities, targetHeight) {
        if (!userHasDragged) {
            // 드래그 안했으면 항상 새로운 targetHeight로 조정
            currentHeight = targetHeight
        } else {
            // 드래그 했어도 활동 개수가 크게 변했으면 자동 조정
            val activityCountDiff =
                kotlin.math.abs(activities.size - (currentHeight.value - fixedHeight.value) / (itemHeight.value + itemSpacing.value))

            when {
                // 활동이 0개가 되면 무조건 작게 조정
                activities.isEmpty() -> {
                    currentHeight = targetHeight
                    userHasDragged = false // 빈 상태에서는 드래그 상태 리셋
                }
                // 활동 개수가 크게 변했으면 (3개 이상 차이) 자동 조정
                activityCountDiff >= 3 -> {
                    currentHeight = targetHeight
                }
                // 현재 높이가 새로운 범위를 벗어나면 조정
                currentHeight < minHeight -> currentHeight = minHeight
                currentHeight > maxHeight -> currentHeight = maxHeight
                // 그 외에는 사용자 설정 유지
            }
        }
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
                                userHasDragged = true
                                // 드래그 종료 시 스냅 (제거)
                                // val halfWay = (targetHeight.value + maxHeight.value) / 2
                                // currentHeight = if (currentHeight.value < halfWay) {
                                //     targetHeight
                                // } else {
                                //     maxHeight
                                // }
                            }
                        ) { _, dragAmount ->
                            val newHeightValue = currentHeight.value - (dragAmount.y / 2)
                            currentHeight = when {
                                newHeightValue < minHeight.value -> minHeight
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
                                .fillMaxSize()
                                .offset(y = (-10).dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "선택 가능한 활동이 없습니다",
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
                                verticalArrangement = Arrangement.spacedBy(8.dp)
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