package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.UI.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import java.util.Locale;

import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.BusAttributes;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.RouteStatus;

public class TicketViewActivity extends BaseDetailsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String orderId = getIntent().getStringExtra("orderItemId");
        appViewModel.getOrderItem(orderId).observe(this, orderItem -> {
            orderCancelView.setOnClickListener(view -> {
                appViewModel.removeOrderItem(orderItem.order);
                finish();
            });
            orderTitle.setText(String.format(Locale.ENGLISH,"Ticket #%s",orderItem.order.orderId.substring(0,10)));
            String s1 = sdf.format(orderItem.order.journeyDate);
            journeyTime.setText(s1);
            journeyStartLocation.setText(orderItem.order.sourcePlace.city+", "+orderItem.order.sourcePlace.stop+" ("+orderItem.order.sourcePlace.state+")");
            journeyEndLocation.setText(orderItem.order.destinationPlace.city+", "+orderItem.order.destinationPlace.stop+" ("+orderItem.order.destinationPlace.state+")");
            journeyTotalDuration.setText(String.format(Locale.ENGLISH, "%02d hours %02d min",
                    (orderItem.journey.journey.duration / 3600),
                    (orderItem.journey.journey.duration % 3600) / 60));
            journeyBusTravelsName.setText(String.format(Locale.ENGLISH, "%s, %s",
                    orderItem.journey.bus.name, orderItem.journey.bus.travels));
            journeyBusAttributes.setText(orderItem.journey.bus.type);
            for(BusAttributes attributes:orderItem.journey.busAttributesList) {
                journeyBusAttributes.setText(String.format(Locale.ENGLISH,"%s %s",
                        journeyBusAttributes.getText().toString(),attributes.travelClass));
            }
            switch (orderItem.journey.journey.routeStatus) {
                case RouteStatus
                        .DELAYED:
                    orderJourneyStatus.setVisibility(View.VISIBLE);
                    orderJourneyStatus.setText("Delayed");
                    orderJourneyStatus.setBackgroundColor(Color.BLACK);
                    orderJourneyStatus.setTextColor(Color.WHITE);
                    break;
                case RouteStatus
                        .CANCELED:
                    orderJourneyStatus.setVisibility(View.VISIBLE);
                    orderJourneyStatus.setText("Cancelled By Operator");
                    orderJourneyStatus.setBackgroundColor(Color.RED);
                    orderJourneyStatus.setTextColor(Color.WHITE);
                    break;
                default:
                    orderJourneyStatus.setVisibility(View.VISIBLE);
                    orderJourneyStatus.setText("On Schedule");
                    orderJourneyStatus.setBackgroundColor(Color.GREEN);
                    orderJourneyStatus.setTextColor(Color.BLACK);
                    break;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}