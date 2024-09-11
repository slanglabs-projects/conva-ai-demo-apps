package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({OrderStatus.CANCELED, OrderStatus.ON_SCHEDULE})
@Retention(RetentionPolicy.SOURCE)
public @interface OrderStatus {
    int CANCELED = 0;
    int ON_SCHEDULE = 1;
}