package com.luckydut97.feature_myinfo.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.tennispark.core.domain.model.ActivityApplication
import com.luckydut97.tennispark.core.ui.theme.Pretendard

/**
 * 활동 신청 내역 개별 아이템 컴포넌트
 * 높이: 45dp
 */
@Composable
fun ActivityApplicationItemComponent(
    application: ActivityApplication,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(45.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        // 왼쪽: 활동 정보
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Top
        ) {
            // 장소(왼쪽)와 코트 타입(오른쪽)을 분리해 장소만 '...' 처리
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = application.place,
                    fontSize = 18.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF262626),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )

                Text(
                    text = " - ${application.courtType}",
                    fontSize = 18.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF262626),
                    modifier = Modifier.padding(end = 7.dp)
                )
            }

            // 날짜 시간
            Text(
                text = "${application.formattedActivityDate} ${application.formattedActivityTime}",
                fontSize = 14.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF6F6F6F)
            )
        }

        // 오른쪽: 상태
        Text(
            text = application.applicationStatus.displayText,
            fontSize = 18.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight.Bold,
            color = Color(android.graphics.Color.parseColor(application.applicationStatus.colorCode)),
            modifier = Modifier.align(Alignment.Top)
        )
    }
}
