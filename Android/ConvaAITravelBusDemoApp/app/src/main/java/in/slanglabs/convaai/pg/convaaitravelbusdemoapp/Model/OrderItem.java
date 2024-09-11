package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "orders")
public class OrderItem implements Serializable {

    @PrimaryKey
    @NonNull
    public String orderId;
    public Date orderTime;
    public long journeyId;
    public @OrderStatus int active;
    public Date journeyDate;
    public Place sourcePlace;
    public Place destinationPlace;
}
