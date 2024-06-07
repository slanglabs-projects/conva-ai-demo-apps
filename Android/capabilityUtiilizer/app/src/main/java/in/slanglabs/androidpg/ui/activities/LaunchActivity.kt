package `in`.slanglabs.androidpg.ui.activities

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import `in`.slanglabs.androidpg.App
import `in`.slanglabs.androidpg.Repository
import `in`.slanglabs.androidpg.navigation.SetupNavigation
import `in`.slanglabs.androidpg.ui.blocks.TitleBarBlock
import `in`.slanglabs.androidpg.ui.viewModel.viewModels.AssistantResponseScreenViewModel
import `in`.slanglabs.androidpg.ui.viewModel.viewModels.ChatScreenViewModel
import `in`.slanglabs.androidpg.ui.viewModel.viewModels.LaunchActivityViewModel
import `in`.slanglabs.androidpg.ui.viewModel.viewModels.SelectionViewModel
import `in`.slanglabs.androidpg.ui.viewModel.viewModelFactory.AssistantResponseScreenViewModelFactory
import `in`.slanglabs.androidpg.ui.viewModel.viewModelFactory.ChatScreenViewModelFactory
import `in`.slanglabs.androidpg.ui.viewModel.viewModelFactory.LaunchActivityViewModelFactory
import `in`.slanglabs.androidpg.ui.viewModel.viewModelFactory.SelectionViewModelFactory
import java.lang.ref.WeakReference

class LaunchActivity : ComponentActivity() {
    private val viewModel: LaunchActivityViewModel by viewModels { LaunchActivityViewModelFactory(application) }
    private val selectionScreenViewModel: SelectionViewModel by viewModels { SelectionViewModelFactory() }
    private val assistantScreenViewModel: AssistantResponseScreenViewModel by viewModels { AssistantResponseScreenViewModelFactory(application, WeakReference(this)) }
    private val chatScreenViewModel: ChatScreenViewModel by viewModels { ChatScreenViewModelFactory(application, WeakReference(this)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(color = Color(0xFF1B1B1B)) {
                Column (modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                ) {
                    TitleBarBlock(
                        titleText = viewModel.title,
                        shouldStartProgressIndicator = viewModel.isListening.collectAsState()
                    )
                    SetupNavigation(
                        selectionViewModel = selectionScreenViewModel,
                        assistantResponseScreenViewModel = assistantScreenViewModel,
                        chatScreenViewModel = chatScreenViewModel
                    )
                }
                BackPressSample(viewModel.backPressToast,viewModel.backPressTimer)
                BackPressSample()
            }
        }
    }
}

@Composable
private fun BackPressSample(toastMessage: String = "Press again to exit", backPressTimer: Long = 2000L) {
    val context = LocalContext.current
    val activity = if (context is Activity) {
        context
    } else {
        null
    }
    var lastBackPressTime by remember { mutableStateOf(0L) }

    BackHandler {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastBackPressTime < backPressTimer) {
            val mRepository: Repository = (context.applicationContext as App).repository
            mRepository.shutdown()
            activity?.finishAndRemoveTask()
        } else {
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
            lastBackPressTime = currentTime
        }
    }
}