package `in`.slanglabs.androidpg.ui.viewModel.viewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.slanglabs.androidpg.App
import `in`.slanglabs.androidpg.ConvaAISDKType
import `in`.slanglabs.androidpg.ConvaAIAssistantState
import `in`.slanglabs.androidpg.Repository
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

    private lateinit var convaAISDKType: ConvaAISDKType

    init {
        viewModelScope.launch {
            repository.convaAISDKType.collect { it ->
                convaAISDKType = it
            }
        }

        viewModelScope.launch {
            repository.convaAIAssistantState.collect { it ->
                _isListening.value = it == ConvaAIAssistantState.PROCESSING && convaAISDKType == ConvaAISDKType.CORE_SDK
            }
        }
    }
}