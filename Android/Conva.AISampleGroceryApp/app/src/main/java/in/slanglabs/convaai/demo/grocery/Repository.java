package in.slanglabs.convaai.demo.grocery;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import in.slanglabs.convaai.demo.grocery.Model.CartItem;
import in.slanglabs.convaai.demo.grocery.Model.CartItemOffer;
import in.slanglabs.convaai.demo.grocery.Model.FeedbackItem;
import in.slanglabs.convaai.demo.grocery.Model.FilterOptions;
import in.slanglabs.convaai.demo.grocery.Model.Item;
import in.slanglabs.convaai.demo.grocery.Model.ItemOfferCart;
import in.slanglabs.convaai.demo.grocery.Model.ListType;
import in.slanglabs.convaai.demo.grocery.Model.Offer;
import in.slanglabs.convaai.demo.grocery.Model.OfferItemCart;
import in.slanglabs.convaai.demo.grocery.Model.OrderBy;
import in.slanglabs.convaai.demo.grocery.Model.OrderItem;
import in.slanglabs.convaai.demo.grocery.Model.PriceRange;
import in.slanglabs.convaai.demo.grocery.Model.SearchItem;
import in.slanglabs.convaai.demo.grocery.UI.Activities.CartActivity;
import in.slanglabs.convaai.demo.grocery.UI.Activities.CheckOutActivity;
import in.slanglabs.convaai.demo.grocery.UI.Activities.OrderActivity;
import in.slanglabs.convaai.demo.grocery.UI.Activities.OrderItemsActivity;
import in.slanglabs.convaai.demo.grocery.UI.Activities.SearchListActivity;
import in.slanglabs.convaai.demo.grocery.convaai.ConvaAIInterface;
import in.slanglabs.convaai.demo.grocery.db.AppDatabase;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;

public class Repository {

    private String TAG = "Repository";

    private final AppDatabase mDatabase;
    private final AppExecutors mAppExecutors;
    private final ConvaAIInterface mConvaAIInterface;
    private final SharedPreferences mPrefs;

    private final MutableLiveData<String> mListType =
            new MutableLiveData<>();

    private final SingleLiveEvent<Pair<Class, Bundle>> mActivityToStart =
            new SingleLiveEvent<>();

    private final SingleLiveEvent<Boolean> mStartSlangSession =
            new SingleLiveEvent<>();

    private final MutableLiveData<Boolean> mSlangInitialized =
            new MutableLiveData<>();

    private final Handler mAddToCartNewJourneyHandler = new Handler();

    private Observer mObserver;

    private int mSelectedSearchItemId = -1;
    private SearchItem mSelectedSearchItem;
    private long mSelectedCartItemId = -1; // Last added item to cart
    private SearchItem mSelectedCartItem;
    private SearchItem mCurrentSearchItem;
    private boolean mAddToCartState = false;

    Repository(final AppDatabase database, final AppExecutors appExecutors,
               final ConvaAIInterface convaAIInterface,
               final SharedPreferences sharedPreferences) {

        this.mDatabase = database;
        this.mAppExecutors = appExecutors;
        mConvaAIInterface = convaAIInterface;
        mPrefs = sharedPreferences;

        mListType.setValue(ListType.GROCERY);

        Random rng = new Random();
        LiveData<List<Item>> items = database.itemDao().getItems();
        mObserver = (Observer<List<Item>>) itemAndOffers -> {
            if (itemAndOffers.size() > 1) {
                items.removeObserver(mObserver);
            } else {
                return;
            }
            List<Offer> offerItems = new ArrayList<>();
            List<Integer> generated = new ArrayList<>();
            int totalNumberOfOffers = (int) (0.3 * itemAndOffers.size());
            for (int i = 0; i < totalNumberOfOffers; i++) {
                while (true) {
                    Integer next = rng.nextInt(itemAndOffers.size());
                    if (!generated.contains(next)) {
                        generated.add(next);
                        break;
                    }
                }
            }
            int counter;
            for (counter = 0; counter < generated.size(); counter++) {
                int randomNumber = generated.get(counter);
                Offer offerItem = new Offer();
                offerItem.offerName = "Offer " + (counter + 1);
                offerItem.itemId = itemAndOffers.get(randomNumber).itemId;
                offerItem.minQuantity = 2 + new Random().nextInt(3);
                offerItem.percentageDiscount = randFloat(0.1f, 0.3f);
                offerItems.add(offerItem);
            }

            appExecutors.diskIO().execute(() -> {
                mDatabase.offerDao().removeAllOffers();
                mDatabase.offerDao().insert(offerItems);
            });
        };

        items.observeForever(mObserver);

    }

