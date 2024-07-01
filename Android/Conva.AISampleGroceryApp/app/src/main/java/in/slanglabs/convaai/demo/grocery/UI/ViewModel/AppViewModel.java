package in.slanglabs.convaai.demo.grocery.UI.ViewModel;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.slanglabs.convaai.demo.grocery.App;
import in.slanglabs.convaai.demo.grocery.Model.CartItemOffer;
import in.slanglabs.convaai.demo.grocery.Model.FeedbackItem;
import in.slanglabs.convaai.demo.grocery.Model.FilterOptions;
import in.slanglabs.convaai.demo.grocery.Model.Item;
import in.slanglabs.convaai.demo.grocery.Model.ItemListUIModel;
import in.slanglabs.convaai.demo.grocery.Model.ItemOfferCart;
import in.slanglabs.convaai.demo.grocery.Model.ListType;
import in.slanglabs.convaai.demo.grocery.Model.OfferItemCart;
import in.slanglabs.convaai.demo.grocery.Model.OrderItem;
import in.slanglabs.convaai.demo.grocery.Model.SearchItem;
import in.slanglabs.convaai.demo.grocery.Repository;
import in.slanglabs.convaai.demo.grocery.SingleLiveEvent;

public class AppViewModel extends AndroidViewModel {

    final Repository mRepository;
    private String mCurrentSearchTerm;
    private FilterOptions mFilterOptions;

    private boolean mIsSearchRequestComplete = false;
    private boolean mIsOrderRequestComplete = false;
    private boolean mIsOrderItemRequestComplete = false;

    private final MediatorLiveData<List<ItemListUIModel>> mSearchForNameMediator =
            new MediatorLiveData<>();
    private final MediatorLiveData<List<OrderItem>> mOrdersMediator =
            new MediatorLiveData<>();
    private final MediatorLiveData<OrderItem> mOrderItemMediator =
            new MediatorLiveData<>();

    private LiveData<List<ItemOfferCart>> mSearchForName =
            new MutableLiveData<>();
    private LiveData<List<OrderItem>> mOrderItems =
            new MutableLiveData<>();
    private LiveData<OrderItem> mOrderItem =
            new MutableLiveData<>();

    private final SingleLiveEvent<Boolean> showCancelConfirmation = new SingleLiveEvent<Boolean>();

    public static final String mMicDisabledMessage = "Assistant has not been initialized yet, Please wait";

    public AppViewModel(@NonNull Application application) {
        super(application);
        mRepository = ((App) application).getRepository();
        mFilterOptions = new FilterOptions();
    }

    //Functions/Methods related to items.
    public LiveData<List<ItemOfferCart>> getItems() {
        return mRepository.getItems();
    }

    public LiveData<ItemOfferCart> getItemForId(int id) {
        return mRepository.getItemForId(id);
    }

    //Functions/Methods related to offers.
    public LiveData<List<OfferItemCart>> getOfferItems() {
        return mRepository.getOfferItems();
    }

    //Functions/Methods related to cart.
    public LiveData<List<CartItemOffer>> getCartItems() {
        return mRepository.getCartItems();
    }

    public void clearCart() {
        mRepository.clearCart();
    }

    public void addItem(Item item, boolean uiAction) {
        mRepository.addItemToCartHelper(item, 1, uiAction, 0, null);
    }

    public void addItem(Item item, int quantity) {
        mRepository.addItemToCartHelper(item, quantity, false, 0, null);
    }

    public void addItem(ItemOfferCart itemOfferCart, int quantity, int withDelay, SearchItem searchItem) {
        mRepository.addItemToCart(itemOfferCart, quantity, false, withDelay, searchItem);
    }

    public void removeItem(Item item) {
        mRepository.removeItemFromCart(item);
    }

    //Functions/Methods related to orders.
    public void makeOrderRequest() {
        mIsOrderRequestComplete = false;
        if (mOrderItems != null) {
            mOrdersMediator.removeSource(mOrderItems);
        }
        mOrderItems = mRepository.getOrderItems();
        mOrdersMediator.addSource(mOrderItems, new Observer<List<OrderItem>>() {
            @Override
            public void onChanged(List<OrderItem> orderItems) {
                mOrdersMediator.postValue(orderItems);
                mIsOrderRequestComplete = true;
            }
        });
    }

