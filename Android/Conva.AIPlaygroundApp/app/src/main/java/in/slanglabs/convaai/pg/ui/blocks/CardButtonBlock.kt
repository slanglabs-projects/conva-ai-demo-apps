package `in`.slanglabs.convaai.pg.ui.blocks

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.slanglabs.convaai.pg.R

@Composable
fun CardButtonBlock(
    message: String,
    onClick: (String) -> Unit,
    backgroundColor: Color = Color(0xFF0046C4),
    onBackgroundColor: Color = Color.White,
    imageVector: ImageVector? = null,
    isWrapContentEnabled: Boolean = false
) {
    val arrangement = Arrangement.Center
    val parentCardModifier: Modifier = Modifier
        .padding(8.dp)
        .clickable(onClick = { onClick(message) })
        .then(
            if (isWrapContentEnabled) {
                Modifier
                    .wrapContentHeight()
                    .wrapContentWidth()
            } else {
                Modifier
                    .fillMaxWidth(0.9f)
                    .aspectRatio(2f)
            }
        )

    Column(
        modifier = Modifier
            .wrapContentWidth()
            .padding(2.dp),
        verticalArrangement = arrangement
    ) {
        Card(
            modifier = parentCardModifier,
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor,
                contentColor = onBackgroundColor
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Box(
                modifier = Modifier.then(
                    if (isWrapContentEnabled) {
                        Modifier
                            .wrapContentHeight()
                            .wrapContentWidth()
                    } else {
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    }
                ),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier = Modifier.then(
                        if (isWrapContentEnabled) {
                            Modifier
                                .wrapContentHeight()
                                .wrapContentWidth()
                        } else {
                            Modifier
                                .fillMaxWidth()
                                .padding(1.dp)
                        }
                    ),
                    verticalArrangement = arrangement,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (imageVector != null) {
                        Image(
                            imageVector = imageVector,
                            contentDescription = message,
                            colorFilter = ColorFilter.tint(onBackgroundColor),
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .aspectRatio(2f)
                        )
                    }
                    Text(
                        text = message,
                        style = TextStyle(fontSize = 18.sp, textAlign = TextAlign.Center),
                        color = onBackgroundColor,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .aspectRatio(6f)
                            .wrapContentHeight(),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardButtonBlockPreview() {
    CardButtonBlock(
        message = "Scan QR",
        onClick = { assistantLib ->
            // pass
        },
        backgroundColor = Color(0xFF0046C4),
        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_qr_code_2_24),
        isWrapContentEnabled = true
    )
}