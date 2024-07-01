package in.slanglabs.convaai.demo.grocery.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

//import in.slanglabs.assistants.retail.RetailUserJourney;
//import in.slanglabs.assistants.retail.SlangRetailAssistant;
//import in.slanglabs.platform.SlangLocale;
import in.slanglabs.convaai.demo.grocery.Model.OrderItem;
import in.slanglabs.convaai.demo.grocery.R;
import in.slanglabs.convaai.demo.grocery.UI.Adapters.OrderAdapter;
import in.slanglabs.convaai.demo.grocery.UI.ItemClickListener;

public class OrderActivity extends MainActivity {

    private OrderAdapter mListAdapter;
    private List<OrderItem> mOrderItems = new ArrayList<>();
    private TextView mOrderEmptyTextView;
    private RecyclerView listItemView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_order, null, false);
        mLinearLayout.addView(contentView, new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Orders");
        }

        mSearchLayoutView.setVisibility(View.GONE);

        mOrderEmptyTextView = contentView.findViewById(R.id.order_empty_text_view);
        mOrderEmptyTextView.setVisibility(View.GONE);

        listItemView = contentView.findViewById(R.id.list_item_view);

        handleIntent(getIntent());

        mAppViewModel.getOrderItems().observe(this,
                orderItems -> {
                    this.mOrderItems = orderItems;
                    mListAdapter.setList(orderItems);
                    if (orderItems.size() == 0) {
                        mOrderEmptyTextView.setVisibility(View.VISIBLE);
                    } else {
                        mOrderEmptyTextView.setVisibility(View.GONE);
                    }
                });

        FloatingActionButton fab = contentView.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(OrderActivity.this, CartActivity.class);
            startActivity(intent);
        });
        TextView cartItemCount = findViewById(R.id.cart_item_count);
        cartItemCount.setVisibility(View.GONE);
        fab.setVisibility(View.GONE);
        mListAdapter = new OrderAdapter(new ItemClickListener() {
            @Override
            public void itemClicked(int position) {
                if(mOrderItems.get(position).active) {
                    mAppViewModel.cancelOrderConfirmation(position + 1);
                }
                handleItemSelected(position);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listItemView.setLayoutManager(layoutManager);
        listItemView.setItemAnimator(null);
        listItemView.setAdapter(mListAdapter);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        mAppViewModel.makeOrderRequest();
    }

    private boolean handleItemSelected(int position) {
        if (position >= mOrderItems.size()) {
            return false;
        }
        if (position == -1) position = mOrderItems.size();
        OrderItem orderItem = mOrderItems.get(position);
        Intent intent = new Intent(OrderActivity.this, OrderItemsActivity.class);
        intent.putExtra("orderItemId", orderItem.orderId);
        startActivity(intent);
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}