package `in`.slanglabs.convaai.demo.travel.data.repository

import `in`.slanglabs.convaai.demo.travel.data.local.TripDao
import `in`.slanglabs.convaai.demo.travel.data.model.BusTrip

class TripRepository(private val tripDao: TripDao) {
    suspend fun insertTrip(trip: BusTrip) = tripDao.insertTrip(trip)
    suspend fun getAllTrips() = tripDao.getAllTrips()
}
