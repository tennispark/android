package com.luckydut97.feature_home_activity.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.luckydut97.feature_home_activity.domain.model.ActivityStatus
import com.luckydut97.feature_home_activity.domain.model.WeeklyActivity
// import com.luckydut97.tennispark.core.R
import com.luckydut97.tennispark.core.ui.theme.Pretendard

/**
 * 활동 아이템 컴포넌트 (367×96.5dp)
 */
@Composable
fun ActivityItemComponent(
    activity: WeeklyActivity,
    onActivityClick: (WeeklyActivity) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDisabled = activity.status == ActivityStatus.FULL
    val isAlmostFull = activity.isAlmostFull

    // 상태에 따른 색상 정의
    val backgroundColor = if (isDisabled) Color(0xFFF4F6F8) else Color(0xFFF2FAF4)
    val borderColor = if (isDisabled) Color(0xFF8B9096) else Color(0xFF145F44)
    val textColor = if (isDisabled) Color(0xFF8B9096) else Color.Black
    val participantBoxColor = when {
        isDisabled -> Color(0xFF8B9096)
        isAlmostFull -> Color(0xFFEF3629)
        else -> Color(0xFF145F44)
    }
    val statusText = when {
        isDisabled -> "모집완료"
        isAlmostFull -> "마감임박"
        else -> "모집중"
    }
    val statusTextColor = when {
        isDisabled -> Color(0xFF8B9096)
        isAlmostFull -> Color(0xFFEF3629)
        else -> Color(0xFF145F44)
    }

    Box(
        modifier = modifier
            .width(367.dp)
            .height(96.5.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(enabled = !isDisabled) {
                onActivityClick(activity)
            }
            .padding(18.dp)
    ) {
        Row(
            modifier = Modifier
                .width(331.dp)
                .height(60.5.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 객체1: 활동 정보 (272×60.5dp)
            Column(
                modifier = Modifier
                    .width(272.dp)
                    .height(60.5.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // 날짜/시간 정보
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = activity.formattedDate,
                        fontSize = 17.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.SemiBold,
                        color = textColor
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
                        text = activity.formattedTime,
                        fontSize = 17.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Normal,
                        color = textColor
                    )
                }

                // 게임코트
                Text(
                    text = activity.gameCode,
                    fontSize = 15.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor
                )

                // 장소 정보
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 위치 아이콘 (임시로 Text 사용, 실제 아이콘으로 교체 필요)
                    Text(
                        text = "📍",
                        fontSize = 10.sp,
                        modifier = Modifier.size(10.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = activity.location,
                        fontSize = 12.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.SemiBold,
                        color = textColor
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
                        text = activity.court,
                        fontSize = 12.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Normal,
                        color = textColor
                    )
                }
            }

            // 객체2: 인원/상태 정보 (59×51dp)
            Column(
                modifier = Modifier
                    .width(59.dp)
                    .height(51.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // 인원 표시 박스 (59×29dp)
                Box(
                    modifier = Modifier
                        .width(59.dp)
                        .height(29.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(participantBoxColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = activity.participantInfo,
                        fontSize = 17.43.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Normal,
                        color = Color.White
                    )
                }

                // 상태 텍스트
                Text(
                    text = statusText,
                    fontSize = 10.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.Normal,
                    color = statusTextColor
                )
            }
        }
    }
}