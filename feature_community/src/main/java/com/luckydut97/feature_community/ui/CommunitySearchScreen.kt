package com.luckydut97.feature_community.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luckydut97.feature_community.ui.components.ReportDialog
import com.luckydut97.feature_community.viewmodel.CommunitySearchViewModel
import com.luckydut97.tennispark.core.R
import com.luckydut97.tennispark.core.domain.model.CommunityPost
import com.luckydut97.tennispark.core.ui.components.community.ConfirmDeleteDialog
import com.luckydut97.tennispark.core.ui.components.community.CommunityPostCard
import com.luckydut97.tennispark.core.ui.components.community.CommunityRecentSearchItem
import com.luckydut97.tennispark.core.ui.theme.Pretendard
import kotlinx.coroutines.launch

@Composable
fun CommunitySearchScreen(
    onBackClick: () -> Unit,
    onSearch: (String) -> Unit,
    onPostClick: (Int) -> Unit,
    onEditPost: (CommunityPost) -> Unit = {},
    onDeletePost: (CommunityPost) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: CommunitySearchViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var pendingDeletePost by remember { mutableStateOf<CommunityPost?>(null) }
    var reportingPost by remember { mutableStateOf<CommunityPost?>(null) }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    LaunchedEffect(uiState.notificationError) {
        uiState.notificationError?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            viewModel.clearNotificationError()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.resetForEntry()
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    val searchPerformed = uiState.lastKeyword != null

    LaunchedEffect(searchPerformed) {
        if (!searchPerformed) {
            pendingDeletePost = null
            reportingPost = null
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 18.dp)
        ) {
            Spacer(modifier = Modifier.height(18.dp))
            TopBar(
                query = uiState.query,
                onQueryChange = viewModel::updateQuery,
                onBackClick = onBackClick,
                focusRequester = focusRequester,
                keyboardController = keyboardController,
                onSearchClick = {
                    viewModel.commitSearch {
                        keyboardController?.hide()
                        onSearch(it)
                    }
                }
            )

            Spacer(modifier = Modifier.height(if (searchPerformed) 24.dp else 36.dp))

            if (!searchPerformed) {
                HeaderRow(
                    canClear = uiState.recentSearches.isNotEmpty(),
                    onClearAll = {
                        viewModel.clearAll()
                    }
                )

                Spacer(modifier = Modifier.height(22.dp))

                if (uiState.recentSearches.isEmpty()) {
                    Text(
                        text = "최근 검색어가 없습니다.",
                        color = Color(0xFF8B9096),
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        letterSpacing = (-0.5).sp
                    )
                } else {
                    RecentSearchList(
                        items = uiState.recentSearches,
                        onSelect = { keyword ->
                            viewModel.selectRecent(keyword)
                            viewModel.commitSearch {
                                keyboardController?.hide()
                                onSearch(it)
                            }
                        },
                        onRemove = viewModel::removeRecent
                    )
                }
            }
        }

        if (searchPerformed) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = true)
            ) {
                when {
                    uiState.results.isNotEmpty() -> {
                        SearchResultList(
                            posts = uiState.results,
                            onPostClick = onPostClick,
                            onLikeClick = { postId -> viewModel.toggleLike(postId) },
                            onCommentClick = onPostClick,
                            onEditPost = onEditPost,
                            onDeletePost = { pendingDeletePost = it },
                            onReportPost = { reportingPost = it },
                            onToggleAlarm = { post ->
                                post.notificationEnabled?.let { viewModel.toggleNotification(post.id) }
                            },
                            isNotificationUpdating = { id -> uiState.notificationUpdatingPostIds.contains(id) },
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    !uiState.isLoading && uiState.errorMessage == null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "검색 결과가 없습니다.",
                                    fontSize = 20.sp,
                                    fontFamily = Pretendard,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF202020)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "다른 검색어를 입력해 주세요.",
                                    fontSize = 15.sp,
                                    fontFamily = Pretendard,
                                    fontWeight = FontWeight.Normal,
                                    color = Color(0xFF8B9096),
                                    letterSpacing = (-0.5).sp
                                )
                            }
                        }
                    }
                }

                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF1C7756))
                    }
                }
            }
        } else {
            Spacer(modifier = Modifier.weight(1f, fill = true))
        }

        pendingDeletePost?.let { post ->
            ConfirmDeleteDialog(
                onConfirm = {
                    if (!post.authoredByMe) {
                        Toast.makeText(context, "작성자가 아닙니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        onDeletePost(post)
                    }
                    pendingDeletePost = null
                },
                onDismiss = { pendingDeletePost = null }
            )
        }

        reportingPost?.let { post ->
            ReportDialog(
                onConfirm = { reason ->
                    val trimmed = reason.trim()
                    if (trimmed.isEmpty()) {
                        Toast.makeText(context, "신고 사유를 입력해주세요.", Toast.LENGTH_SHORT).show()
                        return@ReportDialog
                    }
                    if (trimmed.length > 1000) {
                        Toast.makeText(context, "신고 사유는 1,000자 이하여야 합니다.", Toast.LENGTH_SHORT).show()
                        return@ReportDialog
                    }

                    coroutineScope.launch {
                        val result = viewModel.reportPost(post.id, trimmed)
                        if (result.isSuccess) {
                            Toast.makeText(context, "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show()
                            reportingPost = null
                        } else {
                            Toast.makeText(
                                context,
                                result.exceptionOrNull()?.message ?: "신고 처리에 실패했습니다.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                },
                onDismiss = { reportingPost = null }
            )
        }
    }
}

@Composable
private fun TopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onBackClick: () -> Unit,
    focusRequester: FocusRequester,
    keyboardController: SoftwareKeyboardController?,
    onSearchClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .size(40.dp)
                .clickable { onBackClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_left_arrow),
                contentDescription = null,
                tint = Color(0xFF202020),
                modifier = Modifier.size(15.dp)
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
    Surface(
        modifier = Modifier
            .weight(1f)
            .height(40.dp),
        shape = CircleShape,
        color = Color(0xFFF5F5F5)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = query,
                onValueChange = { value ->
                    if (!value.contains('\n')) {
                        onQueryChange(value)
                    }
                },
                singleLine = true,
                textStyle = TextStyle(
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = Color(0xFF202020),
                    letterSpacing = (-0.5).sp
                ),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        if (query.isNotBlank()) {
                            onSearchClick()
                        }
                        keyboardController?.hide()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp)
                    .focusRequester(focusRequester),
                decorationBox = { innerTextField ->
                    if (query.isEmpty()) {
                        Text(
                            text = "어떤 게시글을 검색할까요?",
                            color = Color(0xFFC2C2C2),
                            fontFamily = Pretendard,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            letterSpacing = (-0.5).sp
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = "검색",
            color = Color(0xFF1C7756),
            fontFamily = Pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            letterSpacing = (-0.5).sp,
            modifier = Modifier.clickable {
                if (query.isNotBlank()) {
                    onSearchClick()
                    keyboardController?.hide()
                }
            }
        )
    }
}

@Composable
private fun HeaderRow(
    canClear: Boolean,
    onClearAll: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "최근 검색",
            color = Color(0xFF202020),
            fontFamily = Pretendard,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            letterSpacing = (-0.5).sp
        )
        if (canClear) {
            Text(
                text = "전체 삭제",
                color = Color(0xFF8B9096),
                fontFamily = Pretendard,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                letterSpacing = (-0.5).sp,
                modifier = Modifier.clickable { onClearAll() }
            )
        }
    }
}

@Composable
private fun RecentSearchList(
    items: List<String>,
    onSelect: (String) -> Unit,
    onRemove: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items.forEach { keyword ->
            CommunityRecentSearchItem(
                text = keyword,
                modifier = Modifier.fillMaxWidth(),
                onClick = onSelect,
                onRemove = onRemove
            )
        }
    }
}

@Composable
private fun SearchResultList(
    posts: List<CommunityPost>,
    onPostClick: (Int) -> Unit,
    onLikeClick: (Int) -> Unit,
    onCommentClick: (Int) -> Unit,
    onEditPost: (CommunityPost) -> Unit,
    onDeletePost: (CommunityPost) -> Unit,
    onReportPost: (CommunityPost) -> Unit,
    onToggleAlarm: (CommunityPost) -> Unit,
    isNotificationUpdating: (Int) -> Boolean,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(bottom = 30.dp)
    ) {
        itemsIndexed(posts, key = { _, post -> post.id }) { index, post ->
            CommunityPostCard(
                post = post,
                onPostClick = { onPostClick(post.id) },
                onLikeClick = { onLikeClick(post.id) },
                onCommentClick = { onCommentClick(post.id) },
                onAlarmClick = { onToggleAlarm(post) },
                onEditClick = onEditPost,
                onDeleteClick = onDeletePost,
                onReportClick = onReportPost,
                isDetailView = false,
                isNotificationToggling = isNotificationUpdating(post.id)
            )

            if (index < posts.lastIndex) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .background(Color(0xFFF5F5F5))
                )
            }
        }
    }
}
