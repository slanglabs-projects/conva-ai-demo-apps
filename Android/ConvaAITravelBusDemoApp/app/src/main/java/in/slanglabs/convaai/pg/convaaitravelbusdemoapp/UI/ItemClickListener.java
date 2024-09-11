package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.UI;


import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.JourneyBusPlace;

public interface ItemClickListener {
    default void itemClicked(int position) {
    }

    default void itemClicked(JourneyBusPlace journeyBusPlace) {
    }
}