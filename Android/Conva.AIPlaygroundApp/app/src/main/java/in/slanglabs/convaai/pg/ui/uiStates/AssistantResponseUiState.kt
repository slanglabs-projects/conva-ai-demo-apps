package `in`.slanglabs.convaai.pg.ui.uiStates

data class AssistantResponseUiState (
    val title: String = "",
    val body: String = "",
    val clipboardIcon: Int = 0,
    val invokeButtonTitle: String = "",
    val isAssistantInitialized: Boolean = false
)