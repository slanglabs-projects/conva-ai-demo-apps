package `in`.slanglabs.convaai.demo.grocery.convaai

interface ConvaAIListener {
    fun onInitSuccessful()
    fun onInitFailure(errorMessage: String)
}