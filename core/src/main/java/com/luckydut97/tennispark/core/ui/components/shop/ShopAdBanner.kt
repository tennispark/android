package com.luckydut97.tennispark.core.ui.components.shop

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.luckydut97.tennispark.core.data.model.Advertisement
import com.luckydut97.tennispark.core.data.model.AdPosition
import com.luckydut97.tennispark.core.data.network.NetworkModule
import com.luckydut97.tennispark.core.data.repository.AdBannerRepository
import com.luckydut97.tennispark.core.data.repository.AdBannerRepositoryImpl
import com.luckydut97.tennispark.core.ui.components.ad.UnifiedAdBannerApi
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * 상품 광고 배너 컴포넌트 - API 기반 (PURCHASE position)
 * UnifiedAdBannerApi를 사용하여 통합된 광고 배너 제공
 */
@Composable
fun ShopAdBanner(
    adImages: List<Int> = emptyList(), // 호환성을 위해 유지하지만 사용하지 않음
    modifier: Modifier = Modifier
) {
    val tag = "🔍 디버깅: ShopAdBanner"

    var advertisements by remember { mutableStateOf<List<Advertisement>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    val adBannerRepository = remember {
        AdBannerRepositoryImpl(NetworkModule.apiService)
    }

    LaunchedEffect(Unit) {
        Log.d(tag, "[ShopAdBanner] loading PURCHASE advertisements")
        isLoading = true
        try {
            adBannerRepository.getAdvertisements(AdPosition.PURCHASE).collect { ads ->
                Log.d(tag, "[ShopAdBanner] received ${ads.size} PURCHASE advertisements")
                advertisements = ads
            }
        } catch (e: Exception) {
            Log.e(tag, "[ShopAdBanner] Exception: ${e.message}", e)
            advertisements = emptyList()
        } finally {
            isLoading = false
        }
    }

    if (advertisements.isNotEmpty()) {
        UnifiedAdBannerApi(
            advertisements = advertisements,
            modifier = modifier
        )
    } else if (!isLoading) {
        Log.d(tag, "[ShopAdBanner] no PURCHASE advertisements available")
    }
}
