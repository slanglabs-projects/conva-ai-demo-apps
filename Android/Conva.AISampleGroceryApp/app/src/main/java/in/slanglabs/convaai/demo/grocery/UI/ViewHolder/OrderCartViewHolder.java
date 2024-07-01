package in.slanglabs.convaai.demo.grocery.UI.ViewHolder;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Locale;

import in.slanglabs.convaai.demo.grocery.BuildConfig;
import in.slanglabs.convaai.demo.grocery.Model.CartItem;
import in.slanglabs.convaai.demo.grocery.Model.Item;
import in.slanglabs.convaai.demo.grocery.Model.ListType;
import in.slanglabs.convaai.demo.grocery.Model.Offer;
import in.slanglabs.convaai.demo.grocery.R;

public class OrderCartViewHolder extends RecyclerView.ViewHolder {

    private TextView mItemName;
    private TextView mQuantities;
    private TextView mCurrentNumber;
    private TextView mPrice;
    private TextView mBrandName;
    private ImageView mImageView;

    public OrderCartViewHolder(@NonNull View itemView) {
        super(itemView);
        mItemName = itemView.findViewById(R.id.item_name);
        mBrandName = itemView.findViewById(R.id.item_brand_name);
        mQuantities = itemView.findViewById(R.id.item_quantities);
        mCurrentNumber = itemView.findViewById(R.id.item_total_quantity);
        mPrice = itemView.findViewById(R.id.item_price);
        mImageView = itemView.findViewById(R.id.imageView);
    }

    public void setData(Item item, CartItem cartItem, Offer offerItem) {
        mItemName.setText(item.name);
        mBrandName.setText(item.brand);
        mQuantities.setText(String.format(Locale.ENGLISH, "%s", item.size));
        mCurrentNumber.setText("0");
        mCurrentNumber.setText(String.format(Locale.ENGLISH, "Quantity : %d", cartItem.quantity));
        if(item.imageUrl != null) {
            Glide.with(itemView).load(item.imageUrl).into(mImageView);
        }
        else {
            Glide.with(itemView).load(getImageUrl(item.name, item.type)).into(mImageView);
        }

        int totalPrice = (int) (cartItem.quantity * item.price);
        String priceString = String.format(Locale.ENGLISH, "%s %d",
                BuildConfig.CURRENCY_TYPE, totalPrice);
        mPrice.setText(priceString);

        if (offerItem == null) {
            return;
        }
        if (cartItem.quantity >= offerItem.minQuantity) {
            int discountedPrice;
            discountedPrice = (int) (totalPrice - (totalPrice * offerItem.percentageDiscount));
            priceString = String.format(Locale.ENGLISH, "%s %d\n%s %d",
                    BuildConfig.CURRENCY_TYPE, totalPrice, BuildConfig.CURRENCY_TYPE, discountedPrice);
        }
        Spannable spannable = new SpannableString(priceString);
        if (cartItem.quantity >= offerItem.minQuantity) {
            spannable.setSpan(
                    new StrikethroughSpan(),
                    0, priceString.lastIndexOf("\n"),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        mPrice.setText(spannable);

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
