package `in`.slanglabs.convaai.pg.ui.blocks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MessageBlock(
    defaultMessage: String,
    secondString: String,
    messageType: MessageType = MessageType.PRIMARY,
    primaryColor: Color = Color(0xFF222222),
    onPrimaryColor: Color = Color.White,
    secondColor: Color = Color(0xFF414141),
    onSecondaryColor: Color = Color.White,
    onSecondStringShown: (Boolean) -> Unit = {}
) {
    var showSecondString by remember { mutableStateOf(false) }
    val messageToShow =
        if (showSecondString && secondString.isNotEmpty()) secondString else defaultMessage
    val context = LocalContext.current

    val backgroundColor = if (messageType == MessageType.PRIMARY) primaryColor else secondColor
    val onBackgroundColor =
        if (messageType == MessageType.PRIMARY) onPrimaryColor else onSecondaryColor
    val arrangement = if (messageType == MessageType.PRIMARY) Arrangement.End else Arrangement.Start
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = arrangement,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Card(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .padding(vertical = 4.dp)
                .clickable {
                    if (messageType != MessageType.PRIMARY) {
                        showSecondString = !showSecondString
                        onSecondStringShown(showSecondString)
                    }
                },
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor,
                contentColor = onBackgroundColor
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Text(
                text = messageToShow,
                style = TextStyle(fontSize = 16.sp),
                color = onBackgroundColor,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

enum class MessageType {
    PRIMARY,
    SECONDARY
}

@Preview
@Composable
fun MessageBlockPreview() {
    MessageBlock(
        defaultMessage = "Hey how can i help you",
        secondString = "{text : \"Hey how can i help you \"}"
    )
}