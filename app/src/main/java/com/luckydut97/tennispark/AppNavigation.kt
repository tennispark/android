package com.luckydut97.tennispark

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.lifecycle.viewmodel.compose.viewModel
import android.net.Uri
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.luckydut97.feature_home.main.ui.HomeScreen
import com.luckydut97.feature_home_shop.data.model.ShopItem
import com.luckydut97.feature_home_shop.ui.ShopDetailScreen
import com.luckydut97.feature_home_shop.ui.ShopScreen
import com.luckydut97.feature_myinfo.navigation.MyInfoNavigation
import com.luckydut97.tennispark.core.ui.components.navigation.BottomNavigationBar
import com.luckydut97.tennispark.core.ui.components.navigation.BottomNavigationItem
import com.luckydut97.tennispark.feature_auth.navigation.AuthNavigation
import com.luckydut97.feature_community.ui.CommunityDetailScreen
import com.luckydut97.feature_community.ui.CommunityHomeScreen
import com.luckydut97.feature_community.ui.CommunitySearchScreen
import com.luckydut97.feature_community.ui.CommunityWriteScreen
import com.luckydut97.feature_community.ui.CommentEditScreen
import com.luckydut97.feature_community.viewmodel.CommunityCommentEditViewModel
import com.luckydut97.feature_community.viewmodel.CommunityDetailViewModel
import com.luckydut97.feature_community.viewmodel.CommunityHomeViewModel
import com.luckydut97.feature_community.viewmodel.CommunityPostEditViewModel
import com.luckydut97.feature_community.viewmodel.CommunitySearchViewModel

/**
 * 탭 순서에 따른 슬라이드 방향 결정
 */
fun getTabOrder(route: String): Int {
    return when {
        route == BottomNavigationItem.HOME.route -> 0
        route == BottomNavigationItem.SHOP.route -> 1
        route.startsWith("shop_detail") -> 1 // 상품 상세 화면도 상품구매 탭으로 처리
        route == BottomNavigationItem.COMMUNITY.route -> 2
        route == BottomNavigationItem.PROFILE.route -> 3
        // 내정보 탭의 하위 화면들
        route == "myinfo" || route == "settings" || route == "notice" ||
                route == "app_settings" || route == "faq" || route == "terms" ||
                route == "version_info" || route == "withdrawal" -> 3
        else -> 0
    }
}

