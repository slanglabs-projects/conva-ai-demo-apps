package `in`.slanglabs.convaai.pg.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import `in`.slanglabs.convaai.pg.model.AppData
import `in`.slanglabs.convaai.pg.model.LegacyAppData
import `in`.slanglabs.convaai.pg.model.RichAppData
import `in`.slanglabs.convaai.pg.ui.screens.AssistantResponseScreen
import `in`.slanglabs.convaai.pg.ui.screens.AssistantSelectionScreen
import `in`.slanglabs.convaai.pg.ui.screens.CameraScreen
import `in`.slanglabs.convaai.pg.ui.screens.ChatScreen
import `in`.slanglabs.convaai.pg.ui.screens.HomeSelectionScreen
import `in`.slanglabs.convaai.pg.ui.viewModel.viewModels.AssistantResponseScreenViewModel
import `in`.slanglabs.convaai.pg.ui.viewModel.viewModels.ChatScreenViewModel
import `in`.slanglabs.convaai.pg.ui.viewModel.viewModels.SelectionViewModel
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

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
            arguments = listOf(navArgument("appData") { type = NavType.StringType })
        ) {backStackEntry ->
            val json = backStackEntry.arguments?.getString("appData") ?: ""
            val decodedJson = Uri.decode(json)
            val jsonElement = Json.parseToJsonElement(decodedJson)
            val appData: AppData = if (jsonElement is JsonObject && jsonElement.containsKey("capabilities")) {
                Json.decodeFromJsonElement(RichAppData.serializer(), jsonElement)
            } else {
                Json.decodeFromJsonElement(LegacyAppData.serializer(), jsonElement)
            }
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
                type = NavType.StringType
            })
        ) {backStackEntry->
            val json = backStackEntry.arguments?.getString("appData") ?: ""
            val decodedJson = Uri.decode(json)
            val jsonElement = Json.parseToJsonElement(decodedJson)
            val appData: AppData = if (jsonElement is JsonObject && jsonElement.containsKey("capabilities")) {
                Json.decodeFromJsonElement(RichAppData.serializer(), jsonElement)
            } else {
                Json.decodeFromJsonElement(LegacyAppData.serializer(), jsonElement)
            }
            appData?.let {
                AssistantResponseScreen(
                    viewModel = assistantResponseScreenViewModel,
                    appData = it
                )
            }
        }

        composable(
            route = Screen.ChatScreen.route,
            arguments = listOf(
                navArgument("appData") { type = NavType.StringType },
                navArgument("assistantType") { type = NavType.StringType },
                navArgument("showInputBox") { type = NavType.BoolType },
                navArgument("showBottomBar") { type = NavType.BoolType },
            )
        ) { backStackEntry->
            val json = backStackEntry.arguments?.getString("appData") ?: ""
            val decodedJson = Uri.decode(json)
            val jsonElement = Json.parseToJsonElement(decodedJson)
            val appData: AppData = if (jsonElement is JsonObject && jsonElement.containsKey("capabilities")) {
                Json.decodeFromJsonElement(RichAppData.serializer(), jsonElement)
            } else {
                Json.decodeFromJsonElement(LegacyAppData.serializer(), jsonElement)
            }
            val assistantType = backStackEntry.arguments?.getString("assistantType") ?: ""
            val showInputBox = backStackEntry.arguments?.getBoolean("showInputBox") ?: false
            val showBottomBar = backStackEntry.arguments?.getBoolean("showBottomBar") ?: false
            appData?.let {
                ChatScreen(
                    viewModel = chatScreenViewModel,
                    appData = it,
                    assistantType = assistantType,
                    showInputBox = showInputBox,
                    showBottomBar = showBottomBar
                )
            }
        }
    }
}