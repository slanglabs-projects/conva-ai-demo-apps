package `in`.slanglabs.androidpg.convaai.impl.tts

import android.app.Application
import `in`.slanglabs.convaai.tts.ConvaAITTS
import `in`.slanglabs.convaai.tts.ConvaAITTSConfiguration
import `in`.slanglabs.convaai.tts.ConvaAITTSListener

class ConvaAITTSImpl(application: Application, responseCallBack: `in`.slanglabs.androidpg.convaai.impl.tts.ConvaAITTSListener): ConvaAITTSFacade {

    private var application: Application = application
    private var convaAITTSServer: ConvaAITTS? = null
    private var listener: `in`.slanglabs.androidpg.convaai.impl.tts.ConvaAITTSListener = responseCallBack

    init {
        this.application = application
    }

    override fun initialiseConvaAITTS(assistantID: String, assistantKey: String) {
        val convaAITTSConfig = ConvaAITTSConfiguration.Builder()
            .setAPIKey(assistantKey)
            .setAssistantID(assistantID)
            .setTTSListener(getTTSListener())
            .build()
        convaAITTSServer = ConvaAITTS(application,convaAITTSConfig)
    }

    override fun speak(text: String) {
        convaAITTSServer?.speak(text)
    }

    override fun skip() {
        convaAITTSServer?.stop()
    }

    override fun shutdown() {
        convaAITTSServer?.stop()
        convaAITTSServer?.shutdown()
    }

    private fun getTTSListener() : ConvaAITTSListener = object : ConvaAITTSListener {
        override fun onStart(id: String) {
            listener.onStart(id)
        }
        override fun onDone(id: String) {
            listener.onDone(id)
        }
        override fun onError(message: String) {
            listener.onError(message)
        }
        override fun onRangeStart(id: String, start: Int, end: Int, frame: Int) {
            listener.onRangeStart(id,start,end,frame)
        }
    }
}
