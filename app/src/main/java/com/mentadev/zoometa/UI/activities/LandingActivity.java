package com.mentadev.zoometa.UI.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.mentadev.zoometa.R;
import com.mentadev.zoometa.datamodel.MyProfile;
import com.mentadev.zoometa.exceptionhandling.ExceptionHandling;
import com.mentadev.zoometa.restconnector.FinderREST;
import com.mentadev.zoometa.restconnector.MyProfileInterface;
import com.mentadev.zoometa.restconnector.NetworkConnectionInterceptor;
import com.mentadev.zoometa.restconnector.NoConnectivityException;

import java.util.Date;


public class LandingActivity extends AppCompatActivity {

    static public MyProfile MyProfile;
    MaterialButton landing_fragment_btn_retry;
    MaterialButton landing_fragment_btn_go_to_login;
    MaterialTextView landing_fragment_txt_error;
    ProgressBar simpleProgressBar;
    Activity activity;

    public static void Logout(Activity activity) {
        MyProfile.DeleteFromSharedPreferences(activity);
        Intent activityIntent = new Intent(activity, LandingActivity.class);
        activity.startActivity(activityIntent);
    }

    public static Boolean getIsLoggedIn() {
        return MyProfile != null;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_activity_layout);
        activity = this;
        landing_fragment_btn_retry = findViewById(R.id.landing_fragment_btn_retry);
        simpleProgressBar = findViewById(R.id.simpleProgressBar);
        landing_fragment_txt_error = findViewById(R.id.landing_fragment_txt_error);
        landing_fragment_btn_go_to_login = findViewById(R.id.landing_fragment_btn_go_to_login);
        landing_fragment_btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReloadActivity();
            }
        });
        landing_fragment_btn_go_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartLoginActivitiy();
            }
        });
        landing_fragment_btn_retry.setVisibility(View.INVISIBLE);
        landing_fragment_btn_go_to_login.setVisibility(View.INVISIBLE);
       // BuildMyProfile();
        ReloadActivity();
    }

    private void BuildMyProfile() {
        MyProfile = new MyProfile();
        MyProfile.setUserId(1);
        MyProfile.setDateOfBirth(new Date());
        MyProfile.setEmail("ahmadk80@gmail.com");
        MyProfile.setFirstName("Ahmad");
        MyProfile.setLastName("Kaaki");
        MyProfile.setMiddleName("Mohieddine");
        MyProfile.setOtp("123456");
        MyProfile.setTitle("Mr.");
        MyProfile.SaveToSharedPreferences(activity);
    }

    public void ReloadActivity() {

        FinderREST controller = new FinderREST(getApplicationContext(), "");
        MyProfile = MyProfile.GetFromSharedPreferences(this);
        if (MyProfile != null) {
            MyProfile.setOtp(getApplicationContext().getSharedPreferences(String.valueOf(R.string.shared_preferences_otp), MODE_PRIVATE)
                    .getString(String.valueOf(R.string.shared_preferences_otp), null));
            //getSharedPreferences(String.valueOf(R.string.shared_preferences_otp), MODE_PRIVATE).toString());
            controller.validateOTP(LandingActivity.MyProfile, new MyProfileInterface.ValidateOTP() {
                @Override
                public void validateOTP(MyProfile myProfile) {
                  LoginActivity.SuccessLogin(myProfile, getApplicationContext());
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplication().startActivity(intent);
                }
                @Override
                public void OnError(Throwable exception) {
                    ExceptionHandling.HandleUIDataEntryValidation(getApplicationContext(), exception, landing_fragment_txt_error);
                    if (exception instanceof NoConnectivityException) {
                        landing_fragment_btn_retry.setVisibility(View.VISIBLE);
                    } else {
                        landing_fragment_btn_go_to_login.setVisibility(View.VISIBLE);
                    }
                    simpleProgressBar.setVisibility(View.GONE);

                }

                @Override
                public void OnUnauthorized() {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent);
                }

                @Override
                public void OnResourceNotFound() {
                    OnError(new Throwable(getApplicationContext().getResources().getString(R.string.message_error_resource_not_found)));
                }

                @Override
                public void OnInternalServerError() {
                    OnError(new Throwable(getApplicationContext().getResources().getString(R.string.message_error_internal_server_error)));

                }

                @Override
                public void OnGenericError() {
                    OnError(new Throwable(getApplicationContext().getResources().getString(R.string.message_error_unknown_error)));
                }
            });
        } else {
            StartLoginActivitiy();
        }
    }

    private void StartLoginActivitiy() {
        Intent activityIntent = new Intent(this, LoginActivity.class);
        startActivity(activityIntent);
    }
}