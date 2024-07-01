package `in`.slanglabs.convaai.pg

import android.app.Activity
import `in`.slanglabs.convaai.pg.model.ASRData
import `in`.slanglabs.convaai.pg.model.ChatResponse
import `in`.slanglabs.convaai.pg.convaai.interfaces.ConvaAICopilotInterface
import `in`.slanglabs.convaai.pg.convaai.interfaces.ConvaAICoreInterface
import `in`.slanglabs.convaai.pg.convaai.interfaces.ConvaAISpeechInterface
import `in`.slanglabs.convaai.pg.convaai.interfaces.ConvaAITalkInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class Repository(
    private val convaAICopilotInterface: ConvaAICopilotInterface,
    private val convaAICoreInterface: ConvaAICoreInterface,
    private val convaAISpeechInterface: ConvaAISpeechInterface,
    private val convaAITalkInterface: ConvaAITalkInterface
) {
    private val responseData = MutableStateFlow<ChatResponse>(ChatResponse("",""))
    private val responseStreamData = MutableStateFlow<ChatResponse?>(null)
    private val asrData = MutableStateFlow<ASRData>(ASRData("",null))
    private val _convaAICopilotState = MutableStateFlow<ConvaAICopilotState>(ConvaAICopilotState.IDLE)
    private val _convaAISDKType = MutableStateFlow<ConvaAISDKType>(ConvaAISDKType.FOUNDATION_SDK)

    val response: Flow<ChatResponse>
        get() = responseData

    val responseStream: Flow<ChatResponse?>
        get() = responseStreamData

    val asrDataStream: Flow<ASRData>
        get() = asrData

    val convaAICopilotState : Flow<ConvaAICopilotState>
            get() = _convaAICopilotState

    val convaAISDKType : Flow<ConvaAISDKType>
            get() = _convaAISDKType

    fun initConvaAI(
        assistantID: String,
        assistantKey: String,
        assistantVersion: String,
        assistantType: String,
        startActivity: Activity
    ) {
        when (assistantType) {
            ConvaAISDKType.FOUNDATION_SDK.typeValue -> {
                _convaAISDKType.value = ConvaAISDKType.FOUNDATION_SDK
                convaAICoreInterface.initialiseCoreService(assistantID, assistantKey, assistantVersion)
                convaAITalkInterface.initTTSService(assistantID, assistantKey)
                convaAISpeechInterface.initASRService(assistantID, assistantKey, assistantVersion, startActivity)
            }
            else -> {
                _convaAISDKType.value = ConvaAISDKType.COPILOT_SDK
                convaAICopilotInterface.initialiseCopilotService(assistantID, assistantKey, assistantVersion, startActivity)
            }
        }

        _convaAICopilotState.value = ConvaAICopilotState.INITIALIZING
    }

    fun setConvaAIInitialized(isInitialized: Boolean, errorMessage: String = "") {
        _convaAICopilotState.value = if (isInitialized) {
            ConvaAICopilotState.READY
        } else {
            ConvaAICopilotState.FAILED.apply { setFailureMessage(errorMessage) }
        }
    }

    fun invokeCopilot(activity: Activity) {
        convaAICopilotInterface.invokeCopilotService(activity)
    }

    fun showUI(activity: Activity) {
        convaAICopilotInterface.showUI(activity)
    }

    fun sendToLLM(text: String, capabilityGroupSelected: String, capabilitySelected: String) {
        _convaAICopilotState.value = ConvaAICopilotState.PROCESSING
        convaAICoreInterface.invokeCoreService(text,capabilityGroupSelected,capabilitySelected)
    }

    fun asrPartialTextAvailable(asrData: ASRData) {
        this.asrData.value = asrData
    }

    fun asrCompleteTextAvailable(asrData: ASRData) {
        this.asrData.value = asrData
    }

    fun asrTimeOut() {
        this.asrData.value = ASRData("",true)
    }

    fun sendResponse(response: ChatResponse) {
        _convaAICopilotState.value = ConvaAICopilotState.READY
        responseData.value = response
    }

    fun sendResponseStream(response: ChatResponse) {
        responseStreamData.value = response
    }

    fun clearData() {
        responseData.value = ChatResponse("","")
        responseStreamData.value = null
        clearASRData()
    }

    private fun clearASRData() {
        asrData.value = ASRData("",null)
    }

    fun speakText(text: String) {
        convaAITalkInterface.speakText(text)
    }

    fun startListening(activity: Activity) {
        clearASRData()
        convaAISpeechInterface.startListening(activity)
    }

    fun stopListening() {
        convaAISpeechInterface.stopListening()
    }


    fun skipTTS() {
        convaAITalkInterface.skipTTS()
    }

    fun shutdown() {
        _convaAICopilotState.value = ConvaAICopilotState.IDLE
        shutdownConvaAI()
        clearData()
    }

    private fun shutdownConvaAI() {
        convaAITalkInterface.shutdown()
        convaAICopilotInterface.shutdown()
        convaAICoreInterface.shutdown()
        convaAISpeechInterface.shutdown()
        convaAITalkInterface.shutdownTTS()
    }
}