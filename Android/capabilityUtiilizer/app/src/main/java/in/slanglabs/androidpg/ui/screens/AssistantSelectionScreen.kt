package `in`.slanglabs.androidpg.ui.screens

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
import `in`.slanglabs.androidpg.ConvaAISDKType
import `in`.slanglabs.androidpg.model.AppData
import `in`.slanglabs.androidpg.ui.blocks.CardButtonBlock
import `in`.slanglabs.androidpg.ui.viewModel.viewModels.SelectionViewModel

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
                    ConvaAISDKType.ASSISTANT_SDK.typeValue,
                    onClick = {it
                        val destination = viewModel.onAssistantSelected(it,appData)
                        navController.navigate(destination)
                    },
                    primarySelectionColor
                )
                CardButtonBlock(
                    ConvaAISDKType.CORE_SDK.typeValue,
                    onClick = {it
                        val destination = viewModel.onAssistantSelected(it,appData)
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