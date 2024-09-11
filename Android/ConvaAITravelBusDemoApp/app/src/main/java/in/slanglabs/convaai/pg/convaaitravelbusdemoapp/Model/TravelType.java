package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({TravelType.AC, TravelType.NON_AC})
@Retention(RetentionPolicy.SOURCE)
public @interface TravelType {
    String AC = "A/C";
    String NON_AC = "Non A/C";
}