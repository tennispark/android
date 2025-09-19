package com.luckydut97.tennispark.core.ui.components.community

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CommunityOverflowMenu(
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(61.dp)
            .background(Color.Transparent),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        MenuItem(
            text = "수정",
            onClick = onEditClick,
            shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.7.dp)
                .background(Color(0xFFE3E3E3))
        )

        MenuItem(
            text = "삭제",
            onClick = onDeleteClick,
            shape = RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp)
        )
    }
}

@Composable
private fun MenuItem(
    text: String,
    onClick: () -> Unit,
    shape: RoundedCornerShape
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(38.dp)
            .clip(shape)
            .background(Color(0xFFF5F5F5))
            .clickable { onClick() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF000000)
        )
    }
}
