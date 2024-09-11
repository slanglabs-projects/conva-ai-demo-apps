package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

@Entity(tableName = "place")
public class Place implements Serializable {

    @PrimaryKey
    @NonNull
    public String id;
    public String city;
    public String state;
    public String stop;
    public String stateFullName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return city.equalsIgnoreCase(place.city) && stop.equalsIgnoreCase(place.stop) && stateFullName.equalsIgnoreCase(place.stateFullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean isComplete() {
        if(id == null) {
            return false;
        }
        return !id.isEmpty();
    }

    @Override
    public String toString() {
        return "Place{" +
                "id='" + id + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", stop='" + stop + '\'' +
                ", stateFullName='" + stateFullName + '\'' +
                '}';
    }
}