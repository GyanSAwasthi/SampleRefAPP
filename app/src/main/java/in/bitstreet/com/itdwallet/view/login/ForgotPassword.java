package in.bitstreet.com.itdwallet.view.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

import in.bitstreet.com.itdwallet.R;
import in.bitstreet.com.itdwallet.controller.ApiClient;
import in.bitstreet.com.itdwallet.model.model.ErrorPojo;
import in.bitstreet.com.itdwallet.model.model.LoginResult;
import in.bitstreet.com.itdwallet.model.model.PostInterface;
import in.bitstreet.com.itdwallet.utills.Constants;
import in.bitstreet.com.itdwallet.utills.PrefManager;
import in.bitstreet.com.itdwallet.utills.SignatureGenerator;
import in.bitstreet.com.itdwallet.utills.Validator;
import in.bitstreet.com.itdwallet.view.DashBoard.activity.DashBoardActivity;
import in.bitstreet.com.itdwallet.view.DashBoard.eventbus.Events;
import in.bitstreet.com.itdwallet.view.DashBoard.eventbus.GlobalBus;
import in.bitstreet.com.itdwallet.view.login.VerifyOTPActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {
    private TextInputLayout inputLayoutEmail;
    EditText inputEmail;
    Context ctx;
    PrefManager prefManager;
    String message, email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_forgot_email);
        inputEmail = (EditText) findViewById(R.id.et_forgot_email);
        Button btnSubmit = (Button) findViewById(R.id.btn_veryfyotp);
        TextView forgotPasswordHeader = (TextView) findViewById(R.id.forgot_password_header);
        TextView forgotPwd = (TextView) findViewById(R.id.tv_forgot_pwd_text);
        // inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));

//To hide the soft key board when activity launches
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ctx = this;
        prefManager = new PrefManager(ctx);

        initActionBar();

        setTypeFaceFont(forgotPasswordHeader,forgotPwd);

        listeners(btnSubmit);

    }






    private void setTypeFaceFont(TextView forgotPasswordHeader,TextView forgotPwd ) {

        Typeface typeFaceForgotPwdHeader = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
        forgotPasswordHeader.setTypeface(typeFaceForgotPwdHeader);

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        forgotPwd.setTypeface(type);
    }

    private void initActionBar() {

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        ab.setElevation(0);

    }

    private void listeners(Button btnSubmit) {

        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));

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

                Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                startActivity(intent);
                finish();


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void submitForm() {
        if (!validateEmail()) {
            return;
        } else {
            getResposne();



        }

    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !Validator.isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
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
        Intent intent=new Intent(ForgotPassword.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void getResposne() {
        email=inputEmail.getText().toString();


        try {
            JsonObject params = new JsonObject();
            String signature = SignatureGenerator.generateSignature(Constants.API_TEXT_VERYFY_FORGOT_PASSWORD + prefManager.getPublicKey(), prefManager.getPrivateKey());
            params.addProperty("email", email);
            params.addProperty("signup_mode", "mobile");
            params.addProperty("publicKey", prefManager.getPublicKey());
            params.addProperty("signature", signature);
            PostInterface apiService = ApiClient.getClient(this).create(PostInterface.class);
            Call call = apiService.getForgotPasswordEmail(params);
           prefManager.showDialog(ctx,Constants.DIALOG_LOADING_OTP);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    JsonObject jsonObject = (JsonObject) response.body();
                    try {

                        if(response.isSuccessful()) {
                            message=   jsonObject.get("message").getAsString();

                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ForgotPassword.this,VerifyOTPActivity.class);
                            intent.putExtra("email",email);
                            startActivity(intent);
                            prefManager.dismissDialog();

                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                ErrorPojo mApiError = gson.fromJson(response.errorBody().string(),ErrorPojo.class);
                                message = mApiError.getMessage();
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                prefManager.dismissDialog();
                            } catch (Exception e) {
                                e.printStackTrace();
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



    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.et_forgot_email:
                    validateEmail();
                    break;

            }
        }
    }
}
