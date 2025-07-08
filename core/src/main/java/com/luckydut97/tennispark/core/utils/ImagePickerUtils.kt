package com.luckydut97.tennispark.core.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

/**
 * 이미지 선택 유틸리티
 */
object ImagePickerUtils {

    /**
     * 갤러리에서 이미지 선택을 위한 인텐트 생성
     */
    fun createImagePickerIntent(): Intent {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        return intent
    }

    /**
     * URI를 File로 변환
     */
    fun uriToFile(context: Context, uri: Uri): File? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val fileName = "temp_image_${System.currentTimeMillis()}.jpg"
            val file = File(context.cacheDir, fileName)

            inputStream?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }

            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 이미지 압축 (선택사항)
     */
    fun compressImage(file: File, maxSize: Long = 1024 * 1024): File {
        // TODO: 이미지 압축 로직 구현 (필요한 경우)
        return file
    }
}

/**
 * Composable에서 사용할 수 있는 이미지 선택 런처
 */
@Composable
fun rememberImagePickerLauncher(
    onImageSelected: (Uri?) -> Unit
): ManagedActivityResultLauncher<String, Uri?> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = onImageSelected
    )
}