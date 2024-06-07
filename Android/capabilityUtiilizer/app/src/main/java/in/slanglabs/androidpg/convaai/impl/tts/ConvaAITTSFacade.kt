package `in`.slanglabs.androidpg.convaai.impl.tts

interface ConvaAITTSFacade {
    fun initialiseConvaAITTS(assistantID: String, assistantKey: String)
    fun speak(text: String)
    fun skip()
    fun shutdown()
}