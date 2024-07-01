package in.slanglabs.convaai.demo.grocery.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.lifecycle.ViewModelProvider;

import java.util.Date;
import java.util.UUID;

import in.slanglabs.convaai.demo.grocery.Model.CartItemOffer;
import in.slanglabs.convaai.demo.grocery.Model.Offer;
import in.slanglabs.convaai.demo.grocery.Model.OrderItem;
import in.slanglabs.convaai.demo.grocery.R;
import in.slanglabs.convaai.demo.grocery.UI.ViewModel.AppViewModel;

public class CheckOutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        mAppViewModel = new ViewModelProvider(this).get(
                AppViewModel.class);
        mAppViewModel.getCartItems().observe(this,
                cartItems -> {
                    if (cartItems.size() == 0) {
                        onBackPressed();
                        return;
                    }
                    int sum = 0;
                    for (CartItemOffer cartItem : cartItems) {
                        float price = cartItem.cart.quantity * cartItem.item.price;
                        Offer offerItem =  cartItem.offer;
                        if (offerItem != null) {
                            if (cartItem.cart.quantity >= offerItem.minQuantity) {
                                price = price - (offerItem.percentageDiscount * price);
                            }
                        }
                        sum += price;
                    }
                    OrderItem orderItem = new OrderItem();
                    orderItem.orderId = UUID.randomUUID().toString();
                    orderItem.numberOfItems = cartItems.size();
                    orderItem.orderPrice = sum;
                    orderItem.active = true;
                    orderItem.orderTime = new Date();
                    orderItem.orderItems = cartItems;
                    mAppViewModel.addOrderItem(orderItem);
                    mAppViewModel.clearCart();
                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(CheckOutActivity.this, SearchListActivity.class);
                        startActivity(intent);
                    }, 2000);

                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}