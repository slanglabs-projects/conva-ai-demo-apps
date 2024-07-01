package `in`.slanglabs.convaai.demo.grocery.convaai.implementation

interface ConvaAICopilotLifecycleObserver {
    fun onInitSuccessful()
    fun onInitFailure(errorMessage: String)
}