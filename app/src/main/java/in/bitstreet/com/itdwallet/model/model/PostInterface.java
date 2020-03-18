package in.bitstreet.com.itdwallet.model.model;

import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import in.bitstreet.com.itdwallet.utills.Constants;
import in.bitstreet.com.itdwallet.utills.PrefManager;
import in.bitstreet.com.itdwallet.utills.SignatureGenerator;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface PostInterface {


    @POST("api/v1/wallet/device/register")
    @Headers({"Content-Type: application/json"})
    Call<JsonObject> getRegisterDevice(@Body JsonObject body);

    @PUT("api/v1/wallet/device/setfcm")
    @Headers({"Content-Type: application/json"})
    Call<JsonObject> sendFcmToken(@Body JsonObject body);



    @POST("api/v1/wallet/device/forceupdate")
    @Headers({"Content-Type: application/json"})
    Call<JsonObject> getForceUpgradeResponse(@Body JsonObject body);


    @POST("api/v1/wallet/user/login")
    @Headers({"Content-Type: application/json"})
    Call<JsonObject> getUser(@Body JsonObject body);


    @POST("api/v1/wallet/user/signup")
    @Headers({"Content-Type: application/json"})
    Call<JsonObject> getSignUpUser(@Body JsonObject body);


    @POST
    @Headers({"Content-Type: application/json"})
    Call<JsonObject> getSignUpUserVerification(@Url String url,@Body JsonObject body);


    @POST("api/v1/wallet/user/forgotpassword")
    @Headers({"Content-Type: application/json"})
    Call<JsonObject> getForgotPassword(@Body JsonObject body);


    @POST
    @Headers({"Content-Type: application/json"})
    Call<JsonObject> getResetPassword(@Url String url, @Body JsonObject body);


    @POST
    @Headers({"Content-Type: application/json"})
    Call<JsonObject> getChangePassword(@Url String url, @Body JsonObject body);

    @GET("api/v1/wallet/user/checkusername")
    Call<JsonObject> getUserAvailable(@Header("x-publickey") String publicKey,@Header("x-signature") String signature,@Query("handle") String username,@Query("signup_mode") String signupMode);

    @GET("api/v1/wallet/user/checkEmail")
    @Headers({"Content-Type: application/json"})
    Call<JsonObject> getEmailAvailable(@Header("x-publickey") String publicKey,@Header("x-signature") String signature, @Query("email") String email,@Query("signup_mode") String signupMode);

    @POST("api/v1/wallet/user/forgotPassword")
    @Headers({"Content-Type: application/json"})
    Call<JsonObject> getForgotPasswordEmail(@Body JsonObject body);

    @POST
    Call<JsonObject> getOTPVerified(@Url String url,@Body JsonObject body);

    @POST("api/v1/wallet/user/verifyforgotpassword")
    @Headers({"Content-Type: application/json"})
    Call<JsonObject> getChangePassword(@Body JsonObject body);

    @POST
    @Headers({"Content-Type: application/json"})
    Call<JsonObject> getResetPasswordConfirmPassword(@Url String url, @Header("x-publickey") String publicKey,@Header("x-signature") String signature, @Body JsonObject emailBody);

    @Multipart
    @POST("api/v1/wallet/user/updateprofile/{token}")
       Call<JsonObject> getEditProfileImageUpload(@Path("token") String url, @Part MultipartBody.Part file,@Part("firstName") RequestBody firstName,
                                         @Part("lastName") RequestBody lastName,@Part("handle") RequestBody userName,@Part("email") RequestBody email,@Part("walletAddress") RequestBody WalletAddress,
                                         @Part("gender") RequestBody gender,@Part("dob") RequestBody dob,@Part("signature") RequestBody signatureStr,@Part("publicKey") RequestBody publicKey,@Part("signup_mode") RequestBody signup_mode);

    @Multipart
    @POST("api/v1/wallet/user/updateprofile/{token}")
    Call<JsonObject> getEditProfileWithoutImage(@Path("token") String url, @Part("firstName") RequestBody firstName,
                        @Part("lastName") RequestBody lastName,@Part("handle") RequestBody userName,@Part("email") RequestBody email,@Part("walletAddress") RequestBody WalletAddress,
                        @Part("gender") RequestBody gender,@Part("dob") RequestBody dob,@Part("signature") RequestBody signatureStr,@Part("publicKey") RequestBody publicKey,@Part("signup_mode") RequestBody signup_mode);

    /*@Multipart
    @POST
    Call<JsonObject> getEditProfile(@Url String url, @Part MultipartBody.Part file,@Part("userId") RequestBody userId,@Part("signature") RequestBody signatureStr,@Part("devicePublicKey") RequestBody publicKey,@Part("text") RequestBody text);
*/
}
