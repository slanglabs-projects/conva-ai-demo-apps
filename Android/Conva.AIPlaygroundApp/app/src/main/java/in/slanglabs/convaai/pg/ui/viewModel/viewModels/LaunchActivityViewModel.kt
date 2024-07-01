package `in`.slanglabs.convaai.pg.ui.viewModel.viewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.slanglabs.convaai.pg.App
import `in`.slanglabs.convaai.pg.ConvaAISDKType
import `in`.slanglabs.convaai.pg.ConvaAICopilotState
import `in`.slanglabs.convaai.pg.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LaunchActivityViewModel (application: Application) : ViewModel() {
    private val repository: Repository = (application as App).repository

    val title: String = "CONVA.ai"
    val backPressToast: String = "Press again to exit"
    val backPressTimer: Long = 2000L

    private val _isListening = MutableStateFlow<Boolean>(false)
    val isListening : StateFlow<Boolean> = _isListening

    private val _toastMessage = MutableStateFlow<String>("")
    val toastMessage: Flow<String>
        get() = _toastMessage

    private lateinit var convaAISDKType: ConvaAISDKType

    init {
        viewModelScope.launch {
            repository.convaAISDKType.collect { it ->
                convaAISDKType = it
            }
        }

        viewModelScope.launch {
            repository.convaAICopilotState.collect { it ->
                _isListening.value = it == ConvaAICopilotState.PROCESSING && convaAISDKType == ConvaAISDKType.FOUNDATION_SDK
                if (it == ConvaAICopilotState.FAILED) _toastMessage.value = it.message
            }
        }
    }

    fun clearContext() {
        _isListening.value = false
        _toastMessage.value = ""
    }
}