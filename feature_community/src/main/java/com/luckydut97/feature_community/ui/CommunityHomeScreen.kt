package com.luckydut97.feature_community.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.luckydut97.feature_community.viewmodel.CommunityHomeViewModel
import com.luckydut97.tennispark.core.domain.model.CommunityPost
import com.luckydut97.tennispark.core.ui.components.community.CommunityTopBar
import com.luckydut97.tennispark.core.ui.components.community.CommunityPostCard
import com.luckydut97.tennispark.core.ui.components.community.FloatingWriteButton
import com.luckydut97.tennispark.core.ui.components.community.ConfirmDeleteDialog

/**
 * 커뮤니티 홈 화면
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityHomeScreen(
    onPostClick: (Int) -> Unit = {},
    onSearchClick: () -> Unit = {},
    onAlarmClick: () -> Unit = {},
    onWriteClick: () -> Unit = {},
    viewModel: CommunityHomeViewModel,
    onEditPost: (CommunityPost) -> Unit = {},
    onDeletePost: (CommunityPost) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val pullToRefreshState = rememberPullToRefreshState()
    val indicatorColor = Color(0xFF1C7756)
    var pendingDeletePost by remember { mutableStateOf<CommunityPost?>(null) }

    // 무한 스크롤: 리스트 끝 감지
    val shouldLoadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItemsNumber = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

            // 디버깅 로그
            println("🔄 무한스크롤 체크:")
            println("   - 전체 아이템 수: $totalItemsNumber")
            println("   - 마지막 보이는 인덱스: $lastVisibleItemIndex")
            println("   - 트리거 기준: ${totalItemsNumber - 3}")
            println("   - canLoadMore: ${uiState.canLoadMore}")
            println("   - isLoadingMore: ${uiState.isLoadingMore}")

            // 마지막에서 3개 전 아이템이 보일 때 다음 페이지 로드
            val shouldTrigger = lastVisibleItemIndex > (totalItemsNumber - 3) &&
                    totalItemsNumber > 0 &&
                    uiState.canLoadMore &&
                    !uiState.isLoadingMore

            if (shouldTrigger) {
                println("✅ 다음 페이지 로드 트리거!")
            }

            shouldTrigger
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            viewModel.loadNextPage()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 상단바
            CommunityTopBar(
                onSearchClick = onSearchClick,
                onAlarmClick = onAlarmClick
            )

            // Pull-to-Refresh Box로 메인 콘텐츠 감싸기
            PullToRefreshBox(
                isRefreshing = uiState.isRefreshing,
                onRefresh = { viewModel.refresh() },
                modifier = Modifier.fillMaxSize(),
                state = pullToRefreshState,
                indicator = {
                    PullToRefreshDefaults.Indicator(
                        state = pullToRefreshState,
                        isRefreshing = uiState.isRefreshing,
                        modifier = Modifier.align(Alignment.TopCenter),
                        color = Color.White,
                        containerColor = indicatorColor
                    )
                }
            ) {
                when {
                    uiState.isLoading && uiState.posts.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = Color(0xFF5AB97D)
                            )
                        }
                    }

                    uiState.errorMessage != null && uiState.posts.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            ) {
                                Text(
                                    text = uiState.errorMessage ?: "오류가 발생했습니다",
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = {
                                        viewModel.clearError()
                                        viewModel.loadCommunityPosts()
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

                    uiState.posts.isEmpty() && !uiState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            ) {
                                Text(
                                    text = "게시글이 없습니다",
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "첫 번째 게시글을 작성해보세요.",
                                    color = Color.LightGray
                                )
                            }
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            state = listState
                        ) {
                            items(uiState.posts) { post ->
                                CommunityPostCard(
                                    post = post,
                                    onPostClick = { onPostClick(post.id) },
                                    onLikeClick = { viewModel.toggleLike(post.id) },
                                    onCommentClick = { onPostClick(post.id) },
                                    onAlarmClick = onAlarmClick,
                                    onEditClick = onEditPost,
                                    onDeleteClick = { pendingDeletePost = it }
                                )

                                // 게시글 구분선
                                Spacer(
                                    modifier = Modifier
                                        .height(12.dp)
                                        .fillMaxWidth()
                                        .background(Color(0xFFF5F5F5))
                                )
                            }

                            // 추가 로딩 인디케이터
                            if (uiState.isLoadingMore) {
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

                            // 마지막 페이지 표시
                            if (!uiState.hasNextPage && uiState.posts.isNotEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(1.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "모든 게시글을 불러왔습니다",
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }

                            // 하단 여백 (플로팅 버튼 공간 확보)
                            item {
                                Spacer(modifier = Modifier.height(30.dp))
                            }
                        }
                    }
                }
            }
        }

        // 플로팅 글쓰기 버튼 (바텀네비 18dp 위, 우측 18dp 여백)
        FloatingWriteButton(
            onClick = onWriteClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 18.dp, bottom = 18.dp)
        )
    }

    pendingDeletePost?.let { post ->
        ConfirmDeleteDialog(
            onConfirm = {
                onDeletePost(post)
                pendingDeletePost = null
            },
            onDismiss = { pendingDeletePost = null }
        )
    }
}
