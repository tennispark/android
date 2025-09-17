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
import androidx.compose.foundation.layout.imePadding

/**
 * 게시글 작성 화면 상단바 컴포넌트
 */
@Composable
fun CommunityWriteTopBar(
    onBackClick: () -> Unit,
    onCompleteClick: () -> Unit,
    isCompleteEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .padding(horizontal = 18.dp)
            .imePadding(),
        contentAlignment = Alignment.Center
    ) {
        // 뒤로가기 버튼 (좌측)
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(32.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_left_arrow),
                contentDescription = "뒤로가기",
                tint = Color.Black,
                modifier = Modifier.size(15.dp)
            )
        }

        // 제목 (정중앙)
        Text(
            text = "게시글 작성",
            fontSize = 19.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            letterSpacing = (-0.5).sp,
            modifier = Modifier.align(Alignment.Center)
        )

        // 완료 버튼 (우측)
        TextButton(
            onClick = onCompleteClick,
            enabled = isCompleteEnabled,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Text(
                text = "완료",
                fontSize = 19.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isCompleteEnabled) Color(0xFF1C7756) else Color(0xFFBEBEBE),
                letterSpacing = (-0.5).sp
            )
        }
    }
}