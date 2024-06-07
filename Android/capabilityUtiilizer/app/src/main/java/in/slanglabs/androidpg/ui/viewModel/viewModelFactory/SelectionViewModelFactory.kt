package `in`.slanglabs.androidpg.ui.viewModel.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import `in`.slanglabs.androidpg.ui.viewModel.viewModels.SelectionViewModel

class SelectionViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SelectionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SelectionViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}