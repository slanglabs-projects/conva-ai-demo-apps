package in.slanglabs.convaai.demo.grocery.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class PriceRange implements Parcelable {

    public int getStartPrice() {
        return startPrice;
    }

    public int getStopPrice() {
        return stopPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceRange priceRange = (PriceRange) o;
        return startPrice == priceRange.startPrice && stopPrice == priceRange.stopPrice;
    }

    private final int startPrice;
    private final int stopPrice;

    public PriceRange(int startPrice, int stopPrice) {
        this.startPrice = startPrice;
        this.stopPrice = stopPrice;
    }

    PriceRange(Parcel in) {
        startPrice = in.readInt();
        stopPrice = in.readInt();
    }

    public static final Creator<PriceRange> CREATOR = new Creator<PriceRange>() {
        @Override
        public PriceRange createFromParcel(Parcel in) {
            return new PriceRange(in);
        }

        @Override
        public PriceRange[] newArray(int size) {
            return new PriceRange[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(startPrice);
        dest.writeInt(stopPrice);
    }
}
