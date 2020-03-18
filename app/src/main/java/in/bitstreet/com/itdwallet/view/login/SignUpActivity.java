package in.bitstreet.com.itdwallet.view.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import in.bitstreet.com.itdwallet.R;
import in.bitstreet.com.itdwallet.controller.ApiClient;
import in.bitstreet.com.itdwallet.model.model.ErrorPojo;
import in.bitstreet.com.itdwallet.model.model.PostInterface;
import in.bitstreet.com.itdwallet.utills.CallegraphyFontActivity;
import in.bitstreet.com.itdwallet.utills.Constants;
import in.bitstreet.com.itdwallet.utills.PrefManager;
import in.bitstreet.com.itdwallet.utills.SignatureGenerator;
import in.bitstreet.com.itdwallet.utills.Validator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.bitstreet.com.itdwallet.R.id.et_input_last_name;

public class SignUpActivity extends CallegraphyFontActivity {

    private TextInputLayout inputLayoutFirstName, inputLayoutLastName, inputLayoutUserName, inputLayoutUserId, inputLayoutPassword, inputLayoutSignupCnfPassword, inputLayoutRefCode;
    private EditText etInputFirstName, etInputLastName, etInputUserName, etInputUserId, etInputPassword, etInputSignupCnfPassword, etInputRefCode;
    String message,userAvailablity,emailAvailablity;
    Context context;
    PrefManager prefManager;
    CheckBox checkBoxTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        TextView alreadyAccountTv = (TextView) findViewById(R.id.alreadyAccountTv);
        Button btnSignUp = (Button) findViewById(R.id.btn_signup);

        //To hide the soft key board when activity launches
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        context = this;
        prefManager = new PrefManager(context);

        init();

