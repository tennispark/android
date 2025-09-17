package com.luckydut97.tennispark.core.ui.components.community

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
 * 키보드 위에 고정되는 사진 첨부 바 컴포넌트
 */
@Composable
fun PhotoAttachmentBar(
    onPhotoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(Color.White)
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .clickable { onPhotoClick() }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_album),
                contentDescription = "사진 첨부",
                tint = Color(0xFF8B9096),
                modifier = Modifier.size(15.31.dp)
            )

            Text(
                text = "사진",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF8B9096),
                letterSpacing = (-0.5).sp
            )
        }
    }
}