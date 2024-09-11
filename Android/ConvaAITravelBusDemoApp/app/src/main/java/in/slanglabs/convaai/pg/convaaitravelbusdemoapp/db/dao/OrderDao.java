package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.db.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.JourneyBusPlaceOrder;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.OrderItem;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.OrderStatus;


@Dao
public interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY journeyDate DESC")
    LiveData<List<JourneyBusPlaceOrder>> loadAllOrders();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(OrderItem orderItem);

    @Query("UPDATE orders SET active=:active WHERE orderId = :orderId")
    void update(@OrderStatus int active, String orderId);

    @Query("select * from orders where orderId = :orderId")
    LiveData<JourneyBusPlaceOrder> loadOrder(String orderId);
}