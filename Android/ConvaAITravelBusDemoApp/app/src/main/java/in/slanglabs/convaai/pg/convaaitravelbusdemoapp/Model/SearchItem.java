package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model;

import java.io.Serializable;
import java.util.Date;

public class SearchItem implements Serializable {
    public Place sourcePlace;
    public Place destinationPlace;
    public Date travelDate;
    public @TravelClass
    String travelClass;
    public @TravelType
    String travelType;

    @Override
    public String toString() {
        return "SearchItem{" +
                "sourcePlace=" + sourcePlace +
                ", destinationPlace=" + destinationPlace +
                ", travelDate=" + travelDate +
                ", travelClass='" + travelClass + '\'' +
                ", travelType='" + travelType + '\'' +
                '}';
    }
}
