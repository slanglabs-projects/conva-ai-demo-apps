package `in`.slanglabs.androidpg.convaai.impl.assistant

interface ConvaAIListener {
    fun onResponse(message: String, jsonString: String) {
        // pass
    }
    fun onResponseStream(message: String, jsonString: String, isFinal: Boolean) {
        if (isFinal) onResponse(message,jsonString)
    }
}