    public void makeOrderItemRequest(String orderId) {
        mIsOrderItemRequestComplete = false;
        if (mOrderItem != null) {
            mOrderItemMediator.removeSource(mOrderItem);
        }
        mOrderItem = mRepository.getOrderItem(orderId);
        mOrderItemMediator.addSource(mOrderItem, new Observer<OrderItem>() {
            @Override
            public void onChanged(OrderItem orderItem) {
                mOrderItemMediator.postValue(orderItem);
                mIsOrderItemRequestComplete = true;
            }
        });
    }

    public LiveData<List<OrderItem>> getOrderItems() {
        return mOrdersMediator;
    }

    public LiveData<OrderItem> getOrderItem() {
        return mOrderItemMediator;
    }

    public void addOrderItem(OrderItem orderItem) {
        mRepository.addOrderItem(orderItem);
    }

    public void removeOrderItem(OrderItem orderItem) {
        mRepository.removeOrderItem(orderItem);
    }

    public void holdOrderItem(OrderItem orderItem) {
    }

    public void cancelOrderConfirmation(int index) {
        mRepository.cancelOrderConfirmation(index, true);
    }

    public SingleLiveEvent<Boolean> getShowCancelOrderConfirmation() {
        return showCancelConfirmation;
    }

    //Functions/Methods related to search.
    public String getCurrentSearchTerm() {
        return mCurrentSearchTerm;
    }

    public void setCurrentSearchTerm(String searchTerm) {
        this.mCurrentSearchTerm = searchTerm;
    }

    public LiveData<List<ItemListUIModel>> getSearchForNameMediator() {
        return mSearchForNameMediator;
    }

    public void setSelectedSearchItem(Item item) {
    }

    public void getSearchItem(String searchItem) {
        mRepository.setCurrentSearchItem(null);
        if (mSearchForName != null) {
            mSearchForNameMediator.removeSource(mSearchForName);
        }
        mSearchForName = mRepository.getItemsOfferCartForNameSync(searchItem,
                mRepository.getmListType().getValue(), mFilterOptions);
        mSearchForNameMediator.addSource(mSearchForName, itemOfferCarts -> {
            ArrayList<ItemListUIModel> itemListUIModels = new ArrayList<>();
            for (ItemOfferCart itemOfferCart : itemOfferCarts) {
                ItemListUIModel itemListUIModel = new ItemListUIModel();
                itemListUIModel.itemOfferCart = itemOfferCart;
                itemListUIModels.add(itemListUIModel);
            }
            mSearchForNameMediator.postValue(itemListUIModels);
        });
    }

