package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.UI.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Locale;

import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.R;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.UI.ViewModel.AppViewModel;

public class BaseDetailsActivity extends AppCompatActivity {

    AppViewModel appViewModel;
    final SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy, hh:mm a", Locale.ENGLISH);
    TextView orderTitle;
    TextView journeyTime;
    TextView journeyStartLocation;
    TextView journeyEndLocation;
    TextView journeyTotalDuration;
    TextView journeyBusTravelsName;
    TextView journeyBusAttributes;
    TextView orderJourneyStatus;
    TextView orderCancelView;
    TextView orderStatusTitleText;
    View orderStatusView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticket_view);

        ImageView backButtonView = findViewById(R.id.back_button);
        backButtonView.setOnClickListener(view -> finish());

        orderTitle = findViewById(R.id.order_title);
        journeyTime = findViewById(R.id.order_journey_date);
        journeyStartLocation = findViewById(R.id.order_journey_start_location);
        journeyEndLocation = findViewById(R.id.order_journey_end_location);
        journeyTotalDuration = findViewById(R.id.order_journey_total_duration);
        journeyBusTravelsName = findViewById(R.id.order_journey_travels_name);
        journeyBusAttributes = findViewById(R.id.order_journey_bus_type);
        orderJourneyStatus = findViewById(R.id.order_journey_status);
        orderCancelView = findViewById(R.id.order_cancel_button);
        orderStatusView = findViewById(R.id.order_status_view);
        orderStatusTitleText = findViewById(R.id.order_status_title_text);
        appViewModel = new ViewModelProvider(this).get(
                AppViewModel.class);
    }
}
