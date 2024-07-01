package `in`.slanglabs.convaai.pg.convaai.implementation.convaAISpeech

import android.app.Activity
import `in`.slanglabs.convaai.pg.BuildConfig
import `in`.slanglabs.convaai.pg.convaai.implementation.convaAISpeech.utils.AudioUtils
import `in`.slanglabs.convaai.speech.ConvaAIASR
import `in`.slanglabs.convaai.speech.ConvaAIASRConfiguration
import `in`.slanglabs.convaai.speech.ConvaAIASREnvironment
import `in`.slanglabs.convaai.speech.ConvaAIASRListener
import `in`.slanglabs.convaai.speech.ConvaAIASRType

/**
 * Implementation class for the ConvaAISpeech.
 * This class manages speech recognition functionalities.
 */
class ConvaAISpeechImpl : ConvaAISpeechFacade {
    private lateinit var mListener: ConvaAISpeechListener


    /**
     * Initializes speech recognition with the specified ConvaAISpeech credentials and listener.
     *
     * @param assistantID The unique ID of the ConvaAISpeech.
     * @param assistantKey The key required for authentication.
     * @param assistantVersion The version of the ConvaAISpeech.
     * @param startActivity The starting activity where speech recognition is initialized.
     * @param listener The listener to handle speech recognition events.
     */
    override fun initialiseSpeech(assistantID: String, assistantKey: String, assistantVersion: String, startActivity: Activity, listener: ConvaAISpeechListener) {
        // Configure ConvaAIASR with provided credentials and environment
        val convaAIASRConfiguration = ConvaAIASRConfiguration.Builder()
            .setAPIKey(assistantID)
            .setAssistantId(assistantKey)
            .setAssistantVersion(assistantVersion)
            .setASRListener(getConvaAIASRListener())
            .setASRType(ConvaAIASRType.ServerASR)

        convaAIASRConfiguration.setEnvironment(ConvaAIASREnvironment.PRODUCTION)

        // Configure ASR server details
        val serverConfig = ConvaAIASRConfiguration.ASRServerConfig.Builder()
            .setASRServerHost(BuildConfig.ASR_SERVICE_HOST)
            .setASRServerProtocol(BuildConfig.ASR_SERVICE_PROTOCOL)
            .setASRServerVersion(BuildConfig.ASR_SERVICE_VERSION)

        // Initialize ConvaAIASR and audio utilities
        convaAIASRConfiguration.setASRServerConfig(serverConfig.build())
        ConvaAIASR.initialize(convaAIASRConfiguration.build())

        AudioUtils.initASRSounds(startActivity.application)
        mListener = listener
    }

    /**
     * Starts listening for speech input.
     *
     * @param activity The activity where speech recognition is active.
     */
    override fun startListening(activity: Activity) {
        ConvaAIASR.startListening(activity, getConvaAIASRListener())
    }

    /**
     * Stops listening for speech input.
     */
    override fun stopListening() {
        ConvaAIASR.stopListening()
        AudioUtils.playASREndSound()
    }

    override fun shutdown() {
    }

    private fun getConvaAIASRListener() : ConvaAIASRListener {
        return object : ConvaAIASRListener {
            /**
             * Called when an error occurs during speech recognition.
             *
             * @param source The source of the error.
             * @param errorType The type of error.
             * @param errorMessage The error message.
             */
            override fun onError(source: String, errorType: String, errorMessage: String) {
                // Handle Error
            }

            /**
             * Called when text is available from speech recognition.
             *
             * @param text The recognized text.
             * @param isFinal Indicates if the text is final or interim.
             */
            override fun onTextAvailable(text: String, isFinal: Boolean) {
                if (isFinal) {
                    mListener.onCompleteTextAvailable(text)
                } else {
                    mListener.onPartialTextAvailable(text)
                }
            }

            /**
             * Called when speech recognition times out.
             */
            override fun onTimeout() {
                AudioUtils.playASRSuspendSound()
                mListener.onTimeOut()
            }

            /**
             * Called when speech recognition begins.
             */
            override fun onSpeechRecognitionBegin() {
                AudioUtils.playASRBeginSound()
            }
            /**
             * Called when speech recognition ends.
             */
            override fun onSpeechRecognitionEnded() {
                AudioUtils.playASREndSound()
            }
        }
    }
}