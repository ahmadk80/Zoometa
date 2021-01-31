package com.mentadev.zoometa.restconnector;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mentadev.zoometa.R;
import com.mentadev.zoometa.datamodel.DeliveryDetailsRequest;
import com.mentadev.zoometa.datamodel.DeliveryNoteDetails;
import com.mentadev.zoometa.datamodel.DeliveryNoteScanRequest;
import com.mentadev.zoometa.datamodel.DeliveryNoteScanning;
import com.mentadev.zoometa.datamodel.MyProfile;
import com.mentadev.zoometa.datamodel.ScanningHistoryRequest;
import com.mentadev.zoometa.exceptionhandling.ExceptionHandling;


import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FinderREST {
    MyProfile myMyProfile;
    private final Context myContext;
    public static Retrofit retrofit;

    public FinderREST(Context context, String token) {


        this.myContext = context;
        String BASE_URL = myContext.getResources().getString(R.string.settings_url_rest_api);
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .setLenient()
                .create();

        OkHttpClient okHttpClient = getClient(token, context);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static GsonConverterFactory getGsonBuilder() {
        return
                GsonConverterFactory.create(new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                        .setLenient()
                        .create());
    }


    private static OkHttpClient getClient(String token, Context context) {
        //Create OKHttp Client used by retrofit
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        NetworkConnectionInterceptor interceptor1 = new NetworkConnectionInterceptor(context);
        OkHttpClient okHttpClient;
        if (!token.equals("")) {
            TokenInterceptor tokenInterceptor = new TokenInterceptor();
            tokenInterceptor.token = token;
            okHttpClient =
                    new OkHttpClient.Builder()
                            .addInterceptor(tokenInterceptor)
                            .addInterceptor(interceptor)
                            .addInterceptor(interceptor1)
                            //.addNetworkInterceptor(tokenInterceptor)
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(15, TimeUnit.SECONDS)
                            .build();

        } else {
            okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .addInterceptor(interceptor1)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .build();
        }
        return okHttpClient;
    }

    public void validateOTP(MyProfile myProfile, final MyProfileInterface.ValidateOTP callBackInterface) {
//        myProfile.setDeviceId("0");
        retrofit.create(MyProfileInterface.class).validateOTP(myProfile).enqueue(new Callback<ResponseEnvelop<MyProfile>>() {
            @Override
            public void onResponse(@NotNull Call<ResponseEnvelop<MyProfile>> call, @NotNull Response<ResponseEnvelop<MyProfile>> response) {
                if (response.isSuccessful()) {
                    if(response.body().getData() != null) {
                        Gson gson = new Gson();
                        myMyProfile = new Gson().fromJson(gson.toJsonTree(response.body().getData()).getAsJsonObject(), MyProfile.class);
                        callBackInterface.validateOTP(myMyProfile);
                    }else {
                        callBackInterface.OnError(new Throwable(response.body().getMessage()));
                    }
                } else {
                    ExceptionHandling.HandleRetrofitReturnError(response, callBackInterface);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseEnvelop<MyProfile>> call, @NotNull Throwable t) {
                ExceptionHandling.HandleRetrofitFailure(t, callBackInterface, call, myContext);
            }

        });
    }

    public void sendScannedValue(DeliveryNoteScanRequest code, final MyProfileInterface.SendScannedValue callBackInterface) {


        retrofit.create(MyProfileInterface.class).sendScannedValue(code).enqueue(new Callback<ResponseEnvelop<Object>>() {
            @Override
            public void onResponse(@NotNull Call<ResponseEnvelop<Object>> call, @NotNull Response<ResponseEnvelop<Object>> response) {
                if (response.isSuccessful()) {
                    callBackInterface.SendScannedValue(response.body().getMessage());
                } else {
                    ExceptionHandling.HandleRetrofitReturnError(response, callBackInterface);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseEnvelop<Object>> call, @NotNull Throwable t) {
                ExceptionHandling.HandleRetrofitFailure(t, callBackInterface, call, myContext);
            }
        });
    }

    public void getScanningHistory(String fromDate, String toDate, final MyProfileInterface.ScanningHistoryInterface callBackInterface) {
        ScanningHistoryRequest scanningHistoryRequest = new ScanningHistoryRequest();
        scanningHistoryRequest.setFrom(fromDate);
        scanningHistoryRequest.setTo(toDate);
        retrofit.create(MyProfileInterface.class).getScanningHistory(scanningHistoryRequest).enqueue(new Callback<ResponseEnvelop<List<DeliveryNoteScanning>>>() {
            @Override
            public void onResponse(@NotNull Call<ResponseEnvelop<List<DeliveryNoteScanning>>> call, @NotNull Response<ResponseEnvelop<List<DeliveryNoteScanning>>> response) {
                if (response.isSuccessful()) {
                    callBackInterface.getScanningHistory(response.body().getData());
                } else {
                    ExceptionHandling.HandleRetrofitReturnError(response, callBackInterface);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseEnvelop<List<DeliveryNoteScanning>>> call, @NotNull Throwable t) {
                ExceptionHandling.HandleRetrofitFailure(t, callBackInterface, call, myContext);
            }
        });
    }


    public void getDeliverNoteDetails(String deliveryNoteId, final MyProfileInterface.DeliverNoteDetailsInterface callBackInterface) {
        DeliveryDetailsRequest deliveryDetailsRequest = new DeliveryDetailsRequest();
        deliveryDetailsRequest.setDeliveryNoteId(deliveryNoteId);
        retrofit.create(MyProfileInterface.class).getDeliveryNoteDetails(deliveryDetailsRequest).enqueue(new Callback<ResponseEnvelop<List<DeliveryNoteDetails>>>() {
            @Override
            public void onResponse(@NotNull Call<ResponseEnvelop<List<DeliveryNoteDetails>>> call, @NotNull Response<ResponseEnvelop<List<DeliveryNoteDetails>>> response) {
                if (response.isSuccessful()) {
                    callBackInterface.getDeliverNoteDetailsInterface(response.body().getData());
                } else {
                    ExceptionHandling.HandleRetrofitReturnError(response, callBackInterface);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseEnvelop<List<DeliveryNoteDetails>>> call, @NotNull Throwable t) {
                ExceptionHandling.HandleRetrofitFailure(t, callBackInterface, call, myContext);
            }
        });
    }
}