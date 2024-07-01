package `in`.slanglabs.convaai.pg.ui.viewModel.viewModels

import android.app.Activity
import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.slanglabs.convaai.pg.App
import `in`.slanglabs.convaai.pg.ConvaAISDKType
import `in`.slanglabs.convaai.pg.ConvaAICopilotState
import `in`.slanglabs.convaai.pg.R
import `in`.slanglabs.convaai.pg.Repository
import `in`.slanglabs.convaai.pg.model.AppData
import `in`.slanglabs.convaai.pg.ui.Utils
import `in`.slanglabs.convaai.pg.ui.uiStates.ChatScreenUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class ChatScreenViewModel(application: Application, private val activityReference: WeakReference<Activity>) : ViewModel() {
    companion object{
        const val COPILOT: String = "ConvaAI"
        const val USER: String = "You"
        const val PLACEHOLDER: String = "Type your question... "
        const val LOGO_TITLE: String = "Ask me any questions"
        const val DEFAULT: String = "default"
    }

    private val repository: Repository = (application as App).repository

    private val _uiState = MutableStateFlow(ChatScreenUiState())
    val uiState: StateFlow<ChatScreenUiState> = _uiState.asStateFlow()

    private var initialized = false
    lateinit var appData: AppData
    var message: MutableState<String> = mutableStateOf("")

    private val _isProcessing = MutableStateFlow<Boolean>(false)
    private val isProcessing : StateFlow<Boolean> = _isProcessing

    private val _isListening = MutableStateFlow<Boolean>(false)
    val isListening : StateFlow<Boolean> = _isListening

    private val _isTrailingIconEnabled: Flow<Boolean> = combine(isListening, isProcessing) { isListeningValue, loadingValue ->
        !isListeningValue && !loadingValue
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), initialValue = false)

    init {

        viewModelScope.launch {
            repository.convaAICopilotState.collect { it ->
                initialized = it == ConvaAICopilotState.READY
                _isProcessing.value = it == ConvaAICopilotState.PROCESSING
            }
        }

        viewModelScope.launch {
            repository.response.collect { chatResponse ->
                val message = chatResponse.message
                val messageJson = chatResponse.jsonString
                _uiState.update { currentState ->
                    val updatedChatHistory = currentState.chatHistory.toMutableList()
                    updatedChatHistory.add(
                        Utils.getChatMessage(
                            "$COPILOT: $message",
                            messageJson
                        )
                    )
                    currentState.copy(
                        chatHistory = updatedChatHistory
                    )
                }
                speakText(chatResponse.message)
            }
        }

        viewModelScope.launch {
            repository.asrDataStream.collect { asrData ->
                message.value = asrData.text
                asrData.isFinal?.let {
                    _isListening.value = !it
                }
            }
        }
        setUpUiState()
    }

    private fun setUpUiState() {
        _uiState.value = ChatScreenUiState(
            chatWindowVisibility = false,
            chatHistory = emptyList(),
            placeholder = PLACEHOLDER,
            message = "",
            isListening = false,
            appLogo = R.drawable.conva_logo,
            appLogoTitle = LOGO_TITLE,
            capabilityGroup = DEFAULT,
            capability = DEFAULT,
            capabilityGroupList = emptyList(),
            capabilityList = emptyList()
        )
    }

    fun onCapabilityUpdated(selectedCapabilityGroup: String, selectedCapability: String) {

        _uiState.update { currentState ->

            var capabilityGroup = currentState.capabilityGroup
            val capability: String
            var capabilityList = currentState.capabilityList

            if (selectedCapabilityGroup != capabilityGroup) {
                capabilityGroup = selectedCapabilityGroup

                capabilityList = Utils.getAssistantCapabilities(appData, selectedCapabilityGroup)
                capability = capabilityList[0]
            } else {
                capability = selectedCapability
            }

            currentState.copy(
                capabilityGroup = capabilityGroup,
                capability = capability,
                capabilityList = capabilityList
            )
        }
    }

    fun onTextReady(text: String, capabilityGroup: String, capability: String) {
        if (text.isNotBlank()) {
            message.value = ""
            _uiState.update { currentState ->
                val updatedChatHistory = currentState.chatHistory.toMutableList()
                updatedChatHistory.add(
                    Utils.getChatMessage("$USER: $text", "")
                )
                currentState.copy(
                    chatHistory = updatedChatHistory,
                    chatWindowVisibility = updatedChatHistory.isNotEmpty()
                )
            }

            repository.sendToLLM(text,capabilityGroup,capability)
        }
    }

    fun onMicClicked() {
        _isListening.value = if (isListening.value) {
            repository.stopListening()
            false
        } else {
            activityReference.get()?.let {
                repository.startListening(it)
            }
            true
        }
    }

    fun onMessageClicked() {
        repository.skipTTS()
    }

    val isTrailingIconEnabled: Flow<Boolean>
        get() = _isTrailingIconEnabled

    fun onTextValueChanged(text: String) {
        message.value = text
    }

    fun initializeAssistant(appData: AppData) {
        if (!initialized) {
            cacheAppData(appData)
            initAssistant(appData)
            updateCapabilityGroupList(appData, Utils.getAssistantCapabilityGroup(appData))
        }
    }

    private fun initAssistant(appData: AppData) {
        activityReference.get()?.let { activity ->
            repository.initConvaAI(
                assistantID = appData.assistant_id,
                assistantKey = appData.api_key,
                assistantVersion = appData.assistant_version,
                assistantType = ConvaAISDKType.FOUNDATION_SDK.typeValue,
                activity)
        }
    }

    private fun cacheAppData(appData: AppData) {
        this.appData = appData
    }

    private fun updateCapabilityGroupList(appData: AppData, list: List<String>) {
        _uiState.update { currentState ->
            val capabilityGroup = list[0]
            val capabilityList = Utils.getAssistantCapabilities(appData, capabilityGroup)
            val capability = capabilityList[0]

            currentState.copy(
                capabilityGroup = capabilityGroup,
                capabilityGroupList = list,
                capability = capability,
                capabilityList = capabilityList
            )
        }
    }

    private fun speakText(text: String) {
        if (text.isEmpty()) return
        repository.speakText(
            text
        )
    }
}