package in.bitstreet.com.itdwallet.view.DashBoard.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.support.v7.app.AppCompatActivity;

import in.bitstreet.com.itdwallet.R;
import in.bitstreet.com.itdwallet.controller.*;
import in.bitstreet.com.itdwallet.controller.ApiClient;
import in.bitstreet.com.itdwallet.database.DataHelper;
import in.bitstreet.com.itdwallet.database.SQLHelper;
import in.bitstreet.com.itdwallet.model.model.ErrorPojo;
import in.bitstreet.com.itdwallet.model.model.LoginResult;
import in.bitstreet.com.itdwallet.model.model.PostInterface;
import in.bitstreet.com.itdwallet.utills.CallegraphyFontActivity;
import in.bitstreet.com.itdwallet.utills.CircularImageCreater;
import in.bitstreet.com.itdwallet.utills.Constants;
import in.bitstreet.com.itdwallet.utills.PicassoCircleTransformation;
import in.bitstreet.com.itdwallet.utills.PrefManager;
import in.bitstreet.com.itdwallet.utills.SignatureGenerator;
import in.bitstreet.com.itdwallet.utills.Validator;
import in.bitstreet.com.itdwallet.view.alternativecurrency.AlternativeCurrencyActivity;
import in.bitstreet.com.itdwallet.view.changepassword.ChangePasswordActivity;
import in.bitstreet.com.itdwallet.view.contact.activity.AllContactsListActivity;
import in.bitstreet.com.itdwallet.view.login.ForgotPassword;
import in.bitstreet.com.itdwallet.view.login.LoginActivity;
import in.bitstreet.com.itdwallet.view.login.SignUpActivity;
import in.bitstreet.com.itdwallet.view.login.VerifyOTPActivity;
import in.bitstreet.com.itdwallet.view.login.VerifySignUpAccountActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static in.bitstreet.com.itdwallet.R.id.cropImageView;
import static in.bitstreet.com.itdwallet.R.id.et_input_last_name;
import static in.bitstreet.com.itdwallet.R.id.et_input_last_name_profile;

