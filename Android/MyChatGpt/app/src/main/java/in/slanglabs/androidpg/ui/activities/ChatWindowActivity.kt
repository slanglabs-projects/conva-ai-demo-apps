package `in`.slanglabs.androidpg.ui.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelProvider
import `in`.slanglabs.androidpg.BuildConfig
import `in`.slanglabs.androidpg.R
import `in`.slanglabs.androidpg.ui.viewModel.ChatWindowViewModel


class ChatWindowActivity : ComponentActivity() {
    private val assistantID : String = BuildConfig.ASSISTANT_ID
    private val apiKey : String = BuildConfig.API_KEY
    private val assistantVersion : String = BuildConfig.ASSISTANT_VERSION

    @SuppressLint("RememberReturnType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this).get(ChatWindowViewModel::class.java)
        var chatHistory : MutableList<String> = mutableListOf()

        viewModel.initSlang(assistantID,apiKey,assistantVersion)

        val updateTheChatScreen: () -> Unit = {
            setContent {
                ChatScreen(
                    chatHistory = chatHistory,
                    onSendTextClick = { text -> viewModel.sendText(text) }
                )

                val lifecycleOwner = LocalLifecycleOwner.current
                val lifecycle = lifecycleOwner.lifecycle
                DisposableEffect(lifecycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_DESTROY) {
                            viewModel.clearData()
                        }
                    }
                    lifecycle.addObserver(observer)

                    onDispose {
                        lifecycle.removeObserver(observer)
                    }
                }

            }
        }

        viewModel.messageLiveData.observe(this) { message ->
            if (message.isNotBlank()) {
                chatHistory.add("Copilot: $message")
                updateTheChatScreen()
            }

        }
        updateTheChatScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(chatHistory: MutableList<String>, onSendTextClick: (String) -> Unit) {
    val context = LocalContext.current
    val view = LocalView.current
    val focusManager = LocalFocusManager.current
    var message by remember { mutableStateOf("") }
    val imagePainter = painterResource(id = R.drawable.conva_logo)
    var isVisible by remember { mutableStateOf(true) }

    isVisible = chatHistory.isNotEmpty()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFF36A9E0))
                .padding(vertical = 16.dp, horizontal = 16.dp),
            text = "CONVA.ai",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black,
            textAlign = TextAlign.Start
        )

        Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF1B1B1B)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                        .verticalScroll(rememberScrollState()),

                ) {
                    if (isVisible) {
                        Column() {
                            chatHistory.forEach { msg ->
                                AnimatedVisibility(
                                    visible = true,
                                    enter = fadeIn(),
                                    modifier = Modifier.animateContentSize()
                                ) {
                                    MessageBlock(msg)
                                }
                            }
                        }
                    } else {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center

                            ) {
                                Spacer(modifier = Modifier.height(302.dp))
                                Image(
                                    painter = imagePainter,
                                    contentDescription = "Your Image",
                                    modifier = Modifier.fillMaxSize()
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Ask me any questions related to DigiYatra!",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                            }
                    }
                }

                TextField(
                    value = message,
                    onValueChange = {
                        isVisible = true
                        message = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Type your question...", color = Color(0xFF858585)) },
                    textStyle = TextStyle(fontSize = 20.sp),
                    trailingIcon = {
                        Icon(modifier = Modifier.clickable {
                            if (message.isNotBlank()) {
                                val messageToSend = message
                                chatHistory.add("You: $messageToSend")
                                onSendTextClick(messageToSend)
                                message = ""

                                focusManager.clearFocus()
                                val inputMethodManager =
                                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                            }
                        },imageVector = Icons.Filled.Send, contentDescription = null)

                    },
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = Color(0xFF858585),
                        textColor = Color(0xFFFFFFFF),
                        focusedTrailingIconColor = Color(0xFFFFFFFF),
                        unfocusedTrailingIconColor = Color(0xFFFFFFFF),
                        containerColor = Color(0xFF222222),
                        focusedIndicatorColor =Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent),
                )
            }
        }
    }
}

@Composable
fun MessageBlock(message: String) {
    val isUserMessage = message.startsWith("You:")

    val textColor = if (isUserMessage) Color(0xFF222222) else Color(0xFF414141)
    val arrangement = if (isUserMessage) Arrangement.End else Arrangement.Start
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = arrangement
    ) {
    Card(
        modifier = Modifier
            .width(IntrinsicSize.Max)
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = textColor,
            contentColor = androidx.compose.ui.graphics.Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Text(
            text = message,
            style = TextStyle(fontSize = 16.sp),
            color = androidx.compose.ui.graphics.Color.White,
            modifier = Modifier.padding(8.dp)
        )
    }
    }
}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun PreviewChatScreen() {
    var chatHistory : MutableList<String> = mutableListOf()
    ChatScreen(onSendTextClick = {  }, chatHistory = chatHistory)
}
