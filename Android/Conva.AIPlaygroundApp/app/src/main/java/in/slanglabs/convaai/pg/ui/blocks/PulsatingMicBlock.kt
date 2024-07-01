package `in`.slanglabs.convaai.pg.ui.blocks

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import `in`.slanglabs.convaai.pg.R

@Composable
fun PulsatingMicBlock(
    micColor: Color = Color(0xFFFFFFFF),
    backgroundColor: Color = Color(0xFF414141),
    strokeColor: Color = Color(0xFF36A9E0),
    animate: Boolean = true,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .sizeIn(minWidth = 40.dp, minHeight = 40.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            }
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "")
        val size by infiniteTransition.animateValue(
            initialValue = 40.dp,
            targetValue = 35.dp,
            Dp.VectorConverter,
            animationSpec = infiniteRepeatable(
                animation = tween(500, easing = FastOutLinearInEasing),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        )
        val smallCircle by infiniteTransition.animateValue(
            initialValue = 30.dp,
            targetValue = 33.dp,
            Dp.VectorConverter,
            animationSpec = infiniteRepeatable(
                animation = tween(500, easing = FastOutLinearInEasing),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(40.dp)
        ) {
            CircleShape(
                size = if (animate) size else 40.dp,
                color = if (animate) strokeColor.copy(alpha = 0.25f) else backgroundColor
            )
            CircleShape(
                size = if (animate) smallCircle else 30.dp,
                color = if (animate) strokeColor.copy(alpha = 0.25f) else backgroundColor
            )
            CircleShape(
                size = 24.dp,
                color = if (animate) strokeColor else backgroundColor
            )
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_mic_24),
                contentDescription = null,
                colorFilter = ColorFilter.tint(micColor),
                modifier = Modifier
                    .fillMaxSize(0.3f)
            )
        }
    }
}

@Composable
fun CircleShape(
    size: Dp,
    color: Color = Color.White,
    borderWidth: Dp = 0.dp,
    borderColor: Color = Color.LightGray.copy(alpha = 0.0f)
) {
    Column(
        modifier = Modifier
            .wrapContentSize(Alignment.Center)
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(
                    color
                )
                .border(borderWidth, borderColor)
        )
    }
}

@Preview
@Composable
fun PulsatingMicBlockPreview() {
    PulsatingMicBlock(onClick = {})
}