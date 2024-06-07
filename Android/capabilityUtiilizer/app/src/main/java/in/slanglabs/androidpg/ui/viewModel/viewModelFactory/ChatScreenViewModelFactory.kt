package `in`.slanglabs.androidpg.ui.viewModel.viewModelFactory

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import `in`.slanglabs.androidpg.ui.viewModel.viewModels.ChatScreenViewModel
import java.lang.ref.WeakReference

class ChatScreenViewModelFactory(
    private val application: Application,
    private val activityRef: WeakReference<Activity>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatScreenViewModel(application, activityRef) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}