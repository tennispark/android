package com.luckydut97.tennispark.core.ui.components.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.tennispark.core.ui.theme.Pretendard
import com.luckydut97.tennispark.core.R
import java.time.YearMonth

/**
 * 달력 헤더 컴포넌트
 * 년월 표시 + 월 변경 가능 (단, 이번주만 선택 가능)
 */
@Composable
fun CalendarHeader(
    currentYearMonth: YearMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 이전 달 버튼
        IconButton(
            onClick = onPreviousMonth,
            modifier = Modifier.size(44.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_left_arrow),
                contentDescription = "이전 달",
                modifier = Modifier.size(18.dp),
                tint = Color(0xFFCFCFCF)
            )
        }

        Spacer(modifier = Modifier.width(13.dp))

        // 년월 표시
        Text(
            text = "${currentYearMonth.year}년 ${currentYearMonth.monthValue}월",
            fontSize = 20.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = (-0.5).sp,
            color = Color(0xFF262626)
        )

        Spacer(modifier = Modifier.width(13.dp))

        // 다음 달 버튼
        IconButton(
            onClick = onNextMonth,
            modifier = Modifier.size(44.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_right_arrow),
                contentDescription = "다음 달",
                modifier = Modifier.size(18.dp),
                tint = Color(0xFFCFCFCF)
            )
        }
    }
}