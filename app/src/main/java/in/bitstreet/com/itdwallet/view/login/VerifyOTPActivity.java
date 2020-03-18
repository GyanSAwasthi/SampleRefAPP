package in.bitstreet.com.itdwallet.view.login;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import in.bitstreet.com.itdwallet.R;
import in.bitstreet.com.itdwallet.controller.ApiClient;
import in.bitstreet.com.itdwallet.model.model.ErrorPojo;
import in.bitstreet.com.itdwallet.model.model.PostInterface;
import in.bitstreet.com.itdwallet.utills.Constants;
import in.bitstreet.com.itdwallet.utills.PrefManager;
import in.bitstreet.com.itdwallet.utills.SignatureGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOTPActivity extends AppCompatActivity implements View.OnFocusChangeListener, View.OnKeyListener, TextWatcher {
    private EditText mPinFirstDigitEditText;
    private EditText mPinSecondDigitEditText;
    private EditText mPinThirdDigitEditText;
    private EditText mPinForthDigitEditText;
    private EditText mPinHiddenEditText;
     TextView tvResendOtp;
    Context ctx;
    PrefManager prefManager;
    String otpStr, message;

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    /**
     * Hides soft keyboard.
     *
     * @param editText EditText which has focus
     */
    public void hideSoftKeyboard(EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MainLayout(this, null));
        Button btnVerify = (Button) findViewById(R.id.btn_verify_otp);

        //To hide the soft key board when activity launches
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ctx = this;
        prefManager = new PrefManager(ctx);

        init();
        setPINListeners(btnVerify);


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

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        final int id = v.getId();
        switch (id) {
            case R.id.pin_first_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.pin_second_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.pin_third_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.pin_forth_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            final int id = v.getId();
            switch (id) {
                case R.id.pin_hidden_edittext:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if (mPinHiddenEditText.getText().length() == 4)
                            mPinForthDigitEditText.setText("");
                        else if (mPinHiddenEditText.getText().length() == 3)
                            mPinThirdDigitEditText.setText("");
                        else if (mPinHiddenEditText.getText().length() == 2)
                            mPinSecondDigitEditText.setText("");
                        else if (mPinHiddenEditText.getText().length() == 1)
                            mPinFirstDigitEditText.setText("");

                        if (mPinHiddenEditText.length() > 0)
                            mPinHiddenEditText.setText(mPinHiddenEditText.getText().subSequence(0, mPinHiddenEditText.length() - 1));

                        return true;
                    }

                    break;

                default:
                    return false;
            }
        }

        return false;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {


        if (s.length() == 0) {
            setFocusedPinBackground(mPinFirstDigitEditText);
            mPinFirstDigitEditText.setText("");
        } else if (s.length() == 1) {
            setFocusedPinBackground(mPinSecondDigitEditText);
            mPinFirstDigitEditText.setText(s.charAt(0) + "");
            mPinSecondDigitEditText.setText("");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");

            String a = mPinHiddenEditText.getText().toString();
            Log.d("setPinHiddenEditText.", a);
        } else if (s.length() == 2) {
            setFocusedPinBackground(mPinThirdDigitEditText);
            mPinSecondDigitEditText.setText(s.charAt(1) + "");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");
            String a = mPinHiddenEditText.getText().toString();
            Log.d("setPinHiddenEditText.", a);
        } else if (s.length() == 3) {
            setFocusedPinBackground(mPinForthDigitEditText);
            mPinThirdDigitEditText.setText(s.charAt(2) + "");
            mPinForthDigitEditText.setText("");
            String a = mPinHiddenEditText.getText().toString();
            Log.d("setPinHiddenEditText.", a);
        } else if (s.length() == 4) {
            mPinForthDigitEditText.setText(s.charAt(3) + "");
            // String a=mPinHiddenEditText.getText().toString();
            // Log.v("value.......",a);
            otpStr = mPinHiddenEditText.getText().toString();
            Log.d("setPinHiddenEditText.", otpStr);
        }


    }

    /**
     * Initialize EditText fields.
     */
    private void init() {

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        ab.setElevation(0);


        tvResendOtp = (TextView) findViewById(R.id.tvResendOtp);
        mPinFirstDigitEditText = (EditText) findViewById(R.id.pin_first_edittext);
        mPinSecondDigitEditText = (EditText) findViewById(R.id.pin_second_edittext);
        mPinThirdDigitEditText = (EditText) findViewById(R.id.pin_third_edittext);
        mPinForthDigitEditText = (EditText) findViewById(R.id.pin_forth_edittext);
        mPinHiddenEditText = (EditText) findViewById(R.id.pin_hidden_edittext);

        //show the otp field
        setDefaultPinBackground(mPinFirstDigitEditText);
        setDefaultPinBackground(mPinSecondDigitEditText);
        setDefaultPinBackground(mPinThirdDigitEditText);
        setDefaultPinBackground(mPinForthDigitEditText);
    }

    /**
     * Sets default PIN background.
     *
     * @param editText edit text to change
     */
    private void setDefaultPinBackground(EditText editText) {
        //  setViewBackground(editText, getResources().getDrawable(R.mipmap.line));
    }

    /**
     * Sets focus on a specific EditText field.
     *
     * @param editText EditText to set focus on
     */
    public static void setFocus(EditText editText) {
        if (editText == null)
            return;

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    /**
     * Sets focused PIN field background.
     *
     * @param editText edit text to change
     */
    private void setFocusedPinBackground(EditText editText) {
        // setViewBackground(editText, getResources().getDrawable(R.mipmap.line));
    }

    /**
     * Sets listeners for EditText fields.
     */
    private void setPINListeners(Button btnVerify) {
        mPinHiddenEditText.addTextChangedListener(this);

        mPinFirstDigitEditText.setOnFocusChangeListener(this);
        mPinSecondDigitEditText.setOnFocusChangeListener(this);
        mPinThirdDigitEditText.setOnFocusChangeListener(this);
        mPinForthDigitEditText.setOnFocusChangeListener(this);

        mPinFirstDigitEditText.setOnKeyListener(this);
        mPinSecondDigitEditText.setOnKeyListener(this);
        mPinThirdDigitEditText.setOnKeyListener(this);
        mPinForthDigitEditText.setOnKeyListener(this);
        mPinHiddenEditText.setOnKeyListener(this);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mPinHiddenEditText.getText().toString().length() == 4) {
                    String email = getIntent().getStringExtra("email").toString();
                    getOtpVerification(email);

                } else {
                    Toast.makeText(VerifyOTPActivity.this, Constants.verifyToast, Toast.LENGTH_SHORT).show();
                }

            }
        });

        tvResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendOtpResponse();

            }
        });
    }


    public void resendOtpResponse() {

        try {
            JsonObject params = new JsonObject();
            String signature = SignatureGenerator.generateSignature(Constants.API_TEXT_VERYFY_FORGOT_PASSWORD + prefManager.getPublicKey(), prefManager.getPrivateKey());
            params.addProperty("email", prefManager.getEmail());
            params.addProperty("signup_mode", "mobile");
            params.addProperty("publicKey", prefManager.getPublicKey());
            params.addProperty("signature", signature);
            PostInterface apiService = ApiClient.getClient(this).create(PostInterface.class);
            Call call = apiService.getForgotPasswordEmail(params);
            prefManager.showDialog(ctx,Constants.DIALOG_RESEND_OTP);

            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    JsonObject jsonObject = (JsonObject) response.body();
                    try {

                        if(response.isSuccessful()) {
                            message=   jsonObject.get("message").getAsString();

                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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
                        prefManager.dismissDialog();
                    }

                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    prefManager.dismissDialog();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }


    }

    public void getOtpVerification(String email) {

        String url = Constants.BASE_URL + "api/v1/wallet/user/verifyforgotpassword/" + otpStr;
        try {
            JsonObject params = new JsonObject();
            String signature = SignatureGenerator.generateSignature(Constants.API_TEXT_VERIFY_OTP + prefManager.getPublicKey(), prefManager.getPrivateKey());
            params.addProperty("email", email);
            params.addProperty("signup_mode", "mobile");
            params.addProperty("publicKey", prefManager.getPublicKey());
            params.addProperty("signature", signature);
            PostInterface apiService = ApiClient.getClient(this).create(PostInterface.class);
            Call call = apiService.getOTPVerified(url, params);
            prefManager.showDialog(ctx,Constants.DIALOG_LOADING);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    JsonObject jsonObject = (JsonObject) response.body();
                    try {

                        if (response.isSuccessful()) {
                            message = jsonObject.get("message").getAsString();

                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(VerifyOTPActivity.this, ResetPasswordActivity.class);
                            intent.putExtra("otpToken",otpStr);
                            startActivity(intent);
                            prefManager.dismissDialog();

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
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        prefManager.dismissDialog();

                    }

                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    prefManager.dismissDialog();

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * Sets background of the view.
     * This method varies in implementation depending on Android SDK version.
     *
     * @param view       View to which set background
     * @param background Background to set to view
     */
    @SuppressWarnings("deprecation")
    public void setViewBackground(View view, Drawable background) {
        if (view == null || background == null)
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    /**
     * Shows soft keyboard.
     *
     * @param editText EditText which has focus
     */
    public void showSoftKeyboard(EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }


    /**
     * Custom LinearLayout with overridden onMeasure() method
     * for handling software keyboard show and hide events.
     */
    public class MainLayout extends LinearLayout {

        public MainLayout(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.activity_verify_otp, this);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            final int proposedHeight = MeasureSpec.getSize(heightMeasureSpec);
            final int actualHeight = getHeight();

            Log.d("TAG", "proposed: " + proposedHeight + ", actual: " + actualHeight);

            if (actualHeight >= proposedHeight) {
                // Keyboard is shown
                if (mPinHiddenEditText.length() == 0)
                    setFocusedPinBackground(mPinFirstDigitEditText);
                else
                    setDefaultPinBackground(mPinFirstDigitEditText);
            }

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}

