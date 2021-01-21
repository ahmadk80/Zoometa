package com.mentadev.zoometa.UI.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;
import com.mentadev.zoometa.R;
import com.mentadev.zoometa.UI.activities.LandingActivity;

public class UserProfileFragment extends BaseFragment {
    TextInputEditText user_properties_fragment_txt_email;
    TextInputEditText user_properties_fragment_txt_fullname;
    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_profile_fragment, container, false);
        user_properties_fragment_txt_fullname = v.findViewById(R.id.user_properties_fragment_txt_fullname);
        user_properties_fragment_txt_email = v.findViewById(R.id.user_properties_fragment_txt_email);
        FillUserDetails();
        return v;
    }

    private void FillUserDetails() {
        user_properties_fragment_txt_fullname.setText(LandingActivity.MyProfile.getFullName());
        user_properties_fragment_txt_email.setText(LandingActivity.MyProfile.getEmail());
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}