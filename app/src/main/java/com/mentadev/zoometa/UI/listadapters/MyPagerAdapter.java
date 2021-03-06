package com.mentadev.zoometa.UI.listadapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mentadev.zoometa.R;
import com.mentadev.zoometa.UI.fragments.DeliveryNoteHistoryFragment;
import com.mentadev.zoometa.UI.fragments.UserProfileFragment;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;
    private int CurrentItem = 0;

    public Context context;
    public MyPagerAdapter(FragmentManager fragmentManager, Context _context) {
        super(fragmentManager);
        context = _context;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        setCurrentItem(position);
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return DeliveryNoteHistoryFragment.newInstance(0, "Page # 1");
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return UserProfileFragment.newInstance(0, "Page # 1");
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: // Fragment # 0 - This will show FirstFragment
                return context.getResources().getString(R.string.delivery_note_history_fragment_title);
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return context.getResources().getString(R.string.user_profile_fragment_title);
            default:
                return null;
        }
    }

    public int getCurrentItem() {
        return CurrentItem;
    }

    public void setCurrentItem(int currentItem) {
        CurrentItem = currentItem;
    }
}