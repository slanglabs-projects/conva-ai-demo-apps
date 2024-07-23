package `in`.slanglabs.convaai.demo.travel.ui

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import `in`.slanglabs.convaai.demo.travel.data.model.BusTrip
import `in`.slanglabs.convaai.demo.travel.utils.Utils
import `in`.slanglabs.convaai.demo.travel.viewmodel.TripViewModel
import `in`.slanglabs.convaai.demo.travel.viewmodel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.random.Random


@Composable
fun BusListPage(navController: NavController, source: String, destination: String, date: String) {
    val context = LocalContext.current
    val viewModel: TripViewModel = viewModel(
        factory = ViewModelFactory(context.applicationContext as Application)
    )
    val random = Random(System.currentTimeMillis())

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val parsedDate = dateFormat.parse(Utils.decodeDate(date))
    val busList = List(5) {
        val departureTime = Calendar.getInstance().apply {
            time = parsedDate!!
            set(Calendar.HOUR_OF_DAY, random.nextInt(24))
            set(Calendar.MINUTE, random.nextInt(60))
        }
        val duration = random.nextInt(8) + 4
        val arrivalTime = (departureTime.clone() as Calendar).apply {
            add(Calendar.HOUR_OF_DAY, duration)
        }

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        BusTrip(
            source = source,
            destination = destination,
            departureTime = timeFormat.format(departureTime.time),
            duration = duration,
            arrivalTime = timeFormat.format(arrivalTime.time),
            date = date
        )
    }

    var showDialog by remember { mutableStateOf<BusTrip?>(null) }

    BusListContent(busList, parsedDate) {
        showDialog = it
    }

    showDialog?.let { bus ->
        AlertDialog(
            onDismissRequest = { showDialog = null },
            title = { Text("Book Ticket") },
            text = { Text("Do you want to book this ticket?") },
            confirmButton = {
                Button(onClick = {
                    viewModel.insertTrip(bus)
                    navController.popBackStack()
                    showDialog = null
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = null }) {
                    Text("No")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusListContent(busList: List<BusTrip>, parsedDate: Date?, onBusSelected: (BusTrip) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buses") },
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
                .fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Results for ${
                        parsedDate?.let { date ->
                            val resultDateFormat = SimpleDateFormat("dd MMMM", Locale.getDefault())
                            val formattedDate = resultDateFormat.format(date)
                            formattedDate
                        }
                    }",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp)
                )
            }
            LazyColumn(
                contentPadding = PaddingValues(top = 8.dp),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(busList) { bus ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                onBusSelected(bus)
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(bus.source)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Dep: ${bus.departureTime}")
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("→", modifier = Modifier.padding(bottom = 8.dp))
                                Text("${bus.duration} hrs")
                            }
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(bus.destination)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Arr: ${bus.arrivalTime}")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BusListContentPreview() {
    val sampleBusList = listOf(
        BusTrip(1, "Mumbai", "Pune", "12:00", 6, "18:00", "12-07-2024"),
        BusTrip(2, "Mumbai", "Pune", "13:00", 6, "19:00", "12-07-2024"),
        BusTrip(3, "Mumbai", "Pune", "14:00", 6, "20:00", "12-07-2024"),
    )
    BusListContent(sampleBusList, Date()) {} // Empty onBusSelected lambda for preview
}