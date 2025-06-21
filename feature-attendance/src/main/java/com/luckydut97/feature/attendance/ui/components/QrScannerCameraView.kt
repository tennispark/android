package com.luckydut97.feature.attendance.ui.components

import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun QrScannerCameraView(
    modifier: Modifier = Modifier,
    onQrCodeDetected: (String) -> Unit
) {
    val tag = "🔍 디버깅: QrScannerCameraView"
    Log.d(tag, "QR 스캐너 카메라 뷰 생성 시작")

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }

    // QR 코드 인식 상태 관리
    var isScanning by remember { mutableStateOf(true) }
    var lastScannedCode by remember { mutableStateOf("") }
    var lastScannedTime by remember { mutableStateOf(0L) }

    val cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }

    AndroidView(
        factory = { previewView },
        modifier = modifier
    )

    LaunchedEffect(previewView) {
        Log.d(tag, "카메라 설정 시작")

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                Log.d(tag, "CameraProvider 획득 성공")

                // Preview 설정
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                // ImageAnalysis 설정 (QR 코드 인식용)
                val imageAnalyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also { imageAnalysis ->
                        imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                            processImageProxy(
                                imageProxy = imageProxy,
                                onQrDetected = { qrCode ->
                                    // 중복 스캔 방지 (3초 내 같은 QR 코드 무시)
                                    val currentTime = System.currentTimeMillis()
                                    if (isScanning &&
                                        (qrCode != lastScannedCode || currentTime - lastScannedTime > 3000)
                                    ) {

                                        Log.d(tag, "🎯 새로운 QR 코드 인식: $qrCode")
                                        lastScannedCode = qrCode
                                        lastScannedTime = currentTime
                                        isScanning = false

                                        // QR 코드 콜백 호출
                                        onQrCodeDetected(qrCode)

                                        // 3초 후 다시 스캔 가능하도록 설정
                                        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main)
                                            .launch {
                                                kotlinx.coroutines.delay(3000)
                                                isScanning = true
                                                Log.d(tag, "QR 스캔 재개 가능")
                                            }
                                    }
                                },
                                tag = tag
                            )
                        }
                    }

                // 후면 카메라 선택
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    // 기존 바인딩 해제
                    cameraProvider.unbindAll()

                    // 카메라 라이프사이클에 바인딩
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalyzer
                    )

                    Log.d(tag, "✅ 카메라 바인딩 성공 - QR 스캔 준비 완료")

                } catch (exc: Exception) {
                    Log.e(tag, "❌ 카메라 바인딩 실패: ${exc.message}")
                }

            } catch (exc: Exception) {
                Log.e(tag, "❌ CameraProvider 초기화 실패: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(context))
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d(tag, "카메라 리소스 정리")
            cameraExecutor.shutdown()
        }
    }
}

/**
 * MLKit을 사용한 QR 코드 인식 처리
 */
@androidx.camera.core.ExperimentalGetImage
private fun processImageProxy(
    imageProxy: ImageProxy,
    onQrDetected: (String) -> Unit,
    tag: String
) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        // MLKit 바코드 스캐너
        val scanner = BarcodeScanning.getClient()

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    when (barcode.valueType) {
                        Barcode.TYPE_URL -> {
                            val url = barcode.url?.url
                            if (!url.isNullOrEmpty()) {
                                Log.d(tag, "📱 URL 타입 QR 코드 인식: $url")
                                onQrDetected(url)
                            }
                        }

                        Barcode.TYPE_TEXT -> {
                            val text = barcode.displayValue
                            if (!text.isNullOrEmpty()) {
                                Log.d(tag, "📝 텍스트 타입 QR 코드 인식: $text")
                                onQrDetected(text)
                            }
                        }

                        else -> {
                            val rawValue = barcode.rawValue
                            if (!rawValue.isNullOrEmpty()) {
                                Log.d(tag, "🔤 기타 타입 QR 코드 인식: $rawValue")
                                onQrDetected(rawValue)
                            }
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e(tag, "QR 코드 인식 실패: ${exception.message}")
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    } else {
        imageProxy.close()
    }
}