package `in`.slanglabs.convaai.demo.travel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import `in`.slanglabs.convaai.demo.travel.convaai.CopilotManager
import `in`.slanglabs.convaai.demo.travel.data.repository.SearchRepository
import `in`.slanglabs.convaai.demo.travel.ui.MainScreen
import `in`.slanglabs.convaai.demo.travel.ui.theme.ConvaAITravelDemoTheme
import `in`.slanglabs.convaai.demo.travel.viewmodel.CopilotViewModel

class MainActivity : ComponentActivity() {
    private lateinit var copilotManager: CopilotManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        copilotManager = CopilotManager()
        setContent {
            ConvaAITravelDemoTheme {
                val navController = rememberNavController()
                val searchRepository = remember { SearchRepository() }
                val copilotManager = remember { CopilotManager() }
                val copilotViewModel = remember {
                    CopilotViewModel(copilotManager, navController, searchRepository)
                }
                LaunchedEffect(Unit) {
                    copilotManager.initialize(this@MainActivity)
                }
                MainScreen(copilotViewModel, searchRepository)
            }
        }
    }
}