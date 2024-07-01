package `in`.slanglabs.convaai.pg.convaai.implementation.convaAICore

interface ConvaAICoreResponseListener {
    fun onResponse(message: String, jsonString: String) {
        // pass
    }
    fun onResponseStream(message: String, jsonString: String, isFinal: Boolean) {
        // pass
    }
}