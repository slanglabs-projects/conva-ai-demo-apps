package in.slanglabs.convaai.demo.grocery.UI.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.slanglabs.convaai.demo.grocery.Model.CartItemOffer;
import in.slanglabs.convaai.demo.grocery.R;
import in.slanglabs.convaai.demo.grocery.UI.ItemClickListener;
import in.slanglabs.convaai.demo.grocery.UI.ViewHolder.ItemView;
import in.slanglabs.convaai.demo.grocery.UI.ViewModel.AppViewModel;

public class CartAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemClickListener {

    private AppViewModel mAppViewModel;
    private ItemClickListener mItemClickListener;
    private List<CartItemOffer> list = new ArrayList<>();

    public CartAdapter(AppViewModel appViewModel, ItemClickListener itemClickListener) {
        this.mAppViewModel = appViewModel;
        this.mItemClickListener = itemClickListener;
    }

    public void setList(List<CartItemOffer> list) {
        this.list = list;
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
        viewHolder.setData(list.get(position).item, list.get(position).cart, list.get(position).offer, false);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void addItem(int position) {
        mAppViewModel.addItem(list.get(position).item, false);
    }

    @Override
    public void removeItem(int position) {
        mAppViewModel.removeItem(list.get(position).item);
    }

    @Override
    public void itemClicked(int position) {
        mItemClickListener.itemClicked(list.get(position).item);
    }

}