/**
 * 앱 전체의 메인 네비게이션을 처리하는 컴포넌트
 */
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // 스플래시 화면
        composable("splash") {
            com.luckydut97.tennispark.feature_auth.splash.ui.SplashScreen(
                onNavigateToLogin = {
                    navController.navigate("auth") {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                onNavigateToMain = {
                    navController.navigate("main") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        // 🔥 테스트용 화면 추가
        composable("dev_test") {
            MainScreenWithBottomNav(navController)
        }

        // 인증 관련 화면들 (로그인, 회원가입 등)
        composable("auth") {
            AuthNavigation(
                onNavigateToMain = {
                    navController.navigate("main") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }

        // 메인 화면 (바텀 네비게이션 포함)
        composable("main") {
            MainScreenWithBottomNav(navController)
        }

        // 멤버십 등록 화면 (오른쪽에서 왼쪽으로 슬라이드)
        composable(
            "membership",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            }
        ) {
            com.luckydut97.tennispark.feature_auth.membership.ui.MembershipRegistrationScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onMembershipComplete = {
                    navController.navigate("main") {
                        popUpTo("membership") { inclusive = true }
                    }
                },
                onRefundPolicyClick = {
                    navController.navigate("refund_policy")
                }
            )
        }

        // 환불규정 화면 (오른쪽에서 왼쪽으로 슬라이드)
        composable(
            "refund_policy",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            }
        ) {
            com.luckydut97.tennispark.feature_auth.membership.ui.RefundPolicyScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // 출석체크 화면
        composable(
            "attendance",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            }
        ) {
            com.luckydut97.feature.attendance.ui.AttendanceScreen(
                onBackClick = {
                    val canGoBack = navController.previousBackStackEntry != null
                    if (canGoBack) {
                        navController.popBackStack()
                    } else {
                        navController.navigate("main") {
                            popUpTo("main") { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
                onAttendanceComplete = {
                    navController.popBackStack()
                }
            )
        }

        // 푸시 알림 화면
        composable(
            "app_push",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            }
        ) {
            com.luckydut97.feature.push.ui.AppPushScreen(
                onBackClick = {
                    val canGoBack = navController.previousBackStackEntry != null
                    if (canGoBack) {
                        navController.popBackStack()
                    } else {
                        navController.navigate("main") {
                            popUpTo("main") { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        // 활동 신청 화면
        composable(
            "activity_application",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            }
        ) {
            com.luckydut97.feature_home_activity.ui.ActivityApplicationScreen(
                onBackClick = {
                    val canGoBack = navController.previousBackStackEntry != null
                    if (canGoBack) {
                        navController.popBackStack()
                    } else {
                        navController.navigate("main") {
                            popUpTo("main") { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        // 커뮤니티 상세 화면
        composable(
            "community_detail/{postId}",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            }
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId")?.toIntOrNull() ?: 0
            val detailViewModel: CommunityDetailViewModel = viewModel(backStackEntry)
            val editViewModel: CommunityPostEditViewModel = viewModel(backStackEntry)
            val detailUiState by detailViewModel.uiState.collectAsState()
            val editUiState by editViewModel.uiState.collectAsState()
            val context = LocalContext.current

            val refreshFlow = remember(backStackEntry) {
                backStackEntry.savedStateHandle.getStateFlow("community_refresh", false)
            }
            val shouldRefresh by refreshFlow.collectAsState()

            LaunchedEffect(shouldRefresh) {
                if (shouldRefresh) {
                    detailViewModel.loadPostDetail(postId)
                    backStackEntry.savedStateHandle["community_refresh"] = false
                }
            }

            LaunchedEffect(editUiState.deleteSuccess) {
                if (editUiState.deleteSuccess) {
                    editViewModel.consumeDeleteState()
                    navController.previousBackStackEntry?.savedStateHandle?.set("community_refresh", true)
                    runCatching {
                        val mainEntry = navController.getBackStackEntry("main")
                        mainEntry.savedStateHandle["community_refresh"] = true
                    }
                    navController.popBackStack()
                    Toast.makeText(context, "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            LaunchedEffect(editUiState.deleteError) {
                editUiState.deleteError?.let { message ->
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    editViewModel.consumeDeleteState()
                }
            }

            CommunityDetailScreen(
                postId = postId,
                onBackClick = {
                    navController.popBackStack()
                },
                onAlarmClick = {
                    try {
                        navController.navigate("app_push")
                    } catch (e: Exception) {
                    }
                },
                onEditPost = { post ->
                    if (!post.authoredByMe) {
                        Toast.makeText(context, "작성자가 아닙니다.", Toast.LENGTH_SHORT).show()
                        return@CommunityDetailScreen
                    }
                    val encodedTitle = Uri.encode(post.title)
                    val encodedContent = Uri.encode(post.content)
                    val photoPairs = post.photos
                        .mapNotNull { (indexStr, url) ->
                            indexStr.toIntOrNull()?.let { it to url }
                        }
                        .sortedBy { it.first }
                    val fallback = if (photoPairs.isNotEmpty()) photoPairs
                    else post.sortedPhotos.mapIndexed { index, url -> index to url }
                    val photosParam = fallback.joinToString("|") { pair ->
                        "${pair.first}:${Uri.encode(pair.second)}"
                    }
                    navController.navigate("community_edit/${post.id}?title=$encodedTitle&content=$encodedContent&photos=$photosParam")
                },
                onDeletePost = { post ->
                    if (!post.authoredByMe) {
                        Toast.makeText(context, "작성자가 아닙니다.", Toast.LENGTH_SHORT).show()
                        return@CommunityDetailScreen
                    }
                    if (!editUiState.isDeleting) {
                        editViewModel.deletePost(post.id)
                    }
                },
                onEditComment = { comment ->
                    if (!comment.authoredByMe) {
                        Toast.makeText(context, "작성자가 아닙니다.", Toast.LENGTH_SHORT).show()
                        return@CommunityDetailScreen
                    }
                    val encodedContent = Uri.encode(comment.content)
                    val encodedPhoto = Uri.encode(comment.photoUrl ?: "")
                    navController.navigate(
                        "community_comment_edit/$postId/${comment.id}?content=$encodedContent&photoUrl=$encodedPhoto"
                    )
                },
                onDeleteComment = { comment ->
                    if (!comment.authoredByMe) {
                        Toast.makeText(context, "작성자가 아닙니다.", Toast.LENGTH_SHORT).show()
                        return@CommunityDetailScreen
                    }
                    if (!detailUiState.isDeletingComment) {
                        detailViewModel.deleteComment(postId, comment.id)
                    }
                },
                onToggleLike = {
                    navController.previousBackStackEntry?.savedStateHandle?.set("community_refresh", true)
                },
                viewModel = detailViewModel
            )
        }

        // 커뮤니티 글쓰기 화면
        composable(
            "community_write",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            }
        ) {
            CommunityWriteScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onPostCreated = {
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        "community_refresh",
                        true
                    )
                    navController.popBackStack()
                }
            )
        }

        // 커뮤니티 검색 화면
        composable(
            "community_search",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            }
        ) { backStackEntry ->
            val searchViewModel: CommunitySearchViewModel = viewModel(backStackEntry)

            CommunitySearchScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSearch = { keyword ->
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        "community_search_keyword",
                        keyword
                    )
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        "community_refresh",
                        true
                    )
                },
                viewModel = searchViewModel
            )
        }

        // 커뮤니티 게시글 수정 화면 (UI 전용, API 연동 예정)
        composable(
            route = "community_edit/{postId}?title={title}&content={content}&photos={photos}",
            arguments = listOf(
                navArgument("postId") { type = NavType.IntType },
                navArgument("title") { type = NavType.StringType; defaultValue = "" },
                navArgument("content") { type = NavType.StringType; defaultValue = "" },
                navArgument("photos") { type = NavType.StringType; defaultValue = "" }
            ),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            }
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getInt("postId") ?: 0
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val content = backStackEntry.arguments?.getString("content") ?: ""
            val photosArg = backStackEntry.arguments?.getString("photos") ?: ""
            val photoPairs = photosArg
                .takeIf { it.isNotBlank() }
                ?.split("|")
                ?.mapNotNull { token ->
                    val parts = token.split(":", limit = 2)
                    if (parts.size == 2) {
                        parts[0].toIntOrNull()?.let { index ->
                            index to Uri.decode(parts[1])
                        }
                    } else {
                        null
                    }
                }
                ?.filter { it.second.isNotBlank() }
                ?: emptyList()

            val editViewModel: CommunityPostEditViewModel = viewModel()
            val editUiState by editViewModel.uiState.collectAsState()
            val editContext = LocalContext.current

            LaunchedEffect(editUiState.saveSuccess) {
                if (editUiState.saveSuccess) {
                    editViewModel.consumeSaveState()
                    navController.previousBackStackEntry?.savedStateHandle?.set("community_refresh", true)
                    runCatching {
                        val mainEntry = navController.getBackStackEntry("main")
                        mainEntry.savedStateHandle["community_refresh"] = true
                    }
                    navController.popBackStack()
                    Toast.makeText(editContext, "게시글이 수정되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            LaunchedEffect(editUiState.saveError) {
                editUiState.saveError?.let { message ->
                    Toast.makeText(editContext, message, Toast.LENGTH_LONG).show()
                    editViewModel.consumeSaveState()
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                CommunityWriteScreen(
                    onBackClick = { navController.popBackStack() },
                    onPostCreated = { /* handled on success */ },
                    isEditMode = true,
                    initialTitle = title,
                    initialContent = content,
                    initialImages = photoPairs,
                    onSubmit = { updatedTitle, updatedContent, deleteIndexes, newImages ->
                        if (!editUiState.isSaving) {
                            editViewModel.updatePost(postId, updatedTitle, updatedContent, deleteIndexes, newImages)
                        }
                    }
                )

                if (editUiState.isSaving) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF145F44))
                    }
                }
            }
        }

        // 커뮤니티 댓글 수정 화면 (UI 전용)
        composable(
            route = "community_comment_edit/{postId}/{commentId}?content={content}&photoUrl={photoUrl}",
            arguments = listOf(
                navArgument("postId") { type = NavType.IntType },
                navArgument("commentId") { type = NavType.IntType },
                navArgument("content") { type = NavType.StringType; defaultValue = "" },
                navArgument("photoUrl") { type = NavType.StringType; defaultValue = "" }
            ),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            }
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getInt("postId") ?: 0
            val commentId = backStackEntry.arguments?.getInt("commentId") ?: 0
            val encodedContent = backStackEntry.arguments?.getString("content") ?: ""
            val encodedPhotoUrl = backStackEntry.arguments?.getString("photoUrl")
                ?.takeIf { it.isNotBlank() }

            val initialContent = Uri.decode(encodedContent)
            val initialPhotoUrl = encodedPhotoUrl?.let { Uri.decode(it) }

            val commentEditViewModel: CommunityCommentEditViewModel = viewModel(backStackEntry)
            val commentEditUiState by commentEditViewModel.uiState.collectAsState()
            val context = LocalContext.current

            LaunchedEffect(commentEditUiState.saveSuccess) {
                if (commentEditUiState.saveSuccess) {
                    commentEditViewModel.consumeSaveState()
                    navController.previousBackStackEntry?.savedStateHandle?.set("community_refresh", true)
                    Toast.makeText(context, "댓글이 수정되었습니다.", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            }

            LaunchedEffect(commentEditUiState.saveError) {
                commentEditUiState.saveError?.let { message ->
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    commentEditViewModel.consumeSaveState()
                }
            }

            CommentEditScreen(
                initialContent = initialContent,
                initialImageUrl = initialPhotoUrl,
                onBackClick = { navController.popBackStack() },
                onSubmit = { content, deletePhoto, newImage ->
                    if (!commentEditUiState.isSaving) {
                        commentEditViewModel.updateComment(
                            postId = postId,
                            commentId = commentId,
                            content = content,
                            deletePhoto = deletePhoto,
                            newImageUri = newImage
                        )
                    }
                },
                isSaving = commentEditUiState.isSaving
            )
        }
    }
}

/**
 * 바텀 네비게이션 포함된 메인 화면
 */
@Composable
fun MainScreenWithBottomNav(
    mainNavController: NavHostController
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: BottomNavigationItem.HOME.route

    // 바텀 네비게이션 표시/숨김 상태
    var isBottomNavVisible by remember { mutableStateOf(true) }

    // 4개 탭 구조에서 탭/비탭 화면에 따라 바텀네비 탭 강조 및 숨김 로직 적용
    val bottomNavRoute = when {
        // 각 탭의 메인/서브 화면 라우트 매핑
        currentRoute == BottomNavigationItem.HOME.route -> BottomNavigationItem.HOME.route
        currentRoute == BottomNavigationItem.SHOP.route || currentRoute.startsWith("shop_detail") -> BottomNavigationItem.SHOP.route
        currentRoute == BottomNavigationItem.COMMUNITY.route -> BottomNavigationItem.COMMUNITY.route
        // 내정보 하위 screen은 모두 내정보 탭 강조 (except point_history)
        currentRoute == BottomNavigationItem.PROFILE.route || currentRoute == "myinfo" || currentRoute == "settings" ||
                currentRoute == "notice" || currentRoute == "app_settings" || currentRoute == "faq" ||
                currentRoute == "terms" || currentRoute == "version_info" || currentRoute == "withdrawal" -> BottomNavigationItem.PROFILE.route
        // 탭 외 화면에서 바텀네비 숨김
        currentRoute == "point_history" || currentRoute == "app_push" -> null
        else -> null
    }

    // 이전 라우트 추적을 위한 상태
    var previousRoute by remember { mutableStateOf(BottomNavigationItem.HOME.route) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (bottomNavRoute != null && isBottomNavVisible) {
                BottomNavigationBar(
                    currentRoute = bottomNavRoute, // 매핑된 라우트 사용
                    onItemClick = { route ->
                        previousRoute = currentRoute
                        isBottomNavVisible = true // 바텀 네비게이션 클릭시 항상 표시
                        navController.navigate(route) {
                            // 바텀 네비게이션 클릭 시 백스택 관리
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = BottomNavigationItem.HOME.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            // 홈 화면
            composable(
                BottomNavigationItem.HOME.route,
                enterTransition = {
                    val initialRoute = initialState.destination.route ?: ""
                    val initialOrder = getTabOrder(initialRoute)
                    val targetOrder = getTabOrder(BottomNavigationItem.HOME.route)

                    if (initialOrder > targetOrder) {
                        // 오른쪽에서 왼쪽으로 (상품구매/내정보 -> 홈)
                        slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = tween(300)
                        )
                    } else {
                        // 왼쪽에서 오른쪽으로 (처음 진입)
                        slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(300)
                        )
                    }
                },
                exitTransition = {
                    val targetRoute = targetState.destination.route ?: ""
                    val initialOrder = getTabOrder(BottomNavigationItem.HOME.route)
                    val targetOrder = getTabOrder(targetRoute)

                    if (targetOrder > initialOrder) {
                        // 왼쪽으로 나가기 (홈 -> 상품구매/내정보)
                        slideOutHorizontally(
                            targetOffsetX = { -it },
                            animationSpec = tween(300)
                        )
                    } else {
                        // 오른쪽으로 나가기
                        slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(300)
                        )
                    }
                }
            ) {
                HomeScreen(
                    onMembershipClick = {
                        mainNavController.navigate("membership")
                    },
                    onAttendanceClick = {
                        try {
                            mainNavController.navigate("attendance")
                        } catch (e: Exception) {
                        }
                    },
                    onNotificationClick = {
                        try {
                            mainNavController.navigate("app_push")
                        } catch (e: Exception) {
                        }
                    },
                    onActivityApplicationClick = {
                        try {
                            mainNavController.navigate("activity_application")
                        } catch (e: Exception) {
                        }
                    }
                )
            }

            // 상품 구매 화면
            composable(
                BottomNavigationItem.SHOP.route,
                enterTransition = {
                    val initialRoute = initialState.destination.route ?: ""
                    val initialOrder = getTabOrder(initialRoute)
                    val targetOrder = getTabOrder(BottomNavigationItem.SHOP.route)

                    if (initialOrder > targetOrder) {
                        // 오른쪽에서 왼쪽으로 (내정보 -> 상품구매)
                        slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = tween(300)
                        )
                    } else {
                        // 왼쪽에서 오른쪽으로 (홈 -> 상품구매)
                        slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(300)
                        )
                    }
                },
                exitTransition = {
                    val targetRoute = targetState.destination.route ?: ""
                    val initialOrder = getTabOrder(BottomNavigationItem.SHOP.route)
                    val targetOrder = getTabOrder(targetRoute)

                    if (targetOrder > initialOrder) {
                        // 왼쪽으로 나가기 (상품구매 -> 내정보)
                        slideOutHorizontally(
                            targetOffsetX = { -it },
                            animationSpec = tween(300)
                        )
                    } else {
                        // 오른쪽으로 나가기 (상품구매 -> 홈)
                        slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(300)
                        )
                    }
                }
            ) {
                ShopScreen(
                    onBackClick = {
                        navController.navigate(BottomNavigationItem.HOME.route) {
                            popUpTo(BottomNavigationItem.HOME.route) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    },
                    onItemClick = { item ->
                        val encodedImageUrl = item.imageUrl?.let {
                            java.net.URLEncoder.encode(it, "UTF-8")
                        } ?: ""
                        navController.navigate(
                            "shop_detail/${item.id}/${item.brandName}/${item.productName}/${item.price}/$encodedImageUrl"
                        )
                    }
                )
            }

            // 상품 상세 화면
            composable(
                "shop_detail/{productId}/{brandName}/{productName}/{price}/{imageUrl}",
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(300)
                    )
                },
                exitTransition = {
                    val targetRoute = targetState.destination.route ?: ""
                    val currentOrder = getTabOrder("shop_detail")
                    val targetOrder = getTabOrder(targetRoute)

                    if (targetOrder > currentOrder) {
                        // 왼쪽으로 나가기 (상품구매 -> 내정보)
                        slideOutHorizontally(
                            targetOffsetX = { -it },
                            animationSpec = tween(300)
                        )
                    } else {
                        // 오른쪽으로 나가기 (상품구매 -> 홈 또는 뒤로가기)
                        slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(300)
                        )
                    }
                },
                popEnterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(300)
                    )
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(300)
                    )
                }
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId") ?: ""
                val brandName = backStackEntry.arguments?.getString("brandName") ?: ""
                val productName = backStackEntry.arguments?.getString("productName") ?: ""
                val price = backStackEntry.arguments?.getString("price")?.toIntOrNull() ?: 0
                val imageUrl = backStackEntry.arguments?.getString("imageUrl")?.let {
                    java.net.URLDecoder.decode(it, "UTF-8")
                }

                val shopItem = ShopItem(
                    id = productId,
                    brandName = brandName,
                    productName = productName,
                    price = price,
                    imageUrl = imageUrl
                )

                ShopDetailScreen(
                    item = shopItem,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            // 커뮤니티 화면
            composable(
                BottomNavigationItem.COMMUNITY.route,
                enterTransition = {
                    val initialRoute = initialState.destination.route ?: ""
                    val initialOrder = getTabOrder(initialRoute)
                    val targetOrder = getTabOrder(BottomNavigationItem.COMMUNITY.route)

                    if (initialOrder > targetOrder) {
                        // 오른쪽에서 왼쪽으로 (내정보 -> 커뮤니티)
                        slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = tween(300)
                        )
                    } else {
                        // 왼쪽에서 오른쪽으로 (홈/상품구매 -> 커뮤니티)
                        slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(300)
                        )
                    }
                },
                exitTransition = {
                    val targetRoute = targetState.destination.route ?: ""
                    val initialOrder = getTabOrder(BottomNavigationItem.COMMUNITY.route)
                    val targetOrder = getTabOrder(targetRoute)

                    if (targetOrder > initialOrder) {
                        // 왼쪽으로 나가기 (커뮤니티 -> 내정보)
                        slideOutHorizontally(
                            targetOffsetX = { -it },
                            animationSpec = tween(300)
                        )
                    } else {
                        // 오른쪽으로 나가기 (커뮤니티 -> 홈/상품구매)
                        slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(300)
                        )
                    }
                }
            ) { backStackEntry ->
                val communityViewModel: CommunityHomeViewModel = viewModel(backStackEntry)
                val postEditViewModel: CommunityPostEditViewModel = viewModel(backStackEntry)
                val parentEntry = remember(backStackEntry) {
                    mainNavController.getBackStackEntry("main")
                }
                val refreshFlow = remember(parentEntry) {
                    parentEntry.savedStateHandle.getStateFlow("community_refresh", false)
                }
                val shouldRefresh by refreshFlow.collectAsState()
                val editState by postEditViewModel.uiState.collectAsState()
                val context = LocalContext.current

                LaunchedEffect(shouldRefresh) {
                    if (shouldRefresh) {
                        communityViewModel.loadCommunityPosts()
                        parentEntry.savedStateHandle["community_refresh"] = false
                    }
                }

                LaunchedEffect(editState.deleteSuccess) {
                    if (editState.deleteSuccess) {
                        postEditViewModel.consumeDeleteState()
                        communityViewModel.refresh()
                        Toast.makeText(context, "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                LaunchedEffect(editState.deleteError) {
                    editState.deleteError?.let { message ->
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        postEditViewModel.consumeDeleteState()
                    }
                }

                CommunityHomeScreen(
                    onPostClick = { postId ->
                        mainNavController.navigate("community_detail/$postId")
                    },
                    onSearchClick = {
                        mainNavController.navigate("community_search")
                    },
                    onAlarmClick = {
                        // TODO: 알림 화면으로 이동
                        try {
                            mainNavController.navigate("app_push")
                        } catch (e: Exception) {
                        }
                    },
                    onWriteClick = {
                        mainNavController.navigate("community_write")
                    },
                    viewModel = communityViewModel,
                    onEditPost = { post ->
                        if (!post.authoredByMe) {
                            Toast.makeText(context, "작성자가 아닙니다.", Toast.LENGTH_SHORT).show()
                            return@CommunityHomeScreen
                        }
                        val encodedTitle = Uri.encode(post.title)
                        val encodedContent = Uri.encode(post.content)
                        val photoMapPairs = post.photos
                            .mapNotNull { (indexStr, url) ->
                                indexStr.toIntOrNull()?.let { it to url }
                            }
                            .sortedBy { it.first }

                        val fallbackPairs = if (photoMapPairs.isNotEmpty()) {
                            photoMapPairs
                        } else if (post.sortedPhotos.isNotEmpty()) {
                            post.sortedPhotos.mapIndexed { index, url -> index to url }
                        } else {
                            post.mainImage?.let { listOf(0 to it) } ?: emptyList()
                        }

                        val photosParam = fallbackPairs.joinToString("|") { pair ->
                            "${pair.first}:${Uri.encode(pair.second)}"
                        }
                        mainNavController.navigate("community_edit/${post.id}?title=$encodedTitle&content=$encodedContent&photos=$photosParam")
                    },
                    onDeletePost = { post ->
                        if (!post.authoredByMe) {
                            Toast.makeText(context, "작성자가 아닙니다.", Toast.LENGTH_SHORT).show()
                            return@CommunityHomeScreen
                        }
                        if (!editState.isDeleting) {
                            postEditViewModel.deletePost(post.id)
                        }
                    }
                )
            }

            // 내 정보 화면
            composable(
                BottomNavigationItem.PROFILE.route,
                enterTransition = {
                    val initialRoute = initialState.destination.route ?: ""
                    val initialOrder = getTabOrder(initialRoute)
                    val targetOrder = getTabOrder(BottomNavigationItem.PROFILE.route)

                    if (initialOrder < targetOrder) {
                        // 왼쪽에서 오른쪽으로 (홈/상품구매 -> 내정보)
                        slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(300)
                        )
                    } else {
                        // 오른쪽에서 왼쪽으로 (처음 진입)
                        slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = tween(300)
                        )
                    }
                },
                exitTransition = {
                    val targetRoute = targetState.destination.route ?: ""
                    val initialOrder = getTabOrder(BottomNavigationItem.PROFILE.route)
                    val targetOrder = getTabOrder(targetRoute)

                    if (targetOrder < initialOrder) {
                        // 오른쪽으로 나가기 (내정보 -> 홈/상품구매)
                        slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(300)
                        )
                    } else {
                        // 왼쪽으로 나가기
                        slideOutHorizontally(
                            targetOffsetX = { -it },
                            animationSpec = tween(300)
                        )
                    }
                }
            ) {
                MyInfoNavigation(
                    onBackClick = {
                        navController.navigate(BottomNavigationItem.HOME.route) {
                            popUpTo(BottomNavigationItem.HOME.route) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    },
                    onLogoutComplete = {
                        try {
                            // 로그아웃 완료 시 인증 화면으로 이동
                            mainNavController.navigate("auth") {
                                popUpTo("main") { inclusive = true }
                            }
                        } catch (e: Exception) {
                        }
                    },
                    onBottomNavVisibilityChange = { visible ->
                        isBottomNavVisible = visible
                    }
                )
            }
        }
    }
}
