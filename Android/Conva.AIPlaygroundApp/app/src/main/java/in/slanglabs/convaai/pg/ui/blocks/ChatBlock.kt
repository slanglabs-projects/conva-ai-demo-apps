package `in`.slanglabs.convaai.pg.ui.blocks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import `in`.slanglabs.convaai.pg.model.ChatHistory

@Composable
fun ChatBlock(
    modifier: Modifier = Modifier,
    logoImage: Int = 0,
    logoTitle: String = "title",
    isChatHistoryVisible: Boolean = false,
    chatHistory: List<ChatHistory>,
    onMessageClicked: () -> Unit) {
    Column(
    modifier = modifier
        .fillMaxSize()
        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        if (isChatHistoryVisible) {
            ChatSurfaceBlock(
                chatHistoryList = chatHistory,
                onMessageClicked = {
                onMessageClicked()
            })
        } else {
            LogoSurfaceBlock(logoImage,logoTitle)
        }

    }
}