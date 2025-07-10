package com.luckydut97.tennispark.core.ui.components.shop

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.luckydut97.tennispark.core.ui.components.ad.UnifiedAdBanner
import com.luckydut97.tennispark.core.data.model.unifiedAdBannerList

/**
 * 상품 광고 배너 컴포넌트
 * UnifiedAdBanner를 사용하여 통합된 광고 배너 제공
 */
@Composable
fun ShopAdBanner(
    adImages: List<Int> = emptyList(), // 호환성을 위해 유지하지만 사용하지 않음
    modifier: Modifier = Modifier
) {
    UnifiedAdBanner(
        bannerList = unifiedAdBannerList,
        modifier = modifier
    )
}
