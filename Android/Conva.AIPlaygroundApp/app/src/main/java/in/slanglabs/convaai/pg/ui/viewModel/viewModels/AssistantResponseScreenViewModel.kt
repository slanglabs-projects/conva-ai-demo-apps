package `in`.slanglabs.convaai.pg.ui.viewModel.viewModels

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.slanglabs.convaai.pg.App
import `in`.slanglabs.convaai.pg.ConvaAISDKType
import `in`.slanglabs.convaai.pg.ConvaAICopilotState
import `in`.slanglabs.convaai.pg.R
import `in`.slanglabs.convaai.pg.Repository
import `in`.slanglabs.convaai.pg.model.AppData
import `in`.slanglabs.convaai.pg.ui.uiStates.AssistantResponseUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference


class AssistantResponseScreenViewModel (application: Application, private val activityReference: WeakReference<Activity>) : ViewModel() {
    private companion object{
        const val TITLE: String = "Conva.AI Response"
        const val BODY: String = "Action result will be shown here"
        const val INVOKE_COPILOT: String = "Invoke Copilot "
        const val INITIALIZE: String = "Initialize"
        const val INITIALIZE_FAILED: String = "Initialization failed"
        const val INITIALIZE_FAILED_MESSAGE: String = "Initialization failed. Please kill the app and reinitialize"
        const val INITIALIZING: String = "Initializing..."
    }
    private val repository: Repository = (application as App).repository
    private lateinit var appData: AppData

    private val _uiState = MutableStateFlow(AssistantResponseUiState())
    val uiState: StateFlow<AssistantResponseUiState> = _uiState.asStateFlow()

    private var initialized = false

    init {
        viewModelScope.launch {
            repository.convaAICopilotState.collect { it ->
                initialized = it == ConvaAICopilotState.READY || it == ConvaAICopilotState.PROCESSING
                _uiState.update { currentState ->
                    currentState.copy(
                        isAssistantInitialized = initialized,
                        invokeButtonTitle = when (it) {
                            ConvaAICopilotState.IDLE, ConvaAICopilotState.FAILED ->
                                INITIALIZE_FAILED

                            ConvaAICopilotState.INITIALIZING ->
                                INITIALIZING
                            else -> INVOKE_COPILOT
                        },
                        body = if (it == ConvaAICopilotState.IDLE || it == ConvaAICopilotState.FAILED) {
                            INITIALIZE_FAILED_MESSAGE
                        } else {
                            BODY
                        }
                    )
                }
            }
        }
        
        viewModelScope.launch {
            repository.response.collect { chatResponse ->
                _uiState.update { currentState ->
                    currentState.copy(
                        body = chatResponse.jsonString
                    )
                }
            }
        }
        setUpUiState()
    }

    private fun setUpUiState() {
        _uiState.value = AssistantResponseUiState(
            title = TITLE,
            body = BODY,
            clipboardIcon = R.drawable.ic_baseline_content_copy_24,
            invokeButtonTitle = INITIALIZE,
            isAssistantInitialized = false
        )
    }

    fun initializeAssistant(appData: AppData)  {
        if (!initialized) {
            this.appData = appData
            initAssistant(appData)
        }
    }

    fun invokeAssistant() {
        if (initialized) {
            activityReference.get()?.let { repository.invokeCopilot(it) }
        }
    }

    private fun initAssistant(appData: AppData) {
        activityReference.get()?.let { activity ->
            repository.initConvaAI(
                assistantID = appData.assistant_id,
                assistantKey = appData.api_key,
                assistantVersion = appData.assistant_version,
                assistantType = ConvaAISDKType.COPILOT_SDK.name,
                activity)
        }
    }
}