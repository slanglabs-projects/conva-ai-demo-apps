package in.slanglabs.convaai.demo.grocery.Model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONArray;
import org.json.JSONObject;

@Entity(tableName = "items")
public class Item {
    private static final String TAG = "Item";

    @PrimaryKey(autoGenerate = true)
    public int itemId;
    @NonNull
    public @ListType String type;
    @NonNull
    public int id;
    public String name;
    public String synonyms;
    public String brand;
    public String productType;
    public Float price;
    public String size;
    public int quantity;
    public String imageUrl;
    public String gender;
    public String category;
    public String color;
    public int confidence;

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("Id", id);
            if (null != name) obj.put("Name", name);
            if (null != synonyms) obj.put("Synonyms", synonyms);
            if (null != brand) obj.put("Brand", brand);
            if (null != productType) obj.put("ProductType", productType);
            if (null != price) obj.put("Price", price);
            if (null != size) obj.put("Size", size);
            obj.put("Quantity", quantity);
            if (null != imageUrl) obj.put("ImageUrl", imageUrl);
            if (null != gender) obj.put("Gender", gender);
            if (null != category) obj.put("Category", category);
            if (null != color) obj.put("Color", color);
            obj.put("Confidence", confidence);
        } catch (Exception ex) {
            Log.e(TAG, "Error while converting Item[" + toString() + "] to JSON:" + ex.toString());
        }

        return obj;
    }
}
