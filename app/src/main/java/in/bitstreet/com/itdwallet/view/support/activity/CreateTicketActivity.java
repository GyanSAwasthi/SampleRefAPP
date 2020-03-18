package in.bitstreet.com.itdwallet.view.support.activity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import in.bitstreet.com.itdwallet.R;
import in.bitstreet.com.itdwallet.database.DataHelper;
import in.bitstreet.com.itdwallet.database.SQLHelper;
import in.bitstreet.com.itdwallet.utills.CallegraphyFontActivity;
import in.bitstreet.com.itdwallet.utills.PicassoCircleTransformation;
import in.bitstreet.com.itdwallet.utills.PrefManager;
import in.bitstreet.com.itdwallet.utills.Validator;
import in.bitstreet.com.itdwallet.view.DashBoard.activity.EditProfileActivity;
import in.bitstreet.com.itdwallet.view.alternativecurrency.AlternativeCurrencyActivity;
import in.bitstreet.com.itdwallet.view.changepassword.ChangePasswordActivity;
import in.bitstreet.com.itdwallet.view.contact.activity.AllContactsListActivity;
import in.bitstreet.com.itdwallet.view.mpin.MPinActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CreateTicketActivity extends CallegraphyFontActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener{

    TextInputLayout  inputLayoutCreateTicketEmailId,inputLayoutPasswordCreateTicketSubject,inputLayoutPasswordCreateTicketTextInfo;
    EditText inputPasswordCreateTicketEmailId,inputPasswordCreateTicketSubject,inputPasswordCreateTicketTextInfo;
    DrawerLayout drawer;
    TextView tvUserName;
    ImageView userImage;
    DataHelper db;
    SQLHelper helper;
    SQLiteDatabase database;
    Context ctx;
    PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_drawer);

        ViewGroup inclusionViewGroup = (ViewGroup)findViewById(R.id.ll_container);

        View child1 = LayoutInflater.from(this).inflate(
                R.layout.content_create_ticket, null);

        inclusionViewGroup.addView(child1);

        init();
        initDB();
        setIds();
        populateUserImage();




        drawer.setDrawerListener(this);


    }

    private void setIds() {

        inputLayoutCreateTicketEmailId = (TextInputLayout) findViewById(R.id.input_layout_create_ticket_email_id);
        inputLayoutPasswordCreateTicketSubject = (TextInputLayout) findViewById(R.id.input_layout_create_ticket_subject);
        inputLayoutPasswordCreateTicketTextInfo = (TextInputLayout) findViewById(R.id.input_layout_create_ticket_textinfo);
        inputPasswordCreateTicketEmailId = (EditText) findViewById(R.id.input_password_create_ticket_email_id);
        inputPasswordCreateTicketSubject = (EditText) findViewById(R.id.input_create_ticket_subject);
        inputPasswordCreateTicketTextInfo = (EditText) findViewById(R.id.input_create_ticket_textinfo);

        drawer = (DrawerLayout) findViewById(R.id.common_drawer_layout);
        ImageButton imgBtnUAMSliding = (ImageButton) findViewById(R.id.btnSliding_common);
        NavigationView navigationView = (NavigationView) findViewById(R.id.common_nav_view);
        View header = navigationView.getHeaderView(0);
        TextView editProfile=(TextView)header. findViewById(R.id.textVieweditProfile) ;

        userImage = (ImageView)header. findViewById(R.id.imageView);
        tvUserName = (TextView) header.findViewById(R.id.tvusernamecommon);
        tvUserName.setText(prefManager.getUserNamePref());

        Listeners(imgBtnUAMSliding,navigationView,editProfile);
    }

    private void init() {
        ctx = this;
        prefManager = new PrefManager(ctx);
    }

    private void initDB() {
        helper = new SQLHelper(ctx);
        database = helper.getWritableDatabase();
        db = new DataHelper(ctx);
        db.open();
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
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }


    private void Listeners(ImageButton imgBtnUAMSliding , NavigationView navigationView, TextView editProfile) {
        Button btnSubmit = (Button) findViewById(R.id.btn_submit_create_ticket);
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
                Intent intent=new Intent(CreateTicketActivity.this,EditProfileActivity.class);
                startActivity(intent);

            }
        });
    }


    private void submitForm() {


        if (!validation()) {
            return;
        }



    }

    private boolean validation() {
        String createTicketEmailId=inputPasswordCreateTicketEmailId.getText().toString().trim();
        String createTicketSubject=inputPasswordCreateTicketSubject.getText().toString().trim();
        String createTicketTextInfo=inputPasswordCreateTicketTextInfo.getText().toString().trim();

        if(createTicketEmailId.isEmpty() && createTicketSubject.isEmpty() && createTicketTextInfo.isEmpty()){
            validateEmail();
           // inputLayoutCreateTicketEmailId.setError(getString(R.string.err_msg_your_text));
            inputLayoutPasswordCreateTicketSubject.setError(getString(R.string.err_msg_subject));
            inputLayoutPasswordCreateTicketTextInfo.setError(getString(R.string.err_msg_your_text));
            return  false;

        }else if(createTicketEmailId.isEmpty() || createTicketSubject.isEmpty() || createTicketTextInfo.isEmpty()){
            if(createTicketEmailId.length()<=0){
                validateEmail();
               // inputLayoutCreateTicketEmailId.setError(getString(R.string.err_msg_your_text));
                inputLayoutPasswordCreateTicketSubject.setErrorEnabled(false);
                inputLayoutPasswordCreateTicketTextInfo.setErrorEnabled(false);
                return  false;

            }else if(createTicketSubject.length()<=0){
                inputLayoutPasswordCreateTicketSubject.setError(getString(R.string.err_msg_subject));
                inputLayoutCreateTicketEmailId.setErrorEnabled(false);
                inputLayoutPasswordCreateTicketTextInfo.setErrorEnabled(false);
                return  false;


            }
            else{
                inputLayoutPasswordCreateTicketTextInfo.setError(getString(R.string.err_msg_your_text));
                inputLayoutCreateTicketEmailId.setErrorEnabled(false);
                inputLayoutPasswordCreateTicketSubject.setErrorEnabled(false);
                return  false;

            }

        }else{
            inputLayoutCreateTicketEmailId.setErrorEnabled(false);
            inputLayoutPasswordCreateTicketSubject.setErrorEnabled(false);
            inputLayoutPasswordCreateTicketTextInfo.setErrorEnabled(false);


        }

        return true;
    }

    public void validateEmail() {
        String email = inputPasswordCreateTicketEmailId.getText().toString().trim();

        if (email.isEmpty() || !Validator.isValidEmail(email)) {
            inputLayoutCreateTicketEmailId.setGravity(Gravity.RIGHT);
            inputLayoutCreateTicketEmailId.setError(getString(R.string.err_msg_email));
        } else {
            inputLayoutCreateTicketEmailId.setErrorEnabled(false);
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.contact) {
            Intent intent=new Intent(CreateTicketActivity.this,AllContactsListActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.alternative_currency) {
            Intent intent=new Intent(CreateTicketActivity.this,AlternativeCurrencyActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.mpin) {
            Intent intent=new Intent(CreateTicketActivity.this,MPinActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.changepassword) {

            Intent intent=new Intent(CreateTicketActivity.this,ChangePasswordActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.support) {

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
