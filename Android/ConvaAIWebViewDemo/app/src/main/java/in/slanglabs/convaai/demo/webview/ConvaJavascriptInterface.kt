package `in`.slanglabs.convaai.demo.webview

import android.app.Application
import android.webkit.JavascriptInterface
import `in`.slanglabs.convaai.copilot.platform.ConvaAICopilot
import `in`.slanglabs.convaai.copilot.platform.ConvaAIInteraction
import `in`.slanglabs.convaai.copilot.platform.ConvaAIOptions
import `in`.slanglabs.convaai.copilot.platform.ConvaAIResponse
import `in`.slanglabs.convaai.copilot.platform.action.ConvaAIHandler
import `in`.slanglabs.convaai.copilot.platform.action.ConvaAISuggestionHandler
import `in`.slanglabs.convaai.copilot.platform.template.ConvaAIAppConfigs
import `in`.slanglabs.convaai.copilot.platform.ui.ConvaAISuggestion
import `in`.slanglabs.convaai.core.ConvaAI
import org.json.JSONObject

class ConvaJavascriptInterface(private val listener: JSFunctionListener, private val application: Application) {

    @JavascriptInterface
    fun initializeCopilot(id: String, key: String, version: String) {
        ConvaAI.init(id, key, version, application)
        val options = ConvaAIOptions.Builder()
            .setEnvironment(ConvaAICopilot.Environment.PRODUCTION)
            .setListener(object : ConvaAICopilot.Listener {
                override fun onInitializationFailed(e: ConvaAICopilot.InitializationError?) {
                    listener.onCopilotError("Error: " + (e?.message ?: "Init failed"))
                }

                override fun onInitialized(appConfigs: ConvaAIAppConfigs) {
                    listener.onCopilotInitialized()
                }
            })
            .setCapabilityHandler(object : ConvaAIHandler {
                override fun onCapability(
                    response: ConvaAIResponse,
                    interactionData: ConvaAIInteraction,
                    isFinal: Boolean
                ) {
                    val responseObj = JSONObject()
                    responseObj.put("input_query", response.input)
                    responseObj.put("message", response.message)
                    responseObj.put("reason", response.reason)
                    responseObj.put("capability", response.capability)
                    responseObj.put("is_final", response.isFinal)
                    responseObj.put("is_error", response.isError)
                    responseObj.put("is_unsupported", response.isUnsupported)
                    responseObj.put("params", Utils.mapToJSONObject(response.params))
                    responseObj.put("related_queries", Utils.arrayListToJSONArray(response.relatedQueries))
                    listener.onCopilotResponse(responseObj.toString().replace("'", "\\'"))
                }

            })
            .setSuggestionHandler(object : ConvaAISuggestionHandler {
                override fun onSuggestion(suggestion: ConvaAISuggestion) {
                    val suggestionObj = JSONObject()
                    suggestionObj.put("payload", suggestion.payload)
                    suggestionObj.put("display_text", suggestion.displayText)
                    suggestionObj.put("params", Utils.mapToJSONObject(suggestion.params))
                    suggestionObj.put("capability", suggestion.capability)
                    listener.onCopilotSuggestion(suggestionObj.toString().replace("'", "\\'"))
                }
            })
            .build()
        ConvaAICopilot.setup(options)
    }

    @JavascriptInterface
    fun startConversation() {
        ConvaAICopilot.startConversation()
    }
}