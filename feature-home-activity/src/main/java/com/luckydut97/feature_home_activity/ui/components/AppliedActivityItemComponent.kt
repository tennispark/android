package com.luckydut97.feature_home_activity.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.feature_home_activity.R
import com.luckydut97.feature_home_activity.domain.model.AppliedActivity
import com.luckydut97.tennispark.core.ui.theme.Pretendard

/**
 * 신청한 활동 아이템 컴포넌트 (단순 버전) - 367×96.5dp
 */
@Composable
fun AppliedActivityItemComponent(
    appliedActivity: AppliedActivity,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(107.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF2FAF4)) // 연한 초록색 배경
            .border(
                width = 1.dp,
                color = Color(0xFF145F44), // 진한 초록색 테두리
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 첫 번째 줄: 날짜/시간 정보
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = appliedActivity.formattedDate,
                    fontSize = 17.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.width(8.dp))

                // 세로 구분선
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(16.5.dp)
                        .background(Color(0xFFDDDDDD))
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = appliedActivity.formattedTime,
                    fontSize = 17.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            // 두 번째 줄: 게임코트
            Text(
                text = appliedActivity.gameCode,
                fontSize = 15.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 세 번째 줄: 장소 정보
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 위치 아이콘
                Icon(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = "위치",
                    modifier = Modifier.size(10.dp),
                    tint = Color(0xFF8B9096)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = appliedActivity.location,
                    fontSize = 12.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF8B9096)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // 세로 구분선
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(12.dp)
                        .background(Color(0xFFDDDDDD))
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = appliedActivity.court,
                    fontSize = 12.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF8B9096)
                )
            }
        }
    }
}
