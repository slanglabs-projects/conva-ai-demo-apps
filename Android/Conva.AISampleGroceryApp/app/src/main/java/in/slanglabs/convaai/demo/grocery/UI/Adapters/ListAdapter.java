package in.slanglabs.convaai.demo.grocery.UI.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.slanglabs.convaai.demo.grocery.Model.ItemListUIModel;
import in.slanglabs.convaai.demo.grocery.R;
import in.slanglabs.convaai.demo.grocery.UI.ItemClickListener;
import in.slanglabs.convaai.demo.grocery.UI.ViewHolder.ItemView;
import in.slanglabs.convaai.demo.grocery.UI.ViewModel.AppViewModel;

public class ListAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemClickListener {

    private List<ItemListUIModel> mList = new ArrayList<>();

    private AppViewModel mAppViewModel;
    private ItemClickListener mItemClickListener;

    public ListAdapter(AppViewModel appViewModel, ItemClickListener itemClickListener) {
        this.mAppViewModel = appViewModel;
        this.mItemClickListener = itemClickListener;
    }

    public void setList(List<ItemListUIModel> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public void clear() {
        this.mList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View retailItem = LayoutInflater
                .from(parent.getContext()).inflate(
                        R.layout.retail_item,
                        parent, false);
        return new ItemView(retailItem, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemView viewHolder = (ItemView) holder;
        viewHolder.setData(mList.get(position).itemOfferCart.item, mList.get(position).itemOfferCart.cart, mList.get(position).itemOfferCart.offer, mList.get(position).shouldHightLight);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void itemClicked(int position) {
        this.mItemClickListener.itemClicked(mList.get(position).itemOfferCart.item);
    }

    @Override
    public void addItem(int position) {
        mAppViewModel.addItem(mList.get(position).itemOfferCart.item, true);
    }

    @Override
    public void removeItem(int position) {
        mAppViewModel.removeItem(mList.get(position).itemOfferCart.item);
    }

}
