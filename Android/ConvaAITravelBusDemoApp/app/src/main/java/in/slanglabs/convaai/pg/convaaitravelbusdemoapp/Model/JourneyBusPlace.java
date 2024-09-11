package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.List;

public class JourneyBusPlace implements Serializable {
    @Embedded
    public Journey journey;
    @Relation(
            parentColumn = "busId",
            entityColumn = "id",
            entity = Bus.class
    )
    public Bus bus;
    @Relation(
            parentColumn = "busId",
            entityColumn = "busId"
    )
    public List<BusAttributes> busAttributesList;
    @Relation(
            parentColumn = "startLocation",
            entityColumn = "id",
            entity = Place.class
    )
    public Place sourcePlace;
    @Relation(
            parentColumn = "endLocation",
            entityColumn = "id",
            entity = Place.class
    )
    public Place destinationPlace;
}
