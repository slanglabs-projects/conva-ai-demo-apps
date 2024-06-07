package `in`.slanglabs.androidpg.ui.viewModel.viewModelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import `in`.slanglabs.androidpg.ui.viewModel.viewModels.LaunchActivityViewModel

class LaunchActivityViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LaunchActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LaunchActivityViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}