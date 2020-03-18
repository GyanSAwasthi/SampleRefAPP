package in.bitstreet.com.itdwallet.view.DashBoard.activity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;



import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by shahbaz on 2/6/17.
 */

public interface ApiInterface {


    @FormUrlEncoded
    @POST("device/register/")
    @Headers({
            "Content-Type: application/x-www-form-urlencoded"
    })
    Call<APIRESPONSE> registerDevice(@Field("deviceId") String deviceId, @Field("model") String model, @Field("platform") String platform,
                                     @Field("manufacturer") String manufacturer, @Field("version") String version, @Field("serial") String serial);


    @FormUrlEncoded
    @POST("device/register/")
    @Headers({
            "Content-Type: application/x-www-form-urlencoded"
    })
    Call<APIRESPONSE> updateprofilepic(@Field("Body") MultipartBody.Part Body);


    @FormUrlEncoded
    @POST("device/fcm/")
    @Headers({
            "Content-Type: application/x-www-form-urlencoded"
    })
    Call<APIRESPONSE> setFCM(@Field("devicePublicKey") String devicePublicKey, @Field("signature") String signature, @Field("deviceId") String deviceId,
                             @Field("regId") String regId);



    @FormUrlEncoded
    @POST("forceupdate/")
    @Headers({
            "Content-Type: application/x-www-form-urlencoded"
    })
    Call<APIRESPONSE> forceUpdate(@Field("devicePublicKey") String devicePublicKey, @Field("signature") String signature, @Field("appVersion") String appVersion);


    @FormUrlEncoded
    @POST("login/")
    @Headers({
            "Content-Type: application/x-www-form-urlencoded"
    })
    Call<APIRESPONSE> memberLogin(@Field("devicePublicKey") String devicePublicKey, @Field("signature") String signature, @Field("countryCode") String countryCode,
                                  @Field("mobileNo") String mobileNo);


    @FormUrlEncoded
    @POST("enquiry/")
    @Headers({
            "Content-Type: application/x-www-form-urlencoded"
    })
    Call<APIRESPONSE> memberSignup(@Field("devicePublicKey") String devicePublicKey, @Field("signature") String signature, @Field("countryCode") String countryCode,
                                   @Field("mobileNo") String mobileNo, @Field("buildingName") String buildingName,
                                   @Field("managerMobile") String managerMobile, @Field("managerName") String managerName, @Field("fullAddress") String fullAddress);






    @GET("/maps/api/place/autocomplete/json")
    @Headers({
            "Content-Type: application/json"})
    Call<JsonElement> getCityResults(@Query("types") String types, @Query("input") String input, @Query("key") String key);

    @GET("/maps/api/place/details/json")
    @Headers({
            "Content-Type: application/json"})
    Call<JsonElement> getLatLongResults(@Query("placeid") String type, @Query("key") String key);



}