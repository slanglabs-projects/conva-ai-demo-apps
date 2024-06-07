package `in`.slanglabs.androidpg.ui.blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.slanglabs.androidpg.ui.Utils

@Composable
fun TitleBarBlock(
    titleText: String,
    backgroundColor: Color = Color(0xFF36A9E0),
    onBackgroundColor: Color = Color.White,
    surfaceColor: Color = Color.White,
    shouldStartProgressIndicator: State<Boolean>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(color = backgroundColor)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier
                .padding(end = 8.dp)
                .wrapContentWidth(),
            text = titleText.trim(),
            style = MaterialTheme.typography.headlineMedium,
            color = onBackgroundColor,
            textAlign = TextAlign.Start
        )
        if (!shouldStartProgressIndicator.value) return

        CircularProgressIndicator(
            modifier = Modifier.width(28.dp),
            color = surfaceColor,
            strokeWidth = 4.dp
        )
    }
}

@Preview
@Composable
fun TitleBarBlockPreview() {
//    TitleBarBlock(
//        backgroundColor = Color(0xFF36A9E0),
//        titleText = Utils.appTitle,
//        shouldStartProgressIndicator = false
//    )
}