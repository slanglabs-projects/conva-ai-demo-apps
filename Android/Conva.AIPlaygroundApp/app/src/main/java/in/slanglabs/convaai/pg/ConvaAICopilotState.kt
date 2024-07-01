package `in`.slanglabs.convaai.pg

enum class ConvaAICopilotState {
    IDLE,
    INITIALIZING,
    READY,
    PROCESSING,
    FAILED;

    var message: String = "No message available"

    fun setFailureMessage(message: String) {
        if (this == FAILED) {
            this.message = message
        }
    }
}