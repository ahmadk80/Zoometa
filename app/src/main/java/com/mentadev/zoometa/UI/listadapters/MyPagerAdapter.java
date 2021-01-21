package com.mentadev.zoometa.UI.listadapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mentadev.zoometa.R;
import com.mentadev.zoometa.UI.fragments.DeliveryNoteHistoryFragment;
import com.mentadev.zoometa.UI.fragments.UserProfileFragment;

import org.jetbrains.annotations.NotNull;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_ITEMS = 2;
    private int CurrentItem = 0;

    public Context context;
    public MyPagerAdapter(FragmentManager fragmentManager, Context _context) {
        super(fragmentManager);
        context = _context;
    }
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
    @NotNull
    @Override
    public Fragment getItem(int position) {
        setCurrentItem(position);
        switch (position) {
            case 0:
                return DeliveryNoteHistoryFragment.newInstance();
            case 1:
                return UserProfileFragment.newInstance();
            default:
                return null;
        }
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return context.getResources().getString(R.string.delivery_note_history_fragment_title);
            case 1:
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