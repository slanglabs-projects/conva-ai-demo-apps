package `in`.slanglabs.convaai.pg.ui.blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BottomBarBlock(
    assistantCapabilitiesGroups: List<String>,
    selectedCapabilityGroup: String,
    assistantCapabilities: List<String>,
    selectedCapability: String,
    backgroundColor: Color = Color(0xFF414141),
    onCapabilityUpdate: (String, String) -> Unit
) {
    var capabilityGroupChosen by remember { mutableStateOf(selectedCapabilityGroup) }
    var capabilityChosen by remember { mutableStateOf(selectedCapability) }
    var selectedRadio by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(color = backgroundColor)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 10.dp)
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedRadio == 0,
                    onClick = { selectedRadio = 0 },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFF38B5FF),
                        unselectedColor = Color.Gray,
                    )
                )
                Text(text = "Capabilities Group", color = Color.White,  modifier = Modifier
                    .offset(x = (-8).dp))
            }


            Spacer(modifier = Modifier.width(16.dp))

            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedRadio == 1,
                    onClick = { selectedRadio = 1 },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFF38B5FF),
                        unselectedColor = Color.Gray,
                    )
                )
                Text(text = "Capabilities", color = Color.White,  modifier = Modifier
                    .offset(x = (-8).dp))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(bottom = 10.dp)
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (selectedRadio == 0) {
                DropDownBlock(
                    title = "Capabilities Group",
                    value = selectedCapabilityGroup,
                    itemList = assistantCapabilitiesGroups,
                    onItemSelected = { item ->
                        capabilityGroupChosen = if (selectedRadio == 0) item else ""
                        onCapabilityUpdate(capabilityGroupChosen, capabilityChosen)
                    })
                onCapabilityUpdate(capabilityGroupChosen, "")
            } else {
                DropDownBlock(
                    title = "Capabilities",
                    value = selectedCapability,
                    itemList = assistantCapabilities,
                    onItemSelected = { item ->
                        capabilityChosen = if (selectedRadio == 1) item else ""
                        onCapabilityUpdate(capabilityGroupChosen, capabilityChosen)
                    })
                onCapabilityUpdate("", capabilityChosen)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBarBlockPrview() {
    var assistantCapabilities: List<String> =
        listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5")
    var assistantCapabilitiesGroup: List<String> =
        listOf("OptionGroup 1", "OptionGroup 2", "OptionGroup 3", "OptionGroup 4", "Option 5")
    BottomBarBlock(
        assistantCapabilitiesGroups = assistantCapabilitiesGroup,
        "",
        assistantCapabilities = assistantCapabilities,
        "",
        onCapabilityUpdate = { capabilityGroupSelected, capabilitySelected -> })
}
