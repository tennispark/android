package com.luckydut97.feature_myinfo.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppSettingsViewModel : ViewModel() {
    private val _adPushEnabled = MutableStateFlow(true)
    val adPushEnabled: StateFlow<Boolean> = _adPushEnabled.asStateFlow()

    private val _infoPushEnabled = MutableStateFlow(false)
    val infoPushEnabled: StateFlow<Boolean> = _infoPushEnabled.asStateFlow()

    fun setAdPushEnabled(value: Boolean) {
        _adPushEnabled.value = value
    }

    fun setInfoPushEnabled(value: Boolean) {
        _infoPushEnabled.value = value
    }
}
