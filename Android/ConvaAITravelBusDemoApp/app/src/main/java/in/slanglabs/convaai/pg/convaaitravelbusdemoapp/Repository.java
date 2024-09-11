package in.slanglabs.convaai.pg.convaaitravelbusdemoapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.BusFilterSortOptions;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.BusWithAttributes;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.Journey;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.JourneyBusPlace;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.JourneyBusPlaceOrder;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.OrderBy;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.OrderItem;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.OrderStatus;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.Place;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.RouteStatus;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.SearchItem;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.TimeRange;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.TravelType;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.UI.Activity.MainActivity;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.UI.Activity.SearchBusActivity;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.convaai.ConvaAIInterface;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.db.AppDatabase;

public class Repository {

    private static final String TAG = Repository.class.getSimpleName();
    private static Repository shared;

    private List<String> busTypes =
            new ArrayList<>();

    private List<TimeRange> timeRanges =
            new ArrayList<>();

    private AppDatabase mDatabase;
    private final AppExecutors appExecutors;

    private final SingleLiveEvent<Pair<Class, Bundle>> activityToStart =
            new SingleLiveEvent<>();


    private MutableLiveData<Place> sourcePlace =
            new MutableLiveData<>();

    private MutableLiveData<Place> destinationPlace =
            new MutableLiveData<>();

    private MutableLiveData<Long> travelDate =
            new MutableLiveData<>();

    private SearchItem searchItem = new SearchItem();
    private final ConvaAIInterface mConvaAIInterface;

    private Repository(final AppDatabase database, final AppExecutors appExecutors, final ConvaAIInterface convaAIInterface) {
        this.mDatabase = database;
        this.appExecutors = appExecutors;
        this.mConvaAIInterface = convaAIInterface;

        busTypes.add(TravelType.AC);
        busTypes.add(TravelType.NON_AC);

        timeRanges.add(new TimeRange(1800000, 23340000));
        timeRanges.add(new TimeRange(23400000, 44940000));
        timeRanges.add(new TimeRange(45000000, 66540000));
        timeRanges.add(new TimeRange(-19800000, 1740000));

        Random rng = new Random();
        final LiveData<List<Journey>> journies = mDatabase.journayDao().getAllJournies();
        Observer observer = new Observer<List<Journey>>() {
            @Override
            public void onChanged(List<Journey> journeys) {
                if (journeys.size() > 0) {
                    List<Integer> generated = new ArrayList<Integer>();
                    int totalNumberOfOffers = (int) (journeys.size() * 0.3);
                    for (int i = 0; i < totalNumberOfOffers; i++) {
                        while (true) {
                            Integer next = rng.nextInt(journeys.size());
                            if (!generated.contains(next)) {
                                generated.add(next);
                                break;
                            }
                        }
                    }
                    int counter;
                    for (counter = 0; counter < generated.size(); counter++) {
                        int randomNumber = generated.get(counter);
                        Journey journey = journeys.get(randomNumber);
                        Log.d("JourneyId", String.valueOf(journey.journeyId));
                        if (randomNumber % 2 == 0) {
                            appExecutors.diskIO().execute(() -> {
                                mDatabase.journayDao().update(RouteStatus.DELAYED, journey.journeyId);
                            });
                        } else {
                            appExecutors.diskIO().execute(() -> {
                                mDatabase.journayDao().update(RouteStatus.CANCELED, journey.journeyId);
                            });
                        }
                    }
                }
                journies.removeObserver(this);
            }
        };
        journies.observeForever(observer);
    }

    static Repository getInstance(final AppDatabase mDatabase, final AppExecutors appExecutors, final ConvaAIInterface convaAIInterface) {
        if (shared == null) {
            synchronized (Repository.class) {
                if (shared == null) {
                    shared = new Repository(mDatabase, appExecutors, convaAIInterface);
                }
            }
        }
        return shared;
    }

    public LiveData<JourneyBusPlace> getJourneyItem(long journeyId) {
        return mDatabase.journayDao().loadJourney(journeyId);
    }

