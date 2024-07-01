package in.slanglabs.convaai.demo.grocery.Model;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({OrderBy.RELEVANCE, OrderBy.HIGH_LOW_PRICE, OrderBy.LOW_HIGH_PRICE, OrderBy.NONE})
@Retention(RetentionPolicy.SOURCE)
public @interface OrderBy {
    int NONE = 0;
    int RELEVANCE = 1;
    int HIGH_LOW_PRICE = 2;
    int LOW_HIGH_PRICE = 3;
}
