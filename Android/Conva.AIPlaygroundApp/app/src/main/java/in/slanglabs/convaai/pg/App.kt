package `in`.slanglabs.convaai.pg

import android.app.Application
import `in`.slanglabs.convaai.pg.convaai.interfaces.ConvaAICopilotInterface
import `in`.slanglabs.convaai.pg.convaai.interfaces.ConvaAICoreInterface
import `in`.slanglabs.convaai.pg.convaai.interfaces.ConvaAISpeechInterface
import `in`.slanglabs.convaai.pg.convaai.interfaces.ConvaAITalkInterface

class App : Application() {

    lateinit var repository: Repository

    override fun onCreate() {
        super.onCreate()
        val convaAICopilotInterface = ConvaAICopilotInterface(this)
        val convaAICoreInterface = ConvaAICoreInterface(this)
        val convaAISpeechInterface = ConvaAISpeechInterface(this)
        val convaAITalkInterface = ConvaAITalkInterface(this)
        repository = Repository(
            convaAICopilotInterface = convaAICopilotInterface,
            convaAICoreInterface = convaAICoreInterface,
            convaAISpeechInterface = convaAISpeechInterface,
            convaAITalkInterface = convaAITalkInterface
        )
    }
}