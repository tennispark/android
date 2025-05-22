package com.luckydut97.feature_home.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
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
import com.luckydut97.tennispark.core.ui.components.navigation.BottomNavigationBar
import com.luckydut97.tennispark.core.ui.components.navigation.BottomNavigationItem

// feature-home-activity 모듈 import
import com.luckydut97.feature_home_activity.viewmodel.WeeklyActivityViewModel
import com.luckydut97.feature_home_activity.ui.components.WeeklyActivityBottomSheet
import com.luckydut97.feature_home_activity.data.repository.MockWeeklyActivityRepository

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onNavigateToRoute: (String) -> Unit
) {
    val currentEventPage by viewModel.currentEventPage.collectAsState()
    val totalEventPages by viewModel.totalEventPages.collectAsState()
    val scrollState = rememberScrollState()

    // WeeklyActivity ViewModel 생성
    val activityViewModel: WeeklyActivityViewModel = viewModel {
        WeeklyActivityViewModel(MockWeeklyActivityRepository())
    }
    val showBottomSheet by activityViewModel.showBottomSheet.collectAsState()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = BottomNavigationItem.HOME.route,
                onItemClick = onNavigateToRoute
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F6F8))
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            // 상단 앱바
            HomeTopAppBar(
                onNotificationClick = { /* 알림 클릭 이벤트 */ },
                onSearchClick = { /* 검색 클릭 이벤트 */ }
            )

            // 광고 배너
            AdBanner()

            // 주간 신청서 섹션 - 바텀시트 연동
            WeeklyApplicationSection(
                onApplicationClick = {
                    activityViewModel.showWeeklyApplicationSheet()
                }
            )

            // 메인 액션 버튼들
            MainActionButtons(
                onAttendanceClick = { /* 출석체크 클릭 이벤트 */ },
                onActivityVerificationClick = { /* 활동인증 클릭 이벤트 */ }
            )

            // 이벤트 섹션
            EventSection(
                currentPage = currentEventPage,
                totalPages = totalEventPages,
                onMembershipClick = { /* 멤버십 등록 클릭 이벤트 */ },
                onAcademyClick = { /* 아카데미 등록 클릭 이벤트 */ }
            )
        }

        // 주간 활동 신청 바텀시트
        WeeklyActivityBottomSheet(
            viewModel = activityViewModel,
            isVisible = showBottomSheet,
            onDismiss = {
                activityViewModel.hideWeeklyApplicationSheet()
            }
        )
    }
}