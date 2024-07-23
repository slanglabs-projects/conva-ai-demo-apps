package `in`.slanglabs.convaai.pg.ui.viewModel.viewModels

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import `in`.slanglabs.convaai.pg.ConvaAISDKType
import `in`.slanglabs.convaai.pg.BuildConfig
import `in`.slanglabs.convaai.pg.model.AppData
import `in`.slanglabs.convaai.pg.navigation.Screen
import `in`.slanglabs.convaai.pg.ui.Utils

class SelectionViewModel () : ViewModel() {
    private val isSkipSelection: Boolean = BuildConfig.SKIP_ASSISTANT_SELECTION
    val qrCodeScanner: String = "Scan QR"
    val defaultAssistant: String = "Default Assistant"

    fun onHomeSelection(selectedString: String) : String {
        if (isSkipSelection) return Screen.ChatScreen.route
        val json = Uri.encode(Gson().toJson(Utils.defaultAppData))

        return when (selectedString) {
            qrCodeScanner ->
                Screen.CameraScreen.route

            else -> Screen.AssistantSelectionScreen.route + "/${json}"
        }
    }

    fun onAssistantSelected(assistantType: String, appData: AppData, showInputBox: Boolean, showBottomBar: Boolean) : String {
        val jsonAppData = Uri.encode(Gson().toJson(appData))
        return Screen.ChatScreen.createRoute(
            assistantType = assistantType,
            appData = jsonAppData,
            showInputBox = showInputBox,
            showBottomBar = showBottomBar
        )
    }

    fun onBarcodeScanned(scannedData: String) : String {
        val appData = extractAppInfo(scannedData)
        val json = Uri.encode(Gson().toJson(appData))
        return Screen.AssistantSelectionScreen.route + "/${json}"
    }

    private fun extractAppInfo(input: String): AppData {
        return Utils.parseJson(input)
    }
}