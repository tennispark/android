package com.luckydut97.tennispark.core.ui.components.community

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.luckydut97.tennispark.core.domain.model.CommunityPost
import com.luckydut97.tennispark.core.R
import com.luckydut97.tennispark.core.ui.components.animation.PressableComponent
import com.luckydut97.tennispark.core.ui.components.common.LinkifiedText
import com.luckydut97.tennispark.core.utils.ImageDownloadManager
import android.widget.Toast
import kotlinx.coroutines.launch

/**
 * 커뮤니티 게시글 카드 컴포넌트
 */
@Composable
fun CommunityPostCard(
    post: CommunityPost,
    onPostClick: () -> Unit,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onAlarmClick: () -> Unit,
    onEditClick: (CommunityPost) -> Unit,
    onDeleteClick: (CommunityPost) -> Unit,
    onReportClick: (CommunityPost) -> Unit = {},
    isDetailView: Boolean = false, // 상세 화면 여부
    isNotificationToggling: Boolean = false,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }

    // 이미지 다운로드 관련 상태
    var showDownloadDialog by remember { mutableStateOf(false) }
    var selectedImageUrl by remember { mutableStateOf("") }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val imageDownloadManager = remember { ImageDownloadManager(context) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onPostClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp)
        ) {
            // 작성자 정보 및 액션 버튼들
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    // 작성자명
                    Text(
                        text = post.authorName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF202020),
                        letterSpacing = (-0.5).sp
                    )

                    Spacer(modifier = Modifier.height(0.dp))

                    // 작성 시간
                    Text(
                        text = post.relativeTimeText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFFD1D1D1),
                        letterSpacing = (-0.5).sp
                    )
                }

                // 알림, 더보기 버튼
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (post.notificationEnabled != null) {
                        val alarmIconRes = if (post.notificationEnabled == true) {
                            R.drawable.ic_alarm_card_on
                        } else {
                            R.drawable.ic_alarm_card
                        }
                        PressableComponent(
                            onClick = {
                                if (!isNotificationToggling) {
                                    onAlarmClick()
                                }
                            },
                            modifier = Modifier.size(20.dp),
                            enabled = !isNotificationToggling
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = alarmIconRes),
                                    contentDescription = "알림",
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Box {
                        PressableComponent(
                            onClick = { showMenu = true },
                            modifier = Modifier.size(20.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_detail_card),
                                    contentDescription = "더보기",
                                    tint = Color(0xFF8B9096),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            containerColor = Color.Transparent,
                            tonalElevation = 0.dp,
                            shadowElevation = 0.dp,
                            shape = RectangleShape
                        ) {
                            CommunityOverflowMenu(
                                isAuthor = post.authoredByMe,
                                onEditClick = {
                                    showMenu = false
                                    onEditClick(post)
                                },
                                onDeleteClick = {
                                    showMenu = false
                                    onDeleteClick(post)
                                },
                                onReportClick = {
                                    showMenu = false
                                    onReportClick(post)
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 이미지 렌더링 로그 (상세 화면에서만)
            if (isDetailView) {
                LaunchedEffect(post.id) {
                    println("🖼️ ===== CommunityPostCard 상세 이미지 렌더링 =====")
                    println("🖼️ 게시글 ID: ${post.id}")
                    println("🖼️ photos Map: ${post.photos}")
                    println("🖼️ sortedPhotos List: ${post.sortedPhotos}")
                    println("🖼️ sortedPhotos 개수: ${post.sortedPhotos.size}")
                }
            }

            // 제목
            Text(
                text = post.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF202020),
                letterSpacing = (-0.5).sp,
                maxLines = if (isDetailView) Int.MAX_VALUE else 1,
                overflow = if (isDetailView) TextOverflow.Visible else TextOverflow.Ellipsis,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onPostClick() }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 내용 (5줄 제한)
            LinkifiedText(
                text = post.content,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF202020),
                    letterSpacing = (-0.5).sp,
                    lineHeight = 24.sp
                ),
                maxLines = if (isDetailView) Int.MAX_VALUE else 5,
                overflow = if (isDetailView) TextOverflow.Visible else TextOverflow.Ellipsis,
                onTextClick = onPostClick
            )

            // 더보기 버튼 (홈 화면에서 5줄 이상일 때만 표시)
            if (!isDetailView && post.isContentLong) {
                Text(
                    text = "더보기",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF5AB97D),
                    letterSpacing = (-0.5).sp,
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onPostClick() } // 더보기 클릭시 상세 화면으로
                        .padding(top = 4.dp)
                )
            }

            // photos 표시 (상세 화면용 - 내용 바로 아래) - 클릭 기능 추가
            if (isDetailView && post.sortedPhotos.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                post.sortedPhotos.forEachIndexed { index, photoUrl ->
                    AsyncImage(
                        model = photoUrl,
                        contentDescription = "게시글 이미지 ${index + 1}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .clickable {
                                selectedImageUrl = photoUrl
                                showDownloadDialog = true
                            },
                        contentScale = ContentScale.FillWidth
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 조회수
            Text(
                text = "${post.formattedViewCount}명이 봤어요",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFFD1D1D1),
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 좋아요 및 댓글 수
            Text(
                text = "좋아요 ${post.likeCount}개 · 댓글 ${post.commentCount}개",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF8B9096),
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 구분선
            Divider(
                color = Color(0xFFF5F5F5),
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 하단 액션 버튼들
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                // 좋아요 버튼
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onLikeClick() }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (post.likedByMe) R.drawable.ic_like_on else R.drawable.ic_like_off
                        ),
                        contentDescription = "좋아요",
                        tint = if (post.likedByMe) Color(0xFF1C7756) else Color(0xFF8B9096),
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "좋아요",
                        fontSize = 14.sp,
                        fontWeight = if (post.likedByMe) FontWeight.Bold else FontWeight.Normal,
                        color = if (post.likedByMe) Color(0xFF1C7756) else Color(0xFF8B9096),
                        letterSpacing = (-0.5).sp
                    )
                }

                // 댓글쓰기 버튼
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onCommentClick() }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_speech_balloon),
                        contentDescription = "댓글쓰기",
                        tint = Color(0xFF8B9096),
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "댓글쓰기",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF8B9096),
                        letterSpacing = (-0.5).sp
                    )
                }
            }
        }
    }

    // 이미지 다운로드 다이얼로그
    if (showDownloadDialog) {
        ImageDownloadDialog(
            onDismiss = { showDownloadDialog = false },
            onConfirm = {
                coroutineScope.launch {
                    val result = imageDownloadManager.downloadImage(selectedImageUrl)
                    result.fold(
                        onSuccess = { message ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        },
                        onFailure = { error ->
                            Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
                        }
                    )
                    showDownloadDialog = false
                }
            }
        )
    }
}
