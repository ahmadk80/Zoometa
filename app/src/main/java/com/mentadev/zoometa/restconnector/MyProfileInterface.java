package com.mentadev.zoometa.restconnector;

import com.mentadev.zoometa.datamodel.DeliveryDetailsRequest;
import com.mentadev.zoometa.datamodel.DeliveryNoteDetails;
import com.mentadev.zoometa.datamodel.DeliveryNoteScanRequest;
import com.mentadev.zoometa.datamodel.DeliveryNoteScanning;
import com.mentadev.zoometa.datamodel.MyProfile;
import com.mentadev.zoometa.datamodel.ScanningHistoryRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;

public interface MyProfileInterface {
    interface MainInterface {
        void OnError(Throwable exception);
        void OnUnauthorized();
        void OnResourceNotFound();
        void OnInternalServerError();
        void OnGenericError();

    }
    interface ValidateOTP extends MainInterface {
        void validateOTP(MyProfile myProfile);

    }
    interface ScanningHistoryInterface  extends MainInterface {
        void getScanningHistory(List<DeliveryNoteScanning> deliveryNoteScannings);
    }

    interface SendScannedValue  extends MainInterface {
        void SendScannedValue(String scanningResponseMessage);
    }
    interface   DeliverNoteDetailsInterface extends MainInterface {
        void getDeliverNoteDetailsInterface(List<DeliveryNoteDetails> deliveryNoteDetails);
    }
    //@GET("save_user_profile/")
    @Headers({"Content-Type: application/json"})
    @HTTP(method = "POST", path = "login", hasBody = true)
    Call<ResponseEnvelop<MyProfile>> validateOTP(@Body MyProfile myProfile);


    //@GET("save_user_profile/")
    @Headers({"Content-Type: application/json"})
    @HTTP(method = "POST", path = "ScanDeliveryNote/", hasBody = true)
    Call<ResponseEnvelop<Object>> sendScannedValue(@Body DeliveryNoteScanRequest deliveryNoteUniqueId);

    @Headers({"Content-Type: application/json"})
    @HTTP(method = "POST", path = "DeliveryNoteHistory/", hasBody = true)
    Call<ResponseEnvelop<List<DeliveryNoteScanning>>> getScanningHistory(@Body ScanningHistoryRequest scanningHistoryRequest);


    @Headers({"Content-Type: application/json"})
    @HTTP(method = "POST", path = "DeliveryNoteDetail/", hasBody = true)
    Call<ResponseEnvelop<List<DeliveryNoteDetails>>> getDeliveryNoteDetails(@Body DeliveryDetailsRequest deliveryDetailsRequest);



//    @GET("getall/")
//    Call<List<MyProfile>> getAllWS();

//    @GET("verify/")
//    Call<MyProfile> loginWS(@Header("email") String userName, @Header("password") String password);
//
//    @GET("getbyid/")
//    Call<MyProfile> getFinderByIdWS(@Header("id") String id);
//
//    @GET("getByServicesNearMe/")
//    Call<List<MyProfile>> getByServicesNearMeWS(@Header("servicesNeeded") List<String> servicesNeeded, @Header("my_location") Location my_location);
//
//    @GET("getByServices/")
//    Call<List<MyProfile>> getByServicesWS(@Header("servicesNeeded") String servicesNeeded);
//
//    @GET("getVerifyRegistrationCodeWS/")
//    Call<Boolean> verifyRegistrationCodeWS(@Header("verificationCode") String strVerificationCode, @Header("finderId") String finderId);
//
// //   @GET("getByServices/")
//  //  Call<List<MyProfile>> getByServices(@Header("servicesNeeded") List<String> servicesNeeded);

}
