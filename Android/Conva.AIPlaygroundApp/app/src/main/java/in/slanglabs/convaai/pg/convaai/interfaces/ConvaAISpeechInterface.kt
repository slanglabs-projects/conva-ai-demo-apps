package `in`.slanglabs.convaai.pg.convaai.interfaces

import android.app.Activity
import android.app.Application
import `in`.slanglabs.convaai.pg.App
import `in`.slanglabs.convaai.pg.Repository
import `in`.slanglabs.convaai.pg.convaai.implementation.convaAISpeech.ConvaAISpeechFacade
import `in`.slanglabs.convaai.pg.convaai.implementation.convaAISpeech.ConvaAISpeechImpl
import `in`.slanglabs.convaai.pg.convaai.implementation.convaAISpeech.ConvaAISpeechListener
import `in`.slanglabs.convaai.pg.model.ASRData

class ConvaAISpeechInterface(private val application: Application) {
    private var convaAISpeech: ConvaAISpeechFacade? = null

    fun initASRService(
        assistantID: String,
        assistantKey: String,
        assistantVersion: String,
        startActivity: Activity
    ) {
        val repository: Repository = (application as App).repository
        convaAISpeech = ConvaAISpeechImpl()
        convaAISpeech?.initialiseSpeech(assistantID,assistantKey,assistantVersion,startActivity,listener = object :
            ConvaAISpeechListener {
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

    fun startListening(activity: Activity) {
        convaAISpeech?.startListening(activity)
    }

    fun stopListening() {
        convaAISpeech?.stopListening()
    }

    fun shutdown() {
        convaAISpeech?.shutdown()
    }
}