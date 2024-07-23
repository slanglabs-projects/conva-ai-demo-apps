package `in`.slanglabs.convaai.pg.convaai.implementation.convaAICore

interface ConvaAICoreResponseListener {
    fun onResponse(message: String, params: Map<String, Any>, jsonString: String) {
        // pass
    }
    fun onResponseStream(message: String, params: Map<String, Any>, jsonString: String, isFinal: Boolean) {
        // pass
    }
}