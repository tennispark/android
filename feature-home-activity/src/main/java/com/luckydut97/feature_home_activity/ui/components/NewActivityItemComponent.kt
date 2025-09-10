package com.luckydut97.feature_home_activity.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.feature_home_activity.R
import com.luckydut97.tennispark.core.domain.model.ActivityStatus
import com.luckydut97.tennispark.core.domain.model.WeeklyActivity
import com.luckydut97.tennispark.core.ui.components.animation.PressableComponent
import com.luckydut97.tennispark.core.ui.theme.Pretendard

/**
 * 새로운 활동 아이템 컴포넌트 (71dp 높이)
 * 좌우 분할 레이아웃과 상태별 색상 테마 + 반응형 폰트
 */
@Composable
fun NewActivityItemComponent(
    activity: WeeklyActivity,
    onActivityClick: (WeeklyActivity) -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = getActivityTheme(activity.status)

    // 화면 크기에 따른 반응형 폰트 크기 계산
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    // 기준: 393dp (일반적인 안드로이드 화면), 갤럭시 S25는 약 360dp 정도
    val fontScale = (screenWidth / 393f).coerceIn(0.85f, 1.0f) // 최소 85%, 최대 100%

    val gameCodeFontSize: TextUnit = (14 * fontScale).sp
    val dateTimeFontSize: TextUnit = (14 * fontScale).sp
    val locationFontSize: TextUnit = (12 * fontScale).sp
    val participantInfoFontSize: TextUnit = (13 * fontScale).sp
    val statusFontSize: TextUnit = (12 * fontScale).sp

    PressableComponent(
        onClick = { onActivityClick(activity) },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .height(79.dp),
        enabled = activity.status != ActivityStatus.FULL
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(71.dp)
                .shadow(
                    elevation = 1.dp,
                    shape = RoundedCornerShape(8.dp)
                )
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .border(
                    width = 1.dp,
                    color = theme.strokeColor,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                // 좌측 영역 (71dp × 71dp)
                LeftSection(
                    participantInfo = activity.participantInfo,
                    status = activity.status,
                    theme = theme,
                    participantInfoFontSize = participantInfoFontSize,
                    statusFontSize = statusFontSize
                )

                // 우측 영역
                RightSection(
                    gameCode = activity.gameCode,
                    formattedDate = activity.formattedDate,
                    formattedTime = activity.formattedTime,
                    location = activity.location,
                    court = activity.court,
                    theme = theme,
                    gameCodeFontSize = gameCodeFontSize,
                    dateTimeFontSize = dateTimeFontSize,
                    locationFontSize = locationFontSize
                )
            }
        }
    }
}

@Composable
private fun LeftSection(
    participantInfo: String,
    status: ActivityStatus,
    theme: ActivityItemTheme,
    participantInfoFontSize: TextUnit,
    statusFontSize: TextUnit
) {
    Box(
        modifier = Modifier
            .width(71.dp)
            .fillMaxHeight()
            .background(theme.leftBackgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 인원 표시 박스
            Box(
                modifier = Modifier
                    .width(54.dp)
                    .height(24.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(theme.participantBoxColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = participantInfo,
                    fontSize = participantInfoFontSize,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = (-0.5).sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 상태 텍스트
            Text(
                text = when (status) {
                    ActivityStatus.RECRUITING -> "모집 중"
                    ActivityStatus.ALMOST_FULL -> "마감 임박"
                    ActivityStatus.FULL -> "모집 완료"
                    else -> "모집 중"
                },
                fontSize = statusFontSize,
                fontFamily = Pretendard,
                fontWeight = FontWeight.SemiBold,
                color = theme.statusTextColor
            )
        }
    }
}

@Composable
private fun RightSection(
    gameCode: String,
    formattedDate: String,
    formattedTime: String,
    location: String,
    court: String,
    theme: ActivityItemTheme,
    gameCodeFontSize: TextUnit,
    dateTimeFontSize: TextUnit,
    locationFontSize: TextUnit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            // 게임코드 + 날짜 + 시간 (한 줄)
            Row {
                // 게임코드 (SemiBold)
                Text(
                    text = gameCode,
                    fontSize = gameCodeFontSize,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = (-0.5).sp,
                    color = theme.gameCodeTextColor
                )

                Spacer(modifier = Modifier.width(6.dp))

                // 날짜 + 시간 (Medium)
                Text(
                    text = "$formattedDate $formattedTime",
                    fontSize = dateTimeFontSize,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = (-0.5).sp,
                    color = theme.gameCodeTextColor
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 구분선
            HorizontalDivider(
                color = Color(0xFFDDDDDD),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 위치 정보
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = "위치",
                    modifier = Modifier.size(10.dp),
                    tint = theme.locationIconColor
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "$location $court",
                    fontSize = locationFontSize,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.SemiBold,
                    color = theme.locationTextColor
                )
            }
        }
    }
}

/**
 * 상태별 테마 정의
 */
private sealed class ActivityItemTheme(
    val strokeColor: Color,
    val leftBackgroundColor: Color,
    val participantBoxColor: Color,
    val statusTextColor: Color,
    val gameCodeTextColor: Color,
    val locationIconColor: Color,
    val locationTextColor: Color
) {
    object Recruiting : ActivityItemTheme(
        strokeColor = Color(0xFFC0D0CA),
        leftBackgroundColor = Color(0xFFE3F4EE),
        participantBoxColor = Color(0xFF145F44),
        statusTextColor = Color(0xFF145F44),
        gameCodeTextColor = Color(0xFF262626),
        locationIconColor = Color(0xFF8B9096),
        locationTextColor = Color(0xFF8B9096)
    )

    object AlmostFull : ActivityItemTheme(
        strokeColor = Color(0xFFEDC9C6),
        leftBackgroundColor = Color(0xFFFFF4EF),
        participantBoxColor = Color(0xFFEF3629),
        statusTextColor = Color(0xFFEF3629),
        gameCodeTextColor = Color(0xFF262626),
        locationIconColor = Color(0xFF8B9096),
        locationTextColor = Color(0xFF8B9096)
    )

    object Full : ActivityItemTheme(
        strokeColor = Color(0xFFD6D6D6),
        leftBackgroundColor = Color(0xFFEEEEEE),
        participantBoxColor = Color(0xFFBEBEBE),
        statusTextColor = Color(0xFFBEBEBE),
        gameCodeTextColor = Color(0xFFBEBEBE),
        locationIconColor = Color(0xFFBEBEBE),
        locationTextColor = Color(0xFFBEBEBE)
    )
}

private fun getActivityTheme(status: ActivityStatus): ActivityItemTheme {
    return when (status) {
        ActivityStatus.RECRUITING -> ActivityItemTheme.Recruiting
        ActivityStatus.ALMOST_FULL -> ActivityItemTheme.AlmostFull
        ActivityStatus.FULL -> ActivityItemTheme.Full
        else -> ActivityItemTheme.Recruiting
    }
}