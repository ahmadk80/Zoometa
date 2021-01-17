package com.mentadev.zoometa.UI.fragments;


import androidx.fragment.app.Fragment;

import java.util.Date;

public class BaseFragment extends Fragment {
    public static String getDateStringFormat(Date date) {
        if(date == null){
            return "";
        }
        return android.text.format.DateFormat.format("dd-MM-yyyy", date).toString();
    }
}
