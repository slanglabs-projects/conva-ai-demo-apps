package `in`.slanglabs.androidpg

import android.app.Application
import android.content.Context
import android.util.Log
import `in`.slanglabs.androidpg.slang.SlangInterface

class App : Application() {

    private lateinit var mRepository: Repository

    override fun onCreate() {
        super.onCreate()
        val mSlangInterface = SlangInterface(this)
        val mPrefs = getSharedPreferences("slang_android_pg", Context.MODE_PRIVATE)
        mRepository = Repository(
            mSlangInterface, mPrefs
        )
    }

    fun getRepository(): Repository {
        return mRepository
    }
}