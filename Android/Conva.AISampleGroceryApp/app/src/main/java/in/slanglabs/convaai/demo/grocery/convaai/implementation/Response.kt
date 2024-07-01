package `in`.slanglabs.convaai.demo.grocery.convaai.implementation

data class Response(val searchTerm: String, val navigationTarget: String, val message: String, val jsonString : String, val isFinal : Boolean = true)
