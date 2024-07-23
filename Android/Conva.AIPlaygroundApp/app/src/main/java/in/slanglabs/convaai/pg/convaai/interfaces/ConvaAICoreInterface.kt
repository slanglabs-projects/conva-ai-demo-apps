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
        if (capabilitySelected.isNotEmpty()) {
            singleShotResponseWithName(text,capabilitySelected)
            return
        }
        convaAICore?.singleShotResponse(
            text,
            capabilityGroupSelected,
            object : ConvaAICoreResponseListener {
                override fun onResponse(message: String, params: Map<String, Any>, jsonString: String) {
                    val repository: Repository = (application as App).repository
                    val response = ChatResponse(
                        message = message,
                        params = params,
                        jsonString = jsonString
                    )
                    repository.sendResponse(response)
                }
            })
    }

    private fun singleShotResponseWithName(text: String, capabilitySelected: String) {
        convaAICore?.singleShotResponseWithName(
            text,
            capabilitySelected,
            object : ConvaAICoreResponseListener {
                override fun onResponse(message: String, params: Map<String, Any>, jsonString: String) {
                    val repository: Repository = (application as App).repository
                    val response = ChatResponse(
                        message = message,
                        params = params,
                        jsonString = jsonString
                    )
                    repository.sendResponse(response)
                }
            })
    }

    private fun streamResponse(
        text: String,
        capabilityGroupSelected: String,
        capabilitySelected: String
    ) {
        if (capabilitySelected.isNotEmpty()) {
            streamResponseWithName(text,capabilitySelected)
            return
        }
        val repository: Repository = (application as App).repository

        convaAICore?.streamResponseWithName(
            text,
            capabilityGroupSelected,
            object : ConvaAICoreResponseListener {
                override fun onResponseStream(
                    message: String,
                    params: Map<String, Any>,
                    jsonString: String,
                    isFinal: Boolean
                ) {
                    val response = ChatResponse(
                        message = message,
                        params = params,
                        jsonString = jsonString,
                        isFinal = isFinal
                    )
                    repository.sendResponseStream(response)
                }
            })
    }

    private fun streamResponseWithName(text: String, capabilitySelected: String) {
        val repository: Repository = (application as App).repository

        convaAICore?.streamResponseWithName(
            text,
            capabilitySelected,
            object : ConvaAICoreResponseListener {
                override fun onResponseStream(
                    message: String,
                    params: Map<String, Any>,
                    jsonString: String,
                    isFinal: Boolean
                ) {
                    val response = ChatResponse(
                        message = message,
                        params = params,
                        jsonString = jsonString,
                        isFinal = isFinal
                    )
                    repository.sendResponseStream(response)
                }
            })
    }

    fun shutdown() {
        convaAICore?.shutdown()
    }
}