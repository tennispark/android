package com.luckydut97.feature_myinfo.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NoticeViewModel : ViewModel() {
    private val _noticeList = MutableStateFlow(
        listOf(
            "[멤버십] 테니스파크 멤버십 안내",
            "[5월 활동] 테니스파크 5월 활동 안내",
            "[이벤트] 수도공고 5월 이벤트 안내",
            "[이벤트] 수도공고 5월 이벤트 안내",
            "[이벤트] 수도공고 5월 이벤트 안내",
            "[이벤트] 수도공고 5월 이벤트 안내",
            "[이벤트] 수도공고 5월 이벤트 안내",
            "[이벤트] 수도공고 5월 이벤트 안내"
        )
    )
    val noticeList: StateFlow<List<String>> = _noticeList.asStateFlow()

    // 추후 API, 에러/로딩 상태 확장 가능
}
