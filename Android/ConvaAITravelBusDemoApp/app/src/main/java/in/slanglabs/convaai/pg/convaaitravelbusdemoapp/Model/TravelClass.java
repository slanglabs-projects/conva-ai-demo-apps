package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({TravelClass.SLEEPER, TravelClass.SEATER, TravelClass.MULTI_AXEL, TravelClass.NON_STOP})
@Retention(RetentionPolicy.SOURCE)
public @interface TravelClass {
    String SLEEPER = "Sleeper";
    String SEATER = "Seater";
    String MULTI_AXEL = "Multi-Axel";
    String NON_STOP = "Non-Stop";
}