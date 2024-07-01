package in.slanglabs.convaai.demo.grocery.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart",
        foreignKeys = @ForeignKey(entity = Item.class,
                parentColumns = "itemId",
                childColumns = "itemId",
                onDelete = ForeignKey.CASCADE))
public class CartItem {

    @PrimaryKey(autoGenerate = true)
    public int cartId;
    @ColumnInfo(name = "itemId", index = true)
    public int itemId;
    public int quantity;
}
