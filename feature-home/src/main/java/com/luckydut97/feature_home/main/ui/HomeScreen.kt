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
import com.luckydut97.feature_home.main.ui.components.PhotoUploadBottomSheet
import com.luckydut97.feature_home.main.ui.components.SuccessDialog
import com.luckydut97.feature_home.main.ui.components.WeeklyApplicationSection
import com.luckydut97.feature_home.main.ui.components.WeeklyPhotoSection
import com.luckydut97.feature_home.main.ui.components.WeeklyPhotoDownloadSection
import com.luckydut97.feature_home.main.viewmodel.HomeViewModel
import com.luckydut97.feature_home.main.viewmodel.PhotoUploadViewModel
import com.luckydut97.tennispark.core.data.repository.ActivityCertificationRepositoryImpl
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onMembershipClick: () -> Unit = {},
    onAttendanceClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {} // 알림 클릭 콜백 추가
) {
    val context = LocalContext.current
    val currentEventPage by viewModel.currentEventPage.collectAsState()
    val scrollState = rememberScrollState()

    // URL 열기 함수
    val openUrl = { url: String ->
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (e: Exception) {
            // URL 열기 실패 시 처리 (선택사항)
        }
    }

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

    // PhotoUpload ViewModel 생성 (신규)
    val photoUploadViewModel: PhotoUploadViewModel = viewModel {
        PhotoUploadViewModel(
            ActivityCertificationRepositoryImpl(NetworkModule.apiService)
        )
    }
    val showPhotoUploadBottomSheet by photoUploadViewModel.showBottomSheet.collectAsState()
    val showPhotoUploadSuccessDialog by photoUploadViewModel.showSuccessDialog.collectAsState()

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
            onNotificationClick = onNotificationClick,
            onSearchClick = { /* 검색 클릭 이벤트 */ },
            notificationCount = 155 // 테스트용 알림 개수 (8 → 50 → 128로 테스트 예정)
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
                    // 사진 업로드 바텀시트 표시
                    photoUploadViewModel.showPhotoUploadSheet()
                }
            )

            // 이벤트 섹션
            EventSection(
                totalPages = 3,
                onMembershipClick = onMembershipClick,
                onAcademyClick = {
                    // TODO: 아카데미 신청 바텀시트 표시
                    academyApplicationViewModel.showAcademyApplicationSheet()
                },
                onCourtIntroClick = {
                    openUrl("https://leeward-verdict-b34.notion.site/238bec73dd0a807eaa2afd798d0ce133?source=copy_link")
                }
            )

            // 이번주 활동 사진 섹션
            WeeklyPhotoSection()
            WeeklyPhotoDownloadSection()
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

    // 사진 업로드 바텀시트 (신규)
    PhotoUploadBottomSheet(
        viewModel = photoUploadViewModel,
        isVisible = showPhotoUploadBottomSheet,
        onDismiss = {
            photoUploadViewModel.hidePhotoUploadSheet()
        }
    )

    // 성공 다이얼로그
    SuccessDialog(
        showDialog = showPhotoUploadSuccessDialog,
        onDismiss = { photoUploadViewModel.onCloseSuccessDialog() }
    )
}
