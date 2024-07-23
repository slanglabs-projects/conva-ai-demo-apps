package `in`.slanglabs.convaai.demo.travel.ui

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import `in`.slanglabs.convaai.demo.travel.ui.navigation.NavigationRoutes
import `in`.slanglabs.convaai.demo.travel.utils.Utils
import `in`.slanglabs.convaai.demo.travel.viewmodel.SearchViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun HomePage(navController: NavController, searchViewModel: SearchViewModel) {
    val context = LocalContext.current
    var source by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    val isButtonEnabled = source.isNotBlank() && destination.isNotBlank() && date.isNotBlank()
    val focusManager = LocalFocusManager.current
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            date = dateFormat.format(Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }.time)
            searchViewModel.updateDate(date)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 100

    // Collect search parameters from ViewModel
    LaunchedEffect(Unit) {
        searchViewModel.searchParams.collect { params ->
            params?.let {
                it.source?.let { s -> source = s}
                it.destination?.let { d -> destination = d}
                it.date?.let {
                    d ->
                    date = Utils.convertDateFormat(d).ifBlank { d }
                }
            }
        }
    }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Search Buses",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = source,
                onValueChange = {
                    source = it
                    searchViewModel.updateSource(source)
                },
                label = { Text("Source") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            TextField(
                value = destination,
                onValueChange = {
                    destination = it
                    searchViewModel.updateDestination(destination)
                },
                label = { Text("Destination") },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box {
            TextField(
                value = date,
                onValueChange = {},
                label = { Text("Date") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { // Place the icon inside the TextField
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Calendar"
                    )
                },
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .alpha(0f)
                    .clickable {
                        focusManager.clearFocus()
                        datePickerDialog.show()
                    }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                navController.navigate(NavigationRoutes.busListRoute(source, destination, Utils.encodeDate(date)))
                focusManager.clearFocus()
            },
            enabled = isButtonEnabled
        ) {
            Text("Search")
        }
    }
}
