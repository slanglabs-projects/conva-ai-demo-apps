package `in`.slanglabs.convaai.pg.convaai.implementation.convaAISpeech

interface ConvaAISpeechListener {
    fun onTimeOut()
    fun onPartialTextAvailable(text: String)
    fun onCompleteTextAvailable(text: String)
}