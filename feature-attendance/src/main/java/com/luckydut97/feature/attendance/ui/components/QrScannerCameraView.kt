package com.luckydut97.feature.attendance.ui.components

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

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()

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


                } catch (exc: Exception) {
                }

            } catch (exc: Exception) {
            }
        }, ContextCompat.getMainExecutor(context))
    }

    DisposableEffect(Unit) {
        onDispose {
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
                                onQrDetected(url)
                            }
                        }

                        Barcode.TYPE_TEXT -> {
                            val text = barcode.displayValue
                            if (!text.isNullOrEmpty()) {
                                onQrDetected(text)
                            }
                        }

                        else -> {
                            val rawValue = barcode.rawValue
                            if (!rawValue.isNullOrEmpty()) {
                                onQrDetected(rawValue)
                            }
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    } else {
        imageProxy.close()
    }
}