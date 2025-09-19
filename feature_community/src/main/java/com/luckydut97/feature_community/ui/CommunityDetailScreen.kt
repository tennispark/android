package com.luckydut97.feature_community.ui

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luckydut97.feature_community.viewmodel.CommunityDetailViewModel
import com.luckydut97.tennispark.core.domain.model.CommunityComment
import com.luckydut97.tennispark.core.domain.model.CommunityPost
import com.luckydut97.tennispark.core.ui.components.community.CommunityDetailTopBar
import com.luckydut97.tennispark.core.ui.components.community.CommunityPostCard
import com.luckydut97.tennispark.core.ui.components.community.CommentItem
import com.luckydut97.tennispark.core.ui.components.community.CommentInputBox
import com.luckydut97.tennispark.core.ui.components.community.ConfirmDeleteDialog
import com.luckydut97.tennispark.core.ui.theme.Pretendard

/**
 * 커뮤니티 게시글 상세 화면
 */
@Composable
fun CommunityDetailScreen(
    postId: Int,
    onBackClick: () -> Unit,
    onAlarmClick: () -> Unit,
    onEditPost: (CommunityPost) -> Unit = {},
    onDeletePost: (CommunityPost) -> Unit = {},
    onEditComment: (CommunityComment) -> Unit = {},
    onDeleteComment: (CommunityComment) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: CommunityDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedImage by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    var pendingPostDelete by remember { mutableStateOf<CommunityPost?>(null) }
    var pendingCommentDelete by remember { mutableStateOf<CommunityComment?>(null) }

    // 갤러리 선택 런처
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImage = uri
    }

    LaunchedEffect(postId) {
        viewModel.loadPostDetail(postId)
    }

    // 댓글 작성 성공/실패 처리
    LaunchedEffect(uiState.createCommentSuccess) {
        if (uiState.createCommentSuccess) {
            selectedImage = null
            viewModel.clearCreateCommentState()
            Toast.makeText(context, "댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(uiState.createCommentError) {
        uiState.createCommentError?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            viewModel.clearCreateCommentState()
        }
    }

    LaunchedEffect(uiState.deleteCommentSuccess) {
        if (uiState.deleteCommentSuccess) {
            viewModel.clearDeleteCommentState()
            Toast.makeText(context, "댓글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(uiState.deleteCommentError) {
        uiState.deleteCommentError?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            viewModel.clearDeleteCommentState()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            CommunityDetailTopBar(
                onBackClick = onBackClick,
                onSearchClick = { /* TODO: 검색 기능 */ },
                onAlarmClick = onAlarmClick
            )
        },
        bottomBar = {
            // 댓글 입력창
            CommentInputBox(
                onSendComment = { comment ->
                    viewModel.createComment(
                        postId = postId,
                        content = comment,
                        imageUri = selectedImage
                    )
                    selectedImage = null
                },
                onImageClick = {
                    galleryLauncher.launch("image/*")
                },
                selectedImage = selectedImage,
                onImageRemove = {
                    selectedImage = null
                },
                isEnabled = !uiState.isCreatingComment
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFF5AB97D)
                        )
                    }
                }

                uiState.errorMessage != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = uiState.errorMessage ?: "오류가 발생했습니다",
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    viewModel.clearError()
                                    viewModel.loadPostDetail(postId)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF5AB97D)
                                )
                            ) {
                                Text("다시 시도")
                            }
                        }
                    }
                }

                uiState.post != null -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                        // 게시글 카드 (더보기 없이 전체 내용 표시)
                        item {
                        CommunityPostCard(
                            post = uiState.post!!,
                            onPostClick = { /* 이미 상세 화면이므로 무시 */ },
                            onLikeClick = { viewModel.toggleLike() },
                            onCommentClick = { /* 이미 상세 화면이므로 무시 */ },
                            onAlarmClick = onAlarmClick,
                            onEditClick = { post ->
                                if (post.authoredByMe) {
                                    onEditPost(post)
                                } else {
                                    Toast.makeText(context, "작성자가 아닙니다.", Toast.LENGTH_SHORT).show()
                                }
                            },
                            onDeleteClick = { post ->
                                if (post.authoredByMe) {
                                    pendingPostDelete = post
                                } else {
                                    Toast.makeText(context, "작성자가 아닙니다.", Toast.LENGTH_SHORT).show()
                                }
                            },
                            isDetailView = true
                        )
                    }

                        // 구분선
                        item {
                        Spacer(
                            modifier = Modifier
                                .height(18.dp)
                                .fillMaxWidth()
                                .background(Color(0xFFF5F5F5))
                        )
                    }

                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Spacer(modifier = Modifier.height(18.dp))
                            Text(
                                text = "댓글 ${uiState.comments.size}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                letterSpacing = (-0.5).sp,
                                color = Color(0xFF8B9096),
                                fontFamily = Pretendard,
                                modifier = Modifier.padding(horizontal = 18.dp)
                            )
                        }
                    }

                    // 댓글 로딩 표시
                        if (uiState.isLoadingComments) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        color = Color(0xFF5AB97D),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }

                        // 댓글 목록
                        items(uiState.comments) { comment ->
                            CommentItem(
                                comment = comment,
                                onEditClick = {
                                    if (comment.authoredByMe) {
                                        onEditComment(comment)
                                    } else {
                                        Toast.makeText(context, "작성자가 아닙니다.라고", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                onDeleteClick = {
                                    if (comment.authoredByMe) {
                                        pendingCommentDelete = comment
                                    } else {
                                        Toast.makeText(context, "작성자가 아닙니다.라고", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                        }

                        // 댓글이 없을 때
                        if (!uiState.isLoadingComments && uiState.comments.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 24.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "첫 번째 댓글을 작성해보세요.",
                                        color = Color.Gray
                                    )
                                }
                            }
                        }

                        // 하단 여백 (댓글 입력창 공간 확보)
                        item {
                            Spacer(modifier = Modifier.height(18.dp))
                        }
                    }
                }
            }

            if (uiState.isDeletingComment) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF5AB97D))
                }
            }
        }
    }

    pendingPostDelete?.let { post ->
        ConfirmDeleteDialog(
            onConfirm = {
                onDeletePost(post)
                pendingPostDelete = null
            },
            onDismiss = { pendingPostDelete = null }
        )
    }

    pendingCommentDelete?.let { comment ->
        ConfirmDeleteDialog(
            onConfirm = {
                onDeleteComment(comment)
                pendingCommentDelete = null
            },
            onDismiss = { pendingCommentDelete = null }
        )
    }
}