    public void switchCategory(@ListType String category) {
        mListType.setValue(category);
        mActivityToStart.setValue(new Pair<>(SearchListActivity.class, null));
    }

    public void setSlangInitialized(boolean value) {
        mSlangInitialized.postValue(value);
    }

    public boolean getOnBoardingComplete() {
        return mPrefs.getBoolean("onBoardingCompleted", false);
    }

    public void setOnBoardingComplete(boolean value) {
        mPrefs.edit().putBoolean("onBoardingCompleted", value).apply();
    }

    //Cart Related Methods

    /**
     * Method to add Item to cart
     * If offer!=null then
     * if offerAccepted addToCartHelper -> notifyAddToCartSuccess
     * if offerRejected notifyAddToCartSuccess
     * else propose offer by notifyPushOffer
     * else
     * addToCartHelper
     */
    public void addItemToCart(ItemOfferCart itemOfferCart, int quantity, boolean uiAction, int withDelay, SearchItem searchItem) {
        Item item = itemOfferCart.item;
        Offer offer = itemOfferCart.offer;
        CartItem cartItem = itemOfferCart.cart;

        // An offer exists
        if (isPromotionAccepted(searchItem.offerAcceptance)) {
            int final_quantity = calculateQuantityToAdd(cartItem.quantity, offer.minQuantity);
            addItemToCartHelper(item, final_quantity, uiAction, withDelay, searchItem);
        } else {
            addItemToCartHelper(item, quantity, uiAction, withDelay, searchItem);
        }
    }

