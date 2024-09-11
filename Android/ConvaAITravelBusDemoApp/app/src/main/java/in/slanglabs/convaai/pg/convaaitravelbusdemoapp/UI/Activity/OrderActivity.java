package in.slanglabs.convaai.pg.convaaitravelbusdemoapp.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.JourneyBusPlaceOrder;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.OrderStatus;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.Model.RouteStatus;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.R;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.UI.Adapters.OrderListAdapter;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.UI.ItemClickListener;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.UI.ViewModel.AppViewModel;

public class OrderActivity extends AppCompatActivity {

    private AppViewModel appViewModel;
    private OrderListAdapter listAdapter;
    private List<JourneyBusPlaceOrder> orderItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_order);

        ImageView backButtonView = findViewById(R.id.back_button);
        backButtonView.setOnClickListener(view -> finish());

        TextView emptyJourneyField = findViewById(R.id.journey_empty_text);
        emptyJourneyField.setVisibility(View.GONE);

        RecyclerView listItemView = findViewById(R.id.journey_list_recycler_view);
        appViewModel = new ViewModelProvider(this).get(
                AppViewModel.class);

        appViewModel.getOrderItems().observe(this, new Observer<List<JourneyBusPlaceOrder>>() {
            @Override
            public void onChanged(List<JourneyBusPlaceOrder> orderItems) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        OrderActivity.this.orderItems = orderItems;
                        listAdapter.setList(orderItems);
                        if (orderItems.size() == 0) {
                            emptyJourneyField.setVisibility(View.VISIBLE);
                        } else {
                            emptyJourneyField.setVisibility(View.GONE);
                        }
                    }
                },10);

            }
        });

        listAdapter = new OrderListAdapter(new ItemClickListener() {
            @Override
            public void itemClicked(int position) {
                Log.d("here", "here");
                List<JourneyBusPlaceOrder> orderItems =  OrderActivity.this.orderItems;
                if(orderItems.size() < position) {
                    return;
                }
                JourneyBusPlaceOrder orderItem = orderItems.get(position);
                if(orderItem.order.active != OrderStatus.CANCELED
                        && orderItem.journey.journey.routeStatus != RouteStatus.CANCELED &&
                        (new Date().getTime() < orderItem.order.journeyDate.getTime())) {
                    Intent intent = new Intent(OrderActivity.this, TicketViewActivity.class);
                    intent.putExtra("orderItemId",orderItem.order.orderId);
                    startActivity(intent);
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listItemView.setLayoutManager(layoutManager);
        listItemView.setItemAnimator(null);
        listItemView.setAdapter(listAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}