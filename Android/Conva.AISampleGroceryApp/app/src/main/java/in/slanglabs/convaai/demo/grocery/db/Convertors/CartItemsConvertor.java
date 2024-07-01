package in.slanglabs.convaai.demo.grocery.db.Convertors;

import androidx.room.TypeConverter;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import in.slanglabs.convaai.demo.grocery.Model.CartItemOffer;

public class CartItemsConvertor {

    @TypeConverter
    public static List<CartItemOffer> toCartItemsList(String jsonString) {
        if (jsonString == null) {
            return null;
        }
        Moshi moshi = new Moshi.Builder().build();
        Type listMyData = Types.newParameterizedType(List.class, CartItemOffer.class);
        JsonAdapter<List<CartItemOffer>> jsonAdapter = moshi.adapter(listMyData);
        List<CartItemOffer> cartItems = null;
        try {
            cartItems = jsonAdapter.fromJson(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cartItems;
    }

    @TypeConverter
    public static String fromCartItemsList(List<CartItemOffer> cartItems) {
        Moshi moshi = new Moshi.Builder().build();
        Type listMyData = Types.newParameterizedType(List.class, CartItemOffer.class);
        JsonAdapter<List<CartItemOffer>> jsonAdapter = moshi.adapter(listMyData);
        return jsonAdapter.toJson(cartItems);
    }

}
