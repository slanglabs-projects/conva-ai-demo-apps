package in.slanglabs.convaai.demo.grocery.UI.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import in.slanglabs.convaai.demo.grocery.R;

public class PlaceOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Your Order");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}