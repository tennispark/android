package com.luckydut97.feature_community.ui

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import com.luckydut97.tennispark.core.R
import com.luckydut97.tennispark.core.ui.components.community.CommunityWriteTopBar
import com.luckydut97.tennispark.core.ui.components.community.PhotoAttachmentBar
import com.luckydut97.tennispark.core.ui.components.common.RemoveImageButton

@Composable
fun CommentEditScreen(
    initialContent: String,
    initialImageUrl: String? = null,
    onBackClick: () -> Unit,
    onSubmit: (String, Boolean, Uri?) -> Unit,
    isSaving: Boolean = false,
    modifier: Modifier = Modifier
) {
    var content by remember(initialContent) { mutableStateOf(initialContent) }
    var selectedImage by remember { mutableStateOf<Uri?>(null) }
    var initialImageRemoved by remember { mutableStateOf(initialImageUrl == null) }
    val scrollState = rememberScrollState()
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImage = it
        }
    }

    LaunchedEffect(initialImageUrl) {
        initialImageRemoved = initialImageUrl == null
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    val isCompleteEnabled = content.isNotBlank() && content.length <= 300 && !isSaving

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.White,
            topBar = {
            CommunityWriteTopBar(
                onBackClick = onBackClick,
                onCompleteClick = {
                    if (content.length > 300) {
                        Toast.makeText(context, "댓글은 300자까지 작성할 수 있습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        val deletePhoto = initialImageUrl != null && initialImageRemoved
                        onSubmit(content.trim(), deletePhoto, selectedImage)
                    }
                },
                isCompleteEnabled = isCompleteEnabled,
                titleText = "댓글 수정"
            )
            },
            bottomBar = {
            PhotoAttachmentBar(
                onPhotoClick = {
                    val hasExistingImage = initialImageUrl != null && !initialImageRemoved
                    val hasNewImage = selectedImage != null
                    if (!isSaving) {
                        if (!hasExistingImage && !hasNewImage) {
                            galleryLauncher.launch("image/*")
                        } else {
                            Toast.makeText(context, "이미지는 최대 1장만 등록할 수 있습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
            }
        ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 18.dp, vertical = 24.dp)
                .verticalScroll(scrollState)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 160.dp)
            ) {
                if (content.isEmpty()) {
                    Text(
                        text = "댓글 내용을 입력해주세요.",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFFC2C2C2),
                        letterSpacing = (-0.5).sp,
                        lineHeight = 24.sp
                    )
                }

                BasicTextField(
                    value = content,
                    onValueChange = {
                        if (it.length <= 300) content = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .focusRequester(focusRequester),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF202020),
                        lineHeight = 24.sp,
                        letterSpacing = (-0.5).sp
                    ),
                    cursorBrush = SolidColor(Color(0xFF1C7756))
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "${content.length}/300",
                fontSize = 14.sp,
                color = Color(0xFF8B9096)
            )

            Spacer(modifier = Modifier.height(16.dp))

            val currentImageUrl = if (!initialImageRemoved && selectedImage == null) initialImageUrl else null

            currentImageUrl?.let { url ->
                ImagePreview(
                    imageModel = url,
                    onRemove = {
                        initialImageRemoved = true
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            selectedImage?.let { uri ->
                ImagePreview(
                    imageModel = uri,
                    onRemove = {
                        selectedImage = null
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        }

        if (isSaving) {
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

@Composable
private fun ImagePreview(
    imageModel: Any,
    onRemove: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        AsyncImage(
            model = imageModel,
            contentDescription = "댓글 이미지",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentScale = ContentScale.FillWidth
        )

        RemoveImageButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 24.dp, end = 24.dp),
            onClick = onRemove
        )
    }
}
