package com.luckydut97.tennispark.core.data.storage

import android.content.Context
import android.content.SharedPreferences

class CommunitySearchPreferenceManager(context: Context) {

    companion object {
        private const val PREF_NAME = "community_search_preferences"
        private const val KEY_RECENT_SEARCHES = "recent_searches"
        private const val MAX_RECENT = 10
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun getRecentSearches(): List<String> {
        val stored = prefs.getString(KEY_RECENT_SEARCHES, null) ?: return emptyList()
        return stored.split('\u0001').filter { it.isNotBlank() }
    }

    fun saveRecentSearches(recentSearches: List<String>) {
        val trimmed = recentSearches.take(MAX_RECENT)
        if (trimmed.isEmpty()) {
            prefs.edit().remove(KEY_RECENT_SEARCHES).apply()
        } else {
            val joined = trimmed.joinToString(separator = "\u0001")
            prefs.edit().putString(KEY_RECENT_SEARCHES, joined).apply()
        }
    }

    fun clear() {
        prefs.edit().remove(KEY_RECENT_SEARCHES).apply()
    }
}
