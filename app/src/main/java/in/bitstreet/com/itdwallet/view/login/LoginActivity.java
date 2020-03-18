package in.bitstreet.com.itdwallet.view.login;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import in.bitstreet.com.itdwallet.R;
import in.bitstreet.com.itdwallet.SplashScreenActivity;
import in.bitstreet.com.itdwallet.controller.ApiClient;
import in.bitstreet.com.itdwallet.database.DataHelper;
import in.bitstreet.com.itdwallet.database.SQLHelper;
import in.bitstreet.com.itdwallet.model.model.ErrorPojo;
import in.bitstreet.com.itdwallet.model.model.LoginResult;
import in.bitstreet.com.itdwallet.model.model.PostInterface;
import in.bitstreet.com.itdwallet.utills.SignatureGenerator;
import in.bitstreet.com.itdwallet.utills.Constants;
import in.bitstreet.com.itdwallet.utills.PrefManager;
import in.bitstreet.com.itdwallet.utills.Validator;
import in.bitstreet.com.itdwallet.view.DashBoard.activity.DashBoardActivity;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Callback;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.SystemClock;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static in.bitstreet.com.itdwallet.utills.SignatureGenerator.getHMAC256;

public class LoginActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText inputName, inputEmail, inputPassword;
    private TextInputLayout inputLayoutEmail, inputLayoutPassword;
    InputMethodManager imm;
    DataHelper db;
    SQLHelper helper;
    SQLiteDatabase database;
    Context ctx;
    boolean resultStatus = false;
    String message, SharedPrefForKey;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        ctx = this;
        prefManager = new PrefManager(ctx);


        TextView forgotPasswordTv = (TextView) findViewById(R.id.forgotPasswordTv);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
        Button btnSignUp = (Button) findViewById(R.id.btn_signup);
        TextView createAcTextView = (TextView) findViewById(R.id.createAccountTv);

        //To hide the soft key board when activity launches
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        //hideSoftKeyboard();


        setTypeFaceFont(forgotPasswordTv, createAcTextView);

        listerns(btnSignUp, forgotPasswordTv, createAcTextView);

        initActionBar();
        //  getUserInfo();
        //  startAlert();


    }

    ////////////////////////////////////retrofit/////////////////////

    public void startAlert() {
        long tenMin = 1000 * 60 * 1;
        Intent intent = new Intent(LoginActivity.this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(LoginActivity.this,
                0, intent, 0);
        AlarmManager am =
                (AlarmManager) getSystemService(Activity.ALARM_SERVICE);


        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), tenMin, pendingIntent);

    }


    private String getAsStringOrNull(JsonElement jsonElement) {
        return jsonElement.isJsonNull() ? "" : jsonElement.getAsString();
    }

    public void getResposne(String email, String password) {
        try {
            JsonObject params = new JsonObject();
            String signature = SignatureGenerator.generateSignature(Constants.API_TEXT_LOGIN + prefManager.getPublicKey(), prefManager.getPrivateKey());
            params.addProperty("email", email);
            params.addProperty("password", password);
            params.addProperty("signature", signature);
            params.addProperty("publicKey", prefManager.getPublicKey());
            params.addProperty("privateKey", prefManager.getPrivateKey());
            params.addProperty("string", Constants.API_TEXT_LOGIN + prefManager.getPublicKey());
            PostInterface apiService = ApiClient.getClient(this).create(PostInterface.class);
            Call call = apiService.getUser(params);
            prefManager.showDialog(ctx, Constants.DIALOG_LOADING);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    try {
                        JsonObject obj = (JsonObject) response.body();
                        try {

                            if (response.isSuccessful()) {

                                Gson gson = new GsonBuilder().create();
                                LoginResult mApiResponse = gson.fromJson(response.body().toString(), LoginResult.class);
                                resultStatus = mApiResponse.getResult();
                                message = mApiResponse.getMessage();
                                JsonObject result = mApiResponse.getData();

                                //change on json structure on 27/12/2017
                                //JsonObject result = data.get("data").getAsJsonObject();
                                String _id = result.get("_id").getAsString();
                                String firstName = result.get("firstName").getAsString();
                                String lastName = result.get("lastName").getAsString();
                                String handle = result.get("handle").getAsString();
                                String email = result.get("email").getAsString();
                                //changed on 19-12-2017 from walletAddress to referralcode as per the changes done from server side.
                                String signup_mode = result.get("signup_mode").getAsString();
                                String signup_token = result.get("signup_token").getAsString();
                                String userId = result.get("userId").getAsString();
                                String referralCode = result.get("referralcode").getAsString();
                                String dob="";
                                String imgurl="";
                                String gender="";
                                try {
                                     dob = result.get("dob").getAsString();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                     dob = getAsStringOrNull(result.get("dob"));
                                }
                                try {
                                     imgurl = result.get("imgurl").getAsString();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    imgurl = getAsStringOrNull(result.get("imgurl"));
                                }
                                try {
                                    gender = result.get("gender").getAsString();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    gender = getAsStringOrNull(result.get("gender"));
                                }


                                String walletAddress = result.get("walletAddress").getAsString();
                                String updatedAt = result.get("updatedAt").getAsString();


                                //change on json structure on 27/12/2017 so below value is commented
                                // String pendingPaidInteractions = result.get("pendingPaidInteractions").getAsString();
                                // String securityMode = result.get("securityMode").getAsString();
                                // String creationTime = result.get("creationTime").getAsString();
                                //  String isValid_inviteCode = result.get("isValid_inviteCode").getAsString();
                                //  String google_id=result.get("google_id").getAsString();
                                //   String itdIcoSold = result.get("itd-ico_sold").getAsString();
                                //  String itdIcoLimit = result.get("itd-ico_limit").getAsString();
                                //   String commission = result.get("commission").getAsString();
                                //   String discount = result.get("discount").getAsString();
                                //  String is_blocked = result.get("is_blocked").getAsString();
                                //   boolean is_valid = result.get("is_valid").getAsBoolean();
                                //  String btc_balance = result.get("btc_balance").getAsString();
                                //   String balance = result.get("balance").getAsString();
                                //    String __v = result.get("__v").getAsString();


                                //storing value in the data base , change on json structure and field on 27/12/2017 so below value is commented
                                /*db.insertLoginUserInfo(_id, firstName, lastName, handle, email, walletAddress,
                                        signup_mode, email_token, pendingPaidInteractions, creationTime,
                                        securityMode, isValid_inviteCode, btc_balance, balance, updatedAt);
*/
                                db.insertLoginUserInfo(_id, firstName, lastName, handle, email,
                                        signup_token, userId, referralCode, dob, imgurl, gender, walletAddress, updatedAt);

                                //success message and opening the DashBoard activity
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
                                startActivity(intent);
                                String combineUserFirstLastName = firstName + " " + lastName;
                                prefManager.storeUserNameSharedPreferences(combineUserFirstLastName);
                                prefManager.storeUseridSharedPreference(email);
                                prefManager.dismissDialog();

                                finish();
                            } else {
                                Gson gson = new GsonBuilder().create();
                                try {
                                    ErrorPojo mApiError = gson.fromJson(response.errorBody().string(), ErrorPojo.class);
                                    message = mApiError.getMessage();
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    prefManager.dismissDialog();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    prefManager.dismissDialog();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            prefManager.dismissDialog();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        prefManager.dismissDialog();
                    }

                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "" + t, Toast.LENGTH_SHORT).show();
                    prefManager.dismissDialog();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            prefManager.dismissDialog();
        }


    }

    private void getUserInfo() {

        db = new DataHelper(getApplicationContext());
        db.open();
        Cursor c = db.getUserInfo();
        if (c != null) {
            if (c.moveToFirst()) {
                do {

                    String ID = c.getString(c.getColumnIndex(Constants.ID));


                } while (c.moveToNext());
            }

        }
    }
    /////////////////////////////////////////////////////////


    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void initActionBar() {

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setElevation(0);


    }

    private void listerns(Button btnSignUp, TextView forgotPasswordTv, TextView createAcTextView) {

        inputEmail.addTextChangedListener(new LoginTextWatcher(inputEmail));
        inputPassword.addTextChangedListener(new LoginTextWatcher(inputPassword));

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  submitForm();
                helper = new SQLHelper(ctx);
                database = helper.getWritableDatabase();
                db = new DataHelper(ctx);
                db.open();
                getResposne(inputEmail.getText().toString(), inputPassword.getText().toString());
                getUserInfo();

                getAdminSettings();

            }
        });

        forgotPasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent);
                finish();

            }
        });

        SpannableString ss = new SpannableString(Constants.createNewAccounTText);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                finish();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 15, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        createAcTextView.setText(ss);
        createAcTextView.setMovementMethod(LinkMovementMethod.getInstance());
        String colorStr = "#ffffff";
        int color = Color.parseColor(colorStr);
        createAcTextView.setHighlightColor(color);


    }

    private void getAdminSettings() {

        try {
            JsonObject params = new JsonObject();
            String signature = SignatureGenerator.generateSignature(Constants.API_TEXT_LOGIN + prefManager.getPublicKey(), prefManager.getPrivateKey());

            params.addProperty("signature", signature);
            params.addProperty("publicKey", prefManager.getPublicKey());
            params.addProperty("privateKey", prefManager.getPrivateKey());
            params.addProperty("string", Constants.API_TEXT_LOGIN + prefManager.getPublicKey());
            PostInterface apiService = ApiClient.getClient(this).create(PostInterface.class);
            Call call = apiService.getUser(params);
            prefManager.showDialog(ctx, Constants.DIALOG_LOADING);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    try {
                        JsonObject obj = (JsonObject) response.body();
                        try {

                            if (response.isSuccessful()) {
                                Gson gson = new GsonBuilder().create();
                                LoginResult mApiResponse = gson.fromJson(response.body().toString(), LoginResult.class);
                                //  resultStatus = mApiResponse.getResult();
                                // message = mApiResponse.getMessage();
                                JsonObject data = mApiResponse.getData();
                                JsonObject result = data.get("result").getAsJsonObject();
                                String _id = result.get("_id").getAsString();
                                String firstName = result.get("firstName").getAsString();
                                String lastName = result.get("lastName").getAsString();
                                String handle = result.get("handle").getAsString();


                                //storing value in the data base
                                db.insertAdminSettingInfo(_id, firstName, lastName, handle);

                                //success message and opening the DashBoard activity
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                                finish();

                            } else {
                                Gson gson = new GsonBuilder().create();
                                try {
                                    ErrorPojo mApiError = gson.fromJson(response.errorBody().string(), ErrorPojo.class);
                                    message = mApiError.getMessage();
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    prefManager.dismissDialog();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }

    }

    private void setTypeFaceFont(TextView forgotPasswordTv, TextView createAcTextView) {

        Typeface typeFaceForgotPwd = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        forgotPasswordTv.setTypeface(typeFaceForgotPwd);

        Typeface typeFaceCreateAccount = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        createAcTextView.setTypeface(typeFaceCreateAccount);
    }


    /**
     * Validating form
     */
    private void submitForm() {


        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

    }


    public boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !Validator.isValidEmail(email)) {
            inputLayoutEmail.setGravity(Gravity.RIGHT);
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty() || inputPassword.getText().toString().trim().length() <= 4) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password_length));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }


    private void requestFocus(View view) {
        /*if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED);
        }*/
    }


    private class LoginTextWatcher implements TextWatcher {

        private View view;

        private LoginTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_password:

                    validatePassword();
                    break;


            }
        }
    }


}