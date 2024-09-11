package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "journey",
        foreignKeys = @ForeignKey(entity = Bus.class,
                parentColumns = "id",
                childColumns = "busId",
                onDelete = ForeignKey.CASCADE))
public class Journey implements Serializable {

    @PrimaryKey
    @NonNull
    public long journeyId;
    public Date date;
    public String startLocation;
    public String endLocation;

    @ColumnInfo(name = "busId", index = true)
    public String busId;

    public long duration;
    public long price;
    public Date startTime;
    public Date endTime;

    public @RouteStatus int routeStatus;
}
