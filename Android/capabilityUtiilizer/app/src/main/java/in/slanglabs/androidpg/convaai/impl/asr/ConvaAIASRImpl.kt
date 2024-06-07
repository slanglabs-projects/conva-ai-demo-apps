package `in`.slanglabs.androidpg.convaai.impl.asr

import android.app.Activity
import `in`.slanglabs.androidpg.BuildConfig
import `in`.slanglabs.androidpg.convaai.impl.asr.utils.AudioUtils
import `in`.slanglabs.convaai.asr.ConvaAIASR
import `in`.slanglabs.convaai.asr.ConvaAIASRConfiguration
import `in`.slanglabs.convaai.asr.ConvaAIASREnvironment
import `in`.slanglabs.convaai.asr.ConvaAIASRListener
import `in`.slanglabs.convaai.asr.ConvaAIASRType

class ConvaAIASRImpl : ConvaAIASRFacade {
    private lateinit var mListener: `in`.slanglabs.androidpg.convaai.impl.asr.ConvaAIASRListener
    override fun initialiseConvaAIASR(assistantID: String, assistantKey: String, assistantVersion: String, startActivity: Activity, listener: `in`.slanglabs.androidpg.convaai.impl.asr.ConvaAIASRListener) {
        val convaAIASRConfiguration = ConvaAIASRConfiguration.Builder()
            .setAPIKey(assistantID)
            .setAssistantId(assistantKey)
            .setAssistantVersion(assistantVersion)
            .setASRListener(getConvaAIASRListener())
            .setASRType(ConvaAIASRType.ServerASR)

        convaAIASRConfiguration.setEnvironment(ConvaAIASREnvironment.PRODUCTION)

        val serverConfig = ConvaAIASRConfiguration.ASRServerConfig.Builder()
            .setASRServerHost(BuildConfig.ASR_SERVICE_HOST)
            .setASRServerProtocol(BuildConfig.ASR_SERVICE_PROTOCOL)
            .setASRServerVersion(BuildConfig.ASR_SERVICE_VERSION)

        convaAIASRConfiguration.setASRServerConfig(serverConfig.build())
        ConvaAIASR.initialize(convaAIASRConfiguration.build())

        AudioUtils.initASRSounds(startActivity.application)
        mListener = listener
    }

    override fun startListening(activity: Activity) {
        ConvaAIASR.startListening(activity, getConvaAIASRListener())
    }

    override fun stopListening() {
        ConvaAIASR.stopListening()
        AudioUtils.playASREndSound()
    }

    override fun shutdown() {
    }

    private fun getConvaAIASRListener() : ConvaAIASRListener {
        return object : ConvaAIASRListener {
            override fun onError(source: String, errorType: String, errorMessage: String) {
                // Handle Error
            }

            override fun onTextAvailable(text: String, isFinal: Boolean) {
                if (isFinal) {
                    mListener.onCompleteTextAvailable(text)
                } else {
                    mListener.onPartialTextAvailable(text)
                }
            }

            override fun onTimeout() {
                AudioUtils.playASRSuspendSound()
                mListener.onTimeOut()
            }

            override fun onSpeechRecognitionBegin() {
                AudioUtils.playASRBeginSound()
            }
            override fun onSpeechRecognitionEnded() {
                AudioUtils.playASREndSound()
            }
        }
    }
}