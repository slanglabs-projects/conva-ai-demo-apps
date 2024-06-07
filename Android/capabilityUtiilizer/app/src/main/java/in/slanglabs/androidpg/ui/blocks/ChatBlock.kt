package `in`.slanglabs.androidpg.ui.blocks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChatBlock(
    modifier: Modifier = Modifier,
    logoImage: Int = 0,
    logoTitle: String = "title",
    isChatHistoryVisible: Boolean = false,
    chatHistory: List<Pair<String, String>>,
    onMessageClicked: () -> Unit) {
    Column(
    modifier = modifier
        .fillMaxSize()
        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        if (isChatHistoryVisible) {
            ChatSurfaceBlock(chatHistoryList = chatHistory, onMessageClicked = {
                onMessageClicked()
            })
        } else {
            LogoSurfaceBlock(logoImage,logoTitle)
        }

    }
}