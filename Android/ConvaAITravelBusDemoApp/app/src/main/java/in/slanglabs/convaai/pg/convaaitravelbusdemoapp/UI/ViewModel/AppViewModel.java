package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.UI.ViewModel;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.App;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.BusFilterSortOptions;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.BusWithAttributes;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.Journey;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.JourneyBusPlace;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.JourneyBusPlaceOrder;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.OrderBy;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.OrderItem;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.Place;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.TimeRange;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Repository;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.SingleLiveEvent;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.UI.Activity.MainActivity;

public class AppViewModel extends AndroidViewModel {

    private static final String TAG = AppViewModel.class.getSimpleName();

    private Repository mRepository;
    private MediatorLiveData<List<JourneyBusPlace>> searchForStartStopMediator =
            new MediatorLiveData<>();

    private LiveData<List<JourneyBusPlace>> searchForStartEndLocation =
            new MutableLiveData<>();

    private BusFilterSortOptions busFilterSortOptions;

    public AppViewModel(@NonNull Application application) {
        super(application);
        mRepository = ((App) application).getRepository();
        busFilterSortOptions = new BusFilterSortOptions();
    }

    public BusFilterSortOptions getBusFilterSortOptions() {
        return busFilterSortOptions;
    }

    public void setBusFilterSortOptions(BusFilterSortOptions busFilterSortOptions) {
        this.busFilterSortOptions = busFilterSortOptions;
    }

    public LiveData<List<Journey>> getAllJournies() {
        return mRepository.getAllJournies();
    }

    public LiveData<JourneyBusPlace> getJourneyItem(long journeyId) {
        return mRepository.getJourneyItem(journeyId);
    }

    public List<String> getBusTypes() {
        return mRepository.getBusTypes();
    }

    public List<TimeRange> getTimeRanges() {
        return mRepository.getTimeRanges();
    }

    public LiveData<List<BusWithAttributes>> getAllBuses() {
        return mRepository.getAllBuses();
    }

    public LiveData<List<String>> getAllBusAttributes() {
        return mRepository.getAllBusAttributes();
    }

    //Functions/Methods related to orders.
    public LiveData<List<JourneyBusPlaceOrder>> getOrderItems() {
        return mRepository.getOrderItems();
    }

    public LiveData<JourneyBusPlaceOrder> getOrderItem(String orderItemId) {
        return mRepository.getOrderItem(orderItemId);
    }

    public void addOrderItem(OrderItem orderItem) {
        mRepository.addOrderItem(orderItem);
    }

    public void removeOrderItem(OrderItem orderItem) {
        mRepository.removeOrderItem(orderItem);
    }


    //Functions/Methods related to search.
    public MediatorLiveData<List<JourneyBusPlace>> getSearchForStartStopLocationMediator() {
        return searchForStartStopMediator;
    }

    public void getItemsForNameOrderBy(Place startLocation,
                                       Place stopLocation,
                                       Date startDate,
                                       List<String> filters,
                                       List<String> busType,
                                       List<String> busOperators,
                                       List<TimeRange> departureTimeRange,
                                       List<TimeRange> arrivalTimeRange,
                                       @OrderBy int orderBy) {
        if (searchForStartEndLocation != null) {
            searchForStartStopMediator.removeSource(searchForStartEndLocation);
        }
        if (startLocation.city != null && stopLocation.city != null && startLocation.city.equalsIgnoreCase(stopLocation.city)) {
            searchForStartStopMediator.postValue(new ArrayList<>());
            return;
        }
        searchForStartEndLocation = mRepository.getItemsForNameOrderBy(startLocation.id,
                stopLocation.id, startDate, filters, busType, busOperators, departureTimeRange, arrivalTimeRange, orderBy);
        searchForStartStopMediator.addSource(searchForStartEndLocation, itemOfferCarts
                -> searchForStartStopMediator.postValue(itemOfferCarts));
    }

    public LiveData<List<Place>> getPlacesForName(String search) {
        return mRepository.getPlacesForName(search);
    }

    public LiveData<List<Place>> getAllPlaces() {
        return mRepository.getAllPlaces();
    }

    public SingleLiveEvent<Pair<Class, Bundle>> getActivityToStart() {
        return mRepository.getActivityToStart();
    }

    public void showAITrigger(Activity activity) {
        mRepository.showAITrigger(activity);
    }

    public LiveData<Place> getSourcePlace() {
        return mRepository.getSourcePlace();
    }

    public LiveData<Place> getDestinationPlace() {
        return mRepository.getDestinationPlace();
    }

    public LiveData<Long> getTravelDate() {
        return mRepository.getTravelDate();
    }

    public void setSourcePlace(Place place) {
        mRepository.setSourcePlace(place);
    }

    public void setDestinationPlace(Place place) {
        mRepository.setDestinationPlace(place);
    }

    public void setTravelDate(long date) {
        mRepository.setTravelDate(date);
    }

    public void notifySearchSourceLocationDisambiguated(Place place) {
        mRepository.notifySearchSourceLocationDisambiguated(place);
    }

    public void notifySearchDestinationLocationDisambiguated(Place place) {
        mRepository.notifySearchDestinationLocationDisambiguated(place);
    }

    public void notifySearchDateDisambiguated(Date date) {
        mRepository.notifySearchDateDisambiguated(date);
    }

    public void startConverstation(Activity activity) {
        mRepository.startConverstation(activity);
    }
}
