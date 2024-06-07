package `in`.slanglabs.androidpg.convaai


import android.app.Activity
import android.app.Application
import `in`.slanglabs.androidpg.App
import `in`.slanglabs.androidpg.Repository
import `in`.slanglabs.androidpg.model.ASRData
import `in`.slanglabs.androidpg.model.ChatResponse
import `in`.slanglabs.androidpg.convaai.impl.asr.ConvaAIASRFacade
import `in`.slanglabs.androidpg.convaai.impl.asr.ConvaAIASRImpl
import `in`.slanglabs.androidpg.convaai.impl.asr.ConvaAIASRListener
import `in`.slanglabs.androidpg.convaai.impl.assistant.implementation.ConavAIAssistantSDKImpl
import `in`.slanglabs.androidpg.convaai.impl.assistant.implementation.ConvaAICoreSDKImpl
import `in`.slanglabs.androidpg.convaai.impl.assistant.ConvaAIFacade
import `in`.slanglabs.androidpg.convaai.impl.assistant.ConvaAILifecycleObserver
import `in`.slanglabs.androidpg.convaai.impl.assistant.ConvaAIListener
import `in`.slanglabs.androidpg.convaai.impl.tts.ConvaAITTSFacade
import `in`.slanglabs.androidpg.convaai.impl.tts.ConvaAITTSImpl
import `in`.slanglabs.androidpg.convaai.impl.tts.ConvaAITTSListener

class ConvaAIInterface(application: Application) {

    private var application: Application = application
    private val isSingleShotResponse: Boolean = true
    private var convaImpl: ConvaAIFacade? = null
    private var convaTTS: ConvaAITTSFacade? = null
    private var convaASR: ConvaAIASRFacade? = null
    private lateinit var assistantID : String
    private lateinit var apiKey : String

    init {
        this.application = application
    }

    fun initASRService(
        assistantID: String,
        assistantKey: String,
        assistantVersion: String,
        startActivity: Activity
    ) {
        val repository: Repository = (application as App).repository
        convaASR = ConvaAIASRImpl()
        convaASR?.initialiseConvaAIASR(assistantID,assistantKey,assistantVersion,startActivity,listener = object : ConvaAIASRListener{
            override fun onTimeOut() {
                repository.asrTimeOut()
            }

            override fun onPartialTextAvailable(text: String) {
                repository.asrPartialTextAvailable(ASRData(text,false))
            }

            override fun onCompleteTextAvailable(text: String) {
                repository.asrCompleteTextAvailable(ASRData(text,true))
            }

        })
    }

    fun initTTSService(
        assistantID: String,
        assistantKey: String
    ) {
        convaTTS = ConvaAITTSImpl(application, responseCallBack = object : ConvaAITTSListener {
            override fun onStart(string: String) {
            }

            override fun onDone(string: String) {
            }

            override fun onError(string: String) {
            }

            override fun onRangeStart(utterance: String, start: Int, end: Int, frame: Int) {
            }

        })
        convaTTS?.initialiseConvaAITTS(assistantID,assistantKey)
    }

    fun speakText(text: String) {
        convaTTS?.speak(text)
    }

    fun initialiseCopilotSDK(
        assistantID: String,
        assistantKey: String,
        assistantVersion: String
    ) {
        this.assistantID = assistantID
        apiKey = assistantKey
        convaImpl = ConvaAICoreSDKImpl(application)
        convaImpl?.initialiseConvaAI(assistantID,assistantKey,assistantVersion)
    }

    fun initialiseCopilotAssistant(
        assistantID: String,
        assistantKey: String,
        assistantVersion: String,
        startActivity: Activity,
        lifecycleObserver: ConvaInterfaceListener
    ) {
        convaImpl = ConavAIAssistantSDKImpl(application,  object : ConvaAIListener {
            override fun onResponse(message: String, jsonString: String) {
                val repository: Repository = (application as App).repository
                val response = ChatResponse(message,jsonString)
                repository.sendResponse(response)
            }
        }, object : ConvaAILifecycleObserver {
            override fun onInitSuccessful() {
                lifecycleObserver.onInitSuccessful()
            }

            override fun onInitFailure() {
                lifecycleObserver.onInitFailure()
            }
        })
        convaImpl?.initialiseConvaAI(assistantID,assistantKey,assistantVersion,startActivity)
    }

    fun startConversation(text: String, capabilityGroupSelected: String, capabilitySelected: String) {
        if (isSingleShotResponse) singleShotResponse(text,capabilityGroupSelected,capabilitySelected)
        else streamResponse(text,capabilityGroupSelected,capabilitySelected)
    }

    private fun singleShotResponse(
        text: String,
        capabilityGroupSelected: String,
        capabilitySelected: String
    ) {
        convaImpl?.singleShotResponse(
            text,
            capabilityGroupSelected,
            capabilitySelected,
            object : ConvaAIListener {
                override fun onResponse(message: String, jsonString: String) {
                    val repository: Repository = (application as App).repository
                    val response = ChatResponse(message,jsonString)
                    repository.sendResponse(response)
                }
            })
    }

    private fun streamResponse(
        text: String,
        capabilityGroupSelected: String,
        capabilitySelected: String
    ) {
        val repository: Repository = (application as App).repository

        convaImpl?.streamResponse(
            text,
            capabilityGroupSelected,
            capabilitySelected,
            object : ConvaAIListener {
                override fun onResponseStream(
                    message: String,
                    jsonString: String,
                    isFinal: Boolean
                ) {
                    val response = ChatResponse(message,jsonString, isFinal)
                    repository.sendResponseStream(response)
                }
            })
    }

    fun showUI(activity: Activity) {
        convaImpl?.showUI(activity)
    }

    fun skipTTS() {
        convaTTS?.skip()
    }

    fun shutdownTTS() {
        convaTTS?.shutdown()
    }

    fun startListening(activity: Activity) {
        convaASR?.startListening(activity)
    }

    fun stopListening() {
        convaASR?.stopListening()
    }

    fun shutdown() {
        convaTTS?.shutdown()
        convaASR?.shutdown()
        convaImpl?.shutdown()
    }
}