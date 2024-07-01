package in.slanglabs.convaai.demo.grocery.Model;

import androidx.annotation.ColorInt;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "offers",
        foreignKeys = @ForeignKey(entity = Item.class,
                parentColumns = "itemId",
                childColumns = "itemId",
                onDelete = ForeignKey.CASCADE))
public class Offer {

    @PrimaryKey(autoGenerate = true)
    public int offerId;
    public String offerName;
    @ColumnInfo(name = "itemId", index = true)
    public int itemId;
    public int minQuantity;
    public float percentageDiscount;
    public @ColorInt
    int color;

    @Override
    public String toString() {
        return "Offer{" +
                "offerId=" + offerId +
                ", offerName='" + offerName + '\'' +
                ", itemId=" + itemId +
                ", minQuantity=" + minQuantity +
                ", percentageDiscount=" + percentageDiscount +
                ", color=" + color +
                '}';
    }
}
