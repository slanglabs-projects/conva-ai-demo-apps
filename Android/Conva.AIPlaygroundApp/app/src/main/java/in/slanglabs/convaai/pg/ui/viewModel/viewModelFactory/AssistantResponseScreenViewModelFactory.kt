package `in`.slanglabs.convaai.pg.ui.viewModel.viewModelFactory

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import `in`.slanglabs.convaai.pg.ui.viewModel.viewModels.AssistantResponseScreenViewModel
import java.lang.ref.WeakReference

class AssistantResponseScreenViewModelFactory(
    private val application: Application,
    private val activityRef: WeakReference<Activity>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AssistantResponseScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AssistantResponseScreenViewModel(application, activityRef) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}