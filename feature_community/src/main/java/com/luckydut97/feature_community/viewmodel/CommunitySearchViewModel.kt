package com.luckydut97.feature_community.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CommunitySearchUiState(
    val query: String = "",
    val recentSearches: List<String> = emptyList()
)

class CommunitySearchViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CommunitySearchUiState())
    val uiState: StateFlow<CommunitySearchUiState> = _uiState.asStateFlow()

    fun updateQuery(value: String) {
        _uiState.value = _uiState.value.copy(query = value.take(100))
    }

    fun selectRecent(keyword: String) {
        _uiState.value = _uiState.value.copy(query = keyword)
    }

    fun commitSearch(onSearch: (String) -> Unit) {
        val trimmed = _uiState.value.query.trim()
        if (trimmed.isEmpty()) return

        viewModelScope.launch {
            val current = _uiState.value.recentSearches.toMutableList().apply {
                remove(trimmed)
                add(0, trimmed)
            }.take(MAX_RECENT)
            _uiState.value = _uiState.value.copy(
                recentSearches = current,
                query = trimmed
            )
            onSearch(trimmed)
        }
    }

    fun removeRecent(keyword: String) {
        val filtered = _uiState.value.recentSearches.filterNot { it == keyword }
        _uiState.value = _uiState.value.copy(recentSearches = filtered)
    }

    fun clearAll() {
        _uiState.value = _uiState.value.copy(recentSearches = emptyList())
    }

    private companion object {
        const val MAX_RECENT = 10
    }
}
