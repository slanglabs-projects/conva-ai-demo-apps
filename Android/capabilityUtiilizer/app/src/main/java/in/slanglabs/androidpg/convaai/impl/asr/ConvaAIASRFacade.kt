package `in`.slanglabs.androidpg.convaai.impl.asr

import android.app.Activity

interface ConvaAIASRFacade {
    fun initialiseConvaAIASR(assistantID: String, assistantKey: String, assistantVersion: String, startActivity: Activity, listener: ConvaAIASRListener)
    fun startListening(activity: Activity)
    fun stopListening()
    fun shutdown()
}