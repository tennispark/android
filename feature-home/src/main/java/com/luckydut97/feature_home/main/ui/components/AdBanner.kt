package com.luckydut97.feature_home.main.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.width
import com.luckydut97.tennispark.feature.home.R

@Composable
fun AdBanner() {
    // 투명한 전체폭 컨테이너
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.67.dp), // 배너 높이만큼
        contentAlignment = Alignment.Center // 중앙 정렬
    ) {
        // 실제 배너
        Box(
            modifier = Modifier
                .width(362.dp)
                .height(120.67.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_ad_ex),
                contentDescription = "Advertisement banner",
                modifier = Modifier.size(width = 362.dp, height = 120.67.dp)
            )
        }
    }
}