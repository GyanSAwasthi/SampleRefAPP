package in.bitstreet.com.itdwallet;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;


import in.bitstreet.com.itdwallet.controller.ApiClient;
import in.bitstreet.com.itdwallet.model.model.ErrorPojo;
import in.bitstreet.com.itdwallet.model.model.LoginResult;
import in.bitstreet.com.itdwallet.model.model.PostInterface;
import in.bitstreet.com.itdwallet.utills.CallFCMApi;
import in.bitstreet.com.itdwallet.utills.Constants;
import in.bitstreet.com.itdwallet.utills.PrefManager;
import in.bitstreet.com.itdwallet.utills.SignatureGenerator;
import in.bitstreet.com.itdwallet.view.DashBoard.activity.DashBoardActivity;
import in.bitstreet.com.itdwallet.view.login.ForgotPassword;
import in.bitstreet.com.itdwallet.view.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.provider.Settings.Secure;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static in.bitstreet.com.itdwallet.utills.SignatureGenerator.getHMAC256;


public class SplashScreenActivity extends AppCompatActivity {
    ImageView imageView;
    String message, forceUpgradeStr;
    Context context;
    PrefManager prefManager;
    TelephonyManager mTelephony;
    String android_id;
    public int PERMISSION_REQUEST_CODE = 1;
    boolean result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set up notitle
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //set up full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        context = this;
        prefManager = new PrefManager(context);
        imageView = (ImageView) findViewById(R.id.imgView);
        android_id = Secure.getString(this.getContentResolver(),
                Secure.ANDROID_ID);


        if (!checkPermission()) {
            requestPermission();
        } else {
            String DeviceModel = android.os.Build.MODEL;
            String DeviceName = android.os.Build.MANUFACTURER;
            String SerialNo = android.os.Build.SERIAL;
            String currentVer = android.os.Build.VERSION.RELEASE;
            // String IMEI = getIMEI();
            String uuid = getUUID(context);
            String key = prefManager.getPrivateKey();
            if (key.length() > 0) {


                forceUpgradeResponse();

            } else {
                registerDevice(DeviceModel, DeviceName, SerialNo, currentVer, android_id, getIMEI(), uuid);


            }

            if(prefManager.getFCMFlagStatus()==0){
                CallFCMApi callFCMApi= new CallFCMApi(context);
                callFCMApi.sendFCMRegistrationToServer(android_id,prefManager.getTokenFireBasePref());
            }


        }

        Thread timer = new Thread() {
            public void run() {
                try {


                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
                    imageView.startAnimation(animation);
                    sleep(4000);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.start();

    }


    ///////////////////////   permission for marshmallow  ///////////////////

    private boolean checkPermission() {
        int result1 = ContextCompat.checkSelfPermission(SplashScreenActivity.this, Manifest.permission.READ_PHONE_STATE);


        if (result1 == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            //  Toast.makeText(this,"You will not be able use all features of this application",Toast.LENGTH_LONG).show();
            return false;

        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreenActivity.this, Manifest.permission.READ_PHONE_STATE)
                ) {


        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    registerDevice(android.os.Build.MODEL, android.os.Build.MANUFACTURER, android.os.Build.SERIAL, android.os.Build.VERSION.RELEASE, android_id, getIMEI(), getUUID(context));
                    //  registerDevice();

                } else {


                }
                break;
        }
    }



