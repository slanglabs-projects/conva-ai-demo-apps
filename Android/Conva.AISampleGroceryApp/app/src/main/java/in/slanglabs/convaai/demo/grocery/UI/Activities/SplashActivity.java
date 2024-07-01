package in.slanglabs.convaai.demo.grocery.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import in.slanglabs.convaai.demo.grocery.R;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(SplashActivity.this, SearchListActivity.class);
            if (getIntent().getAction() != null && getIntent().getData() != null) {
                mainIntent.putExtra("startConv", true);
                mainIntent.putExtra("page", getIntent().getData().getQueryParameter("page"));
                mainIntent.putExtra("description", getIntent().getData().getQueryParameter("description"));
                mainIntent.putExtra("size", getIntent().getData().getQueryParameter("size"));
            }
            SplashActivity.this.startActivity(mainIntent);
            SplashActivity.this.finish();
        }, 3000);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}