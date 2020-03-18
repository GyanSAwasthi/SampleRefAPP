/*
package in.bitstreet.com.itdwallet.utills;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.hansinfotech.omblee.Config;
import in.hansinfotech.omblee.CustomUI;
import in.hansinfotech.omblee.KeyboardUtils;
import in.hansinfotech.omblee.MSG;
import in.hansinfotech.omblee.PrefManager;
import in.hansinfotech.omblee.R;
import in.hansinfotech.omblee.Utils;
import in.hansinfotech.omblee.ValidatorUtil;
import in.hansinfotech.omblee.adapter.UsernameAutoCompleteAdapter;
import in.hansinfotech.omblee.api.ApiClient;
import in.hansinfotech.omblee.api.ApiInterface;
import in.hansinfotech.omblee.model.APIRESPONSE;
import in.hansinfotech.omblee.offline.OfflineController;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileInfoActivity extends AppCompatActivity implements ImagePickerCallback {


    private Toolbar toolbar;

    private DatePickerDialog datePickerDialog;

    private SimpleDateFormat dateFormatter;

    private TextView toolbar_title, dob_tv, gender_error_tv, dob_error_tv;

    private ImageView profile_dp, profile_timeline_dp;

    private FloatingActionButton take_pic_btn;

    private TextInputLayout fname_lt, mname_lt, lname_lt, email_lt, username_lt, designation_lt, company_lt;

    private EditText input_fname, input_mname, input_lname, input_email, input_designation, input_company;

    private AutoCompleteTextView input_username;

    private LinearLayout dob_lt, gender_lt, society_layout;

    private String dob_str = "";

    private Button save_btn;

    private View tutorial_lt;

    private RelativeLayout parent_lt;

    private View topbar_error_lt, gender_ul, dob_ul;

    private ImagePicker imagePicker;

    private CameraImagePicker cameraPicker;

    String image_url, pickerPath ,profile_pic_str;

    private Spinner gender_spinner;
    private ArrayAdapter<CharSequence> adapter_gender;

    private boolean isFtue = false;

    private PrefManager prefManager;
    private Utils utils;
    private ValidatorUtil valid;
    private CustomUI customUI;
    private KeyboardUtils keyboardUtils;

    private static final int PERMISSION_REQUEST_CODE = 1;

    private static final int SAVE_PROFILE_ACTION = 1;

    private static final int UPDATE_IAMGE_ACTION = 2;

    private int moduleStatus = 0;

    private JsonObject accessControl = new JsonObject();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);

        tutorial_lt = findViewById(R.id.tutorial_lt);


        prefManager = new PrefManager(this);
        utils = new Utils(this);
        valid = new ValidatorUtil();
        customUI = new CustomUI(this);
        keyboardUtils = new KeyboardUtils(this);

        try
        {
            accessControl = prefManager.getAccessControl();
        }
        catch (Exception e){}


        gender_spinner = (Spinner) findViewById(R.id.gender_spinner);
        adapter_gender = new ArrayAdapter<CharSequence>(this, R.layout.spinner_label3, getResources().getStringArray(R.array.genderArr));
        adapter_gender.setDropDownViewResource(R.layout.spinner_item3);
        gender_spinner.setAdapter(adapter_gender);


        profile_dp = (ImageView)findViewById(R.id.profile_dp);
        profile_timeline_dp = (ImageView)findViewById(R.id.profile_timeline_dp);

        take_pic_btn = (FloatingActionButton) findViewById(R.id.take_pic_btn);

        topbar_error_lt = findViewById(R.id.topbar_error_lt);

        dob_tv = (TextView) findViewById(R.id.dob_tv);

        fname_lt = (TextInputLayout) findViewById(R.id.fname_lt);
        mname_lt = (TextInputLayout) findViewById(R.id.mname_lt);
        lname_lt = (TextInputLayout) findViewById(R.id.lname_lt);
        email_lt = (TextInputLayout) findViewById(R.id.email_lt);
        username_lt = (TextInputLayout) findViewById(R.id.username_lt);
        designation_lt = (TextInputLayout) findViewById(R.id.designation_lt);
        company_lt = (TextInputLayout) findViewById(R.id.company_lt);
        dob_lt = (LinearLayout) findViewById(R.id.dob_lt);
        gender_lt = (LinearLayout) findViewById(R.id.gender_lt);
        society_layout = (LinearLayout) findViewById(R.id.society_layout);


        input_fname = (EditText) findViewById(R.id.input_fname);
        input_mname = (EditText) findViewById(R.id.input_mname);
        input_lname = (EditText) findViewById(R.id.input_lname);
        input_email = (EditText) findViewById(R.id.input_email);
        input_username = (AutoCompleteTextView) findViewById(R.id.input_username);
        input_designation = (EditText) findViewById(R.id.input_designation);
        input_company = (EditText) findViewById(R.id.input_company);

        gender_ul = findViewById(R.id.gender_ul);
        dob_ul = findViewById(R.id.dob_ul);

        gender_error_tv = (TextView) findViewById(R.id.gender_error_tv);
        dob_error_tv = (TextView) findViewById(R.id.dob_error_tv);

        save_btn = (Button) findViewById(R.id.save_btn);




        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");


        if(!prefManager.getObjBoolean("ftueScreen1"))
        {
            showTutorial(true);
        }
        else
        {
            showTutorial(false);
        }


        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);


                    Date selected_date = newDate.getTime();
                dob_str = dateFormatter.format(selected_date);
                getUsernameSuggestion();
                dob_tv.setText(dob_str);

            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());



        dob_lt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateDob(true,"");
                datePickerDialog.show();
            }
        });




        loadDataLocally();

        setUpFtue();


        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserProfile();
            }
        });


        take_pic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ///  granting permission  ////
                if(!checkPermission())
                {
                    requestPermission();
                }
                else
                {
                    openImageChooser();
                }
                ///////////////////////////////
            }
        });

        getUserInfo();



        /////////


        input_fname.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                validateFirstName(true, "");
                getUsernameSuggestion();
            }
        });


        input_mname.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                validateMiddleName(true, "");
            }
        });



        input_lname.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                validateLastName(true, "");
                getUsernameSuggestion();
            }
        });

        input_email.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                validateEmail(true, "");
            }
        });

        getUsernameSuggestion();

        input_username.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

                validateUsername(true, "");

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                checkUsername();

            }
        });



        input_designation.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                validateDesignation(true, "");
            }
        });



        input_company.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                validateCompany(true, "");
            }
        });


        /////////

        input_company.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
 Write your logic here that will be executed when user taps next button

                    keyboardUtils.toggleKeyboard();
                    saveUserProfile();

                    handled = true;
                }
                return handled;
            }
        });


        //////////

    }


    private void getUsernameSuggestion()
    {
        UsernameAutoCompleteAdapter usd= new UsernameAutoCompleteAdapter(this, R.layout.pad_search_row, input_fname.getText().toString(),
                input_lname.getText().toString(), dob_str);
        input_username.setAdapter(usd);
    }


    private void showErrorLt(boolean flag, String msg)
    {
        if(flag)
        {
            TextView tperror_tv = (TextView) topbar_error_lt.findViewById(R.id.tperror_tv);
            tperror_tv.setText(msg);
            topbar_error_lt.setVisibility(View.VISIBLE);
        }
        else
        {
            topbar_error_lt.setVisibility(View.GONE);
        }
    }



    private void openImageChooser()
    {
        showErrorLt(false, "");



        ///////

        LayoutInflater factory = LayoutInflater.from(this);
        final View ImageDialogView = factory.inflate(R.layout.image_chooser_dialog, null);
        final AlertDialog imageDialog = new AlertDialog.Builder(this).create();
        imageDialog.setView(ImageDialogView);
        imageDialog.setCanceledOnTouchOutside(true);


        LinearLayout gallery_lt = (LinearLayout) ImageDialogView.findViewById(R.id.gallery_lt);
        LinearLayout camera_lt = (LinearLayout) ImageDialogView.findViewById(R.id.camera_lt);


        gallery_lt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageDialog.dismiss();
                pickImageSingle();
            }
        });


        camera_lt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageDialog.dismiss();
                takePicture();
            }
        });




        imageDialog.show();

        ///////
    }



    private void showTutorial(boolean flag)
    {
        if(flag)
        {
            tutorial_lt.setVisibility(View.VISIBLE);
        }
        else
        {
            tutorial_lt.setVisibility(View.GONE);
        }

        ///////

        Button tut_skip_btn = tutorial_lt.findViewById(R.id.tut_skip_btn);

        tut_skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tutorial_lt.setVisibility(View.GONE);
                prefManager.setObjBoolean(true , "ftueScreen1");
            }
        });


        tutorial_lt.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return true;
            }

        });

        ////////
    }




    private void loadData(JsonObject data)
    {
        try {
            setProfilePic(data.get("photo").getAsString());
        }
        catch (Exception e){}

        try {
            input_fname.setText(data.get("firstName").getAsString());
        }
        catch (Exception e){}

        try {
            input_mname.setText(data.get("middleName").getAsString());
        }
        catch (Exception e){}

        try {
            input_lname.setText(data.get("lastName").getAsString());
        }
        catch (Exception e){}

        try {
            input_email.setText(data.get("email").getAsString());
        }
        catch (Exception e){}

        try {
            String username_str = data.get("username").getAsString();
            if(!valid.isNullOrEmpty(username_str))
            {
                input_username.setText(username_str);
            }
        }
        catch (Exception e){}

        try {
            dob_str = data.get("dob").getAsString();
            if(!valid.isNullOrEmpty(dob_str))
            {
                dob_tv.setText(dob_str);
            }
        }
        catch (Exception e){}

        try {
            String genderStr = data.get("gender").getAsString();
            genderStr = genderStr.substring(0, 1).toUpperCase() + genderStr.substring(1);
            utils.selectSpinnerItemByValue(gender_spinner, genderStr);
        }
        catch (Exception e){}

        try {
            input_designation.setText(data.get("designation").getAsString());
        }
        catch (Exception e){}

        try {
            input_company.setText(data.get("organization").getAsString());
        }
        catch (Exception e){}

        loadSocietyInfo();
    }


    private void loadDataLocally()
    {
        JsonObject data = prefManager.fetchUserInfoLocally();

        loadData(data);

    }


    private void loadSocietyInfo()
    {
        try
        {
            JsonArray buildingArray = prefManager.getBuildingDetails();

            if(buildingArray.size()==0)
            {
                throw new Exception("building details not found");
            }
            else
            {
                LinearLayout building_lt = (LinearLayout) findViewById(R.id.building_lt);

                building_lt.removeAllViews();

                LayoutInflater inflater = LayoutInflater.from(ProfileInfoActivity.this);

                int countData = 0;
                for(JsonElement element : buildingArray)
                {
                    countData++;
                    JsonObject object = (JsonObject) element;

                    final String apt_no = object.get("flatNo").getAsString();
                    final String bld_name = object.get("buldingName").getAsString();

                    LinearLayout building_row = (LinearLayout) inflater.inflate(R.layout.building_row, null, false);

                    EditText input_apt = (EditText) building_row.findViewById(R.id.input_apt);
                    EditText input_bld = (EditText) building_row.findViewById(R.id.input_bld);

                    input_apt.setText(apt_no);
                    input_bld.setText(bld_name);

                    building_lt.addView(building_row);

                    if(countData<buildingArray.size())
                    {
                        View spaceView = new View(this);
                        spaceView.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                30));

                        building_lt.addView(spaceView);
                    }
                }


                TextView building_add_more_tv = (TextView) findViewById(R.id.building_add_more_tv);

                building_add_more_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        customUI.myToast(MSG.CONTACT_BUILDING_SECRETARY);
                    }
                });
            }

        }
        catch (Exception e)
        {
            society_layout.setVisibility(View.GONE);
        }
    }




    private void getUserInfo()
    {

        final String token = Config.GET_USER_INFO_API_TEXT;
        try {

            OfflineController offlineController = new OfflineController(ProfileInfoActivity.this ,new OfflineController.OnApiResponse() {
                @Override
                public void messageReceived(String api_text, JsonObject data) {

                    if(api_text.equals(token)) {

                        try {

                            int errCode = data.get("errCode").getAsInt();
                            String errMsg = data.get("errMsg").getAsString();
                            JsonObject result = data.get("result").getAsJsonObject();

                            if (errCode == -1) {

                                prefManager.saveUserInfoLocally(result);

                            } else {
                                showErrorLt(true, errMsg);
                            }

                        }
                        catch (Exception e)
                        {
                            showErrorLt(true, MSG.SOMETHING_WRONG);
                        }

                    }
                }
            });


            String signature = utils.getSignature(Config.GET_USER_INFO_API_TEXT + prefManager.getPublicKey(), prefManager.getPrivatekey());
            Map<String, String> params = new HashMap<String, String>();
            params.put("devicePublicKey", prefManager.getPublicKey());
            params.put("signature", signature);
            params.put("userId", prefManager.getObjString("userId"));
            params.put("requiredFields", "photo, firstName, middleName, lastName, email, username, gender, dob, designation, organization, buildingDetails");
            offlineController.getApiData(true ,token, "user/getUserInfo/", 1, 1, 1, params, 0);

        }
        catch (Exception e){
            showErrorLt(true, MSG.SOMETHING_WRONG);
        }
    }



    private void successDialog(String msg, final int action)
    {
        final Dialog dialog1 = new Dialog(ProfileInfoActivity.this);
        dialog1.setContentView(R.layout.success_dialog);
        TextView text_tv = (TextView) dialog1.findViewById(R.id.text_tv);
        TextView ok_tv = (TextView) dialog1.findViewById(R.id.ok_tv);

        text_tv.setText(msg);



        ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();

                if(action == SAVE_PROFILE_ACTION)
                {
                    moduleStatus = prefManager.checkAccessControl(accessControl, "Family");

                    if(isFtue && moduleStatus==1)
                    {
                        Intent intent = new Intent(ProfileInfoActivity.this, AddFamilyMemberActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    else
                    {
                        startActivity(new Intent(ProfileInfoActivity.this, FeedActivity.class));
                    }
                }
            }
        });

        dialog1.show();
    }


    private void setUpFtue()
    {
        isFtue = prefManager.getFtueMode();
    }


    private void saveUserProfile()
    {
        resetAllInputs();
        showErrorLt(false, "");

        if(!utils.isConnected())
        {
            showErrorLt(true, getString(R.string.offline_str));
            return;
        }

        String fname_str = input_fname.getText().toString();
        String mname_str = input_mname.getText().toString();
        String lname_str = input_lname.getText().toString();
        String email_str = input_email.getText().toString();
        String username_str = input_username.getText().toString();
        String gender_str = gender_spinner.getSelectedItem().toString();
        String designation_str = input_designation.getText().toString();
        String company_str = input_company.getText().toString();



        ////

        if(!valid.isNullOrEmpty(fname_str) && !valid.nameCheck(fname_str))
        {
            validateFirstName(false, MSG.INVALID_FIRST_NAME);
            return;
        }

        if(!valid.isNullOrEmpty(mname_str) && !valid.nameCheck(mname_str))
        {
            validateMiddleName(false, MSG.INVALID_MIDDLE_NAME);
            return;
        }


        if(!valid.isNullOrEmpty(lname_str) && !valid.nameCheck(lname_str))
        {
            validateLastName(false, MSG.INVALID_LAST_NAME);
            return;
        }

        if(!valid.isNullOrEmpty(email_str) && !valid.isValidEmail(email_str))
        {
            validateEmail(false, MSG.INVALID_EMAIL);
            return;
        }





        /////////
        try {


            customUI.showProgress(MSG.SAVING_PROFILE_INFO);

            String signature = utils.getSignature(Config.EDIT_USER_INFO_API_TEXT + prefManager.getPublicKey(), prefManager.getPrivatekey());
            ApiInterface apiService = ApiClient.getClient(this).create(ApiInterface.class);
            Call call = apiService.editUserProfile(prefManager.getPublicKey() , signature, prefManager.getObjString("userId"),
                    fname_str, mname_str, lname_str, dob_str, email_str, username_str, gender_str.toLowerCase(), company_str, designation_str, prefManager.getObjInt("firstLogin"));
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    APIRESPONSE api = (APIRESPONSE) response.body();

                    customUI.hideProgress();

                    try {

                        if(response.isSuccessful()) {

                            if (api.getErrCode() == -1) {

                                prefManager.saveUserInfoLocally(api.getResult());

                                successDialog(api.getErrMsg(), SAVE_PROFILE_ACTION);

                            }

                        }
                        else
                        {
                            Gson gson = new GsonBuilder().create();
                            try {
                                APIRESPONSE mApiError = gson.fromJson(response.errorBody().string(),APIRESPONSE.class);

                                if(mApiError.getErrCode()==Config.validationError)
                                {
                                    try {

                                        if(mApiError.getErrorValidation().get("field1").getAsString().equals("firstName"))
                                        {
                                            validateFirstName(false, mApiError.getErrMsg());
                                        }

                                        if(mApiError.getErrorValidation().get("field1").getAsString().equals("middleName"))
                                        {
                                            validateMiddleName(false, mApiError.getErrMsg());
                                        }

                                        if(mApiError.getErrorValidation().get("field1").getAsString().equals("lastName"))
                                        {
                                            validateLastName(false, mApiError.getErrMsg());
                                        }

                                        if(mApiError.getErrorValidation().get("field1").getAsString().equals("dob"))
                                        {
                                            validateDob(false, mApiError.getErrMsg());
                                        }

                                        if(mApiError.getErrorValidation().get("field1").getAsString().equals("email"))
                                        {
                                            validateEmail(false, mApiError.getErrMsg());
                                        }

                                        if(mApiError.getErrorValidation().get("field1").getAsString().equals("username"))
                                        {
                                            validateUsername(false, mApiError.getErrMsg());
                                        }

                                        if(mApiError.getErrorValidation().get("field1").getAsString().equals("gender"))
                                        {
                                            validateGender(false, mApiError.getErrMsg());
                                        }

                                        if(mApiError.getErrorValidation().get("field1").getAsString().equals("organization"))
                                        {
                                            validateCompany(false, mApiError.getErrMsg());
                                        }

                                        if(mApiError.getErrorValidation().get("field1").getAsString().equals("designation"))
                                        {
                                            validateDesignation(false, mApiError.getErrMsg());
                                        }

                                    }
                                    catch (Exception e){
                                        showErrorLt(true, MSG.SOMETHING_WRONG);
                                    }
                                }
                                else
                                {
                                    showErrorLt(true, mApiError.getErrMsg());
                                }


                            } catch (Exception e) {
                                showErrorLt(true, MSG.SOMETHING_WRONG);
                            }
                        }

                    }

                    catch (Exception e)
                    {
                        showErrorLt(true, MSG.SOMETHING_WRONG);
                    }



                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    customUI.hideProgress();
                    showErrorLt(true, MSG.SOMETHING_WRONG);
                    call.cancel();
                }
            });


        }
        catch (Exception e)
        {
            customUI.hideProgress();
            showErrorLt(true, MSG.SOMETHING_WRONG);
        }
        ///////////

    }


    private void checkUsername()
    {
        if(!utils.isConnected())
        {
            return;
        }


        /////////
        try {

            String signature = utils.getSignature(Config.CHECK_USERNAME_API_TEXT + prefManager.getPublicKey(), prefManager.getPrivatekey());
            ApiInterface apiService = ApiClient.getClient(this).create(ApiInterface.class);
            Call call = apiService.checkUsername(prefManager.getPublicKey() , signature, prefManager.getObjString("userId"), input_username.getText().toString());
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    APIRESPONSE api = (APIRESPONSE) response.body();

                    customUI.hideProgress();

                    try {

                        if(response.isSuccessful()) {

                            if (api.getErrCode() == -1) {


                            }
                        }
                        else
                        {
                            Gson gson = new GsonBuilder().create();
                            try {
                                APIRESPONSE mApiError = gson.fromJson(response.errorBody().string(),APIRESPONSE.class);

                                validateUsername(false, mApiError.getErrMsg());

                            } catch (Exception e) {}
                        }

                    }

                    catch (Exception e)
                    {}
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    call.cancel();
                }
            });


        }
        catch (Exception e)
        {}
        ///////////
    }


    private void updateImage()
    {
        if(!utils.isConnected())
        {
            showErrorLt(true, getString(R.string.offline_str));
            return;
        }

        try
        {
            customUI.showProgress(MSG.SAVING_PROFILE_PIC);


            String signature = utils.getSignature(Config.PROFILE_PIC_API_TEXT + prefManager.getPublicKey(), prefManager.getPrivatekey());

            File file = new File(image_url);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/png"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
            ApiInterface apiService = ApiClient.getClient(this).create(ApiInterface.class);

            RequestBody PublicKey = RequestBody.create(MediaType.parse("text/plain"), prefManager.getPublicKey());
            RequestBody signaturebody = RequestBody.create(MediaType.parse("text/plain"), signature);
            RequestBody userIdbody = RequestBody.create(MediaType.parse("text/plain"), prefManager.getObjString("userId"));

            Call call = apiService.updateprofilepic( PublicKey,signaturebody,userIdbody,body);

            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    APIRESPONSE api = (APIRESPONSE) response.body();

                    customUI.hideProgress();

                    try {

                            if(response.isSuccessful()) {

                                if (api.getErrCode() == -1) {
                                    profile_pic_str = api.getPath();
                                    if(!valid.isNullOrEmpty(profile_pic_str))
                                    {

                                        prefManager.setObjString(profile_pic_str, "photo");
                                        setProfilePic(profile_pic_str);


                                    }
                                } else {
                                    showErrorLt(true, api.getErrMsg());
                                }

                            }
                            else
                            {
                                Gson gson = new GsonBuilder().create();
                                try {
                                    APIRESPONSE mApiError = gson.fromJson(response.errorBody().string(),APIRESPONSE.class);

                                    showErrorLt(true, mApiError.getErrMsg());


                                } catch (Exception e) {
                                    showErrorLt(true, MSG.SOMETHING_WRONG);
                                }
                            }
                    }
                    catch (Exception e)
                    {
                        showErrorLt(true, MSG.SOMETHING_WRONG);
                    }

                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    customUI.hideProgress();
                    showErrorLt(true, MSG.SOMETHING_WRONG);
                    call.cancel();
                }
            });
        }
        catch (Exception e)
        {
            customUI.hideProgress();
            showErrorLt(true, MSG.SOMETHING_WRONG);
        }
    }


    private void setProfilePic(String url)
    {
        url = utils.trimImageUrl(url);


        try {

            Picasso.with(this).load(url).placeholder(R.drawable.default_profile).
                    error(R.drawable.default_profile).centerCrop().fit().into(profile_dp);


            Picasso.with(this).load(url).placeholder(R.drawable.default_profile).
                    error(R.drawable.default_profile).centerCrop().fit().into(profile_timeline_dp);

        }
        catch (Exception e) {}
    }



    public void pickImageSingle() {
        imagePicker = new ImagePicker(this);
        imagePicker.shouldGenerateMetadata(true);
        imagePicker.shouldGenerateThumbnails(true);
        imagePicker.setImagePickerCallback(this);
        imagePicker.pickImage();
    }



    public void takePicture() {
        cameraPicker = new CameraImagePicker(this);
        cameraPicker.shouldGenerateMetadata(true);
        cameraPicker.shouldGenerateThumbnails(true);
        cameraPicker.setImagePickerCallback(this);
        pickerPath = cameraPicker.pickImage();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Picker.PICK_IMAGE_DEVICE) {
                if (imagePicker == null) {
                    imagePicker = new ImagePicker(this);
                    imagePicker.setImagePickerCallback(this);
                }
                imagePicker.submit(data);
            }
            else if (requestCode == Picker.PICK_IMAGE_CAMERA) {


                if (cameraPicker == null) {
                    cameraPicker = new CameraImagePicker(this);
                    cameraPicker.setImagePickerCallback(this);
                }
                cameraPicker.submit(data);
            }
            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    image_url = resultUri.getPath();
                    updateImage();
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    Toast.makeText(ProfileInfoActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    public void onImagesChosen(List<ChosenImage> images) {

        image_url = images.get(0).getOriginalPath();
        if(!valid.isNullOrEmpty(image_url)) {

            Uri uri = Uri.parse(images.get(0).getQueryUri());

            CropImage.activity(uri).setAspectRatio(1,1)
                    .start(this);

Uri myUri = Uri.parse(image_url);
            // start cropping activity for pre-acquired image saved on the device
            CropImage.activity(myUri)
                    .start(getContext(), this);


            //updateImage();

        }
    }

    @Override
    public void onError(String message) {
        Toast.makeText(ProfileInfoActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("picker_path", pickerPath);
        super.onSaveInstanceState(outState);


    }




    ///////////////////////   permission for marshmallow  ///////////////////

    private boolean checkPermission(){
        int result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);


        if (result1 == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            //Toast.makeText(this,"You don't have permission to use camera.",Toast.LENGTH_LONG).show();
            return false;

        }
    }

    private void requestPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ){

            Toast.makeText(this,"Application needs phone state, write external storage, Sms Read, record audio and camera permission.",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){

//                    Toast.makeText(this, "Permission Granted." ,Toast.LENGTH_SHORT).show();
                    openImageChooser();

                } else {

                    Toast.makeText(this, "Permission Denied." ,Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

    ////////////////////////////////////////////////////////////////////////



    private void resetAllInputs()
    {
        validateFirstName(true,"");
        validateMiddleName(true,"");
        validateLastName(true,"");
        validateEmail(true,"");
        validateUsername(true,"");
        validateDesignation(true,"");
        validateCompany(true,"");
        validateGender(true,"");
        validateDob(true,"");
    }




    private void validateFirstName(boolean flag, String error)
    {
        if(flag)
        {
            fname_lt.setErrorEnabled(false);
            fname_lt.setError(error);
            input_fname.setTextColor(Color.BLACK);
        }
        else
        {
            fname_lt.setErrorEnabled(true);
            fname_lt.setError(error);
            input_fname.setTextColor(getResources().getColor(R.color.errorRed));
        }
    }


    private void validateMiddleName(boolean flag, String error)
    {
        if(flag)
        {
            mname_lt.setErrorEnabled(false);
            mname_lt.setError(error);
            input_mname.setTextColor(Color.BLACK);
        }
        else
        {
            mname_lt.setErrorEnabled(true);
            mname_lt.setError(error);
            input_mname.setTextColor(getResources().getColor(R.color.errorRed));
        }
    }


    private void validateLastName(boolean flag, String error)
    {
        if(flag)
        {
            lname_lt.setErrorEnabled(false);
            lname_lt.setError(error);
            input_lname.setTextColor(Color.BLACK);
        }
        else
        {
            lname_lt.setErrorEnabled(true);
            lname_lt.setError(error);
            input_lname.setTextColor(getResources().getColor(R.color.errorRed));
        }
    }


    private void validateEmail(boolean flag, String error)
    {
        if(flag)
        {
            email_lt.setErrorEnabled(false);
            email_lt.setError(error);
            input_email.setTextColor(Color.BLACK);
        }
        else
        {
            email_lt.setErrorEnabled(true);
            email_lt.setError(error);
            input_email.setTextColor(getResources().getColor(R.color.errorRed));
        }
    }


    private void validateUsername(boolean flag, String error)
    {
        if(flag)
        {
            username_lt.setErrorEnabled(false);
            username_lt.setError(error);
            input_username.setTextColor(Color.BLACK);
        }
        else
        {
            username_lt.setErrorEnabled(true);
            username_lt.setError(error);
            input_username.setTextColor(getResources().getColor(R.color.errorRed));
        }
    }


    private void validateDesignation(boolean flag, String error)
    {
        if(flag)
        {
            designation_lt.setErrorEnabled(false);
            designation_lt.setError(error);
            input_designation.setTextColor(Color.BLACK);
        }
        else
        {
            designation_lt.setErrorEnabled(true);
            designation_lt.setError(error);
            input_designation.setTextColor(getResources().getColor(R.color.errorRed));
        }
    }


    private void validateCompany(boolean flag, String error)
    {
        if(flag)
        {
            company_lt.setErrorEnabled(false);
            company_lt.setError(error);
            input_company.setTextColor(Color.BLACK);
        }
        else
        {
            company_lt.setErrorEnabled(true);
            company_lt.setError(error);
            input_company.setTextColor(getResources().getColor(R.color.errorRed));
        }
    }


    private void validateGender(boolean flag, String error)
    {
        if(flag)
        {
            gender_ul.setBackgroundColor(getResources().getColor(R.color.defaultUnderline));
            gender_error_tv.setVisibility(View.GONE);
        }
        else
        {
            gender_ul.setBackgroundColor(getResources().getColor(R.color.errorRed));
            gender_error_tv.setText(error);
            gender_error_tv.setVisibility(View.VISIBLE);
        }
    }



    private void validateDob(boolean flag, String error)
    {
        if(flag)
        {
            dob_tv.setTextColor(getResources().getColor(R.color.textGrey));
            dob_ul.setBackgroundColor(getResources().getColor(R.color.defaultUnderline));
            dob_error_tv.setVisibility(View.GONE);
        }
        else
        {
            dob_tv.setTextColor(getResources().getColor(R.color.errorRed));
            dob_ul.setBackgroundColor(getResources().getColor(R.color.errorRed));
            dob_error_tv.setText(error);
            dob_error_tv.setVisibility(View.VISIBLE);
        }
    }



}
*/
