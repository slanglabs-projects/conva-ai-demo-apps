package `in`.slanglabs.convaai.pg.ui.blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(color = backgroundColor)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DropDownBlock(
            title = "Capabilities Group",
            value = selectedCapabilityGroup,
            itemList = assistantCapabilitiesGroups,
            modifier = Modifier.weight(1f),
            onItemSelected = { item ->
                capabilityGroupChosen = item
                onCapabilityUpdate(capabilityGroupChosen, capabilityChosen)
            })

        Spacer(modifier = Modifier.width(16.dp))

        DropDownBlock(
            title = "Capabilities",
            value = selectedCapability,
            itemList = assistantCapabilities,
            modifier = Modifier.weight(1f),
            onItemSelected = { item ->
                capabilityChosen = item
                onCapabilityUpdate(capabilityGroupChosen, capabilityChosen)
            })
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
