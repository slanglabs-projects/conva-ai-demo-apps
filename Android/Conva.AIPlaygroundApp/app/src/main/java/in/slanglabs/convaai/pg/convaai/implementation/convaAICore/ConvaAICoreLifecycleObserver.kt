package `in`.slanglabs.convaai.pg.convaai.implementation.convaAICore

interface ConvaAICoreLifecycleObserver {
    fun onInitSuccessful()
    fun onInitFailure(errorMessage: String)
}