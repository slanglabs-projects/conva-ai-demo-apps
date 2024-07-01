package `in`.slanglabs.convaai.pg.convaai.implementation.convaAICore

import android.app.Application
import `in`.slanglabs.convaai.core.ConvaAI
import `in`.slanglabs.convaai.core.ConvaAIContext
import `in`.slanglabs.convaai.core.Response
import `in`.slanglabs.convaai.core.ResponseListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Implementation class for the ConvaAICore.
 * This class provides methods to initialize the assistant, handle single-shot and streaming responses,
 * and shut down the assistant.
 *
 * @property mApplication The application instance.
 */
class ConvaAICoreImpl(private val mApplication: Application): ConvaAICoreFacade {
    private var isSDKInit: Boolean = false
    private var conversationHistory: String = ""

    /**
     * Initializes the ConvaAICore with the given parameters.
     *
     * @param assistantID The unique ID of the ConvaAICore.
     * @param assistantKey The key required for authentication.
     * @param assistantVersion The version of the ConvaAICore.
     */
    override fun initialiseCoreAI(
        assistantID: String,
        assistantKey: String,
        assistantVersion: String
    ) {
        if (isSDKInit) return

        // Initialize the ConvaAICore with provided credentials
        ConvaAI.init(assistantID, assistantKey,assistantVersion,mApplication)
        isSDKInit = true
    }

    /**
     * Sends a single-shot request to the ConvaAICore and handles the response.
     *
     * @param text The input text for the ConvaAICore.
     * @param capabilityGroupSelected The selected capability group.
     * @param capabilitySelected The selected capability.
     * @param responseCallBack The callback listener for the response.
     */
    override fun singleShotResponse(
        text: String,
        capabilityGroupSelected: String,
        capabilitySelected: String,
        responseCallBack: ConvaAICoreResponseListener
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ConvaAI.invokeCapability(
                    input = text, capability = capabilitySelected, context = ConvaAIContext(history = conversationHistory)
                )
                conversationHistory = response.history
                responseCallBack.onResponse(response.message, response.responseString)
            } catch (e: Exception) {
                // Handle Exception
                responseCallBack.onResponse("Error while sending the request, Please try again",e.message ?: "")
            }
        }
    }

    /**
     * Sends a streaming request to the ConvaAICore and handles the response.
     *
     * @param text The input text for the ConvaAICore.
     * @param capabilityGroupSelected The selected capability group.
     * @param capabilitySelected The selected capability.
     * @param responseCallBack The callback listener for the response.
     */
    override fun streamResponse(
        text: String,
        capabilityGroupSelected: String,
        capabilitySelected: String,
        responseCallBack: ConvaAICoreResponseListener
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                ConvaAI.invokeCapability(
                    input = text,
                    capability = capabilitySelected,
                    listener = object : ResponseListener {
                        override fun onResponse(response: Response, isFinal: Boolean) {
                            conversationHistory = response.history
                            responseCallBack.onResponseStream(
                                response.message,
                                response.responseString,
                                response.isFinal
                            )
                        }

                        override fun onError(e: Exception) {
                            // Handle error
                            throw e
                        }
                    }
                )
            } catch (e: Exception) {
                // Handle Exception
                responseCallBack.onResponse("Error while sending the request, Please try again",e.message ?: "")
            }
        }
    }

    /**
     * Clears the conversation history.
     */
    override fun shutdown() {
        conversationHistory = ""
    }
}