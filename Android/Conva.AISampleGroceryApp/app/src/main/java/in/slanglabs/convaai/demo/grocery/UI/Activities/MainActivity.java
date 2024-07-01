package in.slanglabs.convaai.demo.grocery.UI.Activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Locale;

import in.slanglabs.convaai.demo.grocery.BuildConfig;
import in.slanglabs.convaai.demo.grocery.Model.FeedbackItem;
import in.slanglabs.convaai.demo.grocery.Model.ListType;
import in.slanglabs.convaai.demo.grocery.Model.SearchItem;
import in.slanglabs.convaai.demo.grocery.R;
import in.slanglabs.convaai.demo.grocery.UI.Fragments.SearchDialogFragment;

public class MainActivity extends BaseActivity {

    protected LinearLayout mLinearLayout;
    protected View mSearchLayoutView;
    protected TextView mSearchTextView;
    protected ImageButton mFilterButton;
    protected ImageButton mSortButton;
    protected ImageButton mClearButton;
    protected ImageButton mFeedbackButton;
    protected Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    protected ImageButton mVoiceButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mSearchTextView = findViewById(R.id.search_text);
        mSearchLayoutView = findViewById(R.id.search_layout);
        mFeedbackButton = findViewById(R.id.feedback_button);
        mFilterButton = mToolbar.findViewById(R.id.filter_button);
        mFilterButton.setVisibility(View.GONE);
        mSortButton = mToolbar.findViewById(R.id.sort_button);
        mSortButton.setVisibility(View.GONE);
        mClearButton = findViewById(R.id.clear_image);
        mClearButton.setVisibility(View.GONE);

        mVoiceButton = findViewById(R.id.mic_button_inline);
        mVoiceButton.setOnClickListener(v -> {
            if (mAppViewModel.getMicVisibility().getValue() == null ||
                    !mAppViewModel.getMicVisibility().getValue()) {
                Toast.makeText(
                        this,
                        mAppViewModel.mMicDisabledMessage,
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }
            mAppViewModel.onMicClicked(this);
            hideOnboardingCoachmark();
        });

        mDrawerLayout = findViewById(R.id.activity_main);
        mLinearLayout = findViewById(R.id.layout_main);
        ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.Open, R.string.Close);

        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        NavigationView mNavigationView = findViewById(R.id.nv);
        TextView textView = mNavigationView.findViewById(R.id.app_version);
        textView.setText(String.format(Locale.ENGLISH,
                "Version : %s(%d)",
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE));
        mNavigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent;
            if (id == R.id.home) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                mAppViewModel.setListType(ListType.GROCERY);
            } else if (id == R.id.my_orders) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                intent = new Intent(MainActivity.this, OrderActivity.class);
                startActivity(intent);
            } else if (id == R.id.my_offers) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                intent = new Intent(MainActivity.this, OffersActivity.class);
                startActivity(intent);
            } else if (id == R.id.idBtnSettings) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            } else if (id == R.id.help_faq) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                intent = new Intent(MainActivity.this, FaqActivity.class);
                startActivity(intent);
            }

            return true;

        });

        mFeedbackButton.setOnClickListener(view -> {
            FeedbackItem feedbackItem = new FeedbackItem();
        });

        mSearchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        mVoiceButton.setAlpha(0.5f);
        mAppViewModel.getMicVisibility().observe(this, aBoolean -> {
                    if (aBoolean) {
                        mVoiceButton.setAlpha(1.0f);
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
        return super.onOptionsItemSelected(item);
    }

    protected void showDialog() {
        SearchDialogFragment newFragment = SearchDialogFragment.newInstance("");
        newFragment.mViewItemListener = item -> {
            Intent intent = new Intent(MainActivity.this,
                    SearchListActivity.class);
            SearchItem searchItem = new SearchItem();
            searchItem.name = item;
            intent.putExtra("search_term", searchItem);
            startActivity(intent);
        };
        newFragment.show(getSupportFragmentManager(), "autoCompleteFragment");
    }

    private void hideOnboardingCoachmark() {
    }

    private ArrayAdapter<String> mAdapter;

    private void showListDialog() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);

        builderSingle.setTitle("Please speak out your list...");
        builderSingle.setNegativeButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_checked);

        builderSingle.setAdapter(mAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });
        builderSingle.show();
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String extra = intent.getStringExtra("streaming_object");
            if (extra.equals("begin")) showListDialog();
            else if (!extra.equals("end")) mAdapter.add(extra);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, new IntentFilter(BROADCAST_FILTER));
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

}