package com.luckydut97.tennispark.core.ui.components.community

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.tennispark.core.R

/**
 * 커뮤니티 상단바 컴포넌트
 */
@Composable
fun CommunityTopBar(
    onSearchClick: () -> Unit,
    onAlarmClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 커뮤니티 텍스트
        Text(
            text = "커뮤니티",
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            letterSpacing = (-0.5).sp
        )

        // 검색 및 알림 아이콘
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onSearchClick,
                modifier = Modifier.size(27.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "검색",
                    tint = Color.Black,
                    modifier = Modifier.size(27.dp)
                )
            }

            IconButton(
                onClick = onAlarmClick,
                modifier = Modifier.size(27.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_alarm),
                    contentDescription = "알림",
                    tint = Color.Black,
                    modifier = Modifier.size(27.dp)
                )
            }
        }
    }
}