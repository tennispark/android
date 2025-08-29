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
import com.luckydut97.feature_home_activity.viewmodel.AppliedActivityViewModel
import com.luckydut97.feature_home_activity.viewmodel.AcademyApplicationViewModel
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
import com.luckydut97.tennispark.core.data.repository.ActivityRepositoryImpl
import com.luckydut97.tennispark.core.data.repository.AcademyRepositoryImpl
import com.luckydut97.tennispark.core.data.repository.ActivityCertificationRepositoryImpl
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import com.luckydut97.feature_home_activity.ui.components.AcademyApplicationBottomSheet
import com.luckydut97.feature_home_activity.ui.components.AppliedActivityBottomSheet
import com.luckydut97.tennispark.core.data.network.NetworkModule
import com.luckydut97.tennispark.core.domain.usecase.ApplyForAcademyUseCase
import com.luckydut97.tennispark.core.domain.usecase.GetAcademiesUseCase

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onMembershipClick: () -> Unit = {},
    onAttendanceClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onActivityApplicationClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val currentEventPage by viewModel.currentEventPage.collectAsState()
    val scrollState = rememberScrollState()

    val openUrl = { url: String ->
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (e: Exception) {
            // URL 열기 실패 시 처리 (선택사항)
        }
    }

    val appliedActivityViewModel: AppliedActivityViewModel = viewModel {
        val activityRepository = ActivityRepositoryImpl(NetworkModule.apiService)
        AppliedActivityViewModel(activityRepository)
    }
    val showAppliedActivityBottomSheet by appliedActivityViewModel.showBottomSheet.collectAsState()

    val photoUploadViewModel: PhotoUploadViewModel = viewModel {
        PhotoUploadViewModel(
            ActivityCertificationRepositoryImpl(NetworkModule.apiService)
        )
    }
    val showPhotoUploadBottomSheet by photoUploadViewModel.showBottomSheet.collectAsState()
    val showPhotoUploadSuccessDialog by photoUploadViewModel.showSuccessDialog.collectAsState()

    val academyApplicationViewModel: AcademyApplicationViewModel = viewModel {
        val academyRepository = AcademyRepositoryImpl(NetworkModule.apiService)
        val getAcademiesUseCase = GetAcademiesUseCase(academyRepository)
        val applyForAcademyUseCase = ApplyForAcademyUseCase(academyRepository)
        AcademyApplicationViewModel(getAcademiesUseCase, applyForAcademyUseCase)
    }
    val showAcademyApplicationBottomSheet by academyApplicationViewModel.showBottomSheet.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6F8))
    ) {
        HomeTopAppBar(
            onNotificationClick = onNotificationClick,
            onSearchClick = { /* 검색 클릭 이벤트 */ }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            AdBanner()

            WeeklyApplicationSection(
                onApplicationClick = onActivityApplicationClick
            )

            MainActionButtons(
                onAttendanceClick = onAttendanceClick,
                onActivityVerificationClick = {
                    photoUploadViewModel.showPhotoUploadSheet()
                }
            )

            EventSection(
                totalPages = 3,
                onMembershipClick = onMembershipClick,
                onAcademyClick = {
                    academyApplicationViewModel.showAcademyApplicationSheet()
                },
                onCourtIntroClick = {
                    openUrl("https://leeward-verdict-b34.notion.site/238bec73dd0a807eaa2afd798d0ce133?source=copy_link")
                }
            )

            WeeklyPhotoSection()
            WeeklyPhotoDownloadSection()
        }
    }

    AppliedActivityBottomSheet(
        viewModel = appliedActivityViewModel,
        isVisible = showAppliedActivityBottomSheet,
        onDismiss = {
            appliedActivityViewModel.hideAppliedActivitiesSheet()
        }
    )

    AcademyApplicationBottomSheet(
        viewModel = academyApplicationViewModel,
        isVisible = showAcademyApplicationBottomSheet,
        onDismiss = {
            academyApplicationViewModel.hideAcademyApplicationSheet()
        }
    )

    PhotoUploadBottomSheet(
        viewModel = photoUploadViewModel,
        isVisible = showPhotoUploadBottomSheet,
        onDismiss = {
            photoUploadViewModel.hidePhotoUploadSheet()
        }
    )

    SuccessDialog(
        showDialog = showPhotoUploadSuccessDialog,
        onDismiss = { photoUploadViewModel.onCloseSuccessDialog() }
    )
}
