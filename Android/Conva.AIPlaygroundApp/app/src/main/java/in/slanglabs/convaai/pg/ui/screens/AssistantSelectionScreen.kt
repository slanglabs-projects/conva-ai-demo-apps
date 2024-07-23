package `in`.slanglabs.convaai.pg.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import `in`.slanglabs.convaai.pg.ConvaAISDKType
import `in`.slanglabs.convaai.pg.model.AppData
import `in`.slanglabs.convaai.pg.ui.blocks.CardButtonBlock
import `in`.slanglabs.convaai.pg.ui.viewModel.viewModels.SelectionViewModel

@Composable
fun AssistantSelectionScreen(
    viewModel: SelectionViewModel,
    backgroundColor: Color = Color(0xFF1B1B1B),
    primarySelectionColor: Color = Color(0xFF0046C4),
    secondarySelectionColor: Color = Color(0xFFF39200),
    navController: NavController,
    appData: AppData
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CardButtonBlock(
                    ConvaAISDKType.COPILOT_SDK.typeValue,
                    onClick = {
                        val destination = viewModel.onAssistantSelected(
                            assistantType = it,
                            appData = appData,
                            showBottomBar = false,
                            showInputBox = false
                        )
                        navController.navigate(destination)
                    },
                    primarySelectionColor
                )
                CardButtonBlock(
                    ConvaAISDKType.FOUNDATION_SDK.typeValue,
                    onClick = {
                        val destination = viewModel.onAssistantSelected(
                            assistantType = it,
                            appData = appData,
                            showBottomBar = true,
                            showInputBox = true
                        )
                        navController.navigate(destination)
                    },
                    secondarySelectionColor
                )
            }
        }
    }
}

@Preview
@Composable
fun SelectionScreenPreview() {
}