    public LiveData<List<JourneyBusPlace>> getItemsForNameOrderBy(
            String startLocation,
            String endLocation,
            Date startDate,
            List<String> filters,
            List<String> busType,
            List<String> busOperators,
            List<TimeRange> departureTimeRange,
            List<TimeRange> arrivalTimeRange,
            @OrderBy int orderBy) {

        StringBuilder stringBuilder = new StringBuilder();
        List<Object> args = new ArrayList();
        stringBuilder.append("SELECT * FROM journey");
        stringBuilder.append(" JOIN bus ON bus.id = journey.busId JOIN busAttributes ON busAttributes.busId = journey.busId");
        stringBuilder.append(" WHERE");
        stringBuilder.append(" startLocation =?");
        args.add("place1");
        stringBuilder.append(" AND endLocation =?");
        args.add("place2");

        if (startDate != null) {
            stringBuilder.append(" AND");
            stringBuilder.append(" startTime >= ?");
            args.add(startDate.getTime());
        }

        if (!filters.isEmpty()) {
            stringBuilder.append(" AND");
            stringBuilder.append(" travelClass IN (");
            appendPlaceholders(stringBuilder, filters.size());
            stringBuilder.append(")");
            args.addAll(filters);
        }
        if (!busType.isEmpty()) {
            stringBuilder.append(" AND");
            stringBuilder.append(" type IN (");
            appendPlaceholders(stringBuilder, busType.size());
            stringBuilder.append(")");
            args.addAll(busType);
        }
        if (!busOperators.isEmpty()) {
            stringBuilder.append(" AND");
            stringBuilder.append(" travels IN (");
            appendPlaceholders(stringBuilder, busOperators.size());
            stringBuilder.append(")");
            args.addAll(busOperators);
        }

        if (!departureTimeRange.isEmpty()) {
            stringBuilder.append(" AND (");
            for (int i = 0; i < departureTimeRange.size(); i++) {
                TimeRange timeRange = departureTimeRange.get(i);
                if (i != 0) {
                    stringBuilder.append(" OR");
                }
                stringBuilder.append(" (startTime >= ?");
                args.add(timeRange.getStartTime());
                stringBuilder.append("AND startTime <= ?)");
                args.add(timeRange.getEndTime());
            }
            stringBuilder.append(")");
        }

        if (!arrivalTimeRange.isEmpty()) {
            stringBuilder.append(" AND (");
            for (int i = 0; i < arrivalTimeRange.size(); i++) {
                TimeRange timeRange = arrivalTimeRange.get(i);
                if (i != 0) {
                    stringBuilder.append(" OR");
                }
                stringBuilder.append(" (endTime >= ?");
                args.add(timeRange.getStartTime());
                stringBuilder.append("AND endTime <= ?)");
                args.add(timeRange.getEndTime());
            }
            stringBuilder.append(")");
        }

        stringBuilder.append(" GROUP BY id");
        if (orderBy == OrderBy.PRICE) {
            stringBuilder.append(" ORDER BY journey.price ASC");
        } else if (orderBy == OrderBy.RATING) {
            stringBuilder.append(" ORDER BY bus.starRating DESC");
        } else if (orderBy == OrderBy.DEPARTURE_TIME) {
            stringBuilder.append(" ORDER BY startTime ASC");
        } else if (orderBy == OrderBy.TRAVEL_DURATION) {
            stringBuilder.append(" ORDER BY duration ASC");
        } else if (orderBy == OrderBy.RELEVANCE) {
            stringBuilder.append(" ORDER BY travels ASC");
        }
        stringBuilder.append(";");
        return mDatabase.journayDao().getJourneyRawQuery(new SimpleSQLiteQuery(stringBuilder.toString(), args.toArray()));
    }

    //Order Related Opertions/Methods
    public LiveData<JourneyBusPlaceOrder> getOrderItem(String orderItemId) {
        return mDatabase.orderDao().loadOrder(orderItemId);
    }

    public void addOrderItem(OrderItem item) {
        appExecutors.diskIO().execute(() -> {
            mDatabase.orderDao().insert(item);
        });
    }

    public void removeOrderItem(OrderItem item) {
        appExecutors.diskIO().execute(() -> {
            mDatabase.orderDao().update(OrderStatus.CANCELED, item.orderId);
        });
    }

