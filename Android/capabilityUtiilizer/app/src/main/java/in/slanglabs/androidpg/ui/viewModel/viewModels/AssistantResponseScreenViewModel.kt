package `in`.slanglabs.androidpg.ui.viewModel.viewModels

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.slanglabs.androidpg.App
import `in`.slanglabs.androidpg.ConvaAISDKType
import `in`.slanglabs.androidpg.ConvaAIAssistantState
import `in`.slanglabs.androidpg.R
import `in`.slanglabs.androidpg.Repository
import `in`.slanglabs.androidpg.model.AppData
import `in`.slanglabs.androidpg.ui.uiStates.AssistantResponseUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference


class AssistantResponseScreenViewModel (application: Application, private val activityReference: WeakReference<Activity>) : ViewModel() {
    private companion object{
        const val TITLE: String = "Copilot Response"
        const val BODY: String = "Action result will be shown here"
        const val INVOKE_ASSISTANT: String = "InvokeAssistant "
        const val INITIALIZE: String = "Initialize"
        const val INITIALIZING: String = "Initializing..."
    }
    private val repository: Repository = (application as App).repository
    private lateinit var appData: AppData

    private val _uiState = MutableStateFlow(AssistantResponseUiState())
    val uiState: StateFlow<AssistantResponseUiState> = _uiState.asStateFlow()

    private var initialized = false

    init {
        viewModelScope.launch {
            repository.convaAIAssistantState.collect { it ->
                initialized = it == ConvaAIAssistantState.READY || it == ConvaAIAssistantState.PROCESSING
                _uiState.update { currentState ->
                    currentState.copy(
                        isAssistantInitialized = initialized,
                        invokeButtonTitle = when (it) {
                            ConvaAIAssistantState.IDLE, ConvaAIAssistantState.FAILED ->
                                INITIALIZE
                            ConvaAIAssistantState.INITIALIZING ->
                                INITIALIZING
                            else -> INVOKE_ASSISTANT
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
            activityReference.get()?.let { repository.showUI(it) }
            repository.invokeAssistant()
        }
        else initAssistant(appData)
    }

    private fun initAssistant(appData: AppData) {
        activityReference.get()?.let { activity ->
            repository.initConvaAI(
                assistantID = appData.assistant_id,
                assistantKey = appData.api_key,
                assistantVersion = appData.assistant_version,
                assistantType = ConvaAISDKType.ASSISTANT_SDK.name,
                activity)
        }
    }
}