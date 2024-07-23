package `in`.slanglabs.convaai.pg.ui.blocks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import `in`.slanglabs.convaai.pg.model.ChatHistory
import `in`.slanglabs.convaai.pg.ui.viewModel.viewModels.ChatScreenViewModel

@Composable
fun ChatSurfaceBlock(
    chatHistoryList: List<ChatHistory>,
    onMessageClicked: () -> Unit
) {
    val scrollState = rememberScrollState()
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)) {
        chatHistoryList.forEach { (firstString, secondString, params) ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(),
                modifier = Modifier.animateContentSize()
            ) {
                MessageBlock(
                    defaultMessage = firstString,
                    secondString = secondString,
                    params = params,
                    messageType =
                    if (firstString.trim().startsWith("${ChatScreenViewModel.USER}:")) MessageType.PRIMARY
                    else MessageType.SECONDARY,
                    onSecondStringShown = {
                        onMessageClicked()
                    }
                )
            }
        }

        LaunchedEffect(key1 = chatHistoryList) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }
}