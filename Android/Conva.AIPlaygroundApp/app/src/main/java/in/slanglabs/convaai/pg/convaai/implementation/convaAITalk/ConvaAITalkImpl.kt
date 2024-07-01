package `in`.slanglabs.convaai.pg.convaai.implementation.convaAITalk

import android.app.Application
import `in`.slanglabs.convaai.talk.ConvaAITTS
import `in`.slanglabs.convaai.talk.ConvaAITTSConfiguration
import `in`.slanglabs.convaai.talk.ConvaAITTSListener

/**
 * Implementation class for the ConvaAITalk.
 * This class handles text-to-speech functionalities.
 *
 * @property application The application instance.
 * @property listener The listener for handling TTS events.
 */
class ConvaAITalkImpl(application: Application, responseCallBack: ConvaAITalkListener): ConvaAITalkFacade {

    private var application: Application = application
    private var convaAITTSServer: ConvaAITTS? = null
    private var listener: ConvaAITalkListener = responseCallBack

    init {
        this.application = application
    }

    /**
     * Initializes the text-to-speech service with the specified ConvaAITalk credentials.
     *
     * @param assistantID The unique ID of the ConvaAITalk.
     * @param assistantKey The key required for authentication.
     */
    override fun initialiseTalk(assistantID: String, assistantKey: String) {
        // Configure ConvaAITTS with provided credentials and listener
        val convaAITTSConfig = ConvaAITTSConfiguration.Builder()
            .setAPIKey(assistantKey)
            .setAssistantID(assistantID)
            .setTTSListener(getTTSListener())
            .build()

        // Initialize ConvaAITTS server with application context and configuration
        convaAITTSServer = ConvaAITTS(application,convaAITTSConfig)
    }

    /**
     * Converts the provided text into speech and speaks it.
     *
     * @param text The text to be spoken.
     */
    override fun speak(text: String) {
        convaAITTSServer?.speak(text)
    }

    /**
     * Skips the current speech playback.
     */
    override fun skip() {
        convaAITTSServer?.stop()
    }

    /**
     * Shuts down the text-to-speech service.
     */
    override fun shutdown() {
        convaAITTSServer?.stop()
        convaAITTSServer?.shutdown()
    }

    /**
     * Retrieves the ConvaAITTSListener instance for handling TTS events.
     *
     * @return The TTS listener implementation.
     */
    private fun getTTSListener() : ConvaAITTSListener = object : ConvaAITTSListener {
        /**
         * Called when TTS playback starts.
         *
         * @param id The ID associated with the TTS playback.
         */
        override fun onStart(id: String) {
            listener.onStart(id)
        }

        /**
         * Called when TTS playback completes.
         *
         * @param id The ID associated with the TTS playback.
         */
        override fun onDone(id: String) {
            listener.onDone(id)
        }

        /**
         * Called when an error occurs during TTS playback.
         *
         * @param message The error message.
         */
        override fun onError(message: String) {
            listener.onError(message)
        }

        /**
         * Called when a specific range of TTS playback starts.
         *
         * @param id The ID associated with the TTS playback.
         * @param start The start position of the range.
         * @param end The end position of the range.
         * @param frame The frame of the range.
         */
        override fun onRangeStart(id: String, start: Int, end: Int, frame: Int) {
            listener.onRangeStart(id,start,end,frame)
        }
    }
}