public class EditProfileActivity extends CallegraphyFontActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener {
    Toolbar toolbar;
    CalendarView calendarView;
    TextView dateDisplay;
    Spinner genderSpinner;
    TextView dateTv, userNameTv;
    DatePickerDialog datePickerDialog;
    ImageView imageViewProfile;
    private Bitmap mBitmap;
    private Resources mResources;
    private TextInputLayout inputLayoutFirstNameProfile, inputLayoutLastNameProfile, inputLayoutUserNameProfile, inputLayoutUserIdProfile, inputLayoutWalletAddressProfile;
    private EditText etInputFirstNameProfile, etInputLastNameProfile, etInputUserNameProfile, etInputUserIdProfile, etInputWalletAddressProfile;
    DataHelper db;
    SQLHelper helper;
    SQLiteDatabase database;
    ImageButton btnSliding_common;
    String dateStr, selectedGender, userIdStr, image_url, message;
    String successFlag = "profileNotUpdated";
    Uri resultUri;
    DrawerLayout drawer;
    NavigationView navigationView;
    private Uri mCropImageUri;
    Context ctx;
    PrefManager prefManager;
    TextView tvUserName;
    ImageView userImage;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_drawer);
        ViewGroup inclusionViewGroup = (ViewGroup) findViewById(R.id.ll_container);
        View child1 = LayoutInflater.from(this).inflate(
                R.layout.content_edit_profile, null);
        inclusionViewGroup.addView(child1);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ctx = this;
        prefManager = new PrefManager(ctx);
        initDB();
        setIds();
        init();

        Button btnSubmitProfile = (Button) findViewById(R.id.btn_submit_profile);
        View header = navigationView.getHeaderView(0);
        TextView textVieweditProfile = (TextView) header.findViewById(R.id.textVieweditProfile);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



        listerners(btnSubmitProfile, textVieweditProfile);
        spinnerInitilized();
        drawer.setDrawerListener(this);
        populateUserInfo();

       /* //Populate the user information
        if(prefManager.getProfileUpdatedFlagStatus().equalsIgnoreCase("profileUpdated")){
            populateUserInfo();

        }else{
            populateAvailableUserInfo();
        }*/

    }



    //display the gender in the spinner
    private void spinnerInitilized() {
        // Get reference of widgets from XML layout
        genderSpinner = (Spinner) findViewById(R.id.android_material_design_spinner);

        // Initializing a String Array
        String[] plants = new String[]{
                "Male",
                "Female",
                "Other"
        };

        final List<String> plantsList = new ArrayList<>(Arrays.asList(plants));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,plantsList){
            @Override
            public boolean isEnabled(int position){

                return true;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(Color.BLACK);

                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        genderSpinner.setAdapter(spinnerArrayAdapter);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGender = (String) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void onDrawerOpened(View arg0) {
        populateUserImage();
        hideSoftKeyboard();
    }

    @Override
    public void onDrawerClosed(View arg0) {
    }

    @Override
    public void onDrawerSlide(View arg0, float arg1) {
        populateUserImage();
        hideSoftKeyboard();
    }

    @Override
    public void onDrawerStateChanged(int arg0) {
        populateUserImage();
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


    private void setIds() {
        drawer = (DrawerLayout) findViewById(R.id.common_drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.common_nav_view);
        View header = navigationView.getHeaderView(0);

        btnSliding_common = (ImageButton) findViewById(R.id.btnSliding_common);
        userNameTv = (TextView) findViewById(R.id.userNameTv);
        inputLayoutFirstNameProfile = (TextInputLayout) findViewById(R.id.input_layout_first_name_profile);
        inputLayoutLastNameProfile = (TextInputLayout) findViewById(R.id.input_layout_last_name_profile);
        inputLayoutUserNameProfile = (TextInputLayout) findViewById(R.id.input_layout_user_name_profile);
        inputLayoutUserIdProfile = (TextInputLayout) findViewById(R.id.input_layout_user_id_profile);
        inputLayoutWalletAddressProfile = (TextInputLayout) findViewById(R.id.input_layout_wallet_address_profile);
        etInputFirstNameProfile = (EditText) findViewById(R.id.et_input_first_name_profile);
        etInputLastNameProfile = (EditText) findViewById(et_input_last_name_profile);
        etInputUserNameProfile = (EditText) findViewById(R.id.et_input_user_name_profile);
        etInputUserIdProfile = (EditText) findViewById(R.id.et_input_user_id_profile);
        etInputWalletAddressProfile = (EditText) findViewById(R.id.et_input_wallet_address_profile);
        imageViewProfile = (ImageView) findViewById(R.id.imgprofile);
        dateTv = (TextView) findViewById(R.id.date);

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


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init() {



        userNameTv.setText(prefManager.getUserNamePref());
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        dateTv.setText(mYear + "-"
                + (mMonth + 1) + "-" + mDay);
        dateStr = mYear + "-"
                + (mMonth + 1) + "-" + mDay;

        mResources = getResources();
        mBitmap = BitmapFactory.decodeResource(mResources, R.drawable.editprofile);
        imageViewProfile.setImageBitmap(mBitmap);
        // Set an image for ImageView
        imageViewProfile.setImageBitmap(mBitmap);
        RoundedBitmapDrawable drawable = new CircularImageCreater().createRoundedBitmapDrawableWithBorder(mBitmap, mResources);
        // Set the ImageView image as drawable object
        imageViewProfile.setImageDrawable(drawable);
    }

    public void populateAvailableUserInfo() {

        ArrayList userInfoArrayList = new ArrayList();
        userInfoArrayList = db.getUserInfoFromDB(ctx);
        userIdStr = userInfoArrayList.get(0).toString();
        etInputFirstNameProfile.setText(userInfoArrayList.get(1).toString());
        etInputLastNameProfile.setText(userInfoArrayList.get(2).toString());
        etInputUserNameProfile.setText(userInfoArrayList.get(3).toString());
        etInputUserIdProfile.setText(userInfoArrayList.get(4).toString());
        etInputWalletAddressProfile.setText(userInfoArrayList.get(5).toString());



    }

    public void populateUserInfo() {

        ArrayList userInfoArrayList = new ArrayList();
        userInfoArrayList = db.getEditedUserInfoFromDB(ctx);
        userIdStr = userInfoArrayList.get(0).toString();
        etInputFirstNameProfile.setText(userInfoArrayList.get(1).toString());
        etInputLastNameProfile.setText(userInfoArrayList.get(2).toString());
        etInputUserNameProfile.setText(userInfoArrayList.get(3).toString());
        etInputUserIdProfile.setText(userInfoArrayList.get(4).toString());
        etInputWalletAddressProfile.setText(userInfoArrayList.get(5).toString());

        dateStr = userInfoArrayList.get(6).toString();
        selectedGender = userInfoArrayList.get(7).toString();

        if(userInfoArrayList.size()>8 && userInfoArrayList.get(8).toString().contains("https://")) {

            Picasso.with(ctx)
                    .load(userInfoArrayList.get(8).toString())
                    .transform(new PicassoCircleTransformation(ctx))
                    .into(imageViewProfile);
        }

    }

    private void listerners(Button btnSubmitProfile, TextView editProfileTv) {

        etInputFirstNameProfile.addTextChangedListener(new EditProfileTextWatcher(etInputFirstNameProfile));
        etInputLastNameProfile.addTextChangedListener(new EditProfileTextWatcher(etInputLastNameProfile));
        etInputUserNameProfile.addTextChangedListener(new EditProfileTextWatcher(etInputUserNameProfile));
        etInputUserIdProfile.addTextChangedListener(new EditProfileTextWatcher(etInputUserIdProfile));
        etInputWalletAddressProfile.addTextChangedListener(new EditProfileTextWatcher(etInputWalletAddressProfile));
        btnSubmitProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
        btnSliding_common.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.END);
            }
        });
        editProfileTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.END);
            }
        });
        navigationView.setNavigationItemSelectedListener(this);


        dateTv.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                dateTv.setText(mYear + "-"
                        + (mMonth + 1) + "-" + mDay);
                dateStr = mYear + "-"
                        + (mMonth + 1) + "-" + mDay;
                // date picker dialog
                datePickerDialog = new DatePickerDialog(EditProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                dateTv.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);
                                dateStr = year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth;
                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });
        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //uncomment and below line for cropping the image Ratio free and comment the ratio line
                // CropImage.startPickImageActivity(EditProfileActivity.this);
                CropImage.activity().setAspectRatio(1, 1).start(EditProfileActivity.this);
            }
        });
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // CropImage.startPickImageActivity(this);
                CropImage.activity().setAspectRatio(1, 1).start(EditProfileActivity.this);
            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // required permissions granted, start crop image activity
                startCropImageActivity(mCropImageUri);
            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                RoundedBitmapDrawable drawable = new CircularImageCreater().createRoundedBitmapDrawableWithBorder(loadBitmap("" + result.getUri()), mResources);

                // Set the ImageView image as drawable object
                imageViewProfile.setImageDrawable(drawable);


             String   url = resultUri.getPath();
             image_url=   new PicassoCircleTransformation(ctx). compressImage(url);
                Toast.makeText(this, "Cropping successful", Toast.LENGTH_LONG).show();
                // updateImage(image_url);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Cropping failed ", Toast.LENGTH_LONG).show();
            }
        }


    }

    public Bitmap loadBitmap(String url) {
        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        try {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 8192);
            bm = BitmapFactory.decodeStream(bis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bm;
    }


    private void startCropImageActivity(Uri imageUri) {
        /*CropImage.activity(imageUri)
                .start(this);*/

        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }


    private void submitForm() {

        if (FirstNameValidation() && lastNameValidation() && userNameValidation() && validateEmail()) {
            String firstName = etInputFirstNameProfile.getText().toString().trim();
            String lastName = etInputLastNameProfile.getText().toString().trim();
            String userName = etInputUserNameProfile.getText().toString().trim();
            String email = etInputUserIdProfile.getText().toString().trim();
            String walletAddress = etInputWalletAddressProfile.getText().toString().trim();


            prefManager.storeEmailSharedPreferences(email);
            getResposne(firstName, lastName, userName, email, walletAddress, selectedGender, dateStr);
        }

    }

    public void getResposne(String firstNameStr, String lastNameStr, String userNameStr, String emailStr, String walletAddress, String genderStr, String dobStr) {

        // String url = Constants.BASE_URL + "api/v1/wallet/user/updateprofile/" + userIdStr;

        try {
            Call call;
            if(image_url==null){
                image_url="";
            }
                String signature = SignatureGenerator.generateSignature(Constants.API_TEXT_UPDATE_PROFILE + prefManager.getPublicKey(), prefManager.getPrivateKey());
                String publicKeyStr = prefManager.getPublicKey();
                RequestBody firstName = RequestBody.create(MediaType.parse("text/plain"), firstNameStr);
                RequestBody lastName = RequestBody.create(MediaType.parse("text/plain"), lastNameStr);
                RequestBody userName = RequestBody.create(MediaType.parse("text/plain"), userNameStr);
                RequestBody email = RequestBody.create(MediaType.parse("text/plain"), emailStr);
                RequestBody WalletAddress = RequestBody.create(MediaType.parse("text/plain"), walletAddress);
                RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), genderStr);
                RequestBody dob = RequestBody.create(MediaType.parse("text/plain"), dobStr);
                RequestBody signatureStr = RequestBody.create(MediaType.parse("text/plain"), signature);
                RequestBody signupMode = RequestBody.create(MediaType.parse("text/plain"), "mobile");

                RequestBody text = RequestBody.create(MediaType.parse("text/plain"), "New post with notification....");
                RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), "3");
                RequestBody publicKey = RequestBody.create(MediaType.parse("text/plain"), publicKeyStr);



            if(image_url.length()>0 )
            {
                File file = new File(image_url);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/png"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("imgurl", file.getName(), requestFile);
                PostInterface apiService = ApiClient.getClient(this).create(PostInterface.class);
                call = apiService.getEditProfileImageUpload(userIdStr, body, firstName, lastName, userName, email, WalletAddress, gender, dob, signatureStr, publicKey, signupMode);
            }
            else
            {
                PostInterface apiService = ApiClient.getClient(this).create(PostInterface.class);
                call = apiService.getEditProfileWithoutImage(userIdStr, firstName, lastName, userName, email, WalletAddress, gender, dob, signatureStr, publicKey, signupMode);

            }
                  prefManager.showDialog(ctx, Constants.DIALOG_LOADING);

                call.enqueue(new Callback() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(Call call, Response response) {
                        try {
                            JsonObject obj = (JsonObject) response.body();
                            try {

                                if (response.isSuccessful()) {
                                    Gson gson = new GsonBuilder().create();
                                    LoginResult mApiResponse = gson.fromJson(response.body().toString(), LoginResult.class);
                                    message = mApiResponse.getMessage();
                                    JsonObject result = mApiResponse.getData();
                                    // JsonObject result = data.get("data").getAsJsonObject();

                                    String firstName = result.get("firstName").getAsString();
                                    String lastName = result.get("lastName").getAsString();
                                    String handle = result.get("handle").getAsString();
                                    String email = result.get("email").getAsString();
                                    //changed on 19-12-2017 from walletAddress to referralcode as per the changes done from server side.
                                    String walletAddress = result.get("referralcode").getAsString();
                                    String signup_mode = result.get("signup_mode").getAsString();
                                    String signup_token = result.get("signup_token").getAsString();
                                    String referralcode = result.get("referralcode").getAsString();
                                    String dob = result.get("dob").getAsString();
                                    String imgurl = result.get("imgurl").getAsString();
                                    String gender = result.get("gender").getAsString();
                                    String updatedAt = result.get("updatedAt").getAsString();

                                    //converting date from 2017-05-23T06:25:50' to 2017-05-23
                                    String strDate = dob;
                                    String finalDateString = "";
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                    Date convertedDate = new Date();
                                    try {
                                        convertedDate = dateFormat.parse(strDate);
                                        SimpleDateFormat sdfnewformat = new SimpleDateFormat("yyyy-MM-dd");
                                         finalDateString = sdfnewformat.format(convertedDate);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    //storing value in the data base
                                    db.updateUserInfo(firstName, lastName, handle, email, walletAddress,
                                            signup_mode, signup_token, finalDateString, imgurl,
                                            gender, referralcode, userIdStr, updatedAt);

                                    //success message and opening the DashBoard activity
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    successFlag = "profileUpdated";


                                    prefManager.storeProfileUpdatedFlagStatus(successFlag);
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
                                prefManager.dismissDialog();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
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
            prefManager.dismissDialog();
        }

    }

    private boolean lastNameValidation() {

        String lastName = etInputLastNameProfile.getText().toString().trim();
        String resultString = new Validator().validateFirstLastName(lastName);

        if (resultString.equals(Constants.errorFirstNameMsg)) {
            inputLayoutLastNameProfile.setError(resultString);
            return false;
        } else
            inputLayoutLastNameProfile.setErrorEnabled(false);
        return true;
    }

    private boolean FirstNameValidation() {
        String firstName = etInputFirstNameProfile.getText().toString().trim();
        String resultString = new Validator().validateFirstLastName(firstName);

        if (resultString.equals(Constants.errorFirstNameMsg)) {
            inputLayoutFirstNameProfile.setError(resultString);
            return false;
        } else
            inputLayoutFirstNameProfile.setErrorEnabled(false);
        return true;
    }

    private boolean userNameValidation() {

        String userName = etInputUserNameProfile.getText().toString().trim();
        String resultString = new Validator().validateName(userName);
        if (resultString.equals(Constants.errorFirstNameMsg)) {

            inputLayoutUserNameProfile.setError(getString(R.string.err_msg_user_name));
            return false;
            //  requestFocus(etInputUserName);
        } else
            inputLayoutUserNameProfile.setErrorEnabled(false);
        return true;
    }


    public boolean validateEmail() {
        String email = etInputUserIdProfile.getText().toString().trim();

        if (email.isEmpty() || !Validator.isValidEmail(email)) {
            inputLayoutUserIdProfile.setError(getString(R.string.err_msg_email));
            //   requestFocus(etInputUserId);
            return false;
        } else {
            inputLayoutUserIdProfile.setErrorEnabled(false);
        }

        return true;
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.contact) {
            Intent intent = new Intent(EditProfileActivity.this, AllContactsListActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.alternative_currency) {
            Intent intent = new Intent(EditProfileActivity.this, AlternativeCurrencyActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }
            // CropImage.startPickImageActivity(this);

        } else if (id == R.id.mpin) {

        } else if (id == R.id.changepassword) {

            Intent intent = new Intent(EditProfileActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.support) {

        } else if (id == R.id.logout) {
            prefManager.clearUserIdSharedPreference();
            Intent intent = new Intent(EditProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        }


        drawer.closeDrawer(GravityCompat.END);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }


    private class EditProfileTextWatcher implements TextWatcher {

        private View view;

        private EditProfileTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.et_input_first_name_profile:
                    FirstNameValidation();
                    break;
                case et_input_last_name_profile:
                    lastNameValidation();
                    break;
                case R.id.et_input_user_name_profile:
                    userNameValidation();
                    break;
                case R.id.et_input_user_id_profile:
                    validateEmail();
                    break;


            }
        }
    }

}
