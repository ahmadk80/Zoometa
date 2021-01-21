package com.mentadev.zoometa.UI.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.android.material.textview.MaterialTextView;
import com.mentadev.zoometa.R;
import com.mentadev.zoometa.datamodel.MyProfile;
import com.mentadev.zoometa.exceptionhandling.ExceptionHandling;
import com.mentadev.zoometa.restconnector.FinderREST;
import com.mentadev.zoometa.restconnector.MyProfileInterface;

public class LoginActivity extends Activity {

    static Button login_activity_btn_login;
    static EditText login_activity_txt_username;
    static MaterialTextView login_activity_textinput_error;

    static ImageView imageView;
    static ProgressBar simpleProgressBar;

    public static Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.login_activity);
        login_activity_txt_username = findViewById(R.id.login_activity_txt_username);
        simpleProgressBar = findViewById(R.id.simpleProgressBar);
        login_activity_textinput_error = findViewById(R.id.login_activity_textinput_error);
        login_activity_textinput_error.setVisibility(View.INVISIBLE);
        login_activity_btn_login = findViewById(R.id.login_activity_btn_login);
        imageView = findViewById(R.id.imageView);
        login_activity_btn_login.setOnClickListener(Login);
        login_activity_txt_username.setOnFocusChangeListener((view, b) -> imageView.setVisibility(b? View.GONE : View.VISIBLE));
        login_activity_txt_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                imageView.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    private final View.OnClickListener Login = view -> Login();


    @SuppressLint("HardwareIds")
    private void Login() {
        imageView.setVisibility(View.VISIBLE);
        toogleUserInteraction(true, false);
        LandingActivity.MyProfile = new MyProfile();
        LandingActivity.MyProfile.setDeviceId(Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));
        LandingActivity.MyProfile.setOtp(String.valueOf(login_activity_txt_username.getText()));
        getSharedPreferences(String.valueOf(R.string.shared_preferences_otp), MODE_PRIVATE).edit().putString(String.valueOf(R.string.shared_preferences_otp), LandingActivity.MyProfile.getOtp()).apply();
        LoginToZoomenta(getApplicationContext(), new Intent(getApplicationContext(), MainActivity.class));
    }

    public static void toogleUserInteraction(boolean disabled, boolean errorMode) {
        login_activity_textinput_error.setVisibility(errorMode? View.VISIBLE : View.GONE);
        simpleProgressBar.setVisibility(disabled? View.VISIBLE : View.GONE);
        login_activity_btn_login.setEnabled(!disabled);
        if(disabled){
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        else {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

    }

    public static void LoginToZoomenta(Context context, Intent intent) {
        FinderREST controller = new FinderREST(context, "");
        controller.validateOTP(LandingActivity.MyProfile, new MyProfileInterface.ValidateOTP() {
            @Override
            public void validateOTP(MyProfile myProfile) {
                SuccessLogin(myProfile, context);
//                if(intent == null) {
//                    intent = new Intent(context, MainActivity.class);
//                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                toogleUserInteraction(false, false);
            }
            @Override
            public void OnError(Throwable exception) {
                ExceptionHandling.HandleUIDataEntryValidation(context, exception, login_activity_textinput_error);
                toogleUserInteraction(false, true);
            }

            @Override
            public void OnUnauthorized() {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               context.startActivity(intent);
            }

            @Override
            public void OnResourceNotFound() {
                OnError(new Throwable(context.getResources().getString(R.string.message_error_resource_not_found)));
            }

            @Override
            public void OnInternalServerError() {
                OnError(new Throwable(context.getResources().getString(R.string.message_error_internal_server_error)));

            }

            @Override
            public void OnGenericError() {
                OnError(new Throwable(context.getResources().getString(R.string.message_error_unknown_error)));
            }
        });
    }

    public static void SuccessLogin(MyProfile myProfile, Context context) {
        LandingActivity.MyProfile = myProfile;
        LandingActivity.MyProfile.SaveToSharedPreferences(context);
        Toast.makeText(context, "Welcome: " + LandingActivity.MyProfile.getFullName(), Toast.LENGTH_LONG).show();
    }

}