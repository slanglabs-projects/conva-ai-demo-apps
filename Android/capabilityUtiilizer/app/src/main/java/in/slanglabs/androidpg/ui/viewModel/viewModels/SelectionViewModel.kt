package `in`.slanglabs.androidpg.ui.viewModel.viewModels

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import `in`.slanglabs.androidpg.ConvaAISDKType
import `in`.slanglabs.androidpg.BuildConfig
import `in`.slanglabs.androidpg.model.AppData
import `in`.slanglabs.androidpg.navigation.Screen
import `in`.slanglabs.androidpg.ui.Utils

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

    fun onAssistantSelected(assistantType: String, appData: AppData) : String {
        val json = Uri.encode(Gson().toJson(appData))
        return when(assistantType) {
            ConvaAISDKType.ASSISTANT_SDK.typeValue ->
                Screen.AssistantResponseScreen.route + "/${json}"
            else -> Screen.ChatScreen.route + "/${json}"
        }
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