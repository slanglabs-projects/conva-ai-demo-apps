package `in`.slanglabs.convaai.pg.convaai.implementation.convaAICopilot

import android.app.Activity
import android.app.Application
import `in`.slanglabs.convaai.copilot.platform.ConvaAICopilot
import `in`.slanglabs.convaai.copilot.platform.ConvaAIInteraction
import `in`.slanglabs.convaai.copilot.platform.ConvaAIOptions
import `in`.slanglabs.convaai.copilot.platform.ConvaAIResponse
import `in`.slanglabs.convaai.copilot.platform.action.ConvaAIHandler
import `in`.slanglabs.convaai.copilot.platform.action.ConvaAIInputListener
import `in`.slanglabs.convaai.copilot.platform.template.ConvaAIAppConfigs
import `in`.slanglabs.convaai.core.ConvaAI

/**
 * Implementation class for the ConvaAICopilot.
 * This class provides methods to initialize, start conversations, and shut down the assistant.
 *
 * @property application The application instance.
 * @property listener The callback listener for assistant responses.
 * @property lifecycleObserver The lifecycle observer for the assistant.
 */
class ConavAICopilotImpl(private val application: Application, responseCallBack: ConvaAICopilotResponseListener, private val lifecycleObserver: ConvaAICopilotLifecycleObserver) :
    ConvaAICopilotFacade {
    var listener: ConvaAICopilotResponseListener = responseCallBack
    var isSDKInit: Boolean = false

    /**
     * Initializes the assistant with the given parameters.
     *
     * @param assistantID The unique ID of the assistant.
     * @param assistantKey The key required for authentication.
     * @param assistantVersion The version of the assistant.
     * @param startActivity Optional parameter to specify the starting activity.
     */
    override fun initialiseCopilot(
        assistantID: String,
        assistantKey: String,
        assistantVersion: String,
        startActivity: Activity
    ) {
        if (isSDKInit) return

        // Initialize the ConvaAICore with provided credentials
        ConvaAI.init(assistantID, assistantKey, assistantVersion, this.application)

        // Build the options for setting up ConvaAI
        val options = ConvaAIOptions.Builder()
            .setListener(getConvaAICopilotListener())
            .setCapabilityHandler(getConvaAICopilotAction())
            .setInputListener(getConvaAIInputListener())

        options.setStartActivity(startActivity)

        // Setup the ConvaAI with the built options
        ConvaAICopilot.setup(options.build())
    }

    /**
     * Starts a conversation with the ConvaAICopilot.
     *
     * @param activity The activity from which the conversation is started.
     */
    override fun startConversation(activity: Activity) {
        showUI(activity)
        ConvaAICopilot.startConversation()
    }

    /**
     * Shuts down the ConvaAICopilot.
     */
    override fun shutdown() {
        ConvaAICopilot.shutdown()
    }

    override fun setGlobalTrigger() {
        ConvaAICopilot.builtinUI.setGlobalTrigger()
    }

    /**
     * Displays the ConvaAIOverlay UI
     *
     * @param activity The activity in which the UI is displayed.
     */
    override fun showUI(activity: Activity) {
        ConvaAICopilot.builtinUI.show(activity)
    }

    private fun getConvaAIInputListener() : ConvaAIInputListener {
        return object : ConvaAIInputListener {
            override fun onTextDetected(userInput: String) {
                lifecycleObserver.onTextDetected(userInput)
            }

            override fun onUtteranceDetected(utterance: String) {
                lifecycleObserver.onTextDetected(utterance)
            }
        }
    }

    /**
     * Provides the ConvaAICopilot action handler.
     *
     * @return The action handler for the ConvaAICopilot.
     */
    private fun getConvaAICopilotAction() : ConvaAIHandler {
        return object : ConvaAIHandler {
            /**
             * Handles the capability response from the ConvaAICopilot.
             *
             * @param response The response from the ConvaAICopilot.
             * @param interactionData The interaction data from the ConvaAICopilot, which you can use to interact with the ConvaAI assistant UI.
             * @param isFinal Indicates if the response is final.
             */
            override fun onCapability(response: ConvaAIResponse, interactionData: ConvaAIInteraction, isFinal: Boolean) {
                if (isFinal) {
                    listener.onResponse(
                        message = response.message,
                        params = response.params,
                        jsonString = response.responseString,
                        capability = response.capability
                    )
                }
            }
        }
    }

    /**
     * Provides the ConvaAICopilot listener for various events.
     *
     * @return The listener for the ConvaAICopilot.
     */
    private fun getConvaAICopilotListener() : ConvaAICopilot.Listener {
        return object : ConvaAICopilot.Listener {
            /**
             * Called when the ConvaAICopilot is initialized successfully.
             *
             * @param appConfigs The configuration of the app.
             */
            override fun onInitialized(appConfigs: ConvaAIAppConfigs) {
                isSDKInit = true
                lifecycleObserver.onInitSuccessful()
            }

            /**
             * Called when the ConvaAICopilot initialization fails.
             *
             * @param e The error occurred during initialization.
             */
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