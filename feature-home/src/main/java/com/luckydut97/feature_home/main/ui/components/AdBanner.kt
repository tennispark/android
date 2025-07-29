package com.luckydut97.feature_home.main.ui.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luckydut97.tennispark.core.ui.components.ad.UnifiedAdBannerApi
import com.luckydut97.feature_home.main.viewmodel.HomeViewModel

/**
 * 메인 화면용 광고 배너 컴포넌트 - API 기반
 */
@Composable
fun AdBanner(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = viewModel()
) {
    val tag = "🔍 디버깅: AdBanner"

    val advertisements by homeViewModel.mainAdvertisements.collectAsState()
    val isLoading by homeViewModel.isLoadingAds.collectAsState()

    Log.d(
        tag,
        "[AdBanner] rendering - advertisements: ${advertisements.size}, isLoading: $isLoading"
    )

    if (advertisements.isNotEmpty()) {
        UnifiedAdBannerApi(
            advertisements = advertisements,
            modifier = modifier
        )
    } else if (!isLoading) {
        // 광고가 없고 로딩 중이 아닐 때는 아무것도 표시하지 않음
        Log.d(tag, "[AdBanner] no advertisements available and not loading")
    }
}
