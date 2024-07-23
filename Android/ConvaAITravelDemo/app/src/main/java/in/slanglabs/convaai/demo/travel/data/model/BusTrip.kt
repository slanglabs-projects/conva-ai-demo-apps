package `in`.slanglabs.convaai.demo.travel.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class BusTrip(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val source: String,
    val destination: String,
    val departureTime: String,
    val duration: Int,
    val arrivalTime: String,
    val date: String
)