    public void addItemToCartHelper(Item item, int quantity, boolean uiAction, int withDelay, SearchItem searchItem) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            mAppExecutors.diskIO().execute(() -> mDatabase.runInTransaction(() -> {
                CartItem cartItem = mDatabase.cartDao().getCartItemForIdSync(item.itemId);
                if (cartItem != null) {
                    int number = cartItem.quantity;
                    number = number + quantity;
                    cartItem.quantity = number;
                    mDatabase.cartDao().update(cartItem);
                    setSelectedCartItemId(cartItem.cartId);
                    if (searchItem != null) {
                        setSelectedCartItem(searchItem);
                    }
                } else {
                    CartItem cartItemFinal = new CartItem();
                    cartItemFinal.itemId = item.itemId;
                    cartItemFinal.quantity = quantity;
                    long cartId = mDatabase.cartDao().insert(cartItemFinal);
                    setSelectedCartItemId(cartId);
                    if (searchItem != null) {
                        setSelectedCartItem(searchItem);
                    }
                }

            }));
            if (uiAction) {
                mAddToCartNewJourneyHandler.removeCallbacksAndMessages(null);
                mAddToCartNewJourneyHandler.postDelayed(
                        () -> mStartSlangSession.postValue(true), 2000);
            }
        }, withDelay);
    }

    public void removeItemFromCart(Item item) {
        mAppExecutors.diskIO().execute(() -> mDatabase.runInTransaction(() -> {
            CartItem cartItem = mDatabase.cartDao().getCartItemForIdSync(item.itemId);
            if (cartItem == null) {
                return;
            }
            int number = cartItem.quantity;
            number = number - 1;
            if (number == 0) {
                mDatabase.cartDao().remove(cartItem);
                return;
            }
            cartItem.quantity = number;
            mDatabase.cartDao().update(cartItem);
        }));
    }

    public void clearCart() {
        mAppExecutors.diskIO().execute(() -> mDatabase.cartDao().removeAllItems());
    }

    //Order Related Methods
    public LiveData<OrderItem> getOrderItem(String orderItemId) {
        return mDatabase.orderDao().loadOrder(orderItemId);
    }

    public void addOrderItem(OrderItem item) {
        mAppExecutors.diskIO().execute(() -> mDatabase.orderDao().insert(item));
    }

    public void removeOrderItem(OrderItem item) {
        mAppExecutors.diskIO().execute(() -> mDatabase.orderDao().update(false, item.orderId));
    }

    private int segregateNumberFromString(String itemSize) {
        StringBuilder num = new StringBuilder();
        for(int i=0 ; i<itemSize.length() ; i++)
        {
            if (Character.isDigit(itemSize.charAt(i)))
                num.append(itemSize.charAt(i));
        }

        return Integer.parseInt(num.toString());
    }

    public void startConversation(Activity currentActivity) {
        mConvaAIInterface.startConversation(currentActivity);
    }

    //Slang callback handlers
    public void onSearch(String searchTerm) {
        //Move to the search activity with the search term found.
        Bundle bundle = new Bundle();
        SearchItem searchItem = new SearchItem();
        searchItem.name = searchTerm;
        bundle.putSerializable("search_term", searchItem);

        Log.d(TAG, "SearchInfo item not empty");
        mActivityToStart.setValue(new Pair<>(SearchListActivity.class, bundle));
    }

    public void onNavigation(String targetString) {
        //Get the target view string and make sure to navigate to the right view.
        switch (targetString) {
            case "back" ->
                //Set null for the targetActivity to indicate that we just to finish the current one.
                    mActivityToStart.setValue(new Pair<>(null, null));
            case "home" -> mActivityToStart.setValue(new Pair<>(SearchListActivity.class, null));
            case "cart" -> mActivityToStart.setValue(new Pair<>(CartActivity.class, null));
            case "order" -> mActivityToStart.setValue(new Pair<>(OrderActivity.class, null));
            case "checkout" -> mActivityToStart.setValue(new Pair<>(CheckOutActivity.class, null));
        }
    }

    public void showOrder(int orderIndex) {
        mAppExecutors.diskIO().execute(() -> {
            //Note, its advisable to perform all SlangRetailAssistant actions on the main thread.
            List<OrderItem> orderItems = mDatabase.orderDao().loadAllOrdersSync();
            if (orderItems.size() < 1) {
                return;
            } else if (orderIndex > orderItems.size()) {
                return;
            }
            if (orderIndex == -1) {

                //If the index is -1, it means that we need to show the latest/last order
                //Obtain the latest order and move to that view.
                String orderId = orderItems.get(0).orderId;
                mAppExecutors.mainThread().execute(() -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("orderItemId", orderId);
                    mActivityToStart.setValue(new Pair<>(OrderItemsActivity.class, bundle));
                });
                return;
            }

            //If the index is not -1, it means that we need move to the item at that specific index.
            //Obtain the item at that index and move to that view.
            //Note: SlangRetailAssistant will provide the index starting from 1 instead of 0 hence, we need to subtract 1.
            String orderId = orderItems.get(orderIndex - 1).orderId;
            mAppExecutors.mainThread().execute(() -> {
                Bundle bundle = new Bundle();
                bundle.putString("orderItemId", orderId);
                mActivityToStart.setValue(new Pair<>(OrderItemsActivity.class, bundle));
            });
        });
    }

    public void cancelOrderConfirmation(int orderIndex, boolean forceUI) {
        if (!forceUI) {
            cancelOrderConfirmation(orderIndex);
        }
    }

    public void cancelOrderConfirmation(int orderIndex) {
        mAppExecutors.diskIO().execute(() -> {
            //Note, its advisable to perform all SlangRetailAssistant actions on the main thread.
            List<OrderItem> orderItems = mDatabase.orderDao().loadAllOrdersSync();
            if (orderItems.size() < 1) {
                return;
            } else if (orderIndex > orderItems.size()) {
                return;
            }

            if (orderIndex == -1) {

                //If the index is -1, it means that we need to show the latest/last order
                //Obtain the latest order and move to that view.
                String orderId = orderItems.get(0).orderId;
                mAppExecutors.mainThread().execute(() -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("orderItemId", orderId);
                    mActivityToStart.setValue(new Pair<>(OrderItemsActivity.class, bundle));
                });
                return;
            }

            //If the index is not -1, it means that we need move to the item at that specific index.
            //Obtain the item at that index and move to that view.
            String orderId = orderItems.get(orderIndex - 1).orderId;
            mAppExecutors.mainThread().execute(() -> {
                Bundle bundle = new Bundle();
                bundle.putString("orderItemId", orderId);
                mActivityToStart.setValue(new Pair<>(OrderItemsActivity.class, bundle));
            });

        });
    }

    public void acceptOrderCancel(int orderIndex) {
        mAppExecutors.diskIO().execute(() -> {
            //Note, its advisable to perform all SlangRetailAssistant actions on the main thread.

            List<OrderItem> orderItems = mDatabase.orderDao().loadAllOrdersSync();
            if (orderItems.size() < 1) {
                return;
            } else if (orderIndex > orderItems.size()) {
                return;
            }

            if (orderIndex == -1) {

                //If the index is -1, it means that we need to show the latest/last order
                //Obtain the latest order and move to that view.
                mAppExecutors.mainThread().execute(() -> {
                    removeOrderItem(orderItems.get(0));
                    Bundle bundle = new Bundle();
                    bundle.putString("orderItemId", orderItems.get(0).orderId);
                    mActivityToStart.setValue(new Pair<>(OrderItemsActivity.class, bundle));
                });
                return;
            }
            mAppExecutors.mainThread().execute(() -> {
                removeOrderItem(orderItems.get(orderIndex - 1));
                Bundle bundle = new Bundle();
                bundle.putString("orderItemId", orderItems.get(orderIndex - 1).orderId);
                mActivityToStart.setValue(new Pair<>(OrderItemsActivity.class, bundle));
            });
        });
    }

    public void denyOrderCancel(int orderIndex) {
        mAppExecutors.diskIO().execute(() -> {

            //Note, its advisable to perform all SlangRetailAssistant actions on the main thread.
            List<OrderItem> orderItems = mDatabase.orderDao().loadAllOrdersSync();
            if (orderItems.size() < 1) {
                return;
            } else if (orderIndex > orderItems.size()) {
                return;
            }

            if (orderIndex == -1) {
                mAppExecutors.mainThread().execute(() -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("orderItemId", orderItems.get(0).orderId);
                    mActivityToStart.setValue(new Pair<>(OrderItemsActivity.class, bundle));
                });
                return;
            }
            mAppExecutors.mainThread().execute(() -> {
                Bundle bundle = new Bundle();
                bundle.putString("orderItemId", orderItems.get(orderIndex - 1).orderId);
                mActivityToStart.setValue(new Pair<>(OrderItemsActivity.class, bundle));
            });
        });
    }

    public void sendFeedbackItem(FeedbackItem feedbackItem) {
        Log.d(TAG, "feedback " + feedbackItem + " sent");
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<FeedbackItem> jsonAdapter = moshi.adapter(FeedbackItem.class);
        String json = jsonAdapter.toJson(feedbackItem);
        Type type = Types.newParameterizedType(Map.class, String.class, String.class);
        JsonAdapter<Map<String, String>> adapter = moshi.adapter(type);
        try {
            Map<String, String> map = adapter.fromJson(json);
            Map<String, String> journeyDetails = feedbackItem.journeyDetails;
            for (String key : journeyDetails.keySet()) {
                if (map != null) {
                    map.put("journeyDetails." + key, journeyDetails.get(key));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Getters
    public MutableLiveData<String> getmListType() {
        return mListType;
    }

    public SingleLiveEvent<Pair<Class, Bundle>> getmActivityToStart() {
        return mActivityToStart;
    }

    public LiveData<List<ItemOfferCart>> getItems() {
        return mDatabase.itemDao().getItemsOffersCart();
    }

    public LiveData<ItemOfferCart> getItemForId(int id) {
        return mDatabase.itemDao().getItemForId(id);
    }

    public LiveData<List<OfferItemCart>> getOfferItems() {
        return mDatabase.offerDao().getOffersAndItems();
    }

    public LiveData<List<CartItemOffer>> getCartItems() {
        return mDatabase.cartDao().getCartItems();
    }

    public CartItem getCartItemFromItemId(int itemId) {
        return mDatabase.cartDao().getCartItemForIdSync(itemId);
    }

    public LiveData<List<OrderItem>> getOrderItems() {
        return mDatabase.orderDao().loadAllOrders();
    }

    public LiveData<List<ItemOfferCart>> getItemsOfferCartForNameSync(
            String name,
            @ListType String listType,
            FilterOptions filterOptions) {

        MediatorLiveData<List<ItemOfferCart>> itemOfferCartListLiveData = new MediatorLiveData<>();

        StringBuilder stringBuilder = new StringBuilder();
        List<Object> args = new ArrayList<>();
        stringBuilder.append("SELECT * FROM items JOIN itemsFts ON items.itemId == itemsFts.itemId WHERE type =?");
        args.add(listType);
        if (!name.equals("")) {
            stringBuilder.append(" AND itemsFts MATCH ?");
            args.add(fixQuery(name));
        }
        if (!filterOptions.getBrands().isEmpty()) {
            stringBuilder.append(" AND");
            stringBuilder.append(" items.brand IN (");
            appendPlaceholders(stringBuilder, filterOptions.getBrands().size());
            stringBuilder.append(")");
            args.addAll(filterOptions.getBrands());
        }
        if (!filterOptions.getSizes().isEmpty()) {
            stringBuilder.append(" AND");
            stringBuilder.append(" items.size IN (");
            appendPlaceholders(stringBuilder, filterOptions.getSizes().size());
            stringBuilder.append(")");
            args.addAll(filterOptions.getSizes());
        }
        if (!filterOptions.getColors().isEmpty()) {
            stringBuilder.append(" AND");
            stringBuilder.append(" items.color IN (");
            appendPlaceholders(stringBuilder, filterOptions.getColors().size());
            stringBuilder.append(")");
            args.addAll(filterOptions.getColors());
        }
        if (!filterOptions.getGenders().isEmpty()) {
            stringBuilder.append(" AND");
            stringBuilder.append(" items.gender IN (");
            appendPlaceholders(stringBuilder, filterOptions.getGenders().size());
            stringBuilder.append(")");
            args.addAll(filterOptions.getGenders());
        }
        if (!filterOptions.getCategories().isEmpty()) {
            stringBuilder.append(" AND");
            stringBuilder.append(" items.category IN (");
            appendPlaceholders(stringBuilder, filterOptions.getCategories().size());
            stringBuilder.append(")");
            args.addAll(filterOptions.getCategories());
        }
        if (!filterOptions.getPriceRanges().isEmpty()) {
            stringBuilder.append(" AND (");
            for (int i = 0; i < filterOptions.getPriceRanges().size(); i++) {
                PriceRange priceRange = filterOptions.getPriceRanges().get(i);
                if (i != 0) {
                    stringBuilder.append(" OR");
                }
                stringBuilder.append(" (items.price >= ?");
                args.add(priceRange.getStartPrice());
                if (priceRange.getStopPrice() != -1) {
                    stringBuilder.append("AND items.price <= ?)");
                    args.add(priceRange.getStopPrice());
                } else {
                    stringBuilder.append(")");
                }
            }
            stringBuilder.append(")");
        }
        if (!filterOptions.getColors().isEmpty()) {
            stringBuilder.append(" AND");
            stringBuilder.append(" items.color IN (");
            appendPlaceholders(stringBuilder, filterOptions.getColors().size());
            stringBuilder.append(")");
            args.addAll(filterOptions.getColors());
        }
        if (!filterOptions.getUnits().isEmpty()) {
            stringBuilder.append(" AND");
            stringBuilder.append(" items.unit IN (");
            appendPlaceholders(stringBuilder, filterOptions.getUnits().size());
            stringBuilder.append(")");
            args.addAll(filterOptions.getUnits());
        }
        if (!filterOptions.getVariants().isEmpty()) {
            stringBuilder.append(" AND");
            stringBuilder.append(" items.variant IN (");
            appendPlaceholders(stringBuilder, filterOptions.getVariants().size());
            stringBuilder.append(")");
            args.addAll(filterOptions.getVariants());
        }
        if (filterOptions.getOrderBy() == OrderBy.RELEVANCE) {
            stringBuilder.append(" GROUP BY items.name");
            stringBuilder.append(" ORDER BY items.name ASC");
        } else if (filterOptions.getOrderBy() == OrderBy.HIGH_LOW_PRICE) {
            stringBuilder.append(" GROUP BY items.name");
            stringBuilder.append(" ORDER BY items.price DESC");
        } else if (filterOptions.getOrderBy() == OrderBy.LOW_HIGH_PRICE) {
            stringBuilder.append(" GROUP BY items.name");
            stringBuilder.append(" ORDER BY items.price ASC");
        } else if (filterOptions.getOrderBy() == OrderBy.NONE) {
            stringBuilder.append(" GROUP BY items.itemId");
            stringBuilder.append(" ORDER BY items.itemId ASC");
        }
        stringBuilder.append(" LIMIT 300");
        stringBuilder.append(";");

        itemOfferCartListLiveData.addSource(
                mDatabase.itemDao().getItemsAndOffersBasedOnSearchFts(
                        new SimpleSQLiteQuery(stringBuilder.toString(), args.toArray())),
                itemOfferCarts -> {
                    if (mDatabase.getDatabaseCreated().getValue() == null || !mDatabase.getDatabaseCreated().getValue()) {
                        return;
                    }
                    mAppExecutors.diskIO().execute(() -> {
                        Set<String> itemNames = new HashSet<>();
                        HashMap<String, List<ItemOfferCart>> itemNamesObjectMap = new HashMap<>();
                        for (ItemOfferCart itemOfferCart : itemOfferCarts) {
                            itemNames.add(itemOfferCart.item.name);
                            List<ItemOfferCart> itemOfferCartObjects = new ArrayList<>();
                            if (itemNamesObjectMap.containsKey(itemOfferCart.item.name)) {
                                itemOfferCartObjects = itemNamesObjectMap.get(itemOfferCart.item.name);
                            }
                            itemOfferCartObjects.add(itemOfferCart);
                            itemNamesObjectMap.put(itemOfferCart.item.name, itemOfferCartObjects);
                        }
                        List<ItemOfferCart> itemOfferCartObjects = new ArrayList<>();
                        List<ExtractedResult> results = FuzzySearch.extractSorted(name, itemNames);
                        for (ExtractedResult result : results) {
                            List<ItemOfferCart> itemOfferCartMapObjects = itemNamesObjectMap.get(result.getString());
                            for (ItemOfferCart itemOfferCart : itemOfferCartMapObjects) {
                                itemOfferCart.item.confidence = result.getScore();
                                itemOfferCartObjects.add(itemOfferCart);
                            }
                        }
                        itemOfferCartListLiveData.postValue(itemOfferCartObjects);
                    });
                });

        return itemOfferCartListLiveData;
    }

    public LiveData<Boolean> getIsDbCreated() {
        return mDatabase.getDatabaseCreated();
    }

    public LiveData<List<String>> getItemBrands(@ListType String type) {
        return mDatabase.itemDao().getItemsBrands(type);
    }

    public LiveData<List<String>> getItemSizes(@ListType String type) {
        return mDatabase.itemDao().getItemsSizes(type);
    }

    public LiveData<List<String>> getItemColors(@ListType String type) {
        return mDatabase.itemDao().getItemColors(type);
    }

    public LiveData<List<String>> getItemCategories(@ListType String type) {
        return mDatabase.itemDao().getItemCategories(type);
    }

    public LiveData<List<String>> getItemGenders(@ListType String type) {
        return mDatabase.itemDao().getItemGenders(type);
    }

    public void setSelectedSearchItem(int itemId) {
        mSelectedSearchItemId = itemId;
    }

    public void setSelectedSearchItemString(SearchItem item) {
        mSelectedSearchItem = item;
    }

    public void setSelectedCartItemId(long cartItemId) {
        Log.d(TAG, "setSelectedCartItemId: " + cartItemId);
        mSelectedCartItemId = cartItemId;
    }

    public void setSelectedCartItem(SearchItem item) {
        mSelectedCartItem = item;
    }

    public void setCurrentSearchItem(SearchItem searchItem) {
        mCurrentSearchItem = searchItem;
    }

    public void setAddToCartState(boolean isAddToCartState) {
        mAddToCartState = isAddToCartState;
    }

    public SearchItem getCurrentSearchItem() {
        return mCurrentSearchItem;
    }


    public LiveData<Boolean> getSlangInitiailzed() {
        return mSlangInitialized;
    }

    public boolean getAddToCartState() {
        return mAddToCartState;
    }

    //Helpers
    public static float randFloat(float min, float max) {
        Random rand = new Random();
        return rand.nextFloat() * (max - min) + min;
    }

    private static String fixQuery(String query) {
        query = query.replaceAll("[^a-zA-Z0-9]", " ");
        String[] splited = query.split("\\s+");
        StringBuilder finalString = new StringBuilder();
        for (String substring : splited) {
            finalString.append(substring);
            try {
                int num = Integer.parseInt(substring);
                finalString.append(" ");
            } catch (NumberFormatException e) {
                finalString.append("*");
            }
        }
        return finalString.toString();
    }

    private static void appendPlaceholders(StringBuilder builder, int count) {
        for (int i = 0; i < count; i++) {
            builder.append("?");
            if (i < count - 1) {
                builder.append(",");
            }
        }
    }

    public SingleLiveEvent<Boolean> getStartSlangSession() {
        return mStartSlangSession;
    }

    private boolean isPromotionAccepted(String promotionAcceptance) {
        return true;
    }

    private boolean isPromotionDenied(String promotionAcceptance) {
        return true;
    }

    private int calculateQuantityToAdd(int present_cart_quantity, int min_offer_quantity) {
        return min_offer_quantity - (present_cart_quantity % min_offer_quantity);
    }
}
