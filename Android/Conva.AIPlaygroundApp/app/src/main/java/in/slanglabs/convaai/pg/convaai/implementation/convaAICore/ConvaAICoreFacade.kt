package `in`.slanglabs.convaai.pg.convaai.implementation.convaAICore

interface ConvaAICoreFacade {
    fun initialiseCoreAI(assistantID: String, assistantKey: String, assistantVersion: String)
    fun singleShotResponseWithName(text: String, capabilitySelected: String, responseCallBack: ConvaAICoreResponseListener)
    fun singleShotResponse(text: String, capabilityGroupSelected: String?, responseCallBack: ConvaAICoreResponseListener)
    fun streamResponse(text: String, capabilityGroupSelected: String?, responseCallBack: ConvaAICoreResponseListener)
    fun streamResponseWithName(text: String, capabilitySelected: String, responseCallBack: ConvaAICoreResponseListener)
    fun shutdown()
}