package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@IntDef({OrderBy.RELEVANCE, OrderBy.RATING, OrderBy.PRICE, OrderBy.DEPARTURE_TIME, OrderBy.TRAVEL_DURATION})
@Retention(RetentionPolicy.SOURCE)
public @interface OrderBy {
    int RELEVANCE = 0;
    int RATING = 1;
    int PRICE = 2;
    int DEPARTURE_TIME = 3;
    int TRAVEL_DURATION = 4;
}

