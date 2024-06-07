package `in`.slanglabs.androidpg.ui.blocks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import `in`.slanglabs.androidpg.ui.viewModel.viewModels.ChatScreenViewModel

@Composable
fun ChatSurfaceBlock(chatHistoryList: List<Pair<String, String>>, onMessageClicked: () -> Unit) {
    val scrollState = rememberScrollState()
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)) {
        chatHistoryList.forEach { (firstString, secondString) ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(),
                modifier = Modifier.animateContentSize()
            ) {
                MessageBlock(
                    firstString,
                    secondString,
                    messageType =
                    if (firstString.trim().startsWith("${ChatScreenViewModel.USER}:")) MessageType.PRIMARY
                    else MessageType.SECONDARY,
                    onSecondStringShown = {
                        onMessageClicked()
                    }
                )
            }
        }
    }
}