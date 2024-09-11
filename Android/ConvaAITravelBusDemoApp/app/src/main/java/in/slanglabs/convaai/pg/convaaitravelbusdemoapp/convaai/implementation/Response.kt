package `in`.slanglabs.convaai.pg.convaaitravelbusdemoapp.convaai.implementation

data class Response(val source: String, val destination: String, val date: String, val convertedDate: String,val message: String, val jsonString : String, val isFinal : Boolean = true)
