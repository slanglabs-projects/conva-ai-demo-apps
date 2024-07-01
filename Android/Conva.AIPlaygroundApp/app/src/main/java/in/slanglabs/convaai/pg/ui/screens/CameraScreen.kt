package `in`.slanglabs.convaai.pg.ui.screens

import android.Manifest
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import `in`.slanglabs.convaai.pg.ui.BarcodeAnalyzer
import androidx.camera.core.Preview
import androidx.compose.runtime.SideEffect
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import `in`.slanglabs.convaai.pg.ui.viewModel.viewModels.SelectionViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    viewModel: SelectionViewModel,
    navController: NavController
) {
    val localContext = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(localContext)
    }
    var gotScan = false
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    when {
        cameraPermissionState.hasPermission -> {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    val previewView = PreviewView(context)
                    val preview = Preview.Builder().build()
                    val selector = CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()

                    preview.setSurfaceProvider(previewView.surfaceProvider)

                    val imageAnalysis = ImageAnalysis.Builder().build()
                    imageAnalysis.setAnalyzer(
                        ContextCompat.getMainExecutor(context),
                        BarcodeAnalyzer() { scannedResult ->
                            if (!gotScan) {
                                val destination = viewModel.onBarcodeScanned(scannedResult)
                                navController.navigate(destination)
                                gotScan = true
                            }
                        }
                    )

                    runCatching {
                        cameraProviderFuture.get().bindToLifecycle(
                            lifecycleOwner,
                            selector,
                            preview,
                            imageAnalysis
                        )
                    }.onFailure {
                        Log.e("CAMERA", "Camera bind error ${it.localizedMessage}", it)
                    }
                    previewView
                }
            )
        }
        cameraPermissionState.shouldShowRationale -> {
            Log.d("android.pg.screen.camera", "Camera Permission permanently denied")
        }
        else -> {
            SideEffect {
                cameraPermissionState.launchPermissionRequest()
            }
            Log.d("android.pg.screen.camera", "No Camera Permission")
        }
    }
}