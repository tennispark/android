package com.luckydut97.feature_home.main.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.luckydut97.tennispark.core.ui.components.ad.UnifiedAdBanner
import com.luckydut97.tennispark.core.data.model.unifiedAdBannerList

@Composable
fun AdBanner(
    modifier: Modifier = Modifier
) {
    UnifiedAdBanner(
        bannerList = unifiedAdBannerList,
        modifier = modifier
    )
}
