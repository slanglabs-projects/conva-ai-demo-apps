package `in`.slanglabs.convaai.pg.ui.blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.ClipData
import android.content.ClipboardManager
import android.widget.Toast
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import `in`.slanglabs.convaai.pg.R

@Composable
fun CodeBox(
    code: String,
    capability: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = context.getSystemService(ClipboardManager::class.java)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF012B36), shape = RoundedCornerShape(8.dp))
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Column(modifier = Modifier
            .fillMaxSize()) {

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween ) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFF39200), shape = RoundedCornerShape(4.dp))
                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(4.dp))
                        .padding(start = 4.dp, end = 4.dp, top = 2.dp, bottom = 2.dp)
                        .weight(1f, fill = false)
                ) {
                    Text(
                        text = capability,
                        style = TextStyle(fontSize = 14.sp, color = Color.White),
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .background(Color.Black, shape = RoundedCornerShape(4.dp))
                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(4.dp))
                        .padding(start = 4.dp, end = 4.dp, top = 2.dp, bottom = 2.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {
                                val clip = ClipData.newPlainText("Code", code)
                                clipboardManager.setPrimaryClip(clip)
                                Toast.makeText(context, "Code copied to clipboard", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(16.dp)
                                .padding(1.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_content_copy_24),
                                contentDescription = "Copy",
                                tint = Color.White
                            )
                        }
                        Text(
                            text = "Copy",
                            style = TextStyle(fontSize = 14.sp, color = Color.White),
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }

            Text(
                text = code,
                style = TextStyle(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp,
                    color = Color.White
                ),
                modifier = Modifier
                    .padding(top= 8.dp, end = 10.dp)
            )

        }
    }
}
