package `in`.slanglabs.androidpg.navigation

sealed class Screen(val route: String) {
    object HomeSelectionScreen: Screen("home_selection_screen")
    object AssistantSelectionScreen: Screen("assistant_selection_screen")
    object CameraScreen: Screen("assistant_selection_screen")
    object ChatScreen: Screen("chat_screen")
    object AssistantResponseScreen : Screen("assistant_response_screen")
}
