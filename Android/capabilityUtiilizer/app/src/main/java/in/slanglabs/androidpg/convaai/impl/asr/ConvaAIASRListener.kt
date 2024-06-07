package `in`.slanglabs.androidpg.convaai.impl.asr

interface ConvaAIASRListener {
    fun onTimeOut()
    fun onPartialTextAvailable(text: String)
    fun onCompleteTextAvailable(text: String)
}