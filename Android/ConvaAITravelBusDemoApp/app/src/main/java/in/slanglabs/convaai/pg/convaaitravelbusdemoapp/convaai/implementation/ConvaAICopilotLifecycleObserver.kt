package `in`.slanglabs.convaai.pg.convaaitravelbusdemoapp.convaai.implementation

interface ConvaAICopilotLifecycleObserver {
    fun onInitSuccessful()
    fun onInitFailure(errorMessage: String)
}