package `in`.slanglabs.convaai.pg.model

data class ChatResponse(val message: String = "", val params: Map<String, Any> = emptyMap(), val jsonString : String = "", val isFinal : Boolean = true)