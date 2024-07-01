package `in`.slanglabs.convaai.pg.model

data class ChatResponse(val message: String = "", val jsonString : String, val isFinal : Boolean = true)