package com.luckydut97.feature_home.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luckydut97.feature_home_activity.data.repository.AcademyRepositoryImpl
import com.luckydut97.feature_home_activity.data.repository.WeeklyActivityRepositoryImpl
import com.luckydut97.feature_home_activity.viewmodel.AppliedActivityViewModel
import com.luckydut97.feature_home_activity.viewmodel.AcademyApplicationViewModel
import com.luckydut97.feature_home_activity.viewmodel.WeeklyActivityViewModel
import com.luckydut97.feature_home_activity.ui.components.AppliedActivityBottomSheet
import com.luckydut97.feature_home_activity.ui.components.AcademyApplicationBottomSheet
import com.luckydut97.feature_home_activity.ui.components.WeeklyActivityBottomSheet
import com.luckydut97.feature_home_activity.data.repository.MockAppliedActivityRepository
import com.luckydut97.tennispark.core.data.network.NetworkModule
import com.luckydut97.tennispark.feature.home.R
import com.luckydut97.feature_home.main.ui.components.AdBanner
import com.luckydut97.feature_home.main.ui.components.EventSection
import com.luckydut97.feature_home.main.ui.components.HomeTopAppBar
import com.luckydut97.feature_home.main.ui.components.MainActionButtons
import com.luckydut97.feature_home.main.ui.components.WeeklyApplicationSection
import com.luckydut97.feature_home.main.ui.components.WeeklyPhotoSection
import com.luckydut97.feature_home.main.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onMembershipClick: () -> Unit = {},
    onAttendanceClick: () -> Unit = {}
) {
    val currentEventPage by viewModel.currentEventPage.collectAsState()
    val totalEventPages by viewModel.totalEventPages.collectAsState()
    val scrollState = rememberScrollState()

    // WeeklyActivity ViewModel 생성 (변경됨)
    val weeklyActivityViewModel: WeeklyActivityViewModel = viewModel {
        WeeklyActivityViewModel(WeeklyActivityRepositoryImpl())
    }
    val showWeeklyActivityBottomSheet by weeklyActivityViewModel.showBottomSheet.collectAsState()

    // AppliedActivity ViewModel 생성 (신규)
    val appliedActivityViewModel: AppliedActivityViewModel = viewModel {
        AppliedActivityViewModel(MockAppliedActivityRepository())
    }
    val showAppliedActivityBottomSheet by appliedActivityViewModel.showBottomSheet.collectAsState()

    // Academy ViewModel 생성 (실제 API 연동)
    val academyApplicationViewModel: AcademyApplicationViewModel = viewModel {
        AcademyApplicationViewModel(AcademyRepositoryImpl())
    }
    val showAcademyApplicationBottomSheet by academyApplicationViewModel.showBottomSheet.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6F8))
    ) {
        // 상단 앱바
        HomeTopAppBar(
            onNotificationClick = { /* 알림 클릭 이벤트 */ },
            onSearchClick = { /* 검색 클릭 이벤트 */ }
        )

        // 스크롤 가능한 컨텐츠
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
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
                onAttendanceClick = onAttendanceClick,
                onActivityVerificationClick = {
                    // 활동인증 바텀시트 표시
                    appliedActivityViewModel.showAppliedActivitiesSheet()
                }
            )

            // 이벤트 섹션
            EventSection(
                totalPages = totalEventPages,
                onMembershipClick = onMembershipClick,
                onAcademyClick = {
                    // TODO: 아카데미 신청 바텀시트 표시
                    academyApplicationViewModel.showAcademyApplicationSheet()
                }
            )

            /*// 이번주 활동 사진 섹션
            WeeklyPhotoSection()*/
        }
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

    // 아카데미 신청 바텀시트 (신규 - TODO: ViewModel 구현 후 활성화)
    AcademyApplicationBottomSheet(
        viewModel = academyApplicationViewModel,
        isVisible = showAcademyApplicationBottomSheet,
        onDismiss = {
            academyApplicationViewModel.hideAcademyApplicationSheet()
        }
    )
}
