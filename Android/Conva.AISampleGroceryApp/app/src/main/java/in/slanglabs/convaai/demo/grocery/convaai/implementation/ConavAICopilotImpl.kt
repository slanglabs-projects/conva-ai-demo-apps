package `in`.slanglabs.convaai.demo.grocery.convaai.implementation

import android.app.Activity
import android.app.Application
import android.util.Log
import `in`.slanglabs.convaai.copilot.platform.ConvaAICopilot
import `in`.slanglabs.convaai.copilot.platform.ConvaAIInteraction
import `in`.slanglabs.convaai.copilot.platform.ConvaAIOptions
import `in`.slanglabs.convaai.copilot.platform.ConvaAIResponse
import `in`.slanglabs.convaai.copilot.platform.action.ConvaAIHandler
import `in`.slanglabs.convaai.copilot.platform.template.ConvaAIAppConfigs
import `in`.slanglabs.convaai.core.ConvaAI

/**
 * Implementation of the ConvaAICopilotFacade interface.
 * This class handles initialization, starting conversation, and other operations of the ConvaAICopilot.
 *
 * @param application The application context
 * @param responseCallBack Callback to handle ConvaAI responses
 * @param lifecycleObserver Observer for lifecycle events related to ConvaAICopilot
 */
class ConavAICopilotImpl(private val application: Application, responseCallBack: ConvaAICopilotListener, private val lifecycleObserver: ConvaAICopilotLifecycleObserver) :
    ConvaAICopilotFacade {
    val TAG: String = "ConavAICopilotImpl"
    var listener: ConvaAICopilotListener = responseCallBack
    var isSDKInit: Boolean = false
    val searchTerm: String = "search_term"
    val navigationTarget: String = "target_page"

    /**
     * Initializes the ConvaAICopilot SDK if it hasn't been initialized already.
     *
     * @param assistantID The ID of the assistant
     * @param assistantKey The key for the assistant
     * @param assistantVersion The version of the assistant
     * @param startActivity The activity to start the assistant in
     */
    override fun initialiseCopilot(
        assistantID: String,
        assistantKey: String,
        assistantVersion: String,
        startActivity: Activity?
    ) {
        if (isSDKInit) return

        ConvaAI.init(assistantID, assistantKey, assistantVersion, application)

        val options = ConvaAIOptions.Builder()
            .setCapabilityHandler(getConvaAIHandlerAction())
            .setListener(getConvaAICopilotListener())
            .build()
        ConvaAICopilot.setup(options)
    }

    /**
     * Provides the handler for ConvaAI actions, defining what to do when a capability is detected.
     *
     * @return An instance of ConvaAIHandler
     */
    private fun getConvaAIHandlerAction() : ConvaAIHandler {
        return object : ConvaAIHandler {
            override fun onCapability(response: ConvaAIResponse, interactionData: ConvaAIInteraction, isFinal: Boolean) {
                if (isFinal) listener.onResponse(executePostProcessing(response))
            }
        }
    }

    /**
     * Processes the ConvaAI response and extracts parameters such as search term and navigation target.
     *
     * @param response The response from ConvaAI
     * @return A Response object containing the processed data
     */
    private fun executePostProcessing(response: ConvaAIResponse): Response {
        return try {
            val paramsMap = response.params as Map<String, String>
            val searchTerm = paramsMap[searchTerm] ?: ""
            val navigationTarget = paramsMap[navigationTarget] ?: ""

            Response(searchTerm, navigationTarget, response.message, response.responseString)
        } catch (e: Exception) {
            Log.d(TAG, "exception: ${e.message}")
            Response("", "", response.message, response.responseString)
        }
    }

    /**
     * Starts a conversation using ConvaAICopilot.
     */
    override fun startConversation() {
        try {
            ConvaAICopilot.startConversation()
        } catch (e : Exception) {
            // pass
        }
    }

    /**
     * Shuts down the ConvaAICopilot.
     */
    override fun shutdown() {
        ConvaAICopilot.shutdown()
    }

    /**
     * Displays the built-in UI of ConvaAICopilot.
     *
     * @param activity The activity in which to show the UI
     */
    override fun showUI(activity: Activity) {
        ConvaAICopilot.attach(activity)
    }

    /**
     * Checks if ConvaAICopilot is initialized.
     *
     * @return True if initialized, false otherwise
     */
    override fun isConvaAICopilotInitialised(): Boolean {
        return isSDKInit
    }

    /**
     * Provides the listener for ConvaAICopilot lifecycle events.
     *
     * @return An instance of ConvaAICopilot.Listener
     */
    private fun getConvaAICopilotListener() : ConvaAICopilot.Listener {
        return object : ConvaAICopilot.Listener {
            override fun onInitialized(appConfigs: ConvaAIAppConfigs) {
                isSDKInit = true
                lifecycleObserver.onInitSuccessful()
            }

            override fun onInitializationFailed(e: ConvaAICopilot.InitializationError?) {
                isSDKInit = false
                lifecycleObserver.onInitFailure(e.toString())
            }

            override fun onSessionStart(isVoice: Boolean) {}

            override fun onSessionEnd(isCanceled: Boolean) {}

            override fun onAppBackgrounded() {}

            override fun onAppForegrounded() {}

            override fun onASRPermissionDenied() {}

            override fun onASRPermissionGranted() {}

        }
    }
}