package `in`.slanglabs.convaai.pg.convaai.implementation.convaAISpeech

import android.app.Activity

interface ConvaAISpeechFacade {
    fun initialiseSpeech(assistantID: String, assistantKey: String, assistantVersion: String, startActivity: Activity, listener: ConvaAISpeechListener)
    fun startListening(activity: Activity)
    fun stopListening()
    fun shutdown()
}