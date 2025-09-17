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
import com.luckydut97.tennispark.core.domain.model.CommunityComment
import com.luckydut97.tennispark.core.R

/**
 * 댓글 아이템 컴포넌트
 */
@Composable
fun CommentItem(
    comment: CommunityComment,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 13.dp)
    ) {
        // 작성자 정보 및 더보기 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 작성자명
                Text(
                    text = comment.authorName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF202020),
                    letterSpacing = (-0.5).sp
                )

                Spacer(modifier = Modifier.width(8.dp))

                // 작성 시간
                Text(
                    text = comment.relativeTimeText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFFD1D1D1),
                    letterSpacing = (-0.5).sp
                )
            }

            // 더보기 버튼
            IconButton(
                onClick = onMoreClick,
                modifier = Modifier.size(20.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_detail_card),
                    contentDescription = "더보기",
                    tint = Color(0xFF8B9096),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // 댓글 내용
        Text(
            text = comment.content,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF202020),
            letterSpacing = (-0.5).sp,
            lineHeight = 20.sp
        )
    }
}