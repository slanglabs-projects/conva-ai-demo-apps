package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class BusFilterSortOptions implements Parcelable {

    private static final int HAS_DATA = 0x01;
    private static final int WITHOUT_DATA = 0x00;

    private List<String> busType;
    private List<String> busFilters;
    private List<String> busOperators;
    private List<TimeRange> busDepartureTimeRange;
    private List<TimeRange> busArrivalTimeRange;
    private @OrderBy int busOrderBy;

    public List<String> getBusType() {
        return busType;
    }

    public void setBusType(List<String> busType) {
        this.busType = busType;
    }

    public List<String> getBusFilters() {
        return busFilters;
    }

    public void setBusFilters(List<String> busFilters) {
        this.busFilters = busFilters;
    }

    public List<String> getBusOperators() {
        return busOperators;
    }

    public void setBusOperators(List<String> busOperators) {
        this.busOperators = busOperators;
    }

    public int getBusOrderBy() {
        return busOrderBy;
    }

    public void setBusOrderBy(int busOrderBy) {
        this.busOrderBy = busOrderBy;
    }

    public List<TimeRange> getBusDepartureTimeRange() {
        return busDepartureTimeRange;
    }

    public void setBusDepartureTimeRange(List<TimeRange> busDepartureTimeRange) {
        this.busDepartureTimeRange = busDepartureTimeRange;
    }

    public List<TimeRange> getBusArrivalTimeRange() {
        return busArrivalTimeRange;
    }

    public void setBusArrivalTimeRange(List<TimeRange> busArrivalTimeRange) {
        this.busArrivalTimeRange = busArrivalTimeRange;
    }

    public BusFilterSortOptions() {
        busType = new ArrayList<>();
        busFilters = new ArrayList<>();
        busOperators = new ArrayList<>();
        busDepartureTimeRange = new ArrayList<>();
        busArrivalTimeRange = new ArrayList<>();
        busOrderBy = OrderBy.RELEVANCE;
    }

    public void clear() {
        busType.clear();
        busFilters.clear();
        busOperators.clear();
        busDepartureTimeRange.clear();
        busArrivalTimeRange.clear();
    }

    protected BusFilterSortOptions(Parcel in) {
        if (in.readByte() == HAS_DATA) {
            busType = new ArrayList<>();
            in.readList(busType, String.class.getClassLoader());
        } else {
            busType = null;
        }
        if (in.readByte() == HAS_DATA) {
            busFilters = new ArrayList<>();
            in.readList(busFilters, String.class.getClassLoader());
        } else {
            busFilters = null;
        }
        if (in.readByte() == HAS_DATA) {
            busOperators = new ArrayList<>();
            in.readList(busOperators, String.class.getClassLoader());
        } else {
            busOperators = null;
        }
        if (in.readByte() == HAS_DATA) {
            busDepartureTimeRange = new ArrayList<>();
            in.readList(busDepartureTimeRange, String.class.getClassLoader());
        } else {
            busDepartureTimeRange = null;
        }
        if (in.readByte() == HAS_DATA) {
            busArrivalTimeRange = new ArrayList<>();
            in.readList(busArrivalTimeRange, String.class.getClassLoader());
        } else {
            busArrivalTimeRange = null;
        }
    }

    public static final Creator<BusFilterSortOptions> CREATOR = new Creator<BusFilterSortOptions>() {
        @Override
        public BusFilterSortOptions createFromParcel(Parcel in) {
            return new BusFilterSortOptions(in);
        }

        @Override
        public BusFilterSortOptions[] newArray(int size) {
            return new BusFilterSortOptions[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (busType == null) {
            parcel.writeByte((byte) (WITHOUT_DATA));
        } else {
            parcel.writeByte((byte) (HAS_DATA));
            parcel.writeList(busType);
        }
        if (busFilters == null) {
            parcel.writeByte((byte) (WITHOUT_DATA));
        } else {
            parcel.writeByte((byte) (HAS_DATA));
            parcel.writeList(busFilters);
        }
        if (busOperators == null) {
            parcel.writeByte((byte) (WITHOUT_DATA));
        } else {
            parcel.writeByte((byte) (HAS_DATA));
            parcel.writeList(busOperators);
        }
        if (busDepartureTimeRange == null) {
            parcel.writeByte((byte) (WITHOUT_DATA));
        } else {
            parcel.writeByte((byte) (HAS_DATA));
            parcel.writeList(busDepartureTimeRange);
        }
        if (busArrivalTimeRange == null) {
            parcel.writeByte((byte) (WITHOUT_DATA));
        } else {
            parcel.writeByte((byte) (HAS_DATA));
            parcel.writeList(busArrivalTimeRange);
        }
    }
}
