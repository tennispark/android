package com.luckydut97.feature.push.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckydut97.tennispark.core.domain.model.PushNotification
import com.luckydut97.tennispark.core.domain.usecase.GetNotificationsUseCase
import com.luckydut97.tennispark.core.domain.usecase.MarkAllNotificationsAsReadUseCase
import com.luckydut97.tennispark.core.data.repository.NotificationRepositoryImpl
import com.luckydut97.tennispark.core.data.network.NetworkModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 푸시 알림 ViewModel (Clean Architecture 기반)
 */
class AppPushViewModel : ViewModel() {

    private val tag = "🔍 AppPushViewModel"

    // UseCase는 Context 의존성 제거됨
    private var getNotificationsUseCase: GetNotificationsUseCase? = null
    private var markAllNotificationsAsReadUseCase: MarkAllNotificationsAsReadUseCase? = null

    // 알림 목록 상태
    private val _notifications = MutableStateFlow<List<PushNotification>>(emptyList())
    val notifications: StateFlow<List<PushNotification>> = _notifications.asStateFlow()

    // 로딩 상태
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // 에러 상태
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // 확장된 알림 ID들 관리
    private val _expandedNotificationIds = MutableStateFlow<Set<String>>(emptySet())
    val expandedNotificationIds: StateFlow<Set<String>> = _expandedNotificationIds.asStateFlow()

    /**
     * Context 초기화 (화면에서 호출)
     */
    fun initializeWithContext(context: Context) {
        if (getNotificationsUseCase == null) {
            // UseCase 초기화 (Context 의존성 제거)
            val repository = NotificationRepositoryImpl(NetworkModule.apiService)
            getNotificationsUseCase = GetNotificationsUseCase(repository)
            markAllNotificationsAsReadUseCase = MarkAllNotificationsAsReadUseCase(repository)

            loadNotifications()
        } else {
        }
    }

    /**
     * 알림 목록 로드
     */
    private fun loadNotifications() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                // 🔥 HomeTopAppBar에서 미리 조회한 배지 수 사용
                val preloadedBadgeCount =
                    com.luckydut97.tennispark.core.utils.BadgeCountManager.getBadgeCount()

                getNotificationsUseCase?.invoke(preloadedBadgeCount)?.collect { notifications ->
                    _notifications.value = notifications
                }
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("인증") == true -> "인증이 되지 않았습니다. 다시 로그인해주세요."
                    e.message?.contains("네트워크") == true -> "네트워크 연결을 확인해주세요."
                    else -> e.message ?: "알림을 불러올 수 없습니다."
                }
                _error.value = errorMessage
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * 모든 알림 읽음 처리 (화면 진입 시 호출)
     * 서버 기반 읽음 처리만 수행하며 로컬 배지 관리 로직은 제거됨
     */
    fun markAllAsRead() {
        viewModelScope.launch {
            try {
                val result = markAllNotificationsAsReadUseCase?.invoke()
                if (result?.isSuccess == true) {
                    val currentNotifications = _notifications.value
                    val updatedNotifications = currentNotifications.map { notification ->
                        notification.copy(isNew = false)
                    }
                    _notifications.value = updatedNotifications
                }
            } catch (e: Exception) {
            }
        }
    }

    /**
     * 알림 더보기/접기 토글
     */
    fun toggleNotificationExpansion(notificationId: String) {
        val currentExpanded = _expandedNotificationIds.value
        _expandedNotificationIds.value = if (currentExpanded.contains(notificationId)) {
            currentExpanded - notificationId
        } else {
            currentExpanded + notificationId
        }
    }

    /**
     * 에러 상태 초기화
     */
    fun clearError() {
        _error.value = null
    }

    /**
     * 데이터 새로고침
     */
    fun refresh() {
        loadNotifications()
    }

    /**
     * 알림이 확장되어 있는지 확인
     */
    fun isNotificationExpanded(notificationId: String): Boolean {
        return _expandedNotificationIds.value.contains(notificationId)
    }
}