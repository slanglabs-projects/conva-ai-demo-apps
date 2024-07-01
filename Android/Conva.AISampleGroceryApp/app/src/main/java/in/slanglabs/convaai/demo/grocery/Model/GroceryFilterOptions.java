package in.slanglabs.convaai.demo.grocery.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class GroceryFilterOptions implements Parcelable {

    private static final int HAS_DATA = 0x01;
    private static final int WITHOUT_DATA = 0x00;

    private List<String> brands;
    private List<PriceRange> priceRanges;
    private List<String> sizes;

    public List<String> getBrands() {
        return brands;
    }

    public void setBrands(List<String> brands) {
        this.brands = brands;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public void setSizes(List<String> sizes) {
        this.sizes = sizes;
    }

    public List<PriceRange> getPriceRanges() {
        return priceRanges;
    }

    public void setPriceRanges(List<PriceRange> priceRanges) {
        this.priceRanges = priceRanges;
    }


    public GroceryFilterOptions() {
        brands = new ArrayList<>();
        priceRanges = new ArrayList<>();
        sizes = new ArrayList<>();
    }

    public void clear() {
        brands.clear();
        priceRanges.clear();
        sizes.clear();
    }

    protected GroceryFilterOptions(Parcel in) {
        if (in.readByte() == HAS_DATA) {
            brands = new ArrayList<>();
            in.readList(brands, String.class.getClassLoader());
        } else {
            brands = null;
        }
        if (in.readByte() == HAS_DATA) {
            sizes = new ArrayList<>();
            in.readList(sizes, String.class.getClassLoader());
        } else {
            sizes = null;
        }
        if (in.readByte() == HAS_DATA) {
            priceRanges = new ArrayList<>();
            in.readList(priceRanges, PriceRange.class.getClassLoader());
        } else {
            priceRanges = null;
        }
    }

    public static final Creator<FilterOptions> CREATOR = new Creator<FilterOptions>() {
        @Override
        public FilterOptions createFromParcel(Parcel in) {
            return new FilterOptions(in);
        }

        @Override
        public FilterOptions[] newArray(int size) {
            return new FilterOptions[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (brands == null) {
            parcel.writeByte((byte) (WITHOUT_DATA));
        } else {
            parcel.writeByte((byte) (HAS_DATA));
            parcel.writeList(brands);
        }
        if (sizes == null) {
            parcel.writeByte((byte) (WITHOUT_DATA));
        } else {
            parcel.writeByte((byte) (HAS_DATA));
            parcel.writeList(sizes);
        }
        if (priceRanges == null) {
            parcel.writeByte((byte) (WITHOUT_DATA));
        } else {
            parcel.writeByte((byte) (HAS_DATA));
            parcel.writeList(priceRanges);
        }
    }
}
