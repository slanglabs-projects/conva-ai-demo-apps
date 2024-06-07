package `in`.slanglabs.androidpg.convaai.impl.assistant

import android.app.Activity

interface ConvaAIFacade {
    fun initialiseConvaAI(assistantID: String, assistantKey: String, assistantVersion: String, startActivity: Activity? = null) {
        // pass
    }
    fun initialiseConvaAI(assistantID: String, assistantKey: String, assistantVersion: String) {
        initialiseConvaAI(assistantID, assistantKey, assistantVersion,null)
    }
    fun singleShotResponse(responseCallBack: ConvaAIListener) {
        // pass
    }
    fun singleShotResponse(text: String, capabilityGroupSelected: String, capabilitySelected: String, responseCallBack: ConvaAIListener) {
        singleShotResponse(responseCallBack)
    }
    fun streamResponse(text: String, capabilityGroupSelected: String, capabilitySelected: String, responseCallBack: ConvaAIListener) {
        singleShotResponse(text, capabilityGroupSelected, capabilitySelected, responseCallBack)
    }

    fun showUI(activity: Activity) {

    }

    fun shutdown()
}