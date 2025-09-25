package com.luckydut97.tennispark.core.ui.components.community

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.luckydut97.tennispark.core.R
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.TextLayoutResult

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
    isEnabled: Boolean = true,
    focusRequester: androidx.compose.ui.focus.FocusRequester? = null
) {
    var commentText by rememberSaveable { mutableStateOf("") }
    val textScrollState = rememberScrollState()
    val minLines = 1
    val maxLines = 4
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    val currentLines = textLayoutResult?.lineCount ?: minLines
    val requiresScroll = currentLines > maxLines

    LaunchedEffect(commentText, requiresScroll) {
        if (requiresScroll) {
            textScrollState.scrollTo(textScrollState.maxValue)
        } else {
            textScrollState.scrollTo(0)
        }
    }

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
                .heightIn(min = 80.dp)
                .padding(horizontal = 18.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
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
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                ) {
                    val textStyle = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF202020),
                        letterSpacing = (-0.5).sp,
                        lineHeight = 20.sp
                    )

                    BasicTextField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        textStyle = textStyle,
                        cursorBrush = SolidColor(Color(0xFF1C7756)),
                        enabled = isEnabled,
                        minLines = minLines,
                        maxLines = maxLines,
                        onTextLayout = { result ->
                            textLayoutResult = result
                        },
                        modifier = Modifier.let { base ->
                            focusRequester?.let { base.focusRequester(it) } ?: base
                        },
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 48.dp, max = 110.dp)
                                    .let { baseModifier ->
                                        if (requiresScroll) {
                                            baseModifier.verticalScroll(textScrollState)
                                        } else {
                                            baseModifier
                                        }
                                    }
                                    .padding(horizontal = 18.dp, vertical = 14.dp),
                                contentAlignment = Alignment.TopStart
                            ) {
                                if (commentText.isEmpty()) {
                                    Text(
                                        text = "댓글을 입력해주세요.",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = Color(0xFFC2C2C2),
                                        letterSpacing = (-0.5).sp,
                                        lineHeight = 20.sp
                                    )
                                }
                                innerTextField()
                            }
                        }
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
