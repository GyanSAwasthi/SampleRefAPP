package in.bitstreet.com.itdwallet.view.changepassword;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import in.bitstreet.com.itdwallet.R;
import in.bitstreet.com.itdwallet.controller.ApiClient;
import in.bitstreet.com.itdwallet.database.DataHelper;
import in.bitstreet.com.itdwallet.database.SQLHelper;
import in.bitstreet.com.itdwallet.model.model.ErrorPojo;
import in.bitstreet.com.itdwallet.model.model.LoginResult;
import in.bitstreet.com.itdwallet.model.model.PostInterface;
import in.bitstreet.com.itdwallet.utills.Constants;
import in.bitstreet.com.itdwallet.utills.PicassoCircleTransformation;
import in.bitstreet.com.itdwallet.utills.PrefManager;
import in.bitstreet.com.itdwallet.utills.SignatureGenerator;
import in.bitstreet.com.itdwallet.view.DashBoard.activity.EditProfileActivity;
import in.bitstreet.com.itdwallet.view.alternativecurrency.AlternativeCurrencyActivity;
import in.bitstreet.com.itdwallet.view.contact.activity.AllContactsListActivity;
import in.bitstreet.com.itdwallet.view.login.ForgotPassword;
import in.bitstreet.com.itdwallet.view.login.VerifyOTPActivity;
import in.bitstreet.com.itdwallet.view.mpin.MPinActivity;
import in.bitstreet.com.itdwallet.view.support.activity.SupportMainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ChangePasswordActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener {
    Toolbar toolbar;

    DrawerLayout drawer;
    NavigationView navigationView;
    ImageButton imgBtnUAMSliding;
    TextView editProfile;
    TextInputLayout inputLayoutChangepasswordOldpwd, inputLayoutPasswordChangepasswordNewpwd, inputLayoutPasswordChangepasswordConfirm;
    EditText inputPasswordChangepasswordOldpwd, inputPasswordChangepasswordNewpwd, inputPasswordChangepasswordConfirm;
    DataHelper db;
    SQLHelper helper;
    SQLiteDatabase database;
    Context ctx;
    PrefManager prefManager;

    String userIdStr;


    TextView tvUserName;
    ImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_drawer);
        ViewGroup inclusionViewGroup = (ViewGroup) findViewById(R.id.ll_container);

        View child = LayoutInflater.from(this).inflate(
                R.layout.content_change_password, null);

        inclusionViewGroup.addView(child);
        ctx = this;
        prefManager = new PrefManager(ctx);


        //for setting the font style for whole activity font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Regular-.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setIDs();
        initDB();
        Listeners();
        drawer.setDrawerListener(this);
        getUserIdFromDB();
        populateUserImage();
    }


    @Override
    public void onDrawerOpened(View arg0) {
        hideSoftKeyboard();
    }

    @Override
    public void onDrawerClosed(View arg0) {

    }

    @Override
    public void onDrawerSlide(View arg0, float arg1) {
        hideSoftKeyboard();
    }

    @Override
    public void onDrawerStateChanged(int arg0) {
        hideSoftKeyboard();
    }

    //for setting the font style for whole activity font
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void setIDs() {

        drawer = (DrawerLayout) findViewById(R.id.common_drawer_layout);
        imgBtnUAMSliding = (ImageButton) findViewById(R.id.btnSliding_common);
        navigationView = (NavigationView) findViewById(R.id.common_nav_view);
        View header = navigationView.getHeaderView(0);
        editProfile = (TextView) header.findViewById(R.id.textVieweditProfile);

        inputLayoutChangepasswordOldpwd = (TextInputLayout) findViewById(R.id.input_layout_changepassword_oldpwd);
        inputLayoutPasswordChangepasswordNewpwd = (TextInputLayout) findViewById(R.id.input_layout_password_changepassword_newpwd);
        inputLayoutPasswordChangepasswordConfirm = (TextInputLayout) findViewById(R.id.input_layout_password_changepassword_confirm);
        inputPasswordChangepasswordOldpwd = (EditText) findViewById(R.id.input_password_changepassword_oldpwd);
        inputPasswordChangepasswordNewpwd = (EditText) findViewById(R.id.input_password_changepassword_newpwd);
        inputPasswordChangepasswordConfirm = (EditText) findViewById(R.id.input_password_changepassword_confirm);

        //To hide the soft key board when activity launches
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        userImage = (ImageView)header. findViewById(R.id.imageView);
        tvUserName = (TextView) header.findViewById(R.id.tvusernamecommon);
        tvUserName.setText(prefManager.getUserNamePref());

    }


    public void populateUserImage() {

        String imageUrl = db.getUserImageFromDB(ctx);
        if (imageUrl.length() > 0 && imageUrl.toString().contains("https://")) {
            Picasso.with(ctx)
                    .load(imageUrl)
                    .transform(new PicassoCircleTransformation(ctx))
                    .into(userImage);
        }

    }

    private void initDB() {
        helper = new SQLHelper(ctx);
        database = helper.getWritableDatabase();
        db = new DataHelper(ctx);
        db.open();
    }

    private void Listeners() {
        Button btnSubmit = (Button) findViewById(R.id.btn_submit_changepassword);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();

            }
        });

        imgBtnUAMSliding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.END);
            }
        });

        navigationView.setNavigationItemSelectedListener(this);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                }
                Intent intent = new Intent(ChangePasswordActivity.this, EditProfileActivity.class);
                startActivity(intent);

            }
        });
    }

    private void submitForm() {


        if (validatePassword()) {
            String oldPwd = inputPasswordChangepasswordOldpwd.getText().toString();
            String newPwd = inputPasswordChangepasswordNewpwd.getText().toString();
            String cnfPwd = inputPasswordChangepasswordConfirm.getText().toString();
            getResposne(oldPwd, newPwd, cnfPwd);
            return;
        }


    }

    private void getUserIdFromDB() {


        Cursor c = db.getUserInfo();
        if (c != null) {
            if (c.moveToFirst()) {
                do {

                    userIdStr = c.getString(c.getColumnIndex(Constants.ID));
                } while (c.moveToNext());
            }

        }
    }
    ////////////////////////////////////retrofit/////////////////////

    public void getResposne(String oldPwd, String newPassword, String cnfPassword) {


        try {

            String url = Constants.BASE_URL + "api/v1/wallet/user/changepassword/" + userIdStr;
            String signature = SignatureGenerator.generateSignature(Constants.API_TEXT_CHANGE_PASSWORD + prefManager.getPublicKey(), prefManager.getPrivateKey());
            JsonObject params = new JsonObject();
            params.addProperty("oldPassword", oldPwd);
            params.addProperty("newPassword", newPassword);
            params.addProperty("confirmPassword", cnfPassword);
            params.addProperty("signup_mode", "mobile");
            params.addProperty("publicKey", prefManager.getPublicKey());
            params.addProperty("signature", signature);
            PostInterface apiService = ApiClient.getClient(this).create(PostInterface.class);
            Call call = apiService.getChangePassword(url, params);

            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    try {

                        if (response.isSuccessful()) {
                            Gson gson = new GsonBuilder().create();
                            LoginResult mApiResponse = gson.fromJson(response.body().toString(), LoginResult.class);

                            String message = mApiResponse.getMessage();
                            String statusCode = mApiResponse.getstatusCodeChangePwd();


                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Gson gson = new GsonBuilder().create();
                            try {
                                ErrorPojo mApiError = gson.fromJson(response.errorBody().string(), ErrorPojo.class);
                                String message = mApiError.getMessage();
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        //saveUserDetailsInDb();


    }


    /////////////////////////////////////////////////////////

    private boolean validatePassword() {
        String oldPwd = inputPasswordChangepasswordOldpwd.getText().toString().trim();
        String newPwd = inputPasswordChangepasswordNewpwd.getText().toString().trim();
        String changePwd = inputPasswordChangepasswordConfirm.getText().toString().trim();

        if ((oldPwd.length() >= 4 && newPwd.length() >= 4 && changePwd.length() >= 4) && (newPwd.equals(changePwd))) {
            inputLayoutChangepasswordOldpwd.setErrorEnabled(false);
            inputLayoutPasswordChangepasswordNewpwd.setErrorEnabled(false);
            inputLayoutPasswordChangepasswordConfirm.setErrorEnabled(false);

            return true;

        } else {
            inputLayoutChangepasswordOldpwd.setError(getString(R.string.err_msg_valid_pwd));
            inputLayoutPasswordChangepasswordNewpwd.setError(getString(R.string.err_msg_valid_pwd));
            inputLayoutPasswordChangepasswordConfirm.setError(getString(R.string.err_msg_valid_pwd));

        }
        return false;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.contact) {
            Intent intent = new Intent(ChangePasswordActivity.this, AllContactsListActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.alternative_currency) {
            Intent intent = new Intent(ChangePasswordActivity.this, AlternativeCurrencyActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.mpin) {
            Intent intent = new Intent(ChangePasswordActivity.this, MPinActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }


        } else if (id == R.id.changepassword) {

            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.support) {
            Intent intent = new Intent(ChangePasswordActivity.this, SupportMainActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.logout) {

        }


        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    @Override
    public void onBackPressed() {
        //  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }


}
