package com.luckydut97.tennispark.core.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat

/**
 * 권한 관리 유틸리티
 */
object PermissionUtils {

    /**
     * 갤러리 접근에 필요한 권한 목록
     */
    fun getRequiredPermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    /**
     * 권한이 허용되어 있는지 확인
     */
    fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
        return permissions.all { permission ->
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * 설정 페이지로 이동하는 인텐트 생성
     */
    fun createSettingsIntent(context: Context): Intent {
        return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
    }
}

/**
 * 권한 상태
 */
enum class PermissionState {
    GRANTED,
    DENIED,
    PERMANENTLY_DENIED
}

/**
 * 권한 요청 결과
 */
data class PermissionResult(
    val state: PermissionState,
    val attemptCount: Int
)

/**
 * Composable에서 사용할 수 있는 권한 요청 런처
 */
@Composable
fun rememberPermissionLauncher(
    onPermissionResult: (PermissionResult) -> Unit
): ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>> {
    var attemptCount by remember { mutableStateOf(0) }

    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            attemptCount++

            val allGranted = permissions.values.all { it }
            val result = when {
                allGranted -> PermissionResult(PermissionState.GRANTED, attemptCount)
                attemptCount >= 3 -> PermissionResult(
                    PermissionState.PERMANENTLY_DENIED,
                    attemptCount
                )

                else -> PermissionResult(PermissionState.DENIED, attemptCount)
            }

            onPermissionResult(result)
        }
    )
}

/**
 * 권한 요청 헬퍼
 */
@Composable
fun rememberPermissionHelper(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    onPermissionPermanentlyDenied: () -> Unit
): () -> Unit {
    val context = androidx.compose.ui.platform.LocalContext.current
    val permissions = remember { PermissionUtils.getRequiredPermissions() }

    val permissionLauncher = rememberPermissionLauncher { result ->
        when (result.state) {
            PermissionState.GRANTED -> onPermissionGranted()
            PermissionState.DENIED -> onPermissionDenied()
            PermissionState.PERMANENTLY_DENIED -> onPermissionPermanentlyDenied()
        }
    }

    return remember {
        {
            if (PermissionUtils.hasPermissions(context, permissions)) {
                onPermissionGranted()
            } else {
                permissionLauncher.launch(permissions)
            }
        }
    }
}