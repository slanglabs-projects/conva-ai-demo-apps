package `in`.slanglabs.convaai.demo.travel.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import `in`.slanglabs.convaai.demo.travel.data.local.AppDatabase
import `in`.slanglabs.convaai.demo.travel.data.model.BusTrip
import `in`.slanglabs.convaai.demo.travel.data.repository.TripRepository
import kotlinx.coroutines.launch

class TripViewModel(application: Application) : AndroidViewModel(application) {
    private val tripDao = Room.databaseBuilder(
        application,
        AppDatabase::class.java, "bus_app_db"
    ).build().tripDao()
    private val repository = TripRepository(tripDao)
    val trips = MutableLiveData<List<BusTrip>>()

    fun insertTrip(trip: BusTrip) = viewModelScope.launch {
        repository.insertTrip(trip)
    }

    fun fetchAllTrips() = viewModelScope.launch {
        trips.postValue(repository.getAllTrips())
    }
}
