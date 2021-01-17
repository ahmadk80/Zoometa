package com.mentadev.zoometa.UI.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.mentadev.zoometa.R;
import com.mentadev.zoometa.UI.listadapters.MyPagerAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.btnTakePicture);
        fab.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ScanActivity.class)));

        CurrentFragment = R.layout.delivery_note_history_fragment;
        ViewPager vpPager =  findViewById(R.id.viewpager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(), getApplicationContext());
        vpPager.setAdapter(adapterViewPager);
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                CurrentFragment = position;
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });
        tabLayout =  findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);
    }
    TabLayout tabLayout;
    MyPagerAdapter adapterViewPager;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        switch (tabLayout.getSelectedTabPosition()){
            case 0:
                finishAffinity();
            case 1:
                tabLayout.getTabAt(0).select();
        }
    }

    public Integer CurrentFragment;

    @SuppressLint("ResourceType")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
//            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
//        }
        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_profile) {
//            CurrentFragment = R.layout.fragment_user_profile;
//          openFragment(new UserProfileFragment(), getFragmentManager());
//        }
//        if (id == R.id.action_history) {
//            CurrentFragment = R.layout.fragment_delivery_note_history;
//            openFragment(new DeliveryNoteHistoryFragment(), getFragmentManager());
//        }
        if (id == R.id.action_logout) {
            showPopup(this);
        }

        return super.onOptionsItemSelected(item);
    }
    // first step helper function
    private void showPopup(MainActivity mainActivity) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setMessage("Are you sure?")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener()                 {

                    public void onClick(DialogInterface dialog, int which) {

                        LandingActivity.Logout(mainActivity);

                    }
                }).setNegativeButton("Cancel", null);

        AlertDialog alert1 = alert.create();
        alert1.show();
    }
}