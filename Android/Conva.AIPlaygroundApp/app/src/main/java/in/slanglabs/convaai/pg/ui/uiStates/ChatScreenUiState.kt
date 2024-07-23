package `in`.slanglabs.convaai.pg.ui.uiStates

import `in`.slanglabs.convaai.pg.model.ChatHistory

data class ChatScreenUiState (
    val chatWindowVisibility: Boolean = false,
    val chatHistory: List<ChatHistory> = emptyList(),
    val placeholder: String = "",
    val message: String = "",
    val isListening: Boolean = false,
    val appLogo: Int = 0,
    val appLogoTitle: String = "",
    var capabilityGroup: String = "",
    var capability: String = "",
    val capabilityGroupList: List<String> = emptyList(),
    val capabilityList: List<String> = emptyList(),
)