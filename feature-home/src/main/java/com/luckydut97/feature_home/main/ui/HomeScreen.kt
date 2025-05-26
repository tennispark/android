package com.luckydut97.feature_home.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luckydut97.feature_home.main.ui.components.AdBanner
import com.luckydut97.feature_home.main.ui.components.EventSection
import com.luckydut97.feature_home.main.ui.components.HomeTopAppBar
import com.luckydut97.feature_home.main.ui.components.MainActionButtons
import com.luckydut97.feature_home.main.ui.components.WeeklyApplicationSection
import com.luckydut97.feature_home.main.viewmodel.HomeViewModel

// feature-home-activity 모듈 import (기존)
import com.luckydut97.feature_home_activity.viewmodel.WeeklyActivityViewModel
import com.luckydut97.feature_home_activity.ui.components.WeeklyActivityBottomSheet
import com.luckydut97.feature_home_activity.data.repository.MockWeeklyActivityRepository

// feature-home-activity 모듈 import (신규 - 활동인증)
import com.luckydut97.feature_home_activity.viewmodel.AppliedActivityViewModel
import com.luckydut97.feature_home_activity.ui.components.AppliedActivityBottomSheet
import com.luckydut97.feature_home_activity.data.repository.MockAppliedActivityRepository

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    val currentEventPage by viewModel.currentEventPage.collectAsState()
    val totalEventPages by viewModel.totalEventPages.collectAsState()
    val scrollState = rememberScrollState()

    // WeeklyActivity ViewModel 생성 (기존)
    val weeklyActivityViewModel: WeeklyActivityViewModel = viewModel {
        WeeklyActivityViewModel(MockWeeklyActivityRepository())
    }
    val showWeeklyActivityBottomSheet by weeklyActivityViewModel.showBottomSheet.collectAsState()

    // AppliedActivity ViewModel 생성 (신규)
    val appliedActivityViewModel: AppliedActivityViewModel = viewModel {
        AppliedActivityViewModel(MockAppliedActivityRepository())
    }
    val showAppliedActivityBottomSheet by appliedActivityViewModel.showBottomSheet.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6F8))
            .verticalScroll(scrollState)
    ) {
        // 상단 앱바
        HomeTopAppBar(
            onNotificationClick = { /* 알림 클릭 이벤트 */ },
            onSearchClick = { /* 검색 클릭 이벤트 */ }
        )

        // 광고 배너
        AdBanner()

        // 주간 신청서 섹션 - 바텀시트 연동 (기존)
        WeeklyApplicationSection(
            onApplicationClick = {
                weeklyActivityViewModel.showWeeklyApplicationSheet()
            }
        )

        // 메인 액션 버튼들 - 활동인증 버튼 연동 (업데이트)
        MainActionButtons(
            onAttendanceClick = { /* 출석체크 클릭 이벤트 */ },
            onActivityVerificationClick = {
                // 활동인증 바텀시트 표시
                appliedActivityViewModel.showAppliedActivitiesSheet()
            }
        )

        // 이벤트 섹션
        EventSection(
            currentPage = currentEventPage,
            totalPages = totalEventPages,
            onMembershipClick = { /* 멤버십 등록 클릭 이벤트 */ },
            onAcademyClick = { /* 아카데미 등록 클릭 이벤트 */ }
        )
    }

    // 주간 활동 신청 바텀시트 (기존)
    WeeklyActivityBottomSheet(
        viewModel = weeklyActivityViewModel,
        isVisible = showWeeklyActivityBottomSheet,
        onDismiss = {
            weeklyActivityViewModel.hideWeeklyApplicationSheet()
        }
    )

    // 활동인증 바텀시트 (신규)
    AppliedActivityBottomSheet(
        viewModel = appliedActivityViewModel,
        isVisible = showAppliedActivityBottomSheet,
        onDismiss = {
            appliedActivityViewModel.hideAppliedActivitiesSheet()
        }
    )
}