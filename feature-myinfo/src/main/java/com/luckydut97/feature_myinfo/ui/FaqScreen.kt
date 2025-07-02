package com.luckydut97.feature_myinfo.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luckydut97.tennispark.core.ui.components.navigation.TopBar
import com.luckydut97.feature_myinfo.ui.components.SettingListItem
import com.luckydut97.feature_myinfo.viewmodel.FaqViewModel

@Composable
fun FaqScreen(
    onBackClick: () -> Unit = {}
) {
    val viewModel: FaqViewModel = viewModel()
    val faqList by viewModel.faqList.collectAsState()

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBar(title = "FAQ", onBackClick = onBackClick)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(faqList) { faq ->
                SettingListItem(text = faq)
            }
        }
    }
}
