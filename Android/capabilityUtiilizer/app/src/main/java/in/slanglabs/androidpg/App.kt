package `in`.slanglabs.androidpg

import android.app.Application
import `in`.slanglabs.androidpg.convaai.ConvaAIInterface

class App : Application() {

    lateinit var repository: Repository

    override fun onCreate() {
        super.onCreate()
        val convaAIInterface = ConvaAIInterface(this)
        repository = Repository(convaAIInterface)
    }
}