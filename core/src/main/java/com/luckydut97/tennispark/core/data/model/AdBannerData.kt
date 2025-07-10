package com.luckydut97.tennispark.core.data.model

import com.luckydut97.tennispark.core.R

data class AdBannerData(
    val imageRes: Int,
    val url: String
)

val unifiedAdBannerList = listOf(
    AdBannerData(
        imageRes = R.drawable.test_ad_img1,
        url = "https://hyggee.com/"
    ),
    AdBannerData(
        imageRes = R.drawable.test_ad_img2,
        url = "https://smartstore.naver.com/altafit"
    ),
    AdBannerData(
        imageRes = R.drawable.test_ad_img3,
        url = "https://smartstore.naver.com/tennispark"
    ),
    AdBannerData(
        imageRes = R.drawable.test_ad_img4,
        url = "https://princetennis.com/"
    )
)
