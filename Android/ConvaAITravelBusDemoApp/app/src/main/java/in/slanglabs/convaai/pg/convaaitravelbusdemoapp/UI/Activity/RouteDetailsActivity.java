package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.UI.Activity;

import android.os.Bundle;

import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.BusAttributes;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.JourneyBusPlace;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.OrderItem;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.OrderStatus;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.R;

public class RouteDetailsActivity extends BaseDetailsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long journeyId = getIntent().getLongExtra("journeyId", 0);
        long dateInt = getIntent().getLongExtra("dateInt", 0);
        JourneyBusPlace journeyBusPlace = (JourneyBusPlace) getIntent().getSerializableExtra("journey");
        Date journeyDate = new Date(dateInt);
        orderCancelView.setText("BOOK TICKET");
        orderCancelView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

//        appViewModel.getJourneyItem(journeyId).observe(this, journeyBusPlace -> {
            orderCancelView.setOnClickListener(view -> {
                OrderItem orderItem = new OrderItem();
                orderItem.orderId = UUID.randomUUID().toString();
                orderItem.active = OrderStatus.ON_SCHEDULE;
                orderItem.orderTime = new Date();
                orderItem.journeyDate = journeyDate;
                orderItem.journeyId = journeyBusPlace.journey.journeyId;
                orderItem.sourcePlace = journeyBusPlace.sourcePlace;
                orderItem.destinationPlace = journeyBusPlace.destinationPlace;
                appViewModel.addOrderItem(orderItem);
                finish();
            });
            orderTitle.setText("Route Details");
            String s1 = sdf.format(journeyDate);
            journeyTime.setText(s1);
            journeyStartLocation.setText(journeyBusPlace.sourcePlace.city+", "+journeyBusPlace.sourcePlace.stop+" ("+journeyBusPlace.sourcePlace.state+")");
            journeyEndLocation.setText(journeyBusPlace.destinationPlace.city+", "+journeyBusPlace.destinationPlace.stop+" ("+journeyBusPlace.destinationPlace.state+")");
            journeyTotalDuration.setText(String.format(Locale.ENGLISH, "%02d hours %02d min",
                    (journeyBusPlace.journey.duration / 3600),
                    (journeyBusPlace.journey.duration % 3600) / 60));
            journeyBusTravelsName.setText(String.format(Locale.ENGLISH, "%s, %s",
                    journeyBusPlace.bus.name, journeyBusPlace.bus.travels));
            journeyBusAttributes.setText(journeyBusPlace.bus.type);
            for (BusAttributes attributes : journeyBusPlace.busAttributesList) {
                journeyBusAttributes.setText(String.format(Locale.ENGLISH, "%s %s",
                        journeyBusAttributes.getText().toString(), attributes.travelClass));
            }
            orderStatusTitleText.setText("Price");
            orderJourneyStatus.setText(String.format(Locale.ENGLISH,"Rs. %d",journeyBusPlace.journey.price));
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}