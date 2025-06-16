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
import com.luckydut97.feature_home_activity.R
import com.luckydut97.feature_home_activity.data.model.Academy
import com.luckydut97.feature_home_activity.data.model.AcademyStatus
import com.luckydut97.tennispark.core.ui.theme.Pretendard

/**
 * 아카데미 아이템 컴포넌트 (367×101.5dp) - ActivityItemComponent와 동일한 디자인
 */
@Composable
fun AcademyItemComponent(
    academy: Academy,
    onAcademyClick: (Academy) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDisabled = academy.status == AcademyStatus.FULL
    val isAlmostFull = academy.currentParticipants >= academy.maxParticipants - 1 && !isDisabled

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
        else -> "모집 중"
    }
    val statusTextColor = when {
        isDisabled -> Color(0xFF8B9096)
        isAlmostFull -> Color(0xFFEF3629)
        else -> Color(0xFF145F44)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(115.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(enabled = !isDisabled) {
                onAcademyClick(academy)
            }
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 왼쪽: 아카데미 정보
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // 날짜/시간 정보
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = academy.date,
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
                        text = academy.time,
                        fontSize = 17.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Normal,
                        color = textColor
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                // 게임코트 (아카데미에서는 court 사용)
                Text(
                    text = academy.court,
                    fontSize = 15.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 장소 정보
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
                        text = academy.location,
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
                        text = academy.activityType,
                        fontSize = 12.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF8B9096)
                    )
                }
            }

            // 오른쪽: 인원/상태 정보
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // 인원 표시 박스
                Box(
                    modifier = Modifier
                        .width(59.dp)
                        .height(29.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(participantBoxColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${academy.currentParticipants}/${academy.maxParticipants}",
                        fontSize = 17.43.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Normal,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // 상태 텍스트
                Text(
                    text = statusText,
                    fontSize = 12.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.SemiBold,
                    color = statusTextColor
                )
            }
        }
    }
}
