package `in`.slanglabs.convaai.pg.convaai.interfaces

import android.app.Activity
import android.app.Application
import `in`.slanglabs.convaai.pg.App
import `in`.slanglabs.convaai.pg.Repository
import `in`.slanglabs.convaai.pg.convaai.implementation.convaAICopilot.ConavAICopilotImpl
import `in`.slanglabs.convaai.pg.convaai.implementation.convaAICopilot.ConvaAICopilotFacade
import `in`.slanglabs.convaai.pg.convaai.implementation.convaAICopilot.ConvaAICopilotLifecycleObserver
import `in`.slanglabs.convaai.pg.convaai.implementation.convaAICopilot.ConvaAICopilotResponseListener
import `in`.slanglabs.convaai.pg.model.ChatResponse

class ConvaAICopilotInterface(private val application: Application) {
    private var convaAICopilot: ConvaAICopilotFacade? = null

    fun initialiseCopilotService(
        assistantID: String,
        assistantKey: String,
        assistantVersion: String,
        startActivity: Activity
    ) {
        val repository: Repository = (application as App).repository
        convaAICopilot = ConavAICopilotImpl(application,  object : ConvaAICopilotResponseListener {
            override fun onResponse(message: String, params: Map<String, Any>, jsonString: String) {
                val response = ChatResponse(
                    message = message,
                    params = params,
                    jsonString = jsonString
                )
                repository.sendResponse(response)
            }
        }, object : ConvaAICopilotLifecycleObserver {
            override fun onInitSuccessful() {
                repository.setConvaAIInitialized(true)
                setGlobalTrigger()
                showUI(startActivity)
            }

            override fun onInitFailure(errorMessage: String) {
                repository.setConvaAIInitialized(false,errorMessage)
            }

            override fun onTextDetected(text: String) {
                repository.setTextDetected(text)
            }
        })
        convaAICopilot?.initialiseCopilot(
            assistantID,
            assistantKey,
            assistantVersion,
            startActivity
        )
    }

    fun invokeCopilotService(activity: Activity) {
        convaAICopilot?.startConversation(activity = activity)
    }

    fun showUI(activity: Activity) {
        convaAICopilot?.showUI(activity)
    }

    fun shutdown() {
        convaAICopilot?.shutdown()
    }

    fun setGlobalTrigger() {
        convaAICopilot?.setGlobalTrigger()
    }
}