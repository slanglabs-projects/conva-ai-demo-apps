package in.slanglabs.convaai.demo.grocery.Model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class CheckoutInfoModel implements Serializable {
    public @NonNull String mName = "";
    public @NonNull String mPhoneNumber = "";
    public @Nullable Date mDeliveryDate;

    public CheckoutInfoModel(@Nullable String name, @Nullable String phoneNumber, @Nullable Date deliveryDate) {
        mName = name != null ? name : "";
        mPhoneNumber = phoneNumber != null ? phoneNumber : "";
        mDeliveryDate = deliveryDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckoutInfoModel that = (CheckoutInfoModel) o;
        return mName.equalsIgnoreCase(that.mName)
                && mPhoneNumber.equalsIgnoreCase(that.mPhoneNumber)
                && (mDeliveryDate != null && mDeliveryDate.equals(that.mDeliveryDate));
    }

    @Override
    public int hashCode() {
        return Objects.hash(mName, mPhoneNumber, mDeliveryDate);
    }

    @Override
    public String toString() {
        return "CheckoutInfo{" +
                "name='" + mName + '\'' +
                ", phoneNumber='" + mPhoneNumber + '\'' +
                ", deliveryDate='" + mDeliveryDate +
                '}';
    }
}