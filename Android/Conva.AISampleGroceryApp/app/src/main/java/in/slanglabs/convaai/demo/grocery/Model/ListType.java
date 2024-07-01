package in.slanglabs.convaai.demo.grocery.Model;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({ListType.GROCERY, ListType.PHARMACY, ListType.FASHION})
@Retention(RetentionPolicy.SOURCE)
public @interface ListType {
    String GROCERY = "grocery";
    String PHARMACY = "pharmacy";
    String FASHION = "fashion";
}
