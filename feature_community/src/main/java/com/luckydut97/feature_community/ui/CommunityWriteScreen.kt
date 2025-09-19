package com.luckydut97.feature_community.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.luckydut97.feature_community.viewmodel.CommunityHomeViewModel
import com.luckydut97.tennispark.core.ui.components.community.CommunityWriteTopBar
import com.luckydut97.tennispark.core.ui.components.community.PhotoAttachmentBar

/**
 * 게시글 작성 화면
 */
@Composable
fun CommunityWriteScreen(
    onBackClick: () -> Unit,
    onPostCreated: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CommunityHomeViewModel? = viewModel(),
    isEditMode: Boolean = false,
    initialTitle: String = "",
    initialContent: String = "",
    initialImageUrls: List<String> = emptyList(),
    onSubmit: ((String, String, List<Uri>, List<String>) -> Unit)? = null
) {
    var title by remember(initialTitle) { mutableStateOf(initialTitle) }
    var content by remember(initialContent) { mutableStateOf(initialContent) }
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var existingImageUrls by remember(initialImageUrls) { mutableStateOf(initialImageUrls) }
    val scrollState = rememberScrollState()
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    // 갤러리 선택 런처
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { newUri ->
            if (selectedImages.size < 3) {
                selectedImages = selectedImages + newUri
            } else {
                Toast
                    .makeText(context, "사진은 최대 3장까지 업로드할 수 있습니다.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    // ViewModel 상태 구독
    val uiStateState = if (!isEditMode && viewModel != null) {
        viewModel.uiState.collectAsState()
    } else {
        null
    }
    val uiState = uiStateState?.value

    // 완료 버튼 활성화 조건
    val isCompleteEnabled = title.isNotBlank() && content.isNotBlank() && (if (isEditMode) true else !(uiState?.isCreatingPost ?: false))

    LaunchedEffect(initialTitle, initialContent, initialImageUrls) {
        focusRequester.requestFocus()
        existingImageUrls = initialImageUrls
    }

    // 게시글 작성 성공/실패 처리
    if (!isEditMode && uiStateState != null) {
        LaunchedEffect(uiState?.createPostSuccess) {
            if (uiState?.createPostSuccess == true) {
                title = ""
                content = ""
                selectedImages = emptyList()
                existingImageUrls = emptyList()
                keyboardController?.hide()
                viewModel?.clearCreatePostState()
                onPostCreated()
            }
        }

        LaunchedEffect(uiState?.createPostError) {
            uiState?.createPostError?.let { error ->
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                viewModel?.clearCreatePostState()
            }
        }
    }

    val submitAction: (String, String, List<Uri>, List<String>) -> Unit = onSubmit
        ?: { submitTitle, submitContent, submitImages, _ ->
            viewModel?.createPost(submitTitle, submitContent, submitImages)
        }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            CommunityWriteTopBar(
                onBackClick = onBackClick,
                onCompleteClick = {
                    val trimmedTitle = title.trim()
                    val trimmedContent = content.trim()
                    submitAction(trimmedTitle, trimmedContent, selectedImages, existingImageUrls)
                    if (isEditMode) {
                        keyboardController?.hide()
                        onPostCreated()
                    }
                },
                isCompleteEnabled = isCompleteEnabled,
                titleText = if (isEditMode) "게시글 수정" else "게시글 작성"
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 스크롤 가능한 콘텐츠 영역 (전체 화면)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 18.dp)
                    .padding(bottom = 52.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // 제목 입력
                Box {
                    if (title.isEmpty()) {
                        Text(
                            text = "제목을 입력하세요 최대 1줄 입니다.",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC2C2C2),
                            letterSpacing = (-0.5).sp
                        )
                    }

                    BasicTextField(
                        value = title,
                        onValueChange = { newValue ->
                            // 1줄 제한
                            if (!newValue.contains('\n')) {
                                title = newValue
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        textStyle = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF202020),
                            letterSpacing = (-0.5).sp
                        ),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // 내용 입력
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (content.isEmpty()) {
                        Text(
                            text = "게시판을 통해 나누고 싶은 이야기를 작성해보세요\n\n부적절한 게시물은 운영진의 검토하에 삭제처리 될 수 있으니 건전한 게시판 사용을 부탁드립니다.",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFFC2C2C2),
                            letterSpacing = (-0.5).sp,
                            lineHeight = 24.sp
                        )
                    }

                    BasicTextField(
                        value = content,
                        onValueChange = { content = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 200.dp),
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF202020),
                            letterSpacing = (-0.5).sp,
                            lineHeight = 24.sp
                        )
                    )
                }

                // 기존 이미지들 (편집 모드)
                if (existingImageUrls.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))

                    existingImageUrls.forEachIndexed { index, url ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            AsyncImage(
                                model = url,
                                contentDescription = "첨부된 이미지",
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.FillWidth
                            )

                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(top = 18.dp, end = 18.dp)
                                    .size(18.dp)
                                    .background(
                                        color = Color.Black.copy(alpha = 0.5f),
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        existingImageUrls = existingImageUrls.filterIndexed { i, _ -> i != index }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "×",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.offset(x = 0.6.dp, y = (-3).dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                // 새로 선택한 이미지들 (맨 밑에 표시)
                if (selectedImages.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))

                    selectedImages.forEachIndexed { index, imageUri ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            AsyncImage(
                                model = imageUri,
                                contentDescription = "첨부된 이미지",
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.FillWidth
                            )

                            // 삭제 버튼 (우측 상단)
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(top = 18.dp, end = 18.dp)
                                    .size(18.dp)
                                    .background(
                                        color = Color.Black.copy(alpha = 0.5f),
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        selectedImages =
                                            selectedImages.filterIndexed { i, _ -> i != index }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "×",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.offset(x = 0.6.dp, y = (-3).dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                // 추가 하단 여백 (키보드 + PhotoAttachmentBar 공간)
                Spacer(modifier = Modifier.height(100.dp))
            }

            // 키보드 위에 절대 위치로 고정되는 사진 첨부 바
            PhotoAttachmentBar(
                onPhotoClick = {
                    val totalImages = existingImageUrls.size + selectedImages.size
                    if (totalImages < 3) {
                        galleryLauncher.launch("image/*")
                    } else {
                        Toast
                            .makeText(context, "사진은 최대 3장까지 업로드할 수 있습니다.", Toast.LENGTH_SHORT)
                            .show()
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            )
        }
    }
}
