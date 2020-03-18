package in.bitstreet.com.itdwallet.view.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.Subscribe;

import in.bitstreet.com.itdwallet.R;
import in.bitstreet.com.itdwallet.controller.ApiClient;
import in.bitstreet.com.itdwallet.model.model.ErrorPojo;
import in.bitstreet.com.itdwallet.model.model.PostInterface;
import in.bitstreet.com.itdwallet.utills.Constants;
import in.bitstreet.com.itdwallet.utills.PrefManager;
import in.bitstreet.com.itdwallet.utills.SignatureGenerator;
import in.bitstreet.com.itdwallet.view.DashBoard.eventbus.Events;
import in.bitstreet.com.itdwallet.view.DashBoard.eventbus.GlobalBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {
    TextInputLayout textInputLayoutPwd, textInputLayoutCnfPwd;
    EditText etPassword, etCnfPassword;
    Context ctx;
    PrefManager prefManager;
    String message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_reset_password);

        TextView tvCnfPwdHeader = (TextView) findViewById(R.id.reset_pwd_header);
        TextView tvCnfPwdText = (TextView) findViewById(R.id.reset_pwd_txt);
        Button btnSubmit = (Button) findViewById(R.id.btn_submit);
        textInputLayoutPwd = (TextInputLayout) findViewById(R.id.input_layout_password_reset);
        textInputLayoutCnfPwd = (TextInputLayout) findViewById(R.id.input_layout_password_reset_cnf);

        etPassword = (EditText) findViewById(R.id.input_password_reset);
        etCnfPassword = (EditText) findViewById(R.id.input_password_reset_cnf);

        //To hide the soft key board when activity launches
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ctx = this;
        prefManager = new PrefManager(ctx);


        initActionBar();

        setTypeFaceFont(tvCnfPwdHeader, tvCnfPwdText);

        Listeners(btnSubmit);


    }





    private void initActionBar() {
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        ab.setElevation(0);

    }

    private void setTypeFaceFont(TextView tvCnfPwdHeader, TextView tvCnfPwdText) {

        Typeface typeFaceForgotPwd = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
        tvCnfPwdHeader.setTypeface(typeFaceForgotPwd);

        Typeface typeFaceCreateAccount = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        tvCnfPwdText.setTypeface(typeFaceCreateAccount);
    }

    private void Listeners(Button btnSubmit) {

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
    }






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Validating form
     */
    private void submitForm() {


        if (!validatePassword()) {
            return;
        } else {
            getResposne();

        }


    }


    private boolean validatePassword() {
        if (etPassword.getText().toString().trim().isEmpty() && etCnfPassword.getText().toString().trim().isEmpty() ) {
            textInputLayoutPwd.setError(getString(R.string.err_msg_password));
            textInputLayoutCnfPwd.setError(getString(R.string.err_msg_password));

            requestFocus(etPassword);
            return false;
        } else if (!(etPassword.getText().toString().trim().equals(etCnfPassword.getText().toString().trim()))) {

            textInputLayoutCnfPwd.setError(getString(R.string.err_msg_password_matching));
            requestFocus(etPassword);
            return false;
        } else {
            textInputLayoutPwd.setErrorEnabled(false);
            textInputLayoutCnfPwd.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

       this. finish();

    }

    public void getResposne() {
        String password=etPassword.getText().toString();
        String passwordCnf=etCnfPassword.getText().toString();
        String otpStr=getIntent().getStringExtra("otpToken").toString();
        String url = Constants.BASE_URL + "api/v1/wallet/user/resetPassword/" + otpStr;

        try {
            String signature = SignatureGenerator.generateSignature(Constants.API_TEXT_RESET_PASSWORD + prefManager.getPublicKey(), prefManager.getPrivateKey());

            JsonObject params = new JsonObject();
            params.addProperty("email",prefManager.getEmail());
            params.addProperty("signup_mode", "mobile");
            params.addProperty("confirmPassword",passwordCnf);
            params.addProperty("newPassword",password);
            PostInterface apiService = ApiClient.getClient(this).create(PostInterface.class);
            Call call = apiService.getResetPasswordConfirmPassword(url,prefManager.getPublicKey(),signature,params);
            prefManager.showDialog(ctx,Constants.DIALOG_RESET_PASSWORD);

            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    JsonObject jsonObject = (JsonObject) response.body();
                    try {

                        if(response.isSuccessful()) {
                            message=   jsonObject.get("message").getAsString();

                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            prefManager.dismissDialog();
                         Intent intent = new Intent(ResetPasswordActivity.this, ResetPwdSuccess.class);
                            startActivity(intent);

                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                ErrorPojo mApiError = gson.fromJson(response.errorBody().string(),ErrorPojo.class);
                                message = mApiError.getMessage();
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                prefManager.dismissDialog();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Some thing went worng", Toast.LENGTH_SHORT).show();
                            }
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



}
