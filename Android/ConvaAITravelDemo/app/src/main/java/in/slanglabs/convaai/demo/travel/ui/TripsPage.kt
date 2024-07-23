package `in`.slanglabs.convaai.demo.travel.ui

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.slanglabs.convaai.demo.travel.data.model.BusTrip
import `in`.slanglabs.convaai.demo.travel.viewmodel.TripViewModel
import `in`.slanglabs.convaai.demo.travel.viewmodel.ViewModelFactory

@Composable
fun TripsPage() {
    val context = LocalContext.current
    val viewModel: TripViewModel = viewModel(
        factory = ViewModelFactory(context.applicationContext as Application)
    )
    val trips by viewModel.trips.observeAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchAllTrips()
    }

    TripsContent(trips)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripsContent(trips: List<BusTrip>) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trips") },
            )
        }
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            items(trips) { trip ->
                TripItem(trip)
            }
        }}
}

@Composable
fun TripItem(trip: BusTrip) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Source: ${trip.source}")
            Text("Destination: ${trip.destination}")
            Text("Departure: ${trip.departureTime}")
            Text("Duration: ${trip.duration} hours")
            Text("Arrival: ${trip.arrivalTime}")
            Text("Date: ${trip.date}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TripItem() {
    val sampleBusList = listOf(
        BusTrip(1, "Mumbai", "Pune", "12:00", 6, "18:00", "12-07-2024"),
        BusTrip(2, "Mumbai", "Pune", "13:00", 6, "19:00", "12-07-2024"),
        BusTrip(3, "Mumbai", "Pune", "14:00", 6, "20:00", "12-07-2024"),
    )
    TripsContent(sampleBusList)
}