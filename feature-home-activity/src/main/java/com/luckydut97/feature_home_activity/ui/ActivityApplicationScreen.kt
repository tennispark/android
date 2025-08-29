package com.luckydut97.feature_home_activity.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luckydut97.feature_home_activity.ui.components.ActivityCompleteBottomSheet
import com.luckydut97.feature_home_activity.ui.components.ActivityDetailBottomSheet
import com.luckydut97.feature_home_activity.ui.components.NewActivityApplicationBottomSheet
import com.luckydut97.feature_home_activity.viewmodel.ActivityApplicationViewModel
import com.luckydut97.tennispark.core.data.network.NetworkModule
import com.luckydut97.tennispark.core.data.repository.ActivityRepositoryImpl
import com.luckydut97.tennispark.core.domain.usecase.ApplyForActivityUseCase
import com.luckydut97.tennispark.core.domain.usecase.GetActivitiesUseCase
import com.luckydut97.tennispark.core.ui.components.navigation.TopBar
import com.luckydut97.tennispark.core.ui.components.calendar.CalendarComponent

/**
 * 활동 신청 메인 화면
 * 달력 + 바텀시트 구조
 */
@Composable
fun ActivityApplicationScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // ViewModel 생성 (Clean Architecture)
    val viewModel: ActivityApplicationViewModel = viewModel {
        val activityRepository = ActivityRepositoryImpl(NetworkModule.apiService)
        val getActivitiesUseCase = GetActivitiesUseCase(activityRepository)
        val applyForActivityUseCase = ApplyForActivityUseCase(activityRepository)
        ActivityApplicationViewModel(getActivitiesUseCase, applyForActivityUseCase)
    }

    // 상태 수집
    val currentYearMonth by viewModel.currentYearMonth.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val filteredActivities by viewModel.filteredActivities.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val showDetailDialog by viewModel.showDetailDialog.collectAsState()
    val selectedActivity by viewModel.selectedActivity.collectAsState()
    val showCompleteDialog by viewModel.showCompleteDialog.collectAsState()
    val isDuplicateError by viewModel.isDuplicateError.collectAsState()

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBar(
                title = "활동 신청",
                onBackClick = onBackClick
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                // 달력 컴포넌트
                CalendarComponent(
                    currentYearMonth = currentYearMonth,
                    selectedDate = selectedDate,
                    onDateSelected = { date ->
                        viewModel.selectDate(date)
                    },
                    onPreviousMonth = {
                        viewModel.goToPreviousMonth()
                    },
                    onNextMonth = {
                        viewModel.goToNextMonth()
                    },
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            // 바텀시트 (항상 표시)
            Box(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                NewActivityApplicationBottomSheet(
                    activities = filteredActivities,
                    isLoading = isLoading,
                    error = error,
                    onActivityClick = { activity ->
                        viewModel.selectActivityAndShowDetail(activity)
                    },
                    onDismiss = {
                        // 바텀시트는 항상 표시되므로 닫기 동작 없음
                        // 필요시 뒤로가기 처리
                    }
                )
            }
        }
    }

    // 상세 다이얼로그 (기존 재사용)
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

    // 완료 다이얼로그 (기존 재사용)
    if (showCompleteDialog) {
        ActivityCompleteBottomSheet(
            isVisible = showCompleteDialog,
            isDuplicateError = isDuplicateError,
            onConfirm = {
                viewModel.hideCompleteDialog()
            }
        )
    }

    // 에러 처리
    LaunchedEffect(error) {
        if (error != null) {
            // 필요시 에러 스낵바 표시 등
            // 현재는 바텀시트 내부에서 에러 표시 처리
        }
    }
}