package `in`.slanglabs.convaai.pg.convaai.implementation.convaAICopilot

interface ConvaAICopilotResponseListener {
    fun onResponse(message: String, params: Map<String, Any>, jsonString: String, capability: String)
}