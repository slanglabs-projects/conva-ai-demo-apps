package `in`.slanglabs.convaai.demo.grocery.convaai


import android.app.Activity
import android.app.Application
import `in`.slanglabs.convaai.demo.grocery.App
import `in`.slanglabs.convaai.demo.grocery.BuildConfig
import `in`.slanglabs.convaai.demo.grocery.Repository
import `in`.slanglabs.convaai.demo.grocery.convaai.implementation.ConvaAICopilotFacade
import `in`.slanglabs.convaai.demo.grocery.convaai.implementation.ConvaAICopilotLifecycleObserver
import `in`.slanglabs.convaai.demo.grocery.convaai.implementation.ConvaAICopilotListener
import `in`.slanglabs.convaai.demo.grocery.convaai.implementation.ConavAICopilotImpl
import `in`.slanglabs.convaai.demo.grocery.convaai.implementation.Response

class ConvaAIInterface(private val application: Application) {
    private var convaAIImpl: ConvaAICopilotFacade? = null
    private var isSlangInitialized: Boolean = false

    init {
        initialiseCopilotSDK(BuildConfig.ASSISTANT_ID, BuildConfig.API_KEY,BuildConfig.ASSISTANT_VERSION)
    }

    private fun initialiseCopilotSDK(
        assistantID: String,
        assistantKey: String,
        assistantVersion: String,
        startActivity: Activity? = null,
        lifecycleObserver: `in`.slanglabs.convaai.demo.grocery.convaai.ConvaAIListener? = null
    ) {
        convaAIImpl = ConavAICopilotImpl(application,  object : ConvaAICopilotListener {
            override fun onResponse(response: Response) {
                val repository: Repository = (application as App).repository
                if (response.navigationTarget.isNotEmpty()) {
                    repository.onNavigation(response.navigationTarget.trim())
                    return
                }
                if (response.searchTerm.isNotEmpty()) {
                    repository.onSearch(response.searchTerm.trim())
                }
            }
        }, object : ConvaAICopilotLifecycleObserver {
            override fun onInitSuccessful() {
                val repository: Repository = (application as App).repository
                isSlangInitialized = true
                lifecycleObserver?.onInitSuccessful()
                repository.setSlangInitialized(true)
            }

            override fun onInitFailure(errorMessage: String) {
                val repository: Repository = (application as App).repository
                isSlangInitialized = false
                lifecycleObserver?.onInitFailure(errorMessage)
                repository.setSlangInitialized(false)
            }
        })
        convaAIImpl?.initialiseCopilot(assistantID,assistantKey,assistantVersion,startActivity)
    }

    fun startConversation(activity: Activity) {
        showUI(activity)
        convaAIImpl?.startConversation()
    }

    fun showUI(activity: Activity) {
        convaAIImpl?.showUI(activity)
    }

    fun shutdown() {
        convaAIImpl?.shutdown()
    }

    fun isConvaAIInitialised():Boolean {
        return convaAIImpl?.isConvaAICopilotInitialised() ?: false
    }
}