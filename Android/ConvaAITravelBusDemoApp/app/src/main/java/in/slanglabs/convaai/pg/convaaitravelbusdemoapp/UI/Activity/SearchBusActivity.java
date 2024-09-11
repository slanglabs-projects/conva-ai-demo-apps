package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.UI.Activity;

import static in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.OrderBy.DEPARTURE_TIME;
import static in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.OrderBy.PRICE;
import static in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.OrderBy.RATING;
import static in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.OrderBy.RELEVANCE;
import static in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.OrderBy.TRAVEL_DURATION;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.JourneyBusPlace;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.Place;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.R;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.UI.Adapters.JourneyListAdapter;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.UI.Fragment.FilterDialogFragment;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.UI.ItemClickListener;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.UI.ViewModel.AppViewModel;

public class SearchBusActivity extends AppCompatActivity {

    private AppViewModel appViewModel;
    private JourneyListAdapter listAdapter;
    private Date startDate;
    private Place startLocation;
    private Place endLocation;
    TextView sourceTextField;
    TextView destionationTextField;
    TextView dateTextField;
    long dateInt;
    boolean isVoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search_bus);

        ImageView backButtonView = findViewById(R.id.back_button);
        backButtonView.setOnClickListener(view -> finish());

        sourceTextField = findViewById(R.id.source_text_field);
        destionationTextField = findViewById(R.id.destination_text_field);
        dateTextField = findViewById(R.id.date_text_field);
        TextView emptyJourneyField = findViewById(R.id.journey_empty_text);
        emptyJourneyField.setVisibility(View.GONE);

        RecyclerView listItemView = findViewById(R.id.journey_list_recycler_view);

        appViewModel = new ViewModelProvider(this).get(
                AppViewModel.class);

        handleIntent(getIntent());

        appViewModel.getActivityToStart().observe(this, classBundlePair -> {
            Intent intent = new Intent(SearchBusActivity.this,
                    classBundlePair.first);
            if (classBundlePair.second != null) {
                intent.putExtras(classBundlePair.second);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        appViewModel.getSearchForStartStopLocationMediator().observe(this, journeys
                -> {
            Log.d("here", String.valueOf(journeys));
            for (JourneyBusPlace journeyBusPlace : journeys) {
                journeyBusPlace.sourcePlace = startLocation;
                journeyBusPlace.destinationPlace = endLocation;
                journeyBusPlace.journey.startLocation = startLocation.id;
                journeyBusPlace.journey.endLocation = endLocation.id;
            }
            listAdapter.setList(journeys);
            listItemView.scrollToPosition(0);
            if (journeys.size() == 0) {
                emptyJourneyField.setVisibility(View.VISIBLE);
            } else {
                emptyJourneyField.setVisibility(View.GONE);
            }
        });
        listAdapter = new JourneyListAdapter(new ItemClickListener() {
            @Override
            public void itemClicked(JourneyBusPlace journeyBusPlace) {
                Log.d("here", "here");
                Date journeyDate = new Date(dateInt);
                Calendar journeyCal = Calendar.getInstance();
                journeyCal.setTime(journeyDate);
                Calendar tripCal = Calendar.getInstance();
                tripCal.setTime(journeyBusPlace.journey.startTime);
                journeyCal.set(Calendar.HOUR_OF_DAY, tripCal.get(Calendar.HOUR_OF_DAY));
                journeyCal.set(Calendar.MINUTE, tripCal.get(Calendar.MINUTE));
                journeyCal.set(Calendar.SECOND, tripCal.get(Calendar.SECOND));
                Date finalJourneyDate = journeyCal.getTime();
                if (finalJourneyDate.getTime() < new Date().getTime()) {
                    Toast.makeText(SearchBusActivity.this, "Cannot book for this time slot",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(SearchBusActivity.this, RouteDetailsActivity.class);
                intent.putExtra("journey", journeyBusPlace);
                intent.putExtra("dateInt", finalJourneyDate.getTime());
                startActivity(intent);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listItemView.setLayoutManager(layoutManager);
        listItemView.setItemAnimator(null);
        listItemView.setAdapter(listAdapter);

        ImageView busFilterButton = findViewById(R.id.bus_filter_button);
        busFilterButton.setOnClickListener((View.OnClickListener) view -> {
            FilterDialogFragment newFragment = FilterDialogFragment.newInstance(appViewModel.getBusFilterSortOptions());
            newFragment.viewItemListener = busFilterOptions -> {
                appViewModel.setBusFilterSortOptions(busFilterOptions);
                appViewModel.getItemsForNameOrderBy(startLocation,
                        endLocation,
                        startDate,
                        busFilterOptions.getBusFilters(),
                        busFilterOptions.getBusType(),
                        busFilterOptions.getBusOperators(),
                        busFilterOptions.getBusDepartureTimeRange(),
                        busFilterOptions.getBusArrivalTimeRange(),
                        appViewModel.getBusFilterSortOptions().getBusOrderBy());
            };
            newFragment.show(getSupportFragmentManager(), "FilterAndSortDialogFragment");
        });

        ImageView busSortButton = findViewById(R.id.bus_sort_button);
        busSortButton.setOnClickListener((View.OnClickListener) view -> {
            final String[] ordering = new String[5];
            int selectedIndxForOrdering = 0;
            ordering[0] = "Relevance";
            ordering[1] = "Rating";
            ordering[2] = "Price";
            ordering[3] = "Deparature Time";
            ordering[4] = "Travel Duration";
            switch (appViewModel.getBusFilterSortOptions().getBusOrderBy()) {
                case RATING:
                    selectedIndxForOrdering = 1;
                    break;
                case PRICE:
                    selectedIndxForOrdering = 2;
                    break;
                case DEPARTURE_TIME:
                    selectedIndxForOrdering = 3;
                    break;
                case TRAVEL_DURATION:
                    selectedIndxForOrdering = 4;
                    break;
                default:
                    selectedIndxForOrdering = 0;
            }
            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(SearchBusActivity.this);
            mBuilder.setTitle("Select preferred sort:");
            mBuilder.setSingleChoiceItems(ordering, selectedIndxForOrdering, (dialogInterface, i) -> {
                switch (i) {
                    case 1:
                        appViewModel.getBusFilterSortOptions().setBusOrderBy(RATING);
                        break;
                    case 2:
                        appViewModel.getBusFilterSortOptions().setBusOrderBy(PRICE);
                        break;
                    case 3:
                        appViewModel.getBusFilterSortOptions().setBusOrderBy(DEPARTURE_TIME);
                        break;
                    case 4:
                        appViewModel.getBusFilterSortOptions().setBusOrderBy(TRAVEL_DURATION);
                        break;
                    default:
                        appViewModel.getBusFilterSortOptions().setBusOrderBy(RELEVANCE);
                }
            });
            mBuilder.setPositiveButton("OK", (dialog, which) -> {
                appViewModel.getItemsForNameOrderBy(startLocation,
                        endLocation,
                        startDate,
                        appViewModel.getBusFilterSortOptions().getBusFilters(),
                        appViewModel.getBusFilterSortOptions().getBusType(),
                        appViewModel.getBusFilterSortOptions().getBusOperators(),
                        appViewModel.getBusFilterSortOptions().getBusDepartureTimeRange(),
                        appViewModel.getBusFilterSortOptions().getBusArrivalTimeRange(),
                        appViewModel.getBusFilterSortOptions().getBusOrderBy());
            });
            AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        appViewModel.setBusFilterSortOptions(intent.getParcelableExtra("busFilterOptions"));
        startLocation = (Place) intent.getSerializableExtra("startLoc");
        endLocation = (Place) intent.getSerializableExtra("endLoc");
        isVoice = intent.getBooleanExtra("isVoice",false);
        dateInt = intent.getLongExtra("date", 0);
        String dateString = new SimpleDateFormat("dd - MMM - yyyy | EEEE").format(new Date(dateInt));
        dateTextField.setText(dateString);

        if(new Date(dateInt).getTime() <= new Date().getTime()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH mm");
            try {
                startDate = dateFormat.parse(dateFormat.format(new Date()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else {
            startDate = null;
        }

        sourceTextField.setText(getLocationString(startLocation));
        destionationTextField.setText(getLocationString(endLocation));

        appViewModel.getAllBusAttributes().observe(this, filters ->
                appViewModel.getItemsForNameOrderBy(startLocation,
                        endLocation,
                        startDate,
                        appViewModel.getBusFilterSortOptions().getBusFilters(),
                        appViewModel.getBusFilterSortOptions().getBusType(),
                        appViewModel.getBusFilterSortOptions().getBusOperators(),
                        appViewModel.getBusFilterSortOptions().getBusDepartureTimeRange(),
                        appViewModel.getBusFilterSortOptions().getBusArrivalTimeRange(),
                        appViewModel.getBusFilterSortOptions().getBusOrderBy()));
    }

    String getLocationString(Place place) {
        String location = "";
        if (place.stop != null) location += place.stop;
        if (place.city != null) {
            if (!"".equals(location)) location += ", ";
            location += place.city;
        }
        if (place.stateFullName != null) {
            if (!"".equals(location)) location += ", ";
            location += place.stateFullName;
        }
        return location;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
