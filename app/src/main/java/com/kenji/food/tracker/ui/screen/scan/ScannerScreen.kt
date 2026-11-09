package com.kenji.food.tracker.ui.screen.scan

import android.graphics.Rect
import androidx.camera.core.ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.kenji.food.tracker.ui.viewmodel.scan.ScanAction
import com.kenji.food.tracker.ui.viewmodel.scan.ScanEffect
import com.kenji.food.tracker.ui.viewmodel.scan.ScanViewModel

@Composable
fun ScannerScreen(viewModel: ScanViewModel = hiltViewModel(), onResult: (String) -> Unit) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ScanEffect.Found -> onResult(effect.code)
            }
        }
    }

    val barcodeOptions = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE,
            Barcode.FORMAT_CODABAR,
            Barcode.FORMAT_CODE_93,
            Barcode.FORMAT_CODE_39,
            Barcode.FORMAT_CODE_128,
            Barcode.FORMAT_EAN_8,
            Barcode.FORMAT_EAN_13,
            Barcode.FORMAT_AZTEC,
            Barcode.FORMAT_DATA_MATRIX,
        )
        .build()

    val barcodeScanner = BarcodeScanning.getClient(barcodeOptions)

    var rect by remember { mutableStateOf<Rect?>(null) }

    val cameraController = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
            setImageAnalysisAnalyzer(
                ContextCompat.getMainExecutor(context),
                MlKitAnalyzer(
                    listOf(barcodeScanner),
                    COORDINATE_SYSTEM_VIEW_REFERENCED,
                    ContextCompat.getMainExecutor(context)
                ) { result ->
                    if (result == null) {
                        return@MlKitAnalyzer
                    }

                    val barcodeScannerResult = result.getValue(barcodeScanner)

                    rect = barcodeScannerResult?.firstOrNull()?.boundingBox

                    barcodeScannerResult?.forEach { result ->
                        val barcode = result.rawValue ?: return@forEach
                        viewModel.onAction(ScanAction.BarcodeScanned(barcode))
                    }
                }
            )
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                PreviewView(it).apply {
                    cameraController.bindToLifecycle(lifecycleOwner)
                    this.controller = cameraController
                }
            }
        )

        val outlineColor = MaterialTheme.colorScheme.primary

        Canvas(modifier = Modifier.fillMaxSize()) {
            rect?.let {
                drawRect(
                    color = outlineColor,
                    topLeft = Offset(it.left.toFloat(), it.top.toFloat()),
                    size = Size(
                        width = (it.right - it.left).toFloat(),
                        height = (it.bottom - it.top).toFloat()
                    ),
                    style = Stroke(width = 5f)
                )
            }
        }
    }
}