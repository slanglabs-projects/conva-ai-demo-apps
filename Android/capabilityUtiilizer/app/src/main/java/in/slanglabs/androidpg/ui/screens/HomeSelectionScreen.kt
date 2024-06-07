package `in`.slanglabs.androidpg.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import `in`.slanglabs.androidpg.R
import `in`.slanglabs.androidpg.ui.Utils
import `in`.slanglabs.androidpg.ui.blocks.CardButtonBlock
import `in`.slanglabs.androidpg.ui.viewModel.viewModels.SelectionViewModel

@Composable
fun HomeSelectionScreen(
    viewModel: SelectionViewModel,
    backgroundColor: Color = Color(0xFF1B1B1B),
    primarySelectionColor: Color = Color(0xFF0046C4),
    secondarySelectionColor: Color = Color(0xFFF39200),
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CardButtonBlock(
                    message = viewModel.qrCodeScanner,
                    onClick = { it ->
                        val destination = viewModel.onHomeSelection(it)
                        navController.navigate(destination)
                    },
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_qr_code_2_24),
                    isWrapContentEnabled = true,
                    backgroundColor = primarySelectionColor
                )
                Spacer(modifier = Modifier.size(15.dp))
                Divider()
                Spacer(modifier = Modifier.size(15.dp))
                CardButtonBlock(
                    message = viewModel.defaultAssistant,
                    onClick = { it ->
                        val destination = viewModel.onHomeSelection(it)
                        navController.navigate(destination)
                    },
                    isWrapContentEnabled = true,
                    backgroundColor = secondarySelectionColor
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectionHomeScreenPreview() {
}

