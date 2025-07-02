package com.luckydut97.feature_myinfo.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FaqViewModel : ViewModel() {
    private val _faqList = MutableStateFlow(
        listOf(
            "[멤버십] 멤버십 혜택안내",
            "[활동] 테니스파크 활동 안내",
            "[활동] 테니스파크 활동 안내",
            "[활동] 테니스파크 활동 안내",
            "[활동] 테니스파크 활동 안내",
            "[활동] 테니스파크 활동 안내",
            "[활동] 테니스파크 활동 안내",
            "[활동] 테니스파크 활동 안내"
        )
    )
    val faqList: StateFlow<List<String>> = _faqList.asStateFlow()

    // 추후 API, 에러/로딩 상태 확장 가능
}
