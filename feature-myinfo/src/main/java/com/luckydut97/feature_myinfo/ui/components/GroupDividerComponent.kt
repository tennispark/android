package com.luckydut97.feature_myinfo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 날짜 그룹 간 구분선 컴포넌트
 * 구조: 24dp 상단 여백 + 1dp 구분선 + 24dp 하단 여백
 */
@Composable
fun GroupDividerComponent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // 상단 여백
        Spacer(modifier = Modifier.height(24.dp))

        // 구분선 (스크린 가로길이 - 36dp padding)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .height(1.dp)
                .background(Color(0xFFDDDDDD))
        )

        // 하단 여백
        Spacer(modifier = Modifier.height(24.dp))
    }
}