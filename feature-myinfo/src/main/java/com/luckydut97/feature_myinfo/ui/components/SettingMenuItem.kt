package com.luckydut97.feature_myinfo.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.feature_myinfo.R
import com.luckydut97.tennispark.core.ui.theme.Pretendard

/**
 * 설정 메뉴 아이템 컴포넌트
 *
 * @param title 메뉴 제목
 * @param onClick 클릭 이벤트 핸들러
 */
@Composable
fun SettingMenuItem(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(46.dp)
            .padding(horizontal = 18.dp)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = Pretendard,
            color = Color.Black,
            letterSpacing = (-1).sp
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right_mypage),
            contentDescription = "이동",
            modifier = Modifier.size(16.dp),
            tint = Color.Unspecified
        )
    }
}