package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.io.Serializable;

@Entity(tableName = "busAttributes",
        foreignKeys = @ForeignKey(entity = Bus.class,
        parentColumns = "id",
        childColumns = "busId",
        onDelete = ForeignKey.CASCADE),
        primaryKeys = {"busId","travelClass"})
public class BusAttributes implements Serializable {
    @NonNull
    public String busId;
    @NonNull
    public @TravelClass String travelClass;
}
