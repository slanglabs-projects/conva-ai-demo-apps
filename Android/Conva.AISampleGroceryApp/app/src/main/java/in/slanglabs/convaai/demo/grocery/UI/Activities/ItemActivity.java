package in.slanglabs.convaai.demo.grocery.UI.Activities;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Locale;

import in.slanglabs.convaai.demo.grocery.BuildConfig;
import in.slanglabs.convaai.demo.grocery.Model.CartItem;
import in.slanglabs.convaai.demo.grocery.Model.Item;
import in.slanglabs.convaai.demo.grocery.Model.ListType;
import in.slanglabs.convaai.demo.grocery.Model.Offer;
import in.slanglabs.convaai.demo.grocery.R;

public class ItemActivity extends BaseActivity {

    private TextView mItemName;
    private TextView mImageName;
    private TextView mQuantities;
    private Button mAddItem;
    private Button mRemoveItem;
    private Button mAddButton;
    private View mControlButton;
    private TextView mCurrentNumber;
    private TextView mPrice;
    private TextView mOfferText;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        mItemName = findViewById(R.id.item_name);
        mImageName = findViewById(R.id.image_item_name);
        mQuantities = findViewById(R.id.item_quantities);
        mControlButton = findViewById(R.id.control_buttons);
        mAddButton = findViewById(R.id.add_button);
        mAddItem = findViewById(R.id.item_add);
        mCurrentNumber = findViewById(R.id.item_current_number);
        mPrice = findViewById(R.id.item_price);
        mOfferText = findViewById(R.id.item_offer_text);
        mRemoveItem = findViewById(R.id.item_remove);
        mImageView = findViewById(R.id.imageView);

        int itemId = getIntent().getIntExtra("itemId", 0);

        mAppViewModel.getItemForId(itemId).observe(this, itemOfferCart -> {
            handleItem(itemOfferCart.cart, itemOfferCart.offer, itemOfferCart.item);
            mAddItem.setOnClickListener(view -> {
                mAppViewModel.addItem(itemOfferCart.item, true);
            });
            mAddButton.setOnClickListener(view -> {
                mAppViewModel.addItem(itemOfferCart.item, true);
            });
            mRemoveItem.setOnClickListener(view -> {
                mAppViewModel.removeItem(itemOfferCart.item);
            });

            if(itemOfferCart.item.imageUrl != null) {
                Glide.with(this).load(itemOfferCart.item.imageUrl).into(mImageView);
            }
            else {
                Glide.with(this).load(getImageUrl(itemOfferCart.item.name, itemOfferCart.item.type))
                        .into(mImageView);
            }
        });
    }

    private void handleItem(CartItem item, Offer offerItem, Item listItem) {
        mAddButton.setVisibility(View.VISIBLE);
        mControlButton.setVisibility(View.GONE);
        mItemName.setText(listItem.name);
        mImageName.setText(String.format(Locale.ENGLISH, "%s %s", listItem.name, listItem.size));
        mQuantities.setText(String.format(Locale.ENGLISH, "%s", listItem.size));
        mCurrentNumber.setText("0");
        String priceString = String.format(Locale.ENGLISH, "%s %.1f",
                BuildConfig.CURRENCY_TYPE, listItem.price);
        mPrice.setText(priceString);
        if (item != null && offerItem != null) {
            if (item.quantity >= offerItem.minQuantity) {
                float discountedPrice;
                discountedPrice = (float) (listItem.price - (listItem.price * offerItem.percentageDiscount));
                priceString = String.format(Locale.ENGLISH, "%s %.1f\n%s %.1f",
                        BuildConfig.CURRENCY_TYPE, listItem.price, BuildConfig.CURRENCY_TYPE, discountedPrice);
                Spannable spannable = new SpannableString(priceString);
                spannable.setSpan(
                        new StrikethroughSpan(),
                        0, priceString.lastIndexOf("\n"),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mPrice.setText(spannable);
            }
        }
        if (item != null) {
            mCurrentNumber.setText(String.format(Locale.ENGLISH, "%d",
                    item.quantity));
            mAddButton.setVisibility(View.GONE);
            mControlButton.setVisibility(View.VISIBLE);
        }
        mOfferText.setVisibility(View.GONE);
        if (offerItem != null) {
            mOfferText.setVisibility(View.VISIBLE);
            mOfferText.setText(String.format(Locale.ENGLISH, "Buy %d and get %d%% off",
                    offerItem.minQuantity,
                    (int) (offerItem.percentageDiscount * 100)));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private String getImageUrl(String name, @ListType String category) {
        String imageUrl = "";
        switch (category) {
            case ListType.GROCERY:
                imageUrl = "https://www.honestbee.tw/images/placeholder.jpg";
                break;
            case ListType.FASHION:
                break;
            case ListType.PHARMACY:
                imageUrl = "https://previews.123rf.com/images/happyicon/happyicon1805/happyicon180500042/100632499-vector-pills-icon.jpg";
                break;
        }

        if (name.toLowerCase().contains("tomato")) {
            imageUrl = "https://www.bigbasket.com/media/uploads/p/s/40022638_3-fresho-tomato-local-organically-grown.jpg";
        } else if (name.toLowerCase().contains("onion")) {
            imageUrl = "https://www.bigbasket.com/media/uploads/p/s/40023472_3-fresho-onion-organically-grown.jpg";
        } else if (name.toLowerCase().contains("potato")) {
            imageUrl = "https://www.bigbasket.com/media/uploads/p/s/40023476_4-fresho-potato-organically-grown.jpg";
        } else if (name.toLowerCase().contains("maggi")) {
            imageUrl = "https://www.bigbasket.com/media/uploads/p/s/266109_15-maggi-2-minute-instant-noodles-masala.jpg";
        } else if (name.toLowerCase().contains("aashirvaad atta")) {
            imageUrl = "https://www.bigbasket.com/media/uploads/p/s/126906_7-aashirvaad-atta-whole-wheat.jpg";
        }

        return imageUrl;
    }
}