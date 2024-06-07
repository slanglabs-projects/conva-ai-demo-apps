package `in`.slanglabs.androidpg

import android.app.Activity
import `in`.slanglabs.androidpg.model.ASRData
import `in`.slanglabs.androidpg.model.ChatResponse
import `in`.slanglabs.androidpg.convaai.ConvaAIInterface
import `in`.slanglabs.androidpg.convaai.ConvaInterfaceListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class Repository(private val convaAIInterface: ConvaAIInterface) {
    private val responseData = MutableStateFlow<ChatResponse>(ChatResponse("",""))
    private val responseStreamData = MutableStateFlow<ChatResponse?>(null)
    private val asrData = MutableStateFlow<ASRData>(ASRData("",null))
    private val _ConvaAI_assistantState = MutableStateFlow<ConvaAIAssistantState>(ConvaAIAssistantState.IDLE)
    private val _convaAISDKType = MutableStateFlow<ConvaAISDKType>(ConvaAISDKType.CORE_SDK)

    val response: Flow<ChatResponse>
        get() = responseData

    val responseStream: Flow<ChatResponse?>
        get() = responseStreamData

    val asrDataStream: Flow<ASRData>
        get() = asrData

    val convaAIAssistantState : Flow<ConvaAIAssistantState>
            get() = _ConvaAI_assistantState

    val convaAISDKType : Flow<ConvaAISDKType>
            get() = _convaAISDKType

    fun initConvaAI(
        assistantID: String,
        assistantKey: String,
        assistantVersion: String,
        assistantType: String,
        startActivity: Activity
    ) {
        if (assistantType == ConvaAISDKType.CORE_SDK.typeValue) {
            _convaAISDKType.value = ConvaAISDKType.CORE_SDK
            convaAIInterface.initialiseCopilotSDK(assistantID,assistantKey,assistantVersion)
            convaAIInterface.initTTSService(assistantID,assistantKey)
            convaAIInterface.initASRService(assistantID,assistantKey,assistantVersion,startActivity)
        } else {
            _convaAISDKType.value = ConvaAISDKType.ASSISTANT_SDK
            convaAIInterface.initialiseCopilotAssistant(assistantID,assistantKey,assistantVersion,startActivity, object : ConvaInterfaceListener{
                override fun onInitSuccessful() {
                    _ConvaAI_assistantState.value = ConvaAIAssistantState.READY
                }

                override fun onInitFailure() {
                    _ConvaAI_assistantState.value = ConvaAIAssistantState.FAILED
                }

            })
        }

        _ConvaAI_assistantState.value = ConvaAIAssistantState.INITIALIZING
    }

    fun invokeAssistant() {
        sendToLLM("","","")
    }

    fun showUI(activity: Activity) {
        convaAIInterface.showUI(activity)
    }

    fun sendToLLM(text: String, capabilityGroupSelected: String, capabilitySelected: String) {
        _ConvaAI_assistantState.value = ConvaAIAssistantState.PROCESSING
        convaAIInterface.startConversation(text,capabilityGroupSelected,capabilitySelected)
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
        _ConvaAI_assistantState.value = ConvaAIAssistantState.READY
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
        convaAIInterface.speakText(text)
    }

    fun startListening(activity: Activity) {
        clearASRData()
        convaAIInterface.startListening(activity)
    }

    fun stopListening() {
        convaAIInterface.stopListening()
    }


    fun skipTTS() {
        convaAIInterface.skipTTS()
    }

    fun shutdown() {
        shutdownConva()
        clearData()
    }

    private fun shutdownConva() {
        convaAIInterface.shutdown()
    }

    fun shutdownTTS() {
        convaAIInterface.shutdownTTS()
    }
}