package in.bitstreet.com.itdwallet.utills;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import in.bitstreet.com.itdwallet.R;
import in.bitstreet.com.itdwallet.database.DataHelper;
import in.bitstreet.com.itdwallet.database.SQLHelper;
import in.bitstreet.com.itdwallet.view.login.ForgotPassword;

/**
 * Class for Shared Preference
 */
public class PrefManager {

    Context context;
    ProgressDialog progressDoalog;
    DataHelper db;
    SQLHelper helper;
    SQLiteDatabase database;

    public PrefManager(Context context) {
        this.context = context;
    }

    public void storeKeySharedPreferences(String publicKey, String privateKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("putKeysDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("publicKey", publicKey);
        editor.putString("privateKey", privateKey);
        editor.commit();

    }

    public void storeFCMFlagSharedPreferences(int setFcmFlag) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("putFCMFlagStatus", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("setFcmFlag", setFcmFlag);
        editor.commit();

    }
    public int getFCMFlagStatus() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("putFCMFlagStatus", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("setFcmFlag", 0);
    }


    public String getPublicKey() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("putKeysDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("publicKey", "");
    }

    public String getPrivateKey() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("putKeysDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("privateKey", "");
    }

    public void storeEmailSharedPreferences(String Email) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("putEmailsDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", Email);
        editor.commit();

    }

    public String getEmail() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("putEmailsDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("email", "");
    }


    public boolean isUserLogedOut() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        boolean isEmailEmpty = sharedPreferences.getString("Email", "").isEmpty();
        boolean isPasswordEmpty = sharedPreferences.getString("Password", "").isEmpty();
        return isEmailEmpty || isPasswordEmpty;
    }

    public void showDialog(Context ctx, String dialogString) {

        progressDoalog = new ProgressDialog(ctx, R.style.AppCompatAlertDialogStyle);
        progressDoalog.setMax(100);
        progressDoalog.setCancelable(false);
        progressDoalog.setMessage(dialogString);
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();
    }

    public void dismissDialog() {

        progressDoalog.dismiss();
    }




    public void storeUserNameSharedPreferences(String combineUserFirstLastName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("putUserNameDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("combineUserFirstLastName", combineUserFirstLastName);
        editor.commit();

    }

    public String getUserNamePref() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("putUserNameDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("combineUserFirstLastName", "");
    }


    public void storeTokenFireBaseSharedPreferences(String FirebaseToken) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("putFirebaseToken", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("FirebaseToken", FirebaseToken);
        editor.commit();

    }

    public String getTokenFireBasePref() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("putFirebaseToken", Context.MODE_PRIVATE);
        return sharedPreferences.getString("FirebaseToken", "");
    }


    public void storeUseridSharedPreference(String email) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("putEmailId", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.commit();
    }

    public void clearUserIdSharedPreference(){

        SharedPreferences sharedPreferences = context.getSharedPreferences("putEmailId", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public String getUseridSharedPreference() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("putEmailId", Context.MODE_PRIVATE);
        return sharedPreferences.getString("email", "");
    }

    public void storeProfileUpdatedFlagStatus(String successFlag) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("putSuccessFlag", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("successFlag", successFlag);
        editor.commit();
    }

    public String getProfileUpdatedFlagStatus() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("putSuccessFlag", Context.MODE_PRIVATE);
        return sharedPreferences.getString("successFlag", "");
    }
}