package `in`.slanglabs.convaai.pg.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import `in`.slanglabs.convaai.pg.model.AppData
import `in`.slanglabs.convaai.pg.model.AppDataType
import `in`.slanglabs.convaai.pg.ui.screens.AssistantResponseScreen
import `in`.slanglabs.convaai.pg.ui.screens.AssistantSelectionScreen
import `in`.slanglabs.convaai.pg.ui.screens.CameraScreen
import `in`.slanglabs.convaai.pg.ui.screens.ChatScreen
import `in`.slanglabs.convaai.pg.ui.screens.HomeSelectionScreen
import `in`.slanglabs.convaai.pg.ui.viewModel.viewModels.AssistantResponseScreenViewModel
import `in`.slanglabs.convaai.pg.ui.viewModel.viewModels.ChatScreenViewModel
import `in`.slanglabs.convaai.pg.ui.viewModel.viewModels.SelectionViewModel

@Composable
fun SetupNavigation(
    selectionViewModel: SelectionViewModel,
    assistantResponseScreenViewModel: AssistantResponseScreenViewModel,
    chatScreenViewModel : ChatScreenViewModel
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.HomeSelectionScreen.route) {
        composable(route = Screen.HomeSelectionScreen.route) {
            HomeSelectionScreen(
                viewModel = selectionViewModel,
                navController = navController
            )
        }

        composable(route = Screen.CameraScreen.route) {
            CameraScreen(
                viewModel = selectionViewModel,
                navController = navController
            )
        }

        composable(
            route = Screen.AssistantSelectionScreen.route + "/{appData}",
            arguments = listOf(navArgument("appData") { type = AppDataType() })
        ) {backStackEntry ->
            val appData = backStackEntry.arguments?.getParcelable<AppData>("appData")
            appData?.let {
                AssistantSelectionScreen(
                    viewModel = selectionViewModel,
                    navController = navController,
                    appData = it
                )
            }
        }

        composable(
            route = Screen.AssistantResponseScreen.route + "/{appData}",
            arguments = listOf(navArgument("appData") {
                type = AppDataType()
            })
        ) {backStackEntry->
            val appData = backStackEntry.arguments?.getParcelable<AppData>("appData")
            appData?.let {
                AssistantResponseScreen(
                    viewModel = assistantResponseScreenViewModel,
                    appData = it
                )
            }
        }

        composable(
            route = Screen.ChatScreen.route + "/{appData}",
            arguments = listOf(navArgument("appData") {
                type = AppDataType()
            })
        ) {backStackEntry->
            val appData = backStackEntry.arguments?.getParcelable<AppData>("appData")
            appData?.let {
                ChatScreen(
                    viewModel = chatScreenViewModel,
                    appData = it
                )
            }
        }
    }
}