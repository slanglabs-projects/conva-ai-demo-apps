package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.UI.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.JourneyBusPlace;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.R;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.UI.ItemClickListener;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.UI.ViewHolder.JourneyViewHolder;

public class JourneyListAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ItemClickListener itemClickListener;

    public JourneyListAdapter(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private List<JourneyBusPlace> list = new ArrayList<>();

    public void setList(List<JourneyBusPlace> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View orderItem = LayoutInflater
                .from(parent.getContext()).inflate(
                        R.layout.journey_list_item,
                        parent, false);
        return new JourneyViewHolder(orderItem,itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        JourneyViewHolder viewHolder = (JourneyViewHolder) holder;
        JourneyBusPlace journeyBusPlace = list.get(position);
        viewHolder.setData(journeyBusPlace);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
