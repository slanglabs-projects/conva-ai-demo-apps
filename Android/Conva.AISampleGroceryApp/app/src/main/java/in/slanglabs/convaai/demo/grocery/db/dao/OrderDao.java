package in.slanglabs.convaai.demo.grocery.db.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import in.slanglabs.convaai.demo.grocery.Model.OrderItem;

@Dao
public interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY orderTime DESC")
    LiveData<List<OrderItem>> loadAllOrders();

    @Query("SELECT * FROM orders ORDER BY orderTime DESC")
    List<OrderItem> loadAllOrdersSync();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(OrderItem orderItem);

    @Query("UPDATE orders SET active=:active WHERE orderId = :orderId")
    void update(boolean active, String orderId);

    @Query("select * from orders where orderId = :orderId")
    LiveData<OrderItem> loadOrder(String orderId);
}