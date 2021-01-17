package com.mentadev.zoometa.datamodel;

import android.content.Context;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mentadev.zoometa.R;
import com.mentadev.zoometa.UI.activities.LandingActivity;


import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class MyProfile  {
    private double userId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String fullName;
  //  private Date dob;
    private Date dateOfBirth;
    private String title;
    private String otp;

    private String email;
    private String deviceId;
    private String token;


    public double getUserId() {
        return userId;
    }

    public void setUserId(double userId) {
        this.userId = userId;
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }
  /*  public long getDateOfBirth() {
        if(getDob() == null){
            return 0;
        }
        return dob.getTime();
    }
    public void setDateOfBirth(long _dateOfBirth) {
        dateOfBirth = _dateOfBirth;
    }*/
    public void setDateOfBirth(Date _dateOfBirth)  {
        dateOfBirth = _dateOfBirth;
       /* SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date newDate = null;
        try {
            newDate = format.parse(dateOfBirth.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.dateOfBirth = newDate;*/
       //dob = _dateOfBirth;
      // setDateOfBirth(_dateOfBirth.getTime());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



//    public String getFullName() {
//        return getFirstName() + " " + getLastName();
//    }

    public String toGsonString() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
        return gson.toJson(this);
    }
    public void SaveToSharedPreferences(Context context)
    {
        context.getSharedPreferences(String.valueOf(R.string.shared_preferences_login_object), MODE_PRIVATE).edit()
                .putString(String.valueOf(R.string.shared_preferences_login_object), this.toGsonString()).commit();
        LandingActivity.MyProfile = this;
    }
    public static void DeleteFromSharedPreferences(Context context)
    {
        context.getSharedPreferences(String.valueOf(R.string.shared_preferences_login_object), MODE_PRIVATE).edit()
                .remove(String.valueOf(R.string.shared_preferences_login_object)).commit();
        LandingActivity.MyProfile = null;
    }
    public static MyProfile GetFromSharedPreferences(Context context) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
        String finderString = context.getSharedPreferences(String.valueOf(R.string.shared_preferences_login_object), MODE_PRIVATE)
                .getString(String.valueOf(R.string.shared_preferences_login_object), null);
        return gson.fromJson(finderString, MyProfile.class);
    }



    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }
}
