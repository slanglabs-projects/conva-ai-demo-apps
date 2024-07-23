package `in`.slanglabs.convaai.pg.model

data class ChatHistory(
    val message: String,
    val jsonString: String,
    val params: String
)