package `in`.slanglabs.androidpg.convaai.impl.tts

interface ConvaAITTSListener {
    fun onStart(string: String)
    fun onDone(string: String)
    fun onError(string: String)
    fun onRangeStart(utterance: String, start: Int, end: Int, frame: Int)
}