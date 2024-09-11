package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.db.Convertors;


import androidx.room.TypeConverter;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter;

import java.io.IOException;
import java.util.Date;

import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.Place;

public class PlaceConvertor {

    @TypeConverter
    public static Place toPlace(String jsonString) {
        if (jsonString == null) {
            return null;
        }
        Moshi moshi = new Moshi.Builder()
                .add(Date.class, new Rfc3339DateJsonAdapter())
                .build();
        JsonAdapter<Place> jsonAdapter = moshi.adapter(Place.class);
        try {
            return jsonAdapter.fromJson(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @TypeConverter
    public static String fromPlace(Place place) {
        Moshi moshi = new Moshi.Builder()
                .add(Date.class, new Rfc3339DateJsonAdapter())
                .build();
        JsonAdapter<Place> jsonAdapter = moshi.adapter(Place.class);
        return jsonAdapter.toJson(place);
    }

}
