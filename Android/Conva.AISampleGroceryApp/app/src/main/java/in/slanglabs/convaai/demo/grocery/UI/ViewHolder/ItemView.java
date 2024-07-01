package in.slanglabs.convaai.demo.grocery.UI.ViewHolder;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import java.util.Locale;

import in.slanglabs.convaai.demo.grocery.BuildConfig;
import in.slanglabs.convaai.demo.grocery.Model.CartItem;
import in.slanglabs.convaai.demo.grocery.Model.Item;
import in.slanglabs.convaai.demo.grocery.Model.ListType;
import in.slanglabs.convaai.demo.grocery.Model.Offer;
import in.slanglabs.convaai.demo.grocery.R;
import in.slanglabs.convaai.demo.grocery.UI.ItemClickListener;

public class ItemView extends RecyclerView.ViewHolder {

    private final MaterialCardView mParentView;
    private final TextView mItemName;
    private final TextView mImageName;
    private final TextView mQuantities;
    private final TextView mAddButton;
    private final View mControlButton;
    private final TextView mCurrentNumber;
    private final TextView mPrice;
    private final View mOffersView;
    private final TextView mOfferText;
    private final TextView mBrandName;
    private final ImageView mImageView;
    private final ImageButton mRemoveItem;

    public ItemView(@NonNull View itemView, ItemClickListener itemClickListener) {
        super(itemView);
        mParentView = itemView.findViewById(R.id.parentView);
        mItemName = itemView.findViewById(R.id.item_name);
        mImageName = itemView.findViewById(R.id.image_item_name);
        mBrandName = itemView.findViewById(R.id.item_brand_name);
        mQuantities = itemView.findViewById(R.id.item_quantities);
        mImageView = itemView.findViewById(R.id.imageView);
        mControlButton = itemView.findViewById(R.id.control_buttons);
        mAddButton = itemView.findViewById(R.id.add_button);
        ImageButton addItem;
        addItem = itemView.findViewById(R.id.item_add);
        addItem.setOnClickListener(view -> itemClickListener.addItem(getAdapterPosition()));
        mAddButton.setOnClickListener(view -> itemClickListener.addItem(getAdapterPosition()));
        mRemoveItem = itemView.findViewById(R.id.item_remove);
        mRemoveItem.setOnClickListener(view -> itemClickListener.removeItem(getAdapterPosition()));
        mCurrentNumber = itemView.findViewById(R.id.item_current_number);
        mPrice = itemView.findViewById(R.id.item_price);
        mOffersView = itemView.findViewById(R.id.offer_view);
        mOfferText = itemView.findViewById(R.id.item_offer_text);
        itemView.setOnClickListener(view -> itemClickListener.itemClicked(getAdapterPosition()));
    }

    public void setData(Item listItem, CartItem item, Offer offerItem, boolean shouldHighlight) {
        mParentView.setStrokeColor(Color.WHITE);
        mAddButton.setVisibility(View.VISIBLE);
        mControlButton.setVisibility(View.VISIBLE);
        mCurrentNumber.setVisibility(View.GONE);
        mRemoveItem.setVisibility(View.GONE);
        mQuantities.setVisibility(View.GONE);
        mOffersView.setVisibility(View.GONE);

        mItemName.setText(listItem.name);
        mBrandName.setText(listItem.brand);
        mImageName.setText(String.format(Locale.ENGLISH, "%s", listItem.name));
        if(listItem.imageUrl != null && !listItem.imageUrl.isEmpty()) {
            Glide.with(itemView)
                    .load(listItem.imageUrl)
                    .transition(withCrossFade())
                    .into(mImageView);
        }
        else {
            Glide.with(itemView)
                    .load(getImageUrl(listItem.name, listItem.type))
                    .transition(withCrossFade())
                    .into(mImageView);
        }

        if(!listItem.size.equalsIgnoreCase("")) {
            mQuantities.setText(String.format(Locale.ENGLISH, "%s", listItem.size));
            mQuantities.setVisibility(View.VISIBLE);
        }
        mCurrentNumber.setText("0");
        String priceString = String.format(Locale.ENGLISH, "%s %.1f",
                BuildConfig.CURRENCY_TYPE,
                listItem.price);
        mPrice.setText(priceString);
        if (item != null && offerItem != null) {
            if (item.quantity >= offerItem.minQuantity) {
                float discountedPrice;
                discountedPrice = (float) (listItem.price - (listItem.price * offerItem.percentageDiscount));
                priceString = String.format(Locale.ENGLISH, "%s %.1f\t %s %.1f",
                        BuildConfig.CURRENCY_TYPE, listItem.price, BuildConfig.CURRENCY_TYPE, discountedPrice);
                Spannable spannable = new SpannableString(priceString);
                spannable.setSpan(
                        new StrikethroughSpan(),
                        0, priceString.lastIndexOf("\t"),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mPrice.setText(spannable);
            }
        }
        if (item != null) {
            mCurrentNumber.setText(String.format(Locale.ENGLISH, "%d",
                    item.quantity));
            mAddButton.setVisibility(View.GONE);
            mRemoveItem.setVisibility(View.VISIBLE);
            mCurrentNumber.setVisibility(View.VISIBLE);
        }
        mOfferText.setText("");
        if (offerItem != null) {
            mOffersView.setVisibility(View.VISIBLE);
            mOfferText.setText(String.format(Locale.ENGLISH, "Buy %d and get %d%% off",
                    offerItem.minQuantity,
                    (int) (offerItem.percentageDiscount * 100)));
        }
        if(shouldHighlight) {
            mParentView.setStrokeColor(ContextCompat.getColor(itemView.getContext(),R.color.colorPrimary));
        }
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

        if(name == null) {
            return "";
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
