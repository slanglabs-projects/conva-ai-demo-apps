package `in`.slanglabs.convaai.pg.convaaitravelbusdemoapp.convaai


import android.app.Activity
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import `in`.slanglabs.convaai.pg.convaaitravelbusdemoapp.App
import `in`.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.Place
import `in`.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.SearchItem
import `in`.slanglabs.convaai.pg.convaaitravelbusdemoapp.BuildConfig
import `in`.slanglabs.convaai.pg.convaaitravelbusdemoapp.Repository
import `in`.slanglabs.convaai.pg.convaaitravelbusdemoapp.convaai.implementation.ConvaAICopilotFacade
import `in`.slanglabs.convaai.pg.convaaitravelbusdemoapp.convaai.implementation.ConvaAICopilotLifecycleObserver
import `in`.slanglabs.convaai.pg.convaaitravelbusdemoapp.convaai.implementation.ConvaAICopilotListener
import `in`.slanglabs.convaai.pg.convaaitravelbusdemoapp.convaai.implementation.ConavAICopilotImpl
import `in`.slanglabs.convaai.pg.convaaitravelbusdemoapp.convaai.implementation.Response
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ConvaAIInterface(private val application: Application) {
    private var convaAIImpl: ConvaAICopilotFacade? = null
    private var isSlangInitialized: Boolean = false
    private var activityReference: WeakReference<Activity>? = null
    var enableTrigger: Boolean = false

    init {
        initialiseCopilotSDK(BuildConfig.ASSISTANT_ID, BuildConfig.API_KEY,BuildConfig.ASSISTANT_VERSION)
    }

    private fun initialiseCopilotSDK(
        assistantID: String,
        assistantKey: String,
        assistantVersion: String,
        startActivity: Activity? = null,
        lifecycleObserver: ConvaAIListener? = null
    ) {
        convaAIImpl = ConavAICopilotImpl(application,  object : ConvaAICopilotListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(response: Response) {
                val repository: Repository = (application as App).repository
                if (response.destination.isNotEmpty()) {
                    val searchItem = SearchItem()
                    searchItem.destinationPlace = Place()
                    searchItem.sourcePlace = Place()

                    searchItem.destinationPlace.city = response.destination
                    searchItem.sourcePlace.city = response.source
                    val date = convertStringToDate(response.date)

                    searchItem.travelDate = date
                    repository.onSearch(searchItem)
                }
            }
        }, object : ConvaAICopilotLifecycleObserver {
            override fun onInitSuccessful() {
                isSlangInitialized = true
                lifecycleObserver?.onInitSuccessful()
                if (enableTrigger) {
                    activityReference?.get()?.let { activity ->
                        showTrigger(activity)
                    }
                }
            }

            override fun onInitFailure(errorMessage: String) {
                isSlangInitialized = false
                lifecycleObserver?.onInitFailure(errorMessage)
            }
        })
        convaAIImpl?.initialiseCopilot(assistantID,assistantKey,assistantVersion,startActivity)
    }

    fun startConversation(activity: Activity) {
        showUI(activity)
        convaAIImpl?.startConversation()
    }

    fun showUI(activity: Activity) {
        convaAIImpl?.showUI(activity)
    }

    fun showTrigger(activity: Activity) {
        enableTrigger = true
        activityReference = WeakReference(activity)
        convaAIImpl?.showTrigger(activity)
    }

    fun shutdown() {
        convaAIImpl?.shutdown()
    }

    fun isConvaAIInitialised():Boolean {
        return convaAIImpl?.isConvaAICopilotInitialised() ?: false
    }

    fun convertStringToDate(dateString: String): Date? {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return try {
            dateFormat.parse(dateString)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}