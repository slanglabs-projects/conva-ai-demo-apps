package `in`.slanglabs.convaai.pg.convaai.implementation.convaAITalk

interface ConvaAITalkFacade {
    fun initialiseTalk(assistantID: String, assistantKey: String)
    fun speak(text: String)
    fun skip()
    fun shutdown()
}