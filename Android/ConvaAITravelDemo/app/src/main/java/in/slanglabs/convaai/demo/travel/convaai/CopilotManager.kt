package `in`.slanglabs.convaai.demo.travel.convaai

import android.app.Activity
import `in`.slanglabs.convaai.copilot.platform.ConvaAICopilot
import `in`.slanglabs.convaai.copilot.platform.ConvaAIInteraction
import `in`.slanglabs.convaai.copilot.platform.ConvaAIOptions
import `in`.slanglabs.convaai.copilot.platform.ConvaAIResponse
import `in`.slanglabs.convaai.copilot.platform.action.ConvaAIHandler
import `in`.slanglabs.convaai.copilot.platform.template.ConvaAIAppConfigs
import `in`.slanglabs.convaai.core.ConvaAI
import `in`.slanglabs.convaai.demo.travel.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CopilotManager {
    private val _copilotResponseState = MutableStateFlow<ConvaAIResponse?>(null)
    val copilotResponseState: StateFlow<ConvaAIResponse?> = _copilotResponseState.asStateFlow()
    private val _isCopilotInitialized = MutableStateFlow(false)
    val isCopilotInitialized: StateFlow<Boolean> = _isCopilotInitialized.asStateFlow()

    fun initialize(activity: Activity) {
        ConvaAI.init(
            BuildConfig.ASSISTANT_ID,
            BuildConfig.API_KEY,
            BuildConfig.COPILOT_VERSION,
            activity.application
        )
        val options = ConvaAIOptions.Builder()
            .setCapabilityHandler(object : ConvaAIHandler {
                override fun onCapability(
                    response: ConvaAIResponse,
                    interactionData: ConvaAIInteraction,
                    isFinal: Boolean
                ) {
                    if (isFinal) {
                        _copilotResponseState.value = response
                    }
                }
            })
            .setListener(object : ConvaAICopilot.Listener {
                override fun onASRPermissionDenied() {}

                override fun onASRPermissionGranted() {}

                override fun onAppBackgrounded() {}

                override fun onAppForegrounded() {}

                override fun onInitializationFailed(e: ConvaAICopilot.InitializationError?) {}

                override fun onInitialized(appConfigs: ConvaAIAppConfigs) {
                    _isCopilotInitialized.value = true
                }

                override fun onOnboardingFailure() {}

                override fun onOnboardingSuccess() {}

                override fun onSessionEnd(isCanceled: Boolean) {}

                override fun onSessionStart(isVoice: Boolean) {}
            })
            .build()
        ConvaAICopilot.setup(options)
        ConvaAICopilot.attach(activity)
    }

    fun invokeCopilot() {
        ConvaAICopilot.startConversation()
    }

}