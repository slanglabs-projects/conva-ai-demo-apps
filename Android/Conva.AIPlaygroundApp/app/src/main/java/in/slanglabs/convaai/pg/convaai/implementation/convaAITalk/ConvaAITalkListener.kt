package `in`.slanglabs.convaai.pg.convaai.implementation.convaAITalk

interface ConvaAITalkListener {
    fun onStart(string: String)
    fun onDone(string: String)
    fun onError(string: String)
    fun onRangeStart(utterance: String, start: Int, end: Int, frame: Int)
}