package `in`.slanglabs.convaai.pg.convaai.implementation.convaAICore

interface ConvaAICoreResponseListener {
    fun onResponse(message: String, params: Map<String, Any>, jsonString: String, capability: String) {
        // pass
    }
    fun onResponseStream(message: String, params: Map<String, Any>, jsonString: String, capability: String, isFinal: Boolean) {
        // pass
    }
}