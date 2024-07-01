package `in`.slanglabs.convaai.pg.convaai.implementation.convaAICopilot

import android.app.Activity

interface ConvaAICopilotFacade {
    fun initialiseCopilot(assistantID: String, assistantKey: String, assistantVersion: String, startActivity: Activity? = null)
    fun startConversation(activity: Activity)
    fun showUI(activity: Activity)
    fun shutdown()
}