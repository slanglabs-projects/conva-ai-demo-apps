package in.slanglabs.convaai.demo.grocery.Model;

import androidx.room.Entity;
import androidx.room.Fts4;

@Fts4(contentEntity = Item.class)
@Entity(tableName = "itemsFts")
public class ItemsFts {
    public int itemId;
    public String name;
    public String brand;
    public String synonyms;
    public String size;
    public String gender;
    public String category;
    public String color;
}
