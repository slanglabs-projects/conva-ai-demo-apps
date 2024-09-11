package in.slanglabs.convaai.pg.convaaitravelbusdemoapp;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Formats dates using <a href="https://www.ietf.org/rfc/rfc3339.txt">RFC 3339</a>, which is
 * formatted like {@code 2015-09-26T18:23:50.250Z}. This adapter is null-safe. To use, add this as
 * an adapter for {@code Date.class} on your {@link com.squareup.moshi.Moshi.Builder Moshi.Builder}:
 *
 * <pre> {@code
 *
 *   Moshi moshi = new Moshi.Builder()
 *       .add(Date.class, new Rfc3339DateJsonAdapter())
 *       .build();
 * }</pre>
 */
public final class TimeJsonAdapter extends JsonAdapter<Date> {
    @Override
    public synchronized Date fromJson(JsonReader reader) throws IOException {
        if (reader.peek() == JsonReader.Token.NULL) {
            return reader.nextNull();
        }
        String string = reader.nextString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
        try {
            return dateFormat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public synchronized void toJson(JsonWriter writer, Date value) throws IOException {
        if (value == null) {
            writer.nullValue();
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
            String string = dateFormat.format(value);
            writer.value(string);
        }
    }
}
