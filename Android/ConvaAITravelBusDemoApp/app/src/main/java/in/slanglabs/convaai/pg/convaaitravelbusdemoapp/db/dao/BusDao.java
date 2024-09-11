package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.Bus;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.BusWithAttributes;


@Dao
public interface BusDao {

    @Transaction
    @Query("SELECT * FROM bus")
    LiveData<List<BusWithAttributes>> getAllBuses();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Bus> items);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Bus item);
}
