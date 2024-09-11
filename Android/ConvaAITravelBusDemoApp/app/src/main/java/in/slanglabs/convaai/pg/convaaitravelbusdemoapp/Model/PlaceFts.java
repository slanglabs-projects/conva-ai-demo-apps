package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model;

import androidx.room.Entity;
import androidx.room.Fts4;

@Fts4(contentEntity = Place.class)
@Entity(tableName = "placeFts")
public class PlaceFts {
    public String id;
    public String city;
    public String state;
    public String stop;
    public String stateFullName;
}
