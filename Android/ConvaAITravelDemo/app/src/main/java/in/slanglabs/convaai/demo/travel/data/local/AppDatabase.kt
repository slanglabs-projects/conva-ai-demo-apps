package `in`.slanglabs.convaai.demo.travel.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import `in`.slanglabs.convaai.demo.travel.data.model.BusTrip

@Database(entities = [BusTrip::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
}