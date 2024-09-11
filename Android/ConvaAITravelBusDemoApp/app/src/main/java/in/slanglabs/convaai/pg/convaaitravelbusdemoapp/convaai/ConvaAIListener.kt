package `in`.slanglabs.convaai.pg.convaaitravelbusdemoapp.convaai

interface ConvaAIListener {
    fun onInitSuccessful()
    fun onInitFailure(errorMessage: String)
}