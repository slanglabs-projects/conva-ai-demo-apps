package `in`.slanglabs.androidpg.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.slanglabs.androidpg.model.AppData
import `in`.slanglabs.androidpg.ui.viewModel.viewModels.AssistantResponseScreenViewModel

@Composable
fun AssistantResponseScreen(
    viewModel: AssistantResponseScreenViewModel,
    primaryColor: Color = Color(0xFF36A9E0),
    onPrimaryColor: Color = Color.White,
    backgroundColor: Color = Color(0xFF1B1B1B),
    surfaceColor: Color = Color(0xFF414141),
    onSurfaceColor: Color = Color.White,
    appData: AppData
) {
    LaunchedEffect(Unit) {
        viewModel.initializeAssistant(appData)
    }
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val clipboardManager =
            LocalContext.current.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(350.dp)
                .padding(16.dp)
                .background(color = surfaceColor, shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        text = uiState.title.trim(),
                        style = MaterialTheme.typography.titleMedium,
                        color = onSurfaceColor,
                        textAlign = TextAlign.Start
                    )
                    Button(
                        onClick = {
                            val clipData = ClipData.newPlainText("response text", uiState.body)
                            clipboardManager.setPrimaryClip(clipData)
                        },
                        modifier = Modifier
                            .padding(4.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor)
                    ) {
                        Icon(
                            painter = painterResource(id = uiState.clipboardIcon),
                            contentDescription = "Copy",
                            tint = surfaceColor
                        )
                    }
                }
                Divider(modifier = Modifier.fillMaxWidth())
                val scroll = rememberScrollState(0)
                Text(
                    text = uiState.body,
                    style = MaterialTheme.typography.bodySmall,
                    color = onSurfaceColor,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .verticalScroll(scroll)
                )
            }
        }
        Box(contentAlignment = Alignment.Center) {
            Button(
                onClick = {
                    viewModel.invokeAssistant()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (uiState.isAssistantInitialized) primaryColor else Color.Gray
                )
            ) {
                Text(
                    text = uiState.invokeButtonTitle,
                    color = onPrimaryColor)
            }
        }
    }
}


@Preview
@Composable
fun CopyTextBoxScreenPreview() {
}