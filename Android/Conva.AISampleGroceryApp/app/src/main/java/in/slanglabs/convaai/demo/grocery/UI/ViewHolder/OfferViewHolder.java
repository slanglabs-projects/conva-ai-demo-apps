package in.slanglabs.convaai.demo.grocery.UI.ViewHolder;

import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.slanglabs.convaai.demo.grocery.Model.Offer;
import in.slanglabs.convaai.demo.grocery.R;
import in.slanglabs.convaai.demo.grocery.UI.ItemClickListener;

public class OfferViewHolder extends RecyclerView.ViewHolder {

    private ImageView mOfferImage;
    private TextView mOfferName;

    public OfferViewHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
        super(itemView);
        mOfferImage = itemView.findViewById(R.id.category_image);
        mOfferName = itemView.findViewById(R.id.category_name);
        this.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.itemClicked(getAdapterPosition());
            }
        });
    }

    public void setData(Offer categoryItem) {
        mOfferImage.getBackground().setColorFilter(categoryItem.color, PorterDuff.Mode.SRC_OVER);
        mOfferName.setText(categoryItem.offerName);
    }
}