    public void getSearchItem(SearchItem searchItem) {
        mIsSearchRequestComplete = false;
        mRepository.setCurrentSearchItem(null);
        if (searchItem == null) {
            return;
        }

        mRepository.setCurrentSearchItem(searchItem);
        if (mSearchForName != null) {
            mSearchForNameMediator.removeSource(mSearchForName);
        }

        StringBuilder stringBuilder = new StringBuilder();
        if (!searchItem.name.isEmpty()) {
            stringBuilder.append(searchItem.name);
        }
        if (!searchItem.brandName.isEmpty()) {
            stringBuilder.append(" ");
            stringBuilder.append(searchItem.brandName);
        }

        String name = stringBuilder.toString();

        if (!searchItem.size.isEmpty()) {
            int currentSize = Integer.parseInt(
                    searchItem.size.replaceAll("[^0-9]", "")
            );
            if (currentSize % 5 == 0) {
                mFilterOptions.setSizes(new ArrayList<>(Arrays.asList("5kg")));
            } else if (currentSize % 2 == 0) {
                mFilterOptions.setSizes(new ArrayList<>(Arrays.asList("2kg")));
            } else {
                mFilterOptions.setSizes(new ArrayList<>(Arrays.asList("1kg")));
            }
        }

        mSearchForName = mRepository.getItemsOfferCartForNameSync(name,
                mRepository.getmListType().getValue(), mFilterOptions);
        mFilterOptions.clear();
        mSearchForNameMediator.addSource(mSearchForName, itemOfferCarts -> {
            boolean shouldHightlightItem = false;
            if (itemOfferCarts.size() == 0) {
                if (!searchItem.name.equalsIgnoreCase(searchItem.productName) &&
                        !searchItem.productName.equalsIgnoreCase("")) {
                    searchItem.name = searchItem.productName;
                    getSearchItem(searchItem);
                    return;
                }
                if (!searchItem.brandName.equalsIgnoreCase("")) {
                    searchItem.brandName = "";
                    getSearchItem(searchItem);
                    return;
                }
            } else if (!mIsSearchRequestComplete) {
                if (searchItem.isAddToCart ||
                        !searchItem.size.equalsIgnoreCase("")) {

                    if (itemOfferCarts.size() == 1 || itemOfferCarts.get(0).item.confidence > 90) {
                        int size = 1;
                        int currentSize = 1;
                        if (!itemOfferCarts.get(0).item.size.replaceAll("[^0-9]", "")
                                .equalsIgnoreCase("")) {
                            size = Integer.parseInt(
                                    itemOfferCarts.get(0).item.size.replaceAll("[^0-9]", ""));
                        }
                        if (!searchItem.size.replaceAll("[^0-9]", "")
                                .equalsIgnoreCase("")) {
                            currentSize = Integer.parseInt(
                                    searchItem.size.replaceAll("[^0-9]", "")
                            );
                        }
                        int numbers = currentSize / size;

                        int quantity = searchItem.quantity == 0 ? 1 : searchItem.quantity;
                        if (itemOfferCarts.size() > 1) {
                            addItem(itemOfferCarts.get(0), quantity * numbers, 700, searchItem);
                            shouldHightlightItem = true;
                        } else {
                            addItem(itemOfferCarts.get(0), quantity * numbers, 0, searchItem);
                        }
                    }
                }
            }

            ArrayList<ItemListUIModel> itemListUIModels = new ArrayList<>();
            for (ItemOfferCart itemOfferCart : itemOfferCarts) {
                ItemListUIModel itemListUIModel = new ItemListUIModel();
                itemListUIModel.itemOfferCart = itemOfferCart;
                itemListUIModels.add(itemListUIModel);
            }
            if (shouldHightlightItem && itemListUIModels.size() > 0) {
                itemListUIModels.get(0).shouldHightLight = true;
            }

            mSearchForNameMediator.postValue(itemListUIModels);
            mIsSearchRequestComplete = true;
        });
    }

    //Functions/Methods related to list type.
    public void setListType(@ListType String listType) {
        mRepository.switchCategory(listType);
        mFilterOptions.clear();
    }

    public LiveData<String> getListType() {
        return mRepository.getmListType();
    }

    //Method related to activity navigation
    public SingleLiveEvent<Pair<Class, Bundle>> getActivityToStart() {
        return mRepository.getmActivityToStart();
    }

    public void onMicClicked(Activity currentActivity) {
        mRepository.startConversation(currentActivity);
    }

    //Method related to triggering a new slang session
    public SingleLiveEvent<Boolean> startSlangSession() {
        return mRepository.getStartSlangSession();
    }

    public LiveData<Boolean> getMicVisibility() {
        return mRepository.getSlangInitiailzed();
    }

    public LiveData<Boolean> isSlangInitialised() {
        return getMicVisibility();
    }

    public boolean getOnBoardingComplete() {
        return mRepository.getOnBoardingComplete();
    }

    //Method related to other misc operations
    public void sendAppFeedBack(FeedbackItem feedbackItem) {
        mRepository.sendFeedbackItem(feedbackItem);
    }

    public LiveData<List<String>> getItemBrands(@ListType String listType) {
        return mRepository.getItemBrands(listType);
    }

    public LiveData<List<String>> getItemSizes(@ListType String listType) {
        return mRepository.getItemSizes(listType);
    }

    public FilterOptions getFilterOptions() {
        return mFilterOptions;
    }

    public void setFilterOptions(FilterOptions mFilterOptions) {
        this.mFilterOptions = mFilterOptions;
    }
}
