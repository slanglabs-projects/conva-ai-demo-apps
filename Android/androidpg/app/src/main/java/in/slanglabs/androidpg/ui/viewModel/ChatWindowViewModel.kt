package `in`.slanglabs.androidpg.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import `in`.slanglabs.androidpg.App
import `in`.slanglabs.androidpg.Repository
import `in`.slanglabs.androidpg.slang.SlangInterface
import kotlinx.coroutines.launch

class ChatWindowViewModel (application: Application) : AndroidViewModel(application) {
    private val mRepository: Repository
    private val mSlangInterface: SlangInterface
    private val _messageLiveData = MutableLiveData<String>()
    val messageLiveData: LiveData<String> = _messageLiveData

    init {
        mRepository = (application as App).getRepository()
        mSlangInterface = mRepository.getSlangInterface()

        viewModelScope.launch {
            mRepository.response.collect { message ->
                _messageLiveData.value = message
            }
        }
    }

    fun sendText(text: String) {
        mRepository.sendToLLM(text)
    }

    fun initSlang(assistantID: String, assistantKey: String, assistantVersion: String) {
        mRepository.getSlangInterface().initialiseCopilot(assistantID,assistantKey,assistantVersion)
    }

    fun clearData() {
        _messageLiveData.value = ""
        mRepository.clearData()
    }

}