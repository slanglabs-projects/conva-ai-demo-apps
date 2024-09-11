package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({RouteStatus.CANCELED, RouteStatus.DELAYED})
@Retention(RetentionPolicy.SOURCE)
public @interface RouteStatus {
    int CANCELED = 1;
    int DELAYED = 2;
}