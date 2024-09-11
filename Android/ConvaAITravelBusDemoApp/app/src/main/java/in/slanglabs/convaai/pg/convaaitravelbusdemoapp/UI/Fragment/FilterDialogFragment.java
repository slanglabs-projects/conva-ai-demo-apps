package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.UI.Fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.BusFilterSortOptions;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.BusWithAttributes;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.TimeRange;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.R;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.UI.ViewModel.AppViewModel;

public class FilterDialogFragment extends DialogFragment {

    private static final String TAG = "FilterAndSortDialogFragment";
    public ViewItemListener viewItemListener;
    private AppViewModel appViewModel;

    private List<String> busFilters = new ArrayList<>();
    private List<Boolean> selectedBusFilters = new ArrayList<>();

    private List<String> busOperatorFilters = new ArrayList<>();
    private List<Boolean> selectedBusOperatorFilters = new ArrayList<>();

    private List<TimeRange> timeRanges = new ArrayList<>();
    private String[] timeRangesStringArray;
    private List<Boolean> selectedBusDepartureTimeRange = new ArrayList<>();
    private List<Boolean> selectedBusArrivalTimeRange = new ArrayList<>();

    private BusFilterSortOptions busFilterSortOptions;

    TextView busTypeSelectedTextView;
    TextView busOperatorSelectedTextView;
    TextView departureTimeRangeSelectedTextView;
    TextView arrivalTimeRangeSelectedTextView;

    public interface ViewItemListener {
        public void onFilterChanged(BusFilterSortOptions busFilterSortOptions);
    }

