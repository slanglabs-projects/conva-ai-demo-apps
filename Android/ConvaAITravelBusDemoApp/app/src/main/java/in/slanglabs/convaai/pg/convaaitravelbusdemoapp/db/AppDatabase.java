package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.db;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.AppExecutors;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.Bus;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.BusAttributes;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.Journey;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.OrderItem;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.Place;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.TimeJsonAdapter;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.db.Convertors.DateConverter;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.db.Convertors.PlaceConvertor;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.db.dao.BusAttributeDao;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.db.dao.BusDao;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.db.dao.JournayDao;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.db.dao.OrderDao;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.db.dao.PlaceDao;


@Database(entities = {OrderItem.class, Journey.class, Bus.class, Place.class, BusAttributes.class}, version = 2)
@TypeConverters({DateConverter.class, PlaceConvertor.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase sInstance;

    @VisibleForTesting
    public static final String DATABASE_NAME = "travel-bug-demo-app-db";

    public abstract OrderDao orderDao();

    public abstract JournayDao journayDao();

    public abstract BusDao busDao();

    public abstract PlaceDao placeDao();

    public abstract BusAttributeDao busAttributeDao();

    public static AppDatabase getInstance(final Context context, final AppExecutors executors) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context.getApplicationContext(), executors);
                }
            }
        }
        return sInstance;
    }

    /**
     * Build the database. {@link Builder#build()} only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private static AppDatabase buildDatabase(final Context appContext, AppExecutors executors) {
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        AppDatabase database = AppDatabase.getInstance(appContext, executors);
                        insertData(appContext, database, executors);
                    }

                    @Override
                    public void onDestructiveMigration(@NonNull SupportSQLiteDatabase db) {
                        super.onDestructiveMigration(db);
                        AppDatabase database = AppDatabase.getInstance(appContext, executors);
                        insertData(appContext, database, executors);
                    }
                })
                .build();
    }

    private static void insertData(final Context context, final AppDatabase database, AppExecutors appExecutors) {
        appExecutors.diskIO().execute(() -> {


            String jsonBus = loadJSONFromAsset(context, "list-bus.json");
            Moshi moshiBus = new Moshi.Builder().build();
            Type typeBus = Types.newParameterizedType(List.class, Bus.class);
            JsonAdapter<List<Bus>> adapterBus = moshiBus.adapter(typeBus);
            List<Bus> listItemsBus;
            try {
                if (jsonBus != null) {
                    listItemsBus = adapterBus.fromJson(jsonBus);
                    List<Bus> finalListItems = listItemsBus;
                    appExecutors.diskIO().execute(() -> {
                        database.runInTransaction(() -> {
                            database.busDao().insert(listItemsBus);
                        });
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            String jsonBusAttributes = loadJSONFromAsset(context, "list-bus-attributes.json");
            Moshi moshiBusAttributes = new Moshi.Builder().build();
            Type typeBusAttributes = Types.newParameterizedType(List.class, BusAttributes.class);
            JsonAdapter<List<BusAttributes>> adapterBusAttributes = moshiBusAttributes.adapter(typeBusAttributes);
            List<BusAttributes> busAttributes;
            try {
                if (jsonBusAttributes != null) {
                    busAttributes = adapterBusAttributes.fromJson(jsonBusAttributes);
                    appExecutors.diskIO().execute(() -> {
                        database.runInTransaction(() -> {
                            database.busAttributeDao().insert(busAttributes);
                        });
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            String jsonPlace = loadJSONFromAsset(context, "list-place.json");
            Moshi moshiPlace = new Moshi.Builder().build();
            Type typePlace = Types.newParameterizedType(List.class, Place.class);
            JsonAdapter<List<Place>> adapterPlace = moshiPlace.adapter(typePlace);
            List<Place> listItemsPlace;
            try {
                if (jsonPlace != null) {
                    listItemsPlace = adapterPlace.fromJson(jsonPlace);
                    appExecutors.diskIO().execute(() -> {
                        database.runInTransaction(() -> {
                            database.placeDao().insert(listItemsPlace);
                        });
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            //Parsing the json file and adding the items to items table.
            String json = loadJSONFromAsset(context, "list.json");
            Moshi moshi = new Moshi.Builder()
                    .add(Date.class, new TimeJsonAdapter())
                    .build();
            Type type = Types.newParameterizedType(List.class, Journey.class);
            JsonAdapter<List<Journey>> adapter = moshi.adapter(type);
            List<Journey> listItems;
            try {
                if (json != null) {
                    listItems = adapter.fromJson(json);
                    List<Journey> finalListItems = listItems;
                    appExecutors.diskIO().execute(() -> {
                        database.runInTransaction(() -> {
                            database.journayDao().insert(finalListItems);
                        });
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    private static String loadJSONFromAsset(Context context, String fileName) {
        String json;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}