package com.luckydut97.tennispark.core.utils

import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.webkit.URLUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 이미지 다운로드 관리자
 * Android 버전에 따라 MediaStore API 또는 DownloadManager 사용
 */
class ImageDownloadManager(private val context: Context) {

    /**
     * 이미지 다운로드 (메인 진입점)
     */
    suspend fun downloadImage(imageUrl: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                if (!URLUtil.isValidUrl(imageUrl)) {
                    return@withContext Result.failure(Exception("유효하지 않은 이미지 URL입니다."))
                }

                val fileName = generateFileName()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    downloadWithMediaStore(imageUrl, fileName)
                } else {
                    downloadWithLegacyMethod(imageUrl, fileName)
                }
            } catch (e: Exception) {
                Result.failure(Exception("다운로드 중 오류가 발생했습니다: ${e.message}"))
            }
        }
    }

    /**
     * Android 10+ MediaStore API 사용 (권한 불필요)
     */
    private suspend fun downloadWithMediaStore(imageUrl: String, fileName: String): Result<String> {
        return try {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES + "/TennisPark"
                )
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                ?: return Result.failure(Exception("미디어 스토어에 파일을 생성할 수 없습니다."))

            resolver.openOutputStream(uri)?.use { outputStream ->
                downloadImageFromUrl(imageUrl).use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            Result.success("사진이 갤러리에 저장되었습니다.")
        } catch (e: Exception) {
            Result.failure(Exception("MediaStore 다운로드 실패: ${e.message}"))
        }
    }

    /**
     * Android 9 이하 레거시 방법 (기존 권한 활용)
     */
    private fun downloadWithLegacyMethod(imageUrl: String, fileName: String): Result<String> {
        return try {
            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val request = DownloadManager.Request(Uri.parse(imageUrl)).apply {
                setTitle("테니스파크 사진 다운로드")
                setDescription("커뮤니티 사진을 저장 중입니다.")
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_PICTURES,
                    "TennisPark/$fileName"
                )
                setAllowedOverMetered(true)
                setAllowedOverRoaming(true)
            }

            downloadManager.enqueue(request)
            Result.success("사진 다운로드가 시작되었습니다.")
        } catch (e: Exception) {
            Result.failure(Exception("DownloadManager 다운로드 실패: ${e.message}"))
        }
    }

    /**
     * URL에서 이미지 스트림 다운로드
     */
    private fun downloadImageFromUrl(imageUrl: String): InputStream {
        val url = URL(imageUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.connect()

        if (connection.responseCode != HttpURLConnection.HTTP_OK) {
            throw Exception("HTTP 오류: ${connection.responseCode}")
        }

        return connection.inputStream
    }

    /**
     * 파일명 생성 (tennispark_yyyyMMdd_HHmmss.jpg)
     */
    private fun generateFileName(): String {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return "tennispark_$timestamp.jpg"
    }
}