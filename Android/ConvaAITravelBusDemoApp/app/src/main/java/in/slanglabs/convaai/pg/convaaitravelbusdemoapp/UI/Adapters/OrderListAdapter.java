package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.UI.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.JourneyBusPlaceOrder;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.R;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.UI.ItemClickListener;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.UI.ViewHolder.OrderItemViewHolder;

public class OrderListAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ItemClickListener itemClickListener;

    public OrderListAdapter(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private List<JourneyBusPlaceOrder> list = new ArrayList<>();

    public void setList(List<JourneyBusPlaceOrder> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View orderItem = LayoutInflater
                .from(parent.getContext()).inflate(
                        R.layout.order_list_item,
                        parent, false);
        return new OrderItemViewHolder(orderItem, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OrderItemViewHolder viewHolder = (OrderItemViewHolder) holder;
        viewHolder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
