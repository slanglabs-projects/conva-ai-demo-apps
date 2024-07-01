package `in`.slanglabs.convaai.pg.ui.blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownBlock(
    title: String,
    value: String,
    itemList: List<String>,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF222222),
    onBackgroundColor: Color = Color.White,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = modifier.background(backgroundColor)
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            readOnly = true,
            value = value.trim(),
            onValueChange = { },
            label = {
                Text(
                    text = title, color = onBackgroundColor, maxLines = 1,
                    overflow = TextOverflow.Ellipsis, fontSize = 8.sp
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                containerColor = backgroundColor,
                focusedLabelColor = onBackgroundColor,
                unfocusedLabelColor = onBackgroundColor,
                focusedTrailingIconColor = onBackgroundColor,
                unfocusedTrailingIconColor = onBackgroundColor
            ),
            textStyle = TextStyle(color = onBackgroundColor, fontSize = 12.sp),
            singleLine = true,
            maxLines = 1
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor),
        ) {
            itemList.forEach { item ->
                DropdownMenuItem(
                    modifier = Modifier.background(backgroundColor),
                    text = {
                        Text(
                            text = item.trim(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = onBackgroundColor,
                            fontSize = 12.sp
                        )
                    },
                    onClick = {
                        onItemSelected(item.trim())
                        expanded = !expanded
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DropDownPreview() {
    val itemList: List<String> = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5")
    DropDownBlock(
        title = "Capabilities",
        "Option 1",
        itemList = itemList,
        onItemSelected = { messageToSend -> })
}
