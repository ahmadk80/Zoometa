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

    TextInputEditText user_properties_fragment_txt_title;
    TextInputEditText user_properties_fragment_txt_email;
    TextInputEditText user_properties_fragment_txt_first_name;
    TextInputEditText user_properties_fragment_txt_middle_name;
    TextInputEditText user_properties_fragment_txt_last_name;
    TextInputEditText user_properties_fragment_txt_date_of_birth;
    TextInputEditText user_properties_fragment_txt_fullname;

    public static UserProfileFragment newInstance(int page, String title) {
        //        Bundle args = new Bundle();
//        args.putInt("someInt", page);
//        args.putString("someTitle", title);
//        fragmentFirst.setArguments(args);
        return new UserProfileFragment();
    }
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.user_profile_fragment, container, false);
        user_properties_fragment_txt_fullname = v.findViewById(R.id.user_properties_fragment_txt_fullname);
//        user_properties_fragment_txt_title = v.findViewById(R.id.user_properties_fragment_txt_title);
        user_properties_fragment_txt_email = v.findViewById(R.id.user_properties_fragment_txt_email);
//        user_properties_fragment_txt_first_name = v.findViewById(R.id.user_properties_fragment_txt_first_name);
//        user_properties_fragment_txt_middle_name = v.findViewById(R.id.user_properties_fragment_txt_middle_name);
//        user_properties_fragment_txt_last_name = v.findViewById(R.id.user_properties_fragment_txt_last_name);
//        user_properties_fragment_txt_date_of_birth = v.findViewById(R.id.user_properties_fragment_txt_date_of_birth);
        FillUserDetails();
        return v;
    }

    private void FillUserDetails() {
//        user_properties_fragment_txt_title.setText(LandingActivity.MyProfile.getTitle());
        user_properties_fragment_txt_fullname.setText(LandingActivity.MyProfile.getFullName());
        user_properties_fragment_txt_email.setText(LandingActivity.MyProfile.getEmail());
//        user_properties_fragment_txt_first_name.setText(LandingActivity.MyProfile.getFirstName());
//        user_properties_fragment_txt_middle_name.setText(LandingActivity.MyProfile.getMiddleName());
//        user_properties_fragment_txt_last_name.setText(LandingActivity.MyProfile.getLastName());
//        user_properties_fragment_txt_date_of_birth.setText(getDateStringFormat(LandingActivity.MyProfile.getDateOfBirth()));
    }

     public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}