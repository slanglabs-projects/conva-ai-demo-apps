package in.slanglabs.convaai.demo.grocery.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import in.slanglabs.convaai.demo.grocery.R;

public class FaqActivity extends AppCompatActivity {

    protected Toolbar mToolbar;
    protected ImageButton mCartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setTitle("Help/FAQs");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mCartButton = mToolbar.findViewById(R.id.cart_button);


        mCartButton.setOnClickListener(view -> {
              Intent intent = new Intent(FaqActivity.this, CartActivity.class);
              startActivity(intent);
        });



    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}