package `in`.slanglabs.convaai.pg.convaai.implementation.convaAICopilot

interface ConvaAICopilotLifecycleObserver {
    fun onInitSuccessful()
    fun onInitFailure(errorMessage: String)
}