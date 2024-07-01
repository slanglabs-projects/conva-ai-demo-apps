package `in`.slanglabs.convaai.pg.convaai.interfaces

import android.app.Application
import `in`.slanglabs.convaai.pg.convaai.implementation.convaAITalk.ConvaAITalkFacade
import `in`.slanglabs.convaai.pg.convaai.implementation.convaAITalk.ConvaAITalkImpl
import `in`.slanglabs.convaai.pg.convaai.implementation.convaAITalk.ConvaAITalkListener

class ConvaAITalkInterface(private val application: Application) {
    private var convaAITalk: ConvaAITalkFacade? = null

    fun initTTSService(
        assistantID: String,
        assistantKey: String
    ) {
        convaAITalk = ConvaAITalkImpl(application, responseCallBack = object : ConvaAITalkListener {
            override fun onStart(string: String) {
            }

            override fun onDone(string: String) {
            }

            override fun onError(string: String) {
            }

            override fun onRangeStart(utterance: String, start: Int, end: Int, frame: Int) {
            }

        })
        convaAITalk?.initialiseTalk(assistantID,assistantKey)
    }

    fun speakText(text: String) {
        convaAITalk?.speak(text)
    }

    fun skipTTS() {
        convaAITalk?.skip()
    }

    fun shutdownTTS() {
        convaAITalk?.shutdown()
    }

    fun shutdown() {
        convaAITalk?.shutdown()
    }
}