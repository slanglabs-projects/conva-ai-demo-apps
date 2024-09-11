package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.Place;

@Dao
public interface PlaceDao {

    @Transaction
    @Query("SELECT * FROM place")
    LiveData<List<Place>> getAllPlaces();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Place> items);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Place item);

    @Transaction
    @Query("SELECT * FROM place WHERE city LIKE '%' || :searchQuery || '%' " +
            "OR state LIKE '%' || :searchQuery || '%' " +
            "OR stop LIKE '%' || :searchQuery || '%' " +
            "OR stateFullName LIKE '%' || :searchQuery || '%'")
    LiveData<List<Place>> searchPlaces(String searchQuery);
}
