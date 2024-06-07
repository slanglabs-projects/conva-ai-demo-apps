package `in`.slanglabs.androidpg

enum class ConvaAISDKType(val typeValue: String) {
    ASSISTANT_SDK("ASSISTANT MODE"),
    CORE_SDK("HEADLESS MODE");

    fun getValue(): String {
        return typeValue
    }
}