package `in`.slanglabs.androidpg.convaai.impl.assistant.implementation

import android.app.Application
import `in`.slanglabs.androidpg.convaai.impl.assistant.ConvaAIFacade
import `in`.slanglabs.androidpg.convaai.impl.assistant.ConvaAIListener
import `in`.slanglabs.convaai.core.ConvaAI
import `in`.slanglabs.convaai.core.Response
import `in`.slanglabs.convaai.core.ResponseListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConvaAICoreSDKImpl(private val mApplication: Application): ConvaAIFacade {
    private var isSDKInit: Boolean = false
    private var conversationHistory: String = ""

    override fun initialiseConvaAI(
        assistantID: String,
        assistantKey: String,
        assistantVersion: String
    ) {
        if (isSDKInit) return

        ConvaAI.init(assistantID, assistantKey,assistantVersion,mApplication)
        isSDKInit = true
    }

    override fun singleShotResponse(
        text: String,
        capabilityGroupSelected: String,
        capabilitySelected: String,
        responseCallBack: ConvaAIListener
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = ConvaAI.invokeCapability(
                input = text, capability = capabilitySelected, history = conversationHistory
            )
            conversationHistory = response.history
            responseCallBack.onResponse(response.message,response.responseString)
        }
    }

    override fun streamResponse(text: String, capabilityGroupSelected: String, capabilitySelected: String, responseCallBack: ConvaAIListener) {
        CoroutineScope(Dispatchers.IO).launch {
            ConvaAI.invokeCapability(
                input = text,
                capability = capabilitySelected,
                listener = object : ResponseListener {
                    override fun onResponse(response: Response) {
                        responseCallBack.onResponseStream(response.message,response.responseString,response.isFinal)
                    }

                    override fun onError(e: Exception) {
                        // Handle error
                    }
                }
            )
        }
    }

    override fun shutdown() {
        conversationHistory = ""
    }
}