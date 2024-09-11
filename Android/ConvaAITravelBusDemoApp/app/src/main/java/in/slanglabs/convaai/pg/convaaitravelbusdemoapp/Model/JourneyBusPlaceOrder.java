package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model;

import androidx.room.Embedded;
import androidx.room.Relation;

public class JourneyBusPlaceOrder {
    @Embedded
    public OrderItem order;
    @Relation(
            parentColumn = "journeyId",
            entityColumn = "journeyId",
            entity = Journey.class
    )
    public JourneyBusPlace journey;
}
