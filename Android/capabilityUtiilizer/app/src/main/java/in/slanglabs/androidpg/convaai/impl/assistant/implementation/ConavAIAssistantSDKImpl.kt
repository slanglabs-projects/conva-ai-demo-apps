package `in`.slanglabs.androidpg.convaai.impl.assistant.implementation

import android.app.Activity
import android.app.Application
import `in`.slanglabs.androidpg.convaai.impl.assistant.ConvaAIFacade
import `in`.slanglabs.androidpg.convaai.impl.assistant.ConvaAILifecycleObserver
import `in`.slanglabs.androidpg.convaai.impl.assistant.ConvaAIListener
import `in`.slanglabs.convaai.assistant.platform.ConvaAIAssistant
import `in`.slanglabs.convaai.assistant.platform.ConvaAIOptions
import `in`.slanglabs.convaai.assistant.platform.ConvaAIResponse
import `in`.slanglabs.convaai.assistant.platform.action.ConvaAIAction
import `in`.slanglabs.convaai.assistant.platform.template.ConvaAIAppConfigs
import `in`.slanglabs.convaai.assistant.ui.ConvaAIRichUI
//import `in`.slanglabs.convaai.assistant.

class ConavAIAssistantSDKImpl(private val application: Application, responseCallBack: ConvaAIListener, private val lifecycleObserver: ConvaAILifecycleObserver) :
    ConvaAIFacade {
    var listener: ConvaAIListener = responseCallBack
    var isSDKInit: Boolean = false

    override fun initialiseConvaAI(
        assistantID: String,
        assistantKey: String,
        assistantVersion: String,
        startActivity: Activity?
    ) {
        if (isSDKInit) return

        val options = ConvaAIOptions.Builder()
            .setApplication(this.application)
            .setAssistantId(assistantID)
            .setAPIKey(assistantKey)
            .setUIProvider(ConvaAIRichUI())
            .setAssistantVersion(assistantVersion)
            .setEnvironment(ConvaAIAssistant.Environment.PRODUCTION)
            .setListener(getConvaAIAssistantListener())
            .setAppAction(getConvaAIAssistantAction())

        if (startActivity != null) {
            options.setStartActivity(startActivity)
        }

        ConvaAIAssistant.initialize(options.build())
    }

    override fun singleShotResponse(responseCallBack: ConvaAIListener) {
        try {
            ConvaAIAssistant.startConversation()
        } catch (e : Exception) {
            // pass
        }
    }

    override fun shutdown() {
        ConvaAIAssistant.shutdown()
    }

    override fun showUI(activity: Activity) {
        ConvaAIAssistant.builtinUI.show(activity)
    }

    private fun getConvaAIAssistantAction() : ConvaAIAction {
        return object : ConvaAIAction {
            override fun onCapability(response: ConvaAIResponse, isFinal: Boolean) {
                if (isFinal) listener.onResponse(response.message,response.responseString)
            }
        }
    }

    private fun getConvaAIAssistantListener() : ConvaAIAssistant.Listener {
        return object : ConvaAIAssistant.Listener {
            override fun onInitialized(appConfigs: ConvaAIAppConfigs) {
                isSDKInit = true
                lifecycleObserver.onInitSuccessful()
            }

            override fun onInitializationFailed(e: ConvaAIAssistant.InitializationError?) {
                isSDKInit = false
                lifecycleObserver.onInitFailure()
            }

            override fun onSessionStart(isVoice: Boolean) {
            }

            override fun onSessionEnd(isCanceled: Boolean) {
            }

            override fun onOnboardingSuccess() {}

            override fun onOnboardingFailure() {}

            override fun onAppBackgrounded() {
            }

            override fun onAppForegrounded() {
            }

            override fun onASRPermissionDenied() {
            }

            override fun onASRPermissionGranted() {
            }

        }
    }
}