        listerners(alreadyAccountTv, btnSignUp);
    }


    private void init() {

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        ab.setElevation(0);

        inputLayoutFirstName = (TextInputLayout) findViewById(R.id.input_layout_first_name);
        inputLayoutLastName = (TextInputLayout) findViewById(R.id.input_layout_last_name);
        inputLayoutUserName = (TextInputLayout) findViewById(R.id.input_layout_user_name);
        inputLayoutUserId = (TextInputLayout) findViewById(R.id.input_layout_user_id);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputLayoutSignupCnfPassword = (TextInputLayout) findViewById(R.id.input_layout_signup_cnf_password);
        inputLayoutRefCode = (TextInputLayout) findViewById(R.id.input_layout_ref_code);


        etInputFirstName = (EditText) findViewById(R.id.et_input_first_name);
        etInputLastName = (EditText) findViewById(et_input_last_name);
        etInputUserName = (EditText) findViewById(R.id.et_input_user_name);
        etInputUserId = (EditText) findViewById(R.id.et_input_user_id);
        etInputPassword = (EditText) findViewById(R.id.et_input_password);
        etInputSignupCnfPassword = (EditText) findViewById(R.id.et_input_signup_cnf_password);
        etInputRefCode = (EditText) findViewById(R.id.et_input_ref_code);


        checkBoxTerms=(CheckBox)findViewById(R.id.ckbox_terms);


    }

    private void listerners(TextView alreadyAccountTv, TextView btnSignUp) {

        etInputFirstName.addTextChangedListener(new SignUpTextWatcher(etInputFirstName));
        etInputLastName.addTextChangedListener(new SignUpTextWatcher(etInputLastName));
        etInputUserName.addTextChangedListener(new SignUpTextWatcher(etInputUserName));
        etInputUserId.addTextChangedListener(new SignUpTextWatcher(etInputUserId));
        etInputPassword.addTextChangedListener(new SignUpTextWatcher(etInputPassword));
        etInputSignupCnfPassword.addTextChangedListener(new SignUpTextWatcher(etInputSignupCnfPassword));
        etInputRefCode.addTextChangedListener(new SignUpTextWatcher(etInputRefCode));

        SpannableString ss = new SpannableString(Constants.alreadyAccounTText);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 19, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        alreadyAccountTv.setText(ss);
        alreadyAccountTv.setMovementMethod(LinkMovementMethod.getInstance());
        alreadyAccountTv.setHighlightColor(Color.TRANSPARENT);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkBoxTerms.isChecked())
                {
                    submitForm();
                }else{
                    Toast.makeText(getApplicationContext(), Constants.ACCEPT_TERMS_CONDITIONS, Toast.LENGTH_SHORT).show();

                }


            }
        });

        etInputFirstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus

                    FirstNameValidation();
                }
            }
        });

        etInputLastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    lastNameValidation();
                }
            }
        });

        etInputUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus

                   userNameAvailable();
                }
            }
        });

        etInputUserId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus

                 emailAvailable();
                }
            }
        });

        etInputSignupCnfPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus

                    validatePassword();
                }
            }
        });


    }

    private void emailAvailable() {
        String emailId = etInputUserId.getText().toString().trim();
        try {

            String signature = SignatureGenerator.generateSignature(Constants.API_TEXT_CHECK_EMAIL + prefManager.getPublicKey(), prefManager.getPrivateKey());
            JsonObject params = new JsonObject();
           // params.addProperty("handle", emailId);
           // params.addProperty("signup_mode", "mobile");
           // params.addProperty("signature", signature);
           // params.addProperty("publicKey", prefManager.getPublicKey());
            PostInterface apiService = ApiClient.getClient(this).create(PostInterface.class);
            Call call = apiService.getEmailAvailable(prefManager.getPublicKey(),signature,emailId,"mobile");
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    JsonObject jsonObjEmail = (JsonObject) response.body();
                    try {

                        if(response.isSuccessful()) {
                            try {
                                emailAvailablity=jsonObjEmail.get("message").getAsString();
                                if(!emailAvailablity.equals("Email available")) {
                                    inputLayoutUserId.setError(emailAvailablity);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                ErrorPojo mApiError = gson.fromJson(response.errorBody().string(),ErrorPojo.class);
                                emailAvailablity= mApiError.getMessage();
                                inputLayoutUserName.setError(emailAvailablity);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), emailAvailablity, Toast.LENGTH_SHORT).show();

                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), emailAvailablity, Toast.LENGTH_SHORT).show();

                    }


                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Toast.makeText(getApplicationContext(), emailAvailablity, Toast.LENGTH_SHORT).show();

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), emailAvailablity, Toast.LENGTH_SHORT).show();

        }
    }

    private void userNameAvailable() {
        String userName = etInputUserName.getText().toString().trim();
        try {

            String signature = SignatureGenerator.generateSignature(Constants.API_TEXT_USER_NAME + prefManager.getPublicKey(), prefManager.getPrivateKey());
            JsonObject params = new JsonObject();
            params.addProperty("handle", userName);
            params.addProperty("signup_mode", "mobile");
          //  params.addProperty("signature", signature);
           // params.addProperty("publicKey", prefManager.getPublicKey());
            PostInterface apiService = ApiClient.getClient(this).create(PostInterface.class);
            Call call = apiService.getUserAvailable(prefManager.getPublicKey(),signature,userName, "mobile");
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    JsonObject jsonObject = (JsonObject) response.body();
                try {

                        if(response.isSuccessful()) {
                            try {
                                userAvailablity = jsonObject.get("message").getAsString();
                                if(!userAvailablity.equals("Handle is available")) {
                                    inputLayoutUserName.setError(userAvailablity);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }else{
                            try {
                                Gson gson = new GsonBuilder().create();
                                ErrorPojo mApiError = gson.fromJson(response.errorBody().string(),ErrorPojo.class);
                                userAvailablity= mApiError.getMessage();
                                inputLayoutUserName.setError(userAvailablity);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), userAvailablity, Toast.LENGTH_SHORT).show();

                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    Toast.makeText(getApplicationContext(), userAvailablity, Toast.LENGTH_SHORT).show();

                }


                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Toast.makeText(getApplicationContext(), userAvailablity, Toast.LENGTH_SHORT).show();

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), userAvailablity, Toast.LENGTH_SHORT).show();

        }
    }


    ////////////////////////////////////retrofit/////////////////////

    public void getResposne(String firstName,String lastName,String userName,String email,String password,String cnfPassword,String walletAddress) {


        try {
            String signature = SignatureGenerator.generateSignature(Constants.API_TEXT_SIGNUP + prefManager.getPublicKey(), prefManager.getPrivateKey());
            JsonObject params = new JsonObject();
            params.addProperty("firstName",firstName);
            params.addProperty("lastName", lastName);
            params.addProperty("handle", userName);
            params.addProperty("email", email);
            params.addProperty("password", password);
            params.addProperty("_confirmPassword", cnfPassword);
            params.addProperty("signup_mode", "mobile");
            params.addProperty("referralcode", walletAddress);
            params.addProperty("signature", signature);
            params.addProperty("publicKey", prefManager.getPublicKey());
            PostInterface apiService = ApiClient.getClient(this).create(PostInterface.class);
            Call call = apiService.getSignUpUser(params);
            prefManager.showDialog(context,Constants.DIALOG_REGISTERING_USER);

            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    JsonObject jsonObject = (JsonObject) response.body();
                   try {

                        if(response.isSuccessful()) {
                            message=   jsonObject.get("message").getAsString();

                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            prefManager.dismissDialog();
                            Intent intent = new Intent(SignUpActivity.this, VerifySignUpAccountActivity.class);
;                            startActivity(intent);


                            finish();
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
    /////////////////////////////////////////////////////////


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void submitForm() {

        if(FirstNameValidation() && lastNameValidation() && userNameValidation() &&  validateEmail() &&   validatePassword() ){
            String firstName = etInputFirstName.getText().toString().trim();
            String lastName = etInputLastName.getText().toString().trim();
            String userName = etInputUserName.getText().toString().trim();
            String email = etInputUserId.getText().toString().trim();
            String password=etInputPassword.getText().toString().trim();
            String cnfPassword =etInputSignupCnfPassword.getText().toString().trim();
            String waleetAddress =etInputRefCode.getText().toString().trim();
            prefManager.storeEmailSharedPreferences(email);

            getResposne(firstName,lastName,userName,email,password,cnfPassword,waleetAddress);




        }

    }


    private boolean lastNameValidation() {

        String lastName = etInputLastName.getText().toString().trim();
        String resultString = new Validator().validateFirstLastName(lastName);

        if (resultString.equals(Constants.errorFirstNameMsg)) {
            inputLayoutLastName.setError(resultString);
            return false;
        }else
            inputLayoutLastName.setErrorEnabled(false);

        return true;
    }

    private boolean FirstNameValidation() {
        String firstName = etInputFirstName.getText().toString().trim();
        String resultString = new Validator().validateFirstLastName(firstName);

        if (resultString.equals(Constants.errorFirstNameMsg)) {
            inputLayoutFirstName.setError(resultString);
            return false;
        }
        else
            inputLayoutFirstName.setErrorEnabled(false);
        return true;
    }

    private boolean userNameValidation() {

        String userName = etInputUserName.getText().toString().trim();
        String resultString = new Validator().validateName(userName);
        if (resultString.equals(Constants.errorFirstNameMsg)) {

            inputLayoutUserName.setError(getString(R.string.err_msg_user_name));
            //  requestFocus(etInputUserName);
            return false;
        } else
            inputLayoutUserName.setErrorEnabled(false);
            return true;
    }


    public boolean validateEmail() {
        String email = etInputUserId.getText().toString().trim();

        if (email.isEmpty() || !Validator.isValidEmail(email)) {
            inputLayoutUserId.setError(getString(R.string.err_msg_email));
            //   requestFocus(etInputUserId);
            return false;
        } else {
            inputLayoutUserId.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (etInputPassword.getText().toString().trim().isEmpty() && etInputSignupCnfPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password_is_empty));
            inputLayoutSignupCnfPassword.setError(getString(R.string.err_msg_password_is_empty));

            //    requestFocus(etInputPassword);
            return false;
        } else if (!(etInputPassword.getText().toString().trim().equals(etInputSignupCnfPassword.getText().toString().trim()))) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password_matching));
            inputLayoutSignupCnfPassword.setError(getString(R.string.err_msg_password_matching));
            //    requestFocus(etInputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
            inputLayoutSignupCnfPassword.setErrorEnabled(false);
            return true;
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private class SignUpTextWatcher implements TextWatcher {

        private View view;

        private SignUpTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.et_input_first_name:
                    FirstNameValidation();
                    break;
                case et_input_last_name:
                    lastNameValidation();
                    break;
                case R.id.et_input_user_name:
                    userNameValidation();
                    break;
                case R.id.et_input_user_id:
                    validateEmail();
                    break;
                case R.id.et_input_signup_cnf_password:
                    validatePassword();
                    break;


            }
        }
    }

}