    public static FilterDialogFragment newInstance(BusFilterSortOptions busFilterSortOptions) {
        FilterDialogFragment myFragment = new FilterDialogFragment();
        myFragment.busFilterSortOptions = new BusFilterSortOptions();
        Bundle args = new Bundle();
        args.putParcelable("busFilterOptions", busFilterSortOptions);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) dismiss();
        setStyle(DialogFragment.STYLE_NORMAL, R.style.full_screen_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (savedInstanceState != null) {
            dismiss();
        }
        View view = inflater.inflate(R.layout.filter_layout, container,
                false);

        busTypeSelectedTextView = view.findViewById(R.id.bus_type_selected_text);
        busOperatorSelectedTextView = view.findViewById(R.id.bus_operator_selected_text);
        departureTimeRangeSelectedTextView = view.findViewById(R.id.departure_selected_text);
        arrivalTimeRangeSelectedTextView = view.findViewById(R.id.arrival_selected_text);

        View busTypeView = view.findViewById(R.id.bus_type_view);
        busTypeView.setOnClickListener((View.OnClickListener) view1 -> {
            final boolean[] selectedBusFiltersArray = new boolean[selectedBusFilters.size()];
            for (int i = 0; i < selectedBusFilters.size(); i++) {
                selectedBusFiltersArray[i] = selectedBusFilters.get(i);
            }
            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
            mBuilder.setTitle("Select preferred filter:");
            mBuilder.setMultiChoiceItems(busFilters.toArray(new String[0]),
                    selectedBusFiltersArray, (dialog, position, isChecked) -> {
                        selectedBusFilters.set(position, isChecked);
                    });
            mBuilder.setPositiveButton("OK", (dialog, which) -> {
                List<String> finalBusFilters = new ArrayList<>();
                List<String> busTypes = new ArrayList<>();
                for (int i = 0; i < selectedBusFilters.size(); i++) {
                    if (i == 0 && selectedBusFilters.get(i)) {
                        busTypes.add("A/C");
                    } else if (i == 1 && selectedBusFilters.get(i)) {
                        busTypes.add("Non A/C");
                    } else {
                        if (selectedBusFilters.get(i)) {
                            finalBusFilters.add(busFilters.get(i));
                        }
                    }
                }
                busFilterSortOptions.setBusFilters(finalBusFilters);
                busFilterSortOptions.setBusType(busTypes);
                int totalCount = busFilterSortOptions.getBusFilters().size() + busFilterSortOptions.getBusType().size();
                if (totalCount > 0) {
                    busTypeSelectedTextView.setVisibility(View.VISIBLE);
                    busTypeSelectedTextView.setText(String.format(Locale.ENGLISH,
                            "%d Selected", busFilterSortOptions.getBusFilters().size() + busFilterSortOptions.getBusType().size()));
                } else {
                    busTypeSelectedTextView.setVisibility(View.GONE);
                }
            });
            AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        });


        View busOperatorView = view.findViewById(R.id.bus_operator_view);
        busOperatorView.setOnClickListener(view1 -> {
            final boolean[] selectedBusOperatorFiltersArray = new boolean[selectedBusOperatorFilters.size()];
            for (int i = 0; i < selectedBusOperatorFilters.size(); i++) {
                selectedBusOperatorFiltersArray[i] = selectedBusOperatorFilters.get(i);
            }
            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
            mBuilder.setTitle("Select preferred filter:");
            mBuilder.setMultiChoiceItems(busOperatorFilters.toArray(new String[0]),
                    selectedBusOperatorFiltersArray, (dialog, position, isChecked) -> {
                        selectedBusOperatorFilters.set(position, isChecked);
                    });
            mBuilder.setPositiveButton("OK", (dialog, which) -> {
                List<String> finalBusOperators = new ArrayList<>();
                for (int i = 0; i < selectedBusOperatorFilters.size(); i++) {
                    if (selectedBusOperatorFilters.get(i)) {
                        finalBusOperators.add(busOperatorFilters.get(i));
                    }
                }
                busFilterSortOptions.setBusOperators(finalBusOperators);
                int totalCount = busFilterSortOptions.getBusOperators().size();
                if (totalCount > 0) {
                    busOperatorSelectedTextView.setVisibility(View.VISIBLE);
                    busOperatorSelectedTextView.setText(String.format(Locale.ENGLISH,
                            "%d Selected", totalCount));
                } else {
                    busOperatorSelectedTextView.setVisibility(View.GONE);
                }
            });
            AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        });


        View busDepartureTimeRangeView = view.findViewById(R.id.departure_time_view);
        busDepartureTimeRangeView.setOnClickListener(view1 -> {
            final boolean[] selectedDepartureTimeFiltersArray = new boolean[selectedBusDepartureTimeRange.size()];
            for (int i = 0; i < selectedBusDepartureTimeRange.size(); i++) {
                selectedDepartureTimeFiltersArray[i] = selectedBusDepartureTimeRange.get(i);
            }
            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
            mBuilder.setTitle("Select preferred filter:");
            mBuilder.setMultiChoiceItems(timeRangesStringArray,
                    selectedDepartureTimeFiltersArray, (dialog, position, isChecked) -> {
                        selectedBusDepartureTimeRange.set(position, isChecked);
                    });
            mBuilder.setPositiveButton("OK", (dialog, which) -> {
                List<TimeRange> finalDeparatureTimeRange = new ArrayList<>();
                for (int i = 0; i < selectedBusDepartureTimeRange.size(); i++) {
                    if (selectedBusDepartureTimeRange.get(i)) {
                        finalDeparatureTimeRange.add(timeRanges.get(i));
                    }
                }
                busFilterSortOptions.setBusDepartureTimeRange(finalDeparatureTimeRange);
                int totalCount = busFilterSortOptions.getBusDepartureTimeRange().size();
                if (totalCount > 0) {
                    departureTimeRangeSelectedTextView.setVisibility(View.VISIBLE);
                    departureTimeRangeSelectedTextView.setText(String.format(Locale.ENGLISH,
                            "%d Selected", totalCount));
                } else {
                    departureTimeRangeSelectedTextView.setVisibility(View.GONE);
                }
            });
            AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        });

        View busArrivalTimeRangeView = view.findViewById(R.id.arrival_time_view);
        busArrivalTimeRangeView.setOnClickListener(view1 -> {
            final boolean[] selectedArrivalTimeFiltersArray = new boolean[selectedBusArrivalTimeRange.size()];
            for (int i = 0; i < selectedBusArrivalTimeRange.size(); i++) {
                selectedArrivalTimeFiltersArray[i] = selectedBusArrivalTimeRange.get(i);
            }
            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
            mBuilder.setTitle("Select preferred filter:");
            mBuilder.setMultiChoiceItems(timeRangesStringArray,
                    selectedArrivalTimeFiltersArray, (dialog, position, isChecked) -> {
                        selectedBusArrivalTimeRange.set(position, isChecked);
                    });
            mBuilder.setPositiveButton("OK", (dialog, which) -> {
                List<TimeRange> finalArrivalTimeRange = new ArrayList<>();
                for (int i = 0; i < selectedBusArrivalTimeRange.size(); i++) {
                    if (selectedBusArrivalTimeRange.get(i)) {
                        finalArrivalTimeRange.add(timeRanges.get(i));
                    }
                }
                busFilterSortOptions.setBusArrivalTimeRange(finalArrivalTimeRange);
                int totalCount = busFilterSortOptions.getBusArrivalTimeRange().size();
                if (totalCount > 0) {
                    arrivalTimeRangeSelectedTextView.setVisibility(View.VISIBLE);
                    arrivalTimeRangeSelectedTextView.setText(String.format(Locale.ENGLISH,
                            "%d Selected", totalCount));
                } else {
                    arrivalTimeRangeSelectedTextView.setVisibility(View.GONE);
                }
            });
            AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        });


        View applyView = view.findViewById(R.id.filter_apply_button);
        applyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewItemListener.onFilterChanged(busFilterSortOptions);
                dismiss();
            }
        });

        View resetView = view.findViewById(R.id.filter_reset_button);
        resetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                busFilterSortOptions.clear();
                viewItemListener.onFilterChanged(busFilterSortOptions);
                dismiss();
            }
        });

        ImageButton cancelButton = view.findViewById(R.id.back_arrow);
        cancelButton.setOnClickListener(v -> {
            dismiss();
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        appViewModel = new ViewModelProvider(this).get(
                AppViewModel.class);
        if (getArguments() != null) {
            busFilterSortOptions = getArguments().getParcelable("busFilterOptions");
        }
        appViewModel.getAllBusAttributes().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> filters) {
                for (String type : appViewModel.getBusTypes()) {
                    busFilters.add(type);
                    selectedBusFilters.add(false);
                }
                for (String filter : filters) {
                    busFilters.add(filter);
                    selectedBusFilters.add(false);
                }
                List<String> finalBusFilters = new ArrayList<>();
                for (int i = 0; i < selectedBusFilters.size(); i++) {
                    if (selectedBusFilters.get(i)) {
                        finalBusFilters.add(busFilters.get(i));
                    }
                }
                if (busFilterSortOptions != null) {
                    for (String busType : busFilterSortOptions.getBusType()) {
                        int index = busFilters.indexOf(busType);
                        selectedBusFilters.set(index, true);
                    }
                    for (String busType : busFilterSortOptions.getBusFilters()) {
                        int index = busFilters.indexOf(busType);
                        selectedBusFilters.set(index, true);
                    }
                }
                int totalCount = busFilterSortOptions.getBusFilters().size() + busFilterSortOptions.getBusType().size();
                if (totalCount > 0) {
                    busTypeSelectedTextView.setVisibility(View.VISIBLE);
                    busTypeSelectedTextView.setText(String.format(Locale.ENGLISH,
                            "%d Selected", busFilterSortOptions.getBusFilters().size() + busFilterSortOptions.getBusType().size()));
                } else {
                    busTypeSelectedTextView.setVisibility(View.GONE);
                }
            }
        });

        appViewModel.getAllBuses().observe(getViewLifecycleOwner(), new Observer<List<BusWithAttributes>>() {
            @Override
            public void onChanged(List<BusWithAttributes> filters) {
                for (BusWithAttributes busWithAttributes : filters) {
                    busOperatorFilters.add(busWithAttributes.bus.travels);
                    selectedBusOperatorFilters.add(false);
                }

                if (busFilterSortOptions != null) {
                    for (String busOperator : busFilterSortOptions.getBusOperators()) {
                        int index = busOperatorFilters.indexOf(busOperator);
                        selectedBusOperatorFilters.set(index, true);
                    }
                }
                int totalCount = busFilterSortOptions.getBusOperators().size();
                if (totalCount > 0) {
                    busOperatorSelectedTextView.setVisibility(View.VISIBLE);
                    busOperatorSelectedTextView.setText(String.format(Locale.ENGLISH,
                            "%d Selected", totalCount));
                } else {
                    busOperatorSelectedTextView.setVisibility(View.GONE);
                }
            }
        });

        timeRanges = appViewModel.getTimeRanges();
        timeRangesStringArray = new String[appViewModel.getTimeRanges().size()];
        SimpleDateFormat dateformatFinal = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
        for (int i = 0; i < appViewModel.getTimeRanges().size(); i++) {
            String totalString = "";
            TimeRange timeRange = appViewModel.getTimeRanges().get(i);
            String startTimeString = dateformatFinal.format(new Date(timeRange.getStartTime()));
            String endTimeString = dateformatFinal.format(new Date(timeRange.getEndTime()));
            totalString = startTimeString + " - " + endTimeString;
            timeRangesStringArray[i] = totalString;
            selectedBusDepartureTimeRange.add(false);
            selectedBusArrivalTimeRange.add(false);
        }

        if (busFilterSortOptions != null) {
            for (TimeRange timeRange : busFilterSortOptions.getBusDepartureTimeRange()) {
                int index = timeRanges.indexOf(timeRange);
                selectedBusDepartureTimeRange.set(index, true);
            }
            for (TimeRange timeRange : busFilterSortOptions.getBusArrivalTimeRange()) {
                int index = timeRanges.indexOf(timeRange);
                selectedBusArrivalTimeRange.set(index, true);
            }
        }

        int totalDepartureCount = busFilterSortOptions.getBusDepartureTimeRange().size();
        if (totalDepartureCount > 0) {
            departureTimeRangeSelectedTextView.setVisibility(View.VISIBLE);
            departureTimeRangeSelectedTextView.setText(String.format(Locale.ENGLISH,
                    "%d Selected", totalDepartureCount));
        } else {
            departureTimeRangeSelectedTextView.setVisibility(View.GONE);
        }
        int totalArrivalCount = busFilterSortOptions.getBusArrivalTimeRange().size();
        if (totalArrivalCount > 0) {
            arrivalTimeRangeSelectedTextView.setVisibility(View.VISIBLE);
            arrivalTimeRangeSelectedTextView.setText(String.format(Locale.ENGLISH,
                    "%d Selected", totalArrivalCount));
        } else {
            arrivalTimeRangeSelectedTextView.setVisibility(View.GONE);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}

