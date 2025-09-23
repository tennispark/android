package com.luckydut97.tennispark.core.ui.components.community

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.tennispark.core.R
import com.luckydut97.tennispark.core.ui.theme.Pretendard

@Composable
fun CommunityRecentSearchItem(
    text: String,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit,
    onRemove: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(text) }
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_search_time),
            contentDescription = null,
            tint = Color(0xFF666666),
            modifier = Modifier
                .width(16.dp)
                .height(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            color = Color(0xFF202020),
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = Pretendard,
            letterSpacing = (-0.5).sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = {
                onRemove(text)
            },
            modifier = Modifier
                .width(32.dp)
                .height(32.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_exe),
                contentDescription = null,
                tint = Color(0xFF666666),
                modifier = Modifier
                    .width(16.dp)
                    .height(16.dp)
            )
        }
    }
}
