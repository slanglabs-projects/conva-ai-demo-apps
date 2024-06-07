package `in`.slanglabs.androidpg.ui.screens

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.slanglabs.androidpg.model.AppData
import `in`.slanglabs.androidpg.ui.blocks.BottomBarBlock
import `in`.slanglabs.androidpg.ui.blocks.ChatBlock
import `in`.slanglabs.androidpg.ui.blocks.PulsatingMicBlock
import `in`.slanglabs.androidpg.ui.viewModel.viewModels.ChatScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatScreenViewModel,
    appData: AppData
    ) {
    LaunchedEffect(Unit) {
        viewModel.initializeAssistant(appData)
    }
    val uiState by viewModel.uiState.collectAsState()
    val isTrailingIconEnabled by viewModel.isTrailingIconEnabled.collectAsState(true)
    val isListening by viewModel.isListening.collectAsState()

    val context = LocalContext.current
    val view = LocalView.current
    val focusManager = LocalFocusManager.current
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    fun hideKeyboard() {
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun sendMessage() {
        val messageToSend = viewModel.message.value
        val capabilityGroupSelected = uiState.capabilityGroup.trim()
        val capabilitySelected = uiState.capability.trim()
        viewModel.onTextReady(
            messageToSend,
            capabilityGroupSelected,
            capabilitySelected
        )
        focusManager.clearFocus()
        hideKeyboard()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF1B1B1B)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                ChatBlock(
                    modifier = Modifier.weight(1f),
                    logoImage = uiState.appLogo,
                    logoTitle = uiState.appLogoTitle,
                    isChatHistoryVisible = uiState.chatWindowVisibility,
                    chatHistory = uiState.chatHistory,
                    onMessageClicked= {
                    viewModel.onMessageClicked()
                })
                TextField(
                    value = viewModel.message.value,
                    onValueChange = { it ->
                        viewModel.onTextValueChanged(it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(uiState.placeholder, color = Color(0xFF858585)) },
                    textStyle = TextStyle(fontSize = 18.sp),
                    trailingIcon = {
                        Row (
                            modifier = Modifier.padding(end = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                                PulsatingMicBlock(onClick = {
                                    hideKeyboard()
                                    viewModel.onMicClicked()
                                }, animate = isListening)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                modifier = Modifier.clickable {
                                    if (isTrailingIconEnabled) {
                                        sendMessage()
                                    }
                                    },
                                    imageVector = Icons.Filled.Send,
                                    contentDescription = null,
                                    tint = if (isTrailingIconEnabled) Color(0xFFFFFFFF) else Color(
                                        0xFF858585
                                    )
                                )
                        }
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = Color(0xFF858585),
                        textColor = Color(0xFFFFFFFF),
                        focusedTrailingIconColor = Color(0xFFFFFFFF),
                        unfocusedTrailingIconColor = Color(0xFFFFFFFF),
                        containerColor = Color(0xFF222222),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            sendMessage()
                        }
                    )
                )
                BottomBarBlock(
                    assistantCapabilitiesGroups = uiState.capabilityGroupList,
                    selectedCapabilityGroup = uiState.capabilityGroup,
                    assistantCapabilities = uiState.capabilityList,
                    selectedCapability = uiState.capability,
                    onCapabilityUpdate = { capabilitiesGroups, capabilitySelected ->
                        viewModel.onCapabilityUpdated(capabilitiesGroups,capabilitySelected)
                    })
            }
        }
    }
}

@Preview
@Composable
fun ChatScreenPreview() {
}