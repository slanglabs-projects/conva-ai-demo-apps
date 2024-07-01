package `in`.slanglabs.convaai.pg

enum class ConvaAISDKType(val typeValue: String) {
    COPILOT_SDK("COPILOT MODE"),
    FOUNDATION_SDK("HEADLESS MODE");

    fun getValue(): String {
        return typeValue
    }
}