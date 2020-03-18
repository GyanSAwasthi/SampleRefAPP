package in.bitstreet.com.itdwallet.controller;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import in.bitstreet.com.itdwallet.model.model.ErrorPojo;
import in.bitstreet.com.itdwallet.model.model.LoginResult;
import in.bitstreet.com.itdwallet.model.model.PostInterface;
import in.bitstreet.com.itdwallet.utills.CallFCMApi;
import in.bitstreet.com.itdwallet.utills.Constants;
import in.bitstreet.com.itdwallet.utills.PrefManager;
import in.bitstreet.com.itdwallet.utills.SignatureGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";
    PrefManager prefManager;
    String message;
    int setFcmFlag=0;
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        Context context=this;

        prefManager = new PrefManager(this);
        prefManager.storeTokenFireBaseSharedPreferences(refreshedToken);

        String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        CallFCMApi callFCMApi= new CallFCMApi(context);
        callFCMApi.sendFCMRegistrationToServer(android_id,refreshedToken);

        /*// TODO: Implement this method to send any registration to your app's servers.
        sendFCMRegistrationToServer(android_id,refreshedToken);*/
    }




   /* private void sendFCMRegistrationToServer( String android_id,String refreshedToken) {
        try {
            JsonObject params = new JsonObject();
            String signature = SignatureGenerator.generateSignature(Constants.API_TEXT_FCM_API + prefManager.getPublicKey(), prefManager.getPrivateKey());
            params.addProperty("signature", signature);
            params.addProperty("deviceId", android_id);
            params.addProperty("deviceToken", refreshedToken);
            params.addProperty("publicKey", prefManager.getPublicKey());
            PostInterface apiService = ApiClient.getClient(this).create(PostInterface.class);
            Call call = apiService.sendFcmToken(params);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    JsonObject jsonObject = (JsonObject) response.body();
                    try {
                            if (response.isSuccessful()) {

                                message= jsonObject.get("message").getAsString();

                                //store public and private key in shared preferences
                                setFcmFlag=1;
                                prefManager.storeFCMFlagSharedPreferences(setFcmFlag);
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();


                            } else {
                                Gson gson = new GsonBuilder().create();
                                try {
                                    ErrorPojo mApiError = gson.fromJson(response.errorBody().string(), ErrorPojo.class);
                                    message = mApiError.getMessage();
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    prefManager.dismissDialog();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }




                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Toast.makeText(getApplicationContext(), ""+t, Toast.LENGTH_SHORT).show();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }*/
}
