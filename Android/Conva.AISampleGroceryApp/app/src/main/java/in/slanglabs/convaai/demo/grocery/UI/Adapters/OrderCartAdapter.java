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
import in.slanglabs.convaai.demo.grocery.UI.ViewHolder.OrderCartViewHolder;
import in.slanglabs.convaai.demo.grocery.UI.ViewModel.AppViewModel;

public class OrderCartAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CartItemOffer> mList = new ArrayList<>();

    private AppViewModel mAppViewModel;

    public OrderCartAdapter(AppViewModel appViewModel) {
        this.mAppViewModel = appViewModel;
    }

    public void setList(List<CartItemOffer> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View orderCartItem = LayoutInflater
                .from(parent.getContext()).inflate(
                        R.layout.order_cart_item,
                        parent, false);
        return new OrderCartViewHolder(orderCartItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OrderCartViewHolder viewHolder = (OrderCartViewHolder) holder;
        viewHolder.setData(mList.get(position).item,mList.get(position).cart, mList.get(position).offer);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
