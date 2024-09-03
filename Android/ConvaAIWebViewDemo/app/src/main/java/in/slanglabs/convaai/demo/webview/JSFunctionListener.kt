package `in`.slanglabs.convaai.demo.webview

interface JSFunctionListener {
    fun onCopilotInitialized()
    fun onCopilotError(error: String)
    fun onCopilotResponse(response: String)
    fun onCopilotSuggestion(suggestion: String)
}