    ////////////////////////////////////////////////////////////////////////
    private void forceUpgradeResponse() {
        JsonObject params = new JsonObject();
        String signature = SignatureGenerator.generateSignature(Constants.API_TEXT_FORCE_UPGRADE + prefManager.getPublicKey(), prefManager.getPrivateKey());
        params.addProperty("appVersion", getVersionInfo());
        params.addProperty("signature", signature);
        params.addProperty("publicKey", prefManager.getPublicKey());
        PostInterface apiService = ApiClient.getClient(this).create(PostInterface.class);
        Call call = apiService.getForceUpgradeResponse(params);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                JsonObject jsonObject = (JsonObject) response.body();

                if (response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    LoginResult mApiResponse = gson.fromJson(response.body().toString(), LoginResult.class);
                    message = mApiResponse.getMessage();
                    JsonObject data = mApiResponse.getData();
                    forceUpgradeStr = data.get("forceUpdate").getAsString();
                    String messageStr = jsonObject.get("message").getAsString();


                    forceUpgrade(forceUpgradeStr, messageStr);


                } else {
                    Gson gson = new GsonBuilder().create();
                    try {
                        ErrorPojo mApiError = gson.fromJson(response.errorBody().string(), ErrorPojo.class);
                        message = mApiError.getMessage();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getApplicationContext(), forceUpgradeStr, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //get the current version number
    private int getVersionInfo() {
        int versionCode = -1;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionCode;
    }

    public void forceUpgrade(String forceUpgradeStr, String message) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.force_upgrade_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);

        final TextView dialogTitleTv = (TextView) mView.findViewById(R.id.dialogTitle);
        dialogTitleTv.setText(message);


        alertDialogBuilderUserInput
                .setCancelable(false)

                .setPositiveButton("Upgrade", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // ToDo get user input here
                        dialogBox.cancel();


                    }
                })

                .setNegativeButton("Skip",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                                if(prefManager.getUseridSharedPreference().length()>0){
                                    Intent intent = new Intent(SplashScreenActivity.this, DashBoardActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        });


        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
        Button btnPositive = alertDialogAndroid.getButton(AlertDialog.BUTTON_POSITIVE);//upgrade
        Button btnNegative = alertDialogAndroid.getButton(AlertDialog.BUTTON_NEGATIVE);//skip

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = 20;

        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);
        btnNegative.setGravity(Gravity.LEFT);
        btnPositive.setGravity(Gravity.RIGHT);
        if (forceUpgradeStr.equals("0")) {
            btnPositive.setVisibility(View.INVISIBLE);
        }

    }

    private void registerDevice(String deviceModel, String deviceName, String serialNo, String currentVer, String android_id, String imei, String uuid) {
        try {
            JsonObject params = new JsonObject();
            params.addProperty("deviceId", android_id);
            params.addProperty("model", deviceModel);
            params.addProperty("platform", "Android");
            params.addProperty("manufacturer", deviceName);
            params.addProperty("version", currentVer);
            params.addProperty("serial", serialNo);

            params.addProperty("signatureAuthKey", Constants.AUTH_KEY);


            PostInterface apiService = ApiClient.getClient(this).create(PostInterface.class);
            Call call = apiService.getRegisterDevice(params);
            prefManager.showDialog(context, Constants.DIALOG_REGISTERING_DEVICE);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    JsonObject jsonObject = (JsonObject) response.body();
                    try {
                        JsonObject obj = (JsonObject) response.body();
                        try {

                            if (response.isSuccessful()) {
                                Gson gson = new GsonBuilder().create();
                                LoginResult mApiResponse = gson.fromJson(response.body().toString(), LoginResult.class);
                                boolean resultStatus = mApiResponse.getResult();
                                message = mApiResponse.getMessage();
                                JsonObject data = mApiResponse.getData();
                                String publicKey = data.get("publicKey").getAsString();
                                String privateKey = data.get("privateKey").getAsString();

                                //store public and private key in shared preferences
                                prefManager.storeKeySharedPreferences(publicKey, privateKey);
                                prefManager.dismissDialog();
                                //Dialog to upgrade the application
                                forceUpgradeResponse();

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


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    public static String getHMAC256() {
        Mac sha512_HMAC = null;
        String result = null;
        String key = Constants.AUTH_KEY;

        try {
            byte[] byteKey = key.getBytes("UTF-8");
            final String HMAC_SHA512 = "HmacSHA512";
            sha512_HMAC = Mac.getInstance(HMAC_SHA512);
            SecretKeySpec keySpec = new SecretKeySpec(byteKey, HMAC_SHA512);
            sha512_HMAC.init(keySpec);
            byte[] mac_data = sha512_HMAC.
                    doFinal("My message".getBytes("UTF-8"));
           //result = Base64.encode(mac_data);
           result = bytesToHex(mac_data);
            System.out.println(result);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


    private String getIMEI() {

        String IMEI = null;
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (null != tm) {
            IMEI = tm.getDeviceId();
        }
        if (null == IMEI || 0 == IMEI.length()) {
            IMEI = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        }


        return IMEI;
    }

    public String getUUID(Context context) {

        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();

        return deviceId;
    }


}

