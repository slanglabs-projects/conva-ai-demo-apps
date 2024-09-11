package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "bus")
public class Bus implements Serializable {

    @PrimaryKey
    @NonNull
    public String id;
    public String name;
    public String travels;
    public float starRating;
    public String type;
}
