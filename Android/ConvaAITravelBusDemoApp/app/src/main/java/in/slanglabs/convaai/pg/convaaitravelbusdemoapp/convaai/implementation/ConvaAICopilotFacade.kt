package `in`.slanglabs.convaai.pg.convaaitravelbusdemoapp.convaai.implementation

import android.app.Activity

interface ConvaAICopilotFacade {
    fun initialiseCopilot(assistantID: String, assistantKey: String, assistantVersion: String, startActivity: Activity? = null)
    
    fun startConversation()

    fun showUI(activity: Activity)

    fun showTrigger(activity: Activity)

    fun shutdown()

    fun isConvaAICopilotInitialised():Boolean
}