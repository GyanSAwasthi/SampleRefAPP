package in.bitstreet.com.itdwallet.utills;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import in.bitstreet.com.itdwallet.controller.ApiClient;
import in.bitstreet.com.itdwallet.model.model.ErrorPojo;
import in.bitstreet.com.itdwallet.model.model.PostInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jay on 26/12/17.
 */

public class CallFCMApi {
    Context context;
    PrefManager prefManager;
    String message;
    int setFcmFlag=0;

    public CallFCMApi(Context ctx){
        context = ctx;
        prefManager = new PrefManager(context);
    }


    public void sendFCMRegistrationToServer( String android_id,String refreshedToken) {
        try {
            JsonObject params = new JsonObject();
            String signature = SignatureGenerator.generateSignature(Constants.API_TEXT_FCM_API + prefManager.getPublicKey(), prefManager.getPrivateKey());
            params.addProperty("signature", signature);
            params.addProperty("deviceId", android_id);
            params.addProperty("deviceToken", refreshedToken);
            params.addProperty("publicKey", prefManager.getPublicKey());
            PostInterface apiService = ApiClient.getClient(context).create(PostInterface.class);
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
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();


                        } else {
                            Gson gson = new GsonBuilder().create();
                            try {
                                ErrorPojo mApiError = gson.fromJson(response.errorBody().string(), ErrorPojo.class);
                                message = mApiError.getMessage();
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(context, ""+t, Toast.LENGTH_SHORT).show();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
