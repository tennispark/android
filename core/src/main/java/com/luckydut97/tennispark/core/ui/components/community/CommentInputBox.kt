package com.luckydut97.tennispark.core.ui.components.community

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.luckydut97.tennispark.core.R

/**
 * 댓글 입력 박스 컴포넌트
 */
@Composable
fun CommentInputBox(
    onSendComment: (String) -> Unit,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedImage: Uri? = null,
    onImageRemove: (() -> Unit)? = null,
    isEnabled: Boolean = true
) {
    var commentText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        // 선택된 이미지 미리보기
        if (selectedImage != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 8.dp)
            ) {
                AsyncImage(
                    model = selectedImage,
                    contentDescription = "첨부된 이미지",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                // 삭제 버튼
                if (onImageRemove != null) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .offset(x = 50.dp, y = (-5).dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                            .clickable { onImageRemove() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "×",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.offset(x = 0.6.dp, y = (-3).dp)
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(horizontal = 18.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 앨범 아이콘
                IconButton(
                    onClick = onImageClick,
                    modifier = Modifier.size(28.dp),
                    enabled = isEnabled
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_album),
                        contentDescription = "사진 첨부",
                        tint = Color(0xFFC2C2C2),
                        modifier = Modifier.size(24.dp)
                    )
                }

                // 댓글 입력 필드
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (commentText.isEmpty()) {
                        Text(
                            text = "댓글을 입력해주세요.",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFFC2C2C2),
                            letterSpacing = (-0.5).sp,
                            modifier = Modifier.padding(horizontal = 18.dp)
                        )
                    }

                    TextField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        modifier = Modifier.fillMaxSize(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true,
                        enabled = isEnabled
                    )
                }

                // 등록 버튼
                TextButton(
                    onClick = {
                        if (commentText.isNotBlank()) {
                            onSendComment(commentText.trim())
                            commentText = ""
                        }
                    },
                    enabled = commentText.isNotBlank() && isEnabled
                ) {
                    Text(
                        text = "등록",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (commentText.isNotBlank() && isEnabled)
                            Color(0xFF1C7756) else Color(0xFFC2C2C2),
                        letterSpacing = (-0.5).sp
                    )
                }
            }
        }
    }
}