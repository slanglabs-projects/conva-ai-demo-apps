package `in`.slanglabs.convaai.demo.travel.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import `in`.slanglabs.convaai.demo.travel.data.model.BusTrip

@Dao
interface TripDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: BusTrip)

    @Query("SELECT * FROM trips")
    suspend fun getAllTrips(): List<BusTrip>
}