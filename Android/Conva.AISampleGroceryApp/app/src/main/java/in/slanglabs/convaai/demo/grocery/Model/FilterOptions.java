package in.slanglabs.convaai.demo.grocery.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class FilterOptions implements Parcelable {

    private static final int HAS_DATA = 0x01;
    private static final int WITHOUT_DATA = 0x00;

    private List<String> variants;
    private List<String> units;
    private List<String> colors;
    private List<String> brands;
    private List<String> categories;
    private List<String> genders;
    private List<PriceRange> priceRanges;
    private List<String> sizes;
    private @OrderBy int orderBy;

    public List<String> getVariants() {
        return variants;
    }

    public void setVariants(List<String> variants) {
        this.variants = variants;
    }

    public List<String> getUnits() {
        return units;
    }

    public void setUnits(List<String> units) {
        this.units = units;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

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

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getGenders() {
        return genders;
    }

    public void setGenders(List<String> genders) {
        this.genders = genders;
    }

    public int getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }

    public FilterOptions() {
        variants = new ArrayList<>();
        units = new ArrayList<>();
        colors = new ArrayList<>();
        brands = new ArrayList<>();
        priceRanges = new ArrayList<>();
        sizes = new ArrayList<>();
        categories = new ArrayList<>();
        genders = new ArrayList<>();
        orderBy = OrderBy.NONE;
    }

    public void clear() {
        variants.clear();
        units.clear();
        colors.clear();
        categories.clear();
        genders.clear();

        brands.clear();
        priceRanges.clear();
        sizes.clear();

        orderBy = OrderBy.NONE;
    }

    protected FilterOptions(Parcel in) {
        if (in.readByte() == HAS_DATA) {
            variants = new ArrayList<>();
            in.readList(variants, String.class.getClassLoader());
        } else {
            variants = null;
        }
        if (in.readByte() == HAS_DATA) {
            units = new ArrayList<>();
            in.readList(units, String.class.getClassLoader());
        } else {
            units = null;
        }
        if (in.readByte() == HAS_DATA) {
            colors = new ArrayList<>();
            in.readList(colors, String.class.getClassLoader());
        } else {
            colors = null;
        }
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
            categories = new ArrayList<>();
            in.readList(sizes, String.class.getClassLoader());
        } else {
            categories = null;
        }
        if (in.readByte() == HAS_DATA) {
            genders = new ArrayList<>();
            in.readList(sizes, String.class.getClassLoader());
        } else {
            genders = null;
        }
        if (in.readByte() == HAS_DATA) {
            priceRanges = new ArrayList<>();
            in.readList(priceRanges, PriceRange.class.getClassLoader());
        } else {
            priceRanges = null;
        }
        orderBy = in.readInt();
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
        if (variants == null) {
            parcel.writeByte((byte) (WITHOUT_DATA));
        } else {
            parcel.writeByte((byte) (HAS_DATA));
            parcel.writeList(variants);
        }
        if (units == null) {
            parcel.writeByte((byte) (WITHOUT_DATA));
        } else {
            parcel.writeByte((byte) (HAS_DATA));
            parcel.writeList(units);
        }
        if (colors == null) {
            parcel.writeByte((byte) (WITHOUT_DATA));
        } else {
            parcel.writeByte((byte) (HAS_DATA));
            parcel.writeList(colors);
        }

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
        if (genders == null) {
            parcel.writeByte((byte) (WITHOUT_DATA));
        } else {
            parcel.writeByte((byte) (HAS_DATA));
            parcel.writeList(genders);
        }
        if (categories == null) {
            parcel.writeByte((byte) (WITHOUT_DATA));
        } else {
            parcel.writeByte((byte) (HAS_DATA));
            parcel.writeList(categories);
        }
        if (priceRanges == null) {
            parcel.writeByte((byte) (WITHOUT_DATA));
        } else {
            parcel.writeByte((byte) (HAS_DATA));
            parcel.writeList(priceRanges);
        }
        parcel.writeInt(orderBy);
    }
}