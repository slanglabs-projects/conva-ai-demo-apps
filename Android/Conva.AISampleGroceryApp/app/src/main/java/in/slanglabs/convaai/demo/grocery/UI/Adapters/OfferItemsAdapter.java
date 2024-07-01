package in.slanglabs.convaai.demo.grocery.UI.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.slanglabs.convaai.demo.grocery.Model.OfferItemCart;
import in.slanglabs.convaai.demo.grocery.R;
import in.slanglabs.convaai.demo.grocery.UI.ItemClickListener;
import in.slanglabs.convaai.demo.grocery.UI.ViewHolder.ItemView;
import in.slanglabs.convaai.demo.grocery.UI.ViewHolder.OfferViewHolder;
import in.slanglabs.convaai.demo.grocery.UI.ViewModel.AppViewModel;

public class OfferItemsAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemClickListener {

    public static enum Type {
        HORIZONTAL_LIST,
        VERTICAL_LIST
    }

    private List<OfferItemCart> mList = new ArrayList<>();
    private Type type;

    private AppViewModel mAppViewModel;
    private ItemClickListener mItemClickListener;
    private Integer mMaxSize;

    public OfferItemsAdapter(Type type, AppViewModel appViewModel, ItemClickListener itemClickListener, Integer maxSize) {
        this.mAppViewModel = appViewModel;
        this.mItemClickListener = itemClickListener;
        this.type = type;
        this.mMaxSize = maxSize;
    }

    public void setList(List<OfferItemCart> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (type == Type.VERTICAL_LIST) {
            View retailItem = LayoutInflater
                    .from(parent.getContext()).inflate(
                            R.layout.retail_item,
                            parent, false);
            return new ItemView(retailItem, this);
        } else {
            View retailItem = LayoutInflater
                    .from(parent.getContext()).inflate(
                            R.layout.offer_item,
                            parent, false);
            return new OfferViewHolder(retailItem, mItemClickListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (type == Type.VERTICAL_LIST) {
            ItemView viewHolder = (ItemView) holder;
            viewHolder.setData(mList.get(position).item, mList.get(position).cart, mList.get(position).offer, false);
        } else {
            OfferViewHolder viewHolder = (OfferViewHolder) holder;
            viewHolder.setData(mList.get(position).offer);
        }
    }

    @Override
    public int getItemCount() {
        if (mMaxSize != null) {
            return Math.min(mMaxSize, mList.size());
        }
        return mList.size();
    }

    @Override
    public void itemClicked(int position) {
        this.mItemClickListener.itemClicked(mList.get(position).item);
    }

    @Override
    public void addItem(int position) {
        mAppViewModel.addItem(mList.get(position).item, true);
    }

    @Override
    public void removeItem(int position) {
        mAppViewModel.removeItem(mList.get(position).item);
    }

}