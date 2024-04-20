package `in`.slanglabs.androidpg

import android.content.SharedPreferences
import `in`.slanglabs.androidpg.slang.SlangInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class Repository(
    private val mSlangInterface: SlangInterface,
    private val mPrefs: SharedPreferences
) {

    private val responseMessage = MutableStateFlow<String>("")

    val response: Flow<String>
        get() = responseMessage

    fun sendToLLM(text: String, capabilitySelected: String) {
        mSlangInterface.startConversation(text,capabilitySelected)
    }

    fun getSlangInterface(): SlangInterface {
        return mSlangInterface
    }

    fun sendResponse(response: String) {
        responseMessage.value = response
    }

    fun clearData() {
        responseMessage.value = ""
    }
}