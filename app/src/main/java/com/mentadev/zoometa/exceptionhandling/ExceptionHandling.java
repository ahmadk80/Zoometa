package com.mentadev.zoometa.exceptionhandling;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textview.MaterialTextView;
import com.mentadev.zoometa.UI.activities.MainActivity;
import com.mentadev.zoometa.restconnector.FinderREST;
import com.mentadev.zoometa.restconnector.MyProfileInterface;
import com.mentadev.zoometa.restconnector.NoConnectivityException;
import com.mentadev.zoometa.restconnector.ResponseEnvelop;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class ExceptionHandling {

    public static void LogMyError(Context context, String displayMsg, String logMsg) {
        Toast.makeText(context, displayMsg, Toast.LENGTH_LONG).show();
        Log.d("FINDMEA_ERROR", logMsg);
    }

    public static void HandleRetrofitFailure(Throwable t, MyProfileInterface.MainInterface callBackInterface) {
        t.printStackTrace();
        callBackInterface.OnError(t);
    }

    public static void HandleUIDataEntryValidation(Context context, String displayMessage, String logMessage, MaterialTextView materialTextView) {
        materialTextView.setVisibility(View.VISIBLE);
        materialTextView.setText(displayMessage);
        LogMyError(context, displayMessage, logMessage);
    }


    public static void HandleUIDataEntryValidation(Context context, Throwable e, MaterialTextView materialTextView) {
//        materialTextView.setVisibility(View.VISIBLE);
//        materialTextView.setText(e.getMessage());
//        LogMyError(context, e.getMessage(), e.toString());
        HandleUIDataEntryValidation(context, e.getMessage(), e.toString(), materialTextView);
    }


    public static void HandleRetrofitReturnError(Response response, MyProfileInterface.MainInterface callBackInterface) {
        switch (response.code()) {
            case 401:
                callBackInterface.OnUnauthorized();
                break;
            case 404:
                callBackInterface.OnResourceNotFound();
                break;
            case 500:
                callBackInterface.OnInternalServerError();
                break;
            default:
                callBackInterface.OnGenericError();
        }
        callBackInterface.OnError(new Throwable(response.errorBody().toString()));
    }
}
