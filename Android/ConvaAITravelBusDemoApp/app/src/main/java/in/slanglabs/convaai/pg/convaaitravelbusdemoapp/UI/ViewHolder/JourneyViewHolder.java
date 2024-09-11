package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.UI.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Locale;

//import in.slanglabs.platform.ui.SlangVoiceAssist;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.BusAttributes;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.JourneyBusPlace;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.R;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.UI.ItemClickListener;

public class JourneyViewHolder extends RecyclerView.ViewHolder {

    private TextView journeyTimeInformation;
    private TextView busName;
    private TextView busType;
    private TextView journeyPrice;
    private TextView journeyDuration;
    private TextView busStarRating;
    private JourneyBusPlace journeyBusPlace;

    public JourneyViewHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
        super(itemView);
        journeyTimeInformation = itemView.findViewById(R.id.journey_time_information);
        busName = itemView.findViewById(R.id.bus_name);
        busType = itemView.findViewById(R.id.bus_type);
        journeyPrice = itemView.findViewById(R.id.journey_price);
        journeyDuration = itemView.findViewById(R.id.journey_duration);
        busStarRating = itemView.findViewById(R.id.bus_star_rating);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.itemClicked(journeyBusPlace);
            }
        });
    }

    public void setData(JourneyBusPlace journeyBusPlace) {
        this.journeyBusPlace = journeyBusPlace;
        SimpleDateFormat  dateformatFinal = new SimpleDateFormat("hh:mm aa",Locale.ENGLISH);
        String strDate = dateformatFinal.format(journeyBusPlace.journey.startTime);
        String endDate = dateformatFinal.format(journeyBusPlace.journey.endTime);
        String journeyTimeInfo = strDate + " - " + endDate;
        long a = journeyBusPlace.journey.startTime.getTime();
        journeyTimeInformation.setText(journeyTimeInfo);
        busName.setText(String.format(Locale.ENGLISH,"%s, %s",journeyBusPlace.bus.name,journeyBusPlace.bus.travels));
        busType.setText(journeyBusPlace.bus.type);
        for(BusAttributes attributes:journeyBusPlace.busAttributesList) {
            busType.setText(String.format(Locale.ENGLISH,"%s %s",busType.getText().toString(),attributes.travelClass));
        }
        journeyPrice.setText(String.format(Locale.ENGLISH,"Rs %d", journeyBusPlace.journey.price));
        journeyDuration.setText(String.format(Locale.ENGLISH,"%02d hours %02d min",
                (journeyBusPlace.journey.duration / 3600),
                (journeyBusPlace.journey.duration % 3600) / 60));
        busStarRating.setText(String.format(Locale.ENGLISH,"%.2f",journeyBusPlace.bus.starRating));
    }
}
