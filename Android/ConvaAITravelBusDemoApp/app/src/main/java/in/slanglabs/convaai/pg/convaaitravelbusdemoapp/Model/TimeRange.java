package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class TimeRange implements Parcelable {

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeRange timeRange = (TimeRange) o;
        return startTime == timeRange.startTime && endTime == timeRange.endTime;
    }

    private final int startTime;
    private final int endTime;

    public TimeRange(int startTime, int endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    TimeRange(Parcel in) {
        startTime = in.readInt();
        endTime = in.readInt();
    }

    public static final Creator<TimeRange> CREATOR = new Creator<TimeRange>() {
        @Override
        public TimeRange createFromParcel(Parcel in) {
            return new TimeRange(in);
        }

        @Override
        public TimeRange[] newArray(int size) {
            return new TimeRange[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(startTime);
        dest.writeInt(endTime);
    }
}
