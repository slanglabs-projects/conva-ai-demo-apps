package `in`.slanglabs.convaai.pg.ui.blocks

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.slanglabs.convaai.pg.R

@Composable
fun TitleBarBlock(
    surfaceColor: Color = Color.White,
    shouldStartProgressIndicator: State<Boolean>
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF003366),
            Color(0xFF000000)
        )
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(brush = gradient)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(modifier = Modifier
            .heightIn(2.dp)
            .widthIn(2.dp),
            painter = painterResource(id = R.drawable.conva_ai_logo),
            contentDescription = "logo")
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
}