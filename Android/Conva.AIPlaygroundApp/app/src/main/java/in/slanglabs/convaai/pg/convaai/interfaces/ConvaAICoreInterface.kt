package `in`.slanglabs.convaai.pg.convaai.interfaces

import android.app.Application
import `in`.slanglabs.convaai.pg.App
import `in`.slanglabs.convaai.pg.Repository
import `in`.slanglabs.convaai.pg.convaai.implementation.convaAICore.ConvaAICoreFacade
import `in`.slanglabs.convaai.pg.convaai.implementation.convaAICore.ConvaAICoreImpl
import `in`.slanglabs.convaai.pg.convaai.implementation.convaAICore.ConvaAICoreResponseListener
import `in`.slanglabs.convaai.pg.model.ChatResponse

class ConvaAICoreInterface(private val application: Application) {
    private val isSingleShotResponse: Boolean = true
    private var convaAICore: ConvaAICoreFacade? = null


    fun initialiseCoreService(
        assistantID: String,
        assistantKey: String,
        assistantVersion: String
    ) {
        convaAICore = ConvaAICoreImpl(application)
        convaAICore?.initialiseCoreAI(assistantID,assistantKey,assistantVersion)
        val repository: Repository = (application as App).repository
        repository.setConvaAIInitialized(true)
    }

    fun invokeCoreService(text: String, capabilityGroupSelected: String, capabilitySelected: String) {
        if (isSingleShotResponse) singleShotResponse(text,capabilityGroupSelected,capabilitySelected)
        else streamResponse(text,capabilityGroupSelected,capabilitySelected)
    }

    private fun singleShotResponse(
        text: String,
        capabilityGroupSelected: String,
        capabilitySelected: String
    ) {
        convaAICore?.singleShotResponse(
            text,
            capabilityGroupSelected,
            capabilitySelected,
            object : ConvaAICoreResponseListener {
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

        convaAICore?.streamResponse(
            text,
            capabilityGroupSelected,
            capabilitySelected,
            object : ConvaAICoreResponseListener {
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

    fun shutdown() {
        convaAICore?.shutdown()
    }
}