    //Slang callback handlers
    public void onSearch(SearchItem searchInfo) {
        this.searchItem = searchInfo;
        appExecutors.diskIO().execute(() -> {

            Place destinationPlace = new Place();
            destinationPlace.city = searchInfo.destinationPlace.city;
            destinationPlace.stop = searchInfo.destinationPlace.stop;
            destinationPlace.stateFullName = searchInfo.destinationPlace.state;
            String destinationName = (destinationPlace.city == null ? "" : destinationPlace.city) + " " + (destinationPlace.stop == null ? "" : destinationPlace.stop) + " " + (destinationPlace.stateFullName == null ? "" : (destinationPlace.stateFullName.equalsIgnoreCase("dummy_state") ? "" : destinationPlace.stateFullName));

            if (destinationName.replaceAll("\\s", "").equalsIgnoreCase("")) {

                return;
            }

            //There is only one destination place for the current destination, hence we choose that.
            Repository.this.destinationPlace.postValue(destinationPlace);

            //We move on to source place resolution.
            Place sourcePlace = new Place();
            sourcePlace.city = searchInfo.sourcePlace.city;
            sourcePlace.stop = searchInfo.sourcePlace.stop;
            sourcePlace.stateFullName = searchInfo.sourcePlace.state;
            if ("bangalore".equals(sourcePlace.city)) {
                sourcePlace.city = "bengaluru";
            }
            String sourceName = (sourcePlace.city == null ? "" : sourcePlace.city) + " " + (sourcePlace.stop == null ? "" : sourcePlace.stop) + " " + (sourcePlace.stateFullName == null ? "" : (sourcePlace.stateFullName.equalsIgnoreCase("dummy_state") ? "" : sourcePlace.stateFullName));

            if (sourceName.replaceAll("\\s", "").equalsIgnoreCase("")) {
                return;
            }

            //There is only one source place for the current source, hence we choose that.
            Repository.this.sourcePlace.postValue(sourcePlace);

            //We move on to onwardDate resolution.
            Date date = searchInfo.travelDate;

            if (date == null) {

                //Onward date is null, choose today's date
                date = new Date();
            }

            //We validate the current date to match the valid dates supported by the current app.
            else if (!validateDate(date, 90)) {

                //Additionally, show the date picker UI to selected an appropriate date.
                Bundle bundle = new Bundle();
                bundle.putBoolean("disambiguateDate", true);
                appExecutors.mainThread().execute(()
                        -> activityToStart.setValue(new Pair<>(MainActivity.class, bundle)));
                return;
            }

            //All the required fields are available now, we use the required fields and move to the SearchBusActivity.
            Date finalDate = date;
            appExecutors.mainThread().execute(() -> {

                Repository.this.sourcePlace.postValue(sourcePlace);
                Repository.this.destinationPlace.postValue(destinationPlace);
                travelDate.postValue(finalDate.getTime());

                Bundle bundle = new Bundle();
                bundle.putSerializable("startLoc", sourcePlace);
                bundle.putSerializable("endLoc", destinationPlace);
                bundle.putLong("date", finalDate.getTime());
                BusFilterSortOptions busFilterSortOptions = new BusFilterSortOptions();
                List<String> busTypes = new ArrayList<>();
                if (searchInfo.travelType != null) {
                    busTypes.add(searchInfo.travelType);
                }
                List<String> busFilters = new ArrayList<>();
                if (searchInfo.travelClass != null) {
                    busFilters.add(searchInfo.travelClass);
                }
                busFilterSortOptions.setBusType(busTypes);
                busFilterSortOptions.setBusFilters(busFilters);
                bundle.putBoolean("isVoice", true);
                bundle.putParcelable("busFilterOptions", busFilterSortOptions);
                activityToStart.setValue(new Pair<>(SearchBusActivity.class, bundle));
            });
        });
    }

    //Getters
    public LiveData<List<JourneyBusPlaceOrder>> getOrderItems() {
        return mDatabase.orderDao().loadAllOrders();
    }

    public LiveData<List<Journey>> getAllJournies() {
        return mDatabase.journayDao().getAllJournies();
    }

    public List<String> getBusTypes() {
        return busTypes;
    }

    public List<TimeRange> getTimeRanges() {
        return timeRanges;
    }

    public LiveData<List<BusWithAttributes>> getAllBuses() {
        return mDatabase.busDao().getAllBuses();
    }

    public SingleLiveEvent<Pair<Class, Bundle>> getActivityToStart() {
        return activityToStart;
    }

    public LiveData<List<String>> getAllBusAttributes() {
        return mDatabase.busAttributeDao().getAllBusAttributes();
    }

    public LiveData<List<Place>> getPlacesForName(String name) {
        return mDatabase.placeDao().searchPlaces(fixQuery(name));
    }

    public LiveData<List<Place>> getAllPlaces() {
        return mDatabase.placeDao().getAllPlaces();
    }

    private static String fixQuery(String query) {
        // Remove leading/trailing whitespace and replace multiple spaces with a single space
        String queryString = query.trim().replaceAll("\\s+", " ");

        // Replace hyphens or commas with spaces
        queryString = queryString.replaceAll("[-,]", " ");

        // Use SQLite compatible wildcard for LIKE operator
        return "%" + queryString + "%";
    }

    //Helpers
    public static void appendPlaceholders(StringBuilder builder, int count) {
        for (int i = 0; i < count; i++) {
            builder.append("?");
            if (i < count - 1) {
                builder.append(",");
            }
        }
    }

    private boolean validateDate(Date date, int maxFutureDate) {
        if (date == null) {
            return false;
        }

        Calendar minDate = Calendar.getInstance();
        minDate.set(Calendar.HOUR_OF_DAY, 0);
        minDate.set(Calendar.MINUTE, 0);
        minDate.set(Calendar.SECOND, 0);
        minDate.set(Calendar.MILLISECOND, 0);

        Calendar maxDate = Calendar.getInstance();
        maxDate.set(Calendar.HOUR_OF_DAY, 0);
        maxDate.set(Calendar.MINUTE, 0);
        maxDate.set(Calendar.SECOND, 0);
        maxDate.set(Calendar.MILLISECOND, 0);
        maxDate.add(Calendar.DAY_OF_MONTH, maxFutureDate);

        Calendar inputDate = Calendar.getInstance();
        inputDate.setTime(date);
        inputDate.set(Calendar.HOUR_OF_DAY, 0);
        inputDate.set(Calendar.MINUTE, 0);
        inputDate.set(Calendar.SECOND, 0);
        inputDate.set(Calendar.MILLISECOND, 0);

        return inputDate.getTimeInMillis() >= minDate.getTimeInMillis() &&
                inputDate.getTimeInMillis() <= maxDate.getTimeInMillis();
    }

    public MutableLiveData<Place> getSourcePlace() {
        return sourcePlace;
    }

    public MutableLiveData<Place> getDestinationPlace() {
        return destinationPlace;
    }

    public MutableLiveData<Long> getTravelDate() {
        return travelDate;
    }

    public void setSourcePlace(Place place) {
        sourcePlace.postValue(place);
    }

    public void setDestinationPlace(Place place) {
        destinationPlace.postValue(place);
    }

    public void setTravelDate(long date) {
        travelDate.postValue(date);
    }

    public void notifySearchSourceLocationDisambiguated(Place place) {
        //Continue with the resolution process, with the current selected source place.
        searchItem.sourcePlace = place;
        onSearch(searchItem);
    }

    public void notifySearchDestinationLocationDisambiguated(Place place) {
        //Continue with the resolution process, with the current selected destination place.
        searchItem.destinationPlace = place;
        onSearch(searchItem);
    }

    public void notifySearchDateDisambiguated(Date date) {

        Repository.this.travelDate.postValue(date.getTime());

        //Continue with the resolution process, with the current selected onward date.
        searchItem.travelDate = date;
        onSearch(searchItem);
    }

    public void showAITrigger(Activity activity) {
        mConvaAIInterface.showTrigger(activity);
    }

    public void startConverstation(Activity activity) {
        mConvaAIInterface.startConversation(activity);
    }
}
