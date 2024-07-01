package in.slanglabs.convaai.demo.grocery.UI.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import in.slanglabs.convaai.demo.grocery.Model.CheckoutInfoModel;
import in.slanglabs.convaai.demo.grocery.R;
import in.slanglabs.convaai.demo.grocery.UI.Fragments.DeliveryFragment;
import in.slanglabs.convaai.demo.grocery.UI.Fragments.PickupFragment;
import in.slanglabs.convaai.demo.grocery.UI.ViewModel.CheckoutViewModel;

public class UserDetailsActivity extends BaseActivity {

    private CheckoutViewModel mCheckoutViewModel;
    private SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy");

    LinearLayout mDatePickerLayout;
    TextView mDateTextView;
    Button mPlaceOrder;
    TextView mDelivery;
    TextView mPickup;
    LinearLayout mFragment;

    private EditText mPhoneNumberView;
    private EditText mNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Checkout Details");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
          mDelivery = findViewById(R.id.btn_Delivery);
          mPickup = findViewById(R.id.btn_Pickup);
          mFragment = findViewById(R.id.linearLayoutt);

        FragmentManager fragmentManager1 = getSupportFragmentManager();
        fragmentManager1.beginTransaction()
                .replace(R.id.linearLayoutt, DeliveryFragment.class, null)
                .setReorderingAllowed(true)
                .commit();


        mDelivery.setOnClickListener(view -> {
              mDelivery.setBackgroundResource(R.drawable.color_btn);
              mDelivery.setTextColor(getResources().getColor(R.color.white));
              mPickup.setBackgroundResource(R.drawable.white_btn);
              mPickup.setTextColor(getResources().getColor(R.color.colorPrimary));
              FragmentManager fragmentManager = getSupportFragmentManager();
              fragmentManager.beginTransaction()
                      .replace(R.id.linearLayoutt, DeliveryFragment.class, null)
                      .setReorderingAllowed(true)
                      .commit();

          });
          mPickup.setOnClickListener(view -> {
              mPickup.setBackgroundResource(R.drawable.color_btn);
              mPickup.setTextColor(getResources().getColor(R.color.white));
              mDelivery.setBackgroundResource(R.drawable.white_btn);
              mDelivery.setTextColor(getResources().getColor(R.color.colorPrimary));

              FragmentManager fragmentManager = getSupportFragmentManager();
              fragmentManager.beginTransaction()
                      .replace(R.id.linearLayoutt, PickupFragment.class, null)
                      .setReorderingAllowed(true)
                      .commit();
          });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void showDatePicker() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        Calendar minDate = Calendar.getInstance();
        minDate.set(mYear, mMonth, mDay);

        Calendar maxDate = Calendar.getInstance();
        maxDate.set(mYear, mMonth, mDay + 30);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view1, year, monthOfYear, dayOfMonth) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    Date date = calendar.getTime();
                    mDateTextView.setText(formatter.format(date));
                }, mYear, mMonth, mDay);

        final DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMinDate(minDate.getTimeInMillis());
        datePicker.setMaxDate(maxDate.getTimeInMillis());
        datePickerDialog.show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        if (intent.getSerializableExtra("checkoutInfo") instanceof CheckoutInfoModel) {
            CheckoutInfoModel checkoutInfoModel
                    = (CheckoutInfoModel) intent.getSerializableExtra("checkoutInfo");
            mCheckoutViewModel.handleCheckoutItem(checkoutInfoModel);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}