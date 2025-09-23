package com.luckydut97.feature_community.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luckydut97.feature_community.viewmodel.CommunitySearchViewModel
import com.luckydut97.tennispark.core.R
import com.luckydut97.tennispark.core.ui.components.community.CommunityRecentSearchItem
import com.luckydut97.tennispark.core.ui.theme.Pretendard

@Composable
fun CommunitySearchScreen(
    onBackClick: () -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CommunitySearchViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
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

        Spacer(modifier = Modifier.height(36.dp))

        HeaderRow(
            canClear = uiState.recentSearches.isNotEmpty(),
            onClearAll = {
                viewModel.clearAll()
            }
        )

        Spacer(modifier = Modifier.height(22.dp))

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
        Spacer(modifier = Modifier.size(16.dp))
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
    onRemove: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items, key = { it }) { keyword ->
            CommunityRecentSearchItem(
                text = keyword,
                onClick = onSelect,
                onRemove = onRemove
            )
        }
    }
}
