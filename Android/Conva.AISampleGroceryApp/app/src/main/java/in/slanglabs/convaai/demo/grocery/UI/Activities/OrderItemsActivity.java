package in.slanglabs.convaai.demo.grocery.UI.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

import in.slanglabs.convaai.demo.grocery.BuildConfig;
import in.slanglabs.convaai.demo.grocery.Model.CartItemOffer;
import in.slanglabs.convaai.demo.grocery.Model.Offer;
import in.slanglabs.convaai.demo.grocery.Model.OrderItem;
import in.slanglabs.convaai.demo.grocery.R;
import in.slanglabs.convaai.demo.grocery.UI.Adapters.OrderCartAdapter;

public class OrderItemsActivity extends BaseActivity {

    private TextView mTotalCost;
    private TextView mTotalSave;
    private RecyclerView mListItemView;
    private Button mRemoveButton;
    private View mOrderControlSection;
    private AlertDialog mAlertDialog;

    private boolean mShowCancelAlert = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_items);
        mRemoveButton = findViewById(R.id.remove_button);
        mListItemView = findViewById(R.id.list_item_view);
        mOrderControlSection = findViewById(R.id.order_control_section);
        mTotalCost = findViewById(R.id.total_cost);
        mTotalSave = findViewById(R.id.total_save);

        handleIntent(getIntent());
        mAppViewModel.getOrderItem()
                .observe(this, this::updateOrderItem);
        mAppViewModel.getShowCancelOrderConfirmation().observe(this, aBoolean -> {
            if(aBoolean != null && aBoolean) {
                mShowCancelAlert = true;
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        mAppViewModel.makeOrderItemRequest(intent.getStringExtra("orderItemId"));
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }
    }

    private void updateOrderItem(OrderItem orderItem) {
        mTotalCost.setText("");
        mTotalSave.setVisibility(View.GONE);
        int sum = 0;
        int sumWithoutDiscount = 0;
        for (CartItemOffer cartItem : orderItem.orderItems) {
            float price = cartItem.cart.quantity * cartItem.item.price;
            sumWithoutDiscount += price;
            Offer offerItem = cartItem.offer;
            if (offerItem != null) {
                if (cartItem.cart.quantity >= offerItem.minQuantity) {
                    price = price - (offerItem.percentageDiscount * price);
                }
            }
            sum += price;
        }
        mTotalCost.setText(String.format(Locale.ENGLISH, "Total: %s %d",
                BuildConfig.CURRENCY_TYPE, sum));
        int saved = sumWithoutDiscount - sum;
        if (saved > 0) {
            mTotalSave.setVisibility(View.VISIBLE);
            mTotalSave.setText(String.format(Locale.ENGLISH, "Saved: %s %d", BuildConfig.CURRENCY_TYPE, saved
            ));
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Order #" + orderItem.orderId.substring(0, 10));
        }
        OrderCartAdapter listAdapter = new OrderCartAdapter(mAppViewModel);
        listAdapter.setList(orderItem.orderItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mListItemView.setLayoutManager(layoutManager);
        mListItemView.setItemAnimator(null);
        mListItemView.setAdapter(listAdapter);
        mRemoveButton.setOnClickListener(view -> {
            showRemoveAction(orderItem);
        });
        if (orderItem.active) {
            mOrderControlSection.setBackgroundColor(Color.BLACK);
            mRemoveButton.setEnabled(true);
            mRemoveButton.setText("CANCEL ORDER");
        } else {
            mOrderControlSection.setBackgroundColor(Color.BLACK);
            mRemoveButton.setEnabled(false);
            mRemoveButton.setText("ORDER CANCELLED");
        }

        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }

        if (mShowCancelAlert && orderItem.active) {
            showRemoveAction(orderItem);
        }
        mShowCancelAlert = false;

    }

    private void showRemoveAction(OrderItem orderItem) {
        mAlertDialog = new AlertDialog.Builder(OrderItemsActivity.this).create();
        mAlertDialog.setTitle("Cancel Order");
        mAlertDialog.setCancelable(false);
        mAlertDialog.setMessage("Are you sure, you want to cancel this order ?");
        mAlertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "YES",
                (dialog, which) -> {
                    mAppViewModel.removeOrderItem(orderItem);
                    dialog.dismiss();
                });
        mAlertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NO",
                (dialog, which) -> {
                    mAppViewModel.holdOrderItem(orderItem);
                    dialog.dismiss();
                });
        mAlertDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}