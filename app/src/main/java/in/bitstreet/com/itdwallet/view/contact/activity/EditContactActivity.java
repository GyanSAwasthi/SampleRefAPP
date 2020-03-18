package in.bitstreet.com.itdwallet.view.contact.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import in.bitstreet.com.itdwallet.R;
import in.bitstreet.com.itdwallet.SplashScreenActivity;
import in.bitstreet.com.itdwallet.database.DataHelper;
import in.bitstreet.com.itdwallet.database.SQLHelper;
import in.bitstreet.com.itdwallet.utills.CallegraphyFontActivity;
import in.bitstreet.com.itdwallet.utills.CircularImageCreater;
import in.bitstreet.com.itdwallet.utills.Constants;
import in.bitstreet.com.itdwallet.utills.PicassoCircleTransformation;
import in.bitstreet.com.itdwallet.utills.Validator;
import in.bitstreet.com.itdwallet.view.login.LoginActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.R.id.button1;

public class EditContactActivity extends CallegraphyFontActivity {
    private TextInputLayout inputLayoutNameEditContact,inputLayoutEmailIdEditContact, inputLayoutUserNameEditContact, inputLayoutWalletAddressEditContact,inputLayoutNotesEditContact;
    private EditText etInputNameEditContact, etInputtEmailIdEditContact, etInputUserNameEditContact, etInputAddressEditContact, etInputNotesEditContact;
    ImageButton imgBtnBack;
    private static final float BLUR_RADIUS = 25f;
    ImageButton btnMenu,btnImgBack;
    DataHelper db;
    SQLHelper helper;
    SQLiteDatabase database;
    Context context;
    ImageView imageViewInner;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        context = this;


        Resources mResources=getResources();
       String userName=getIntent().getStringExtra("username").toString();
        String userEmail=getIntent().getStringExtra("email").toString();

        ImageView imageView = (ImageView) findViewById(R.id.imgprofile);
         imageViewInner = (ImageView) findViewById(R.id.imgProfileinner);


        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.user);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user);

        Bitmap blurredBitmap = blur(bitmap);

        imageView.setImageBitmap(blurredBitmap);


       RoundedBitmapDrawable drawable = new CircularImageCreater().createRoundedBitmapDrawableWithBorder(bitmap1,mResources);
       imageViewInner.setImageDrawable(drawable);


    setIds();
        listeners();
       // initActionBar();
        init();
        //populateUserImage();



    }
    public void populateUserImage() {

        String imageUrl = db.getUserImageFromDB(context);
        ;

        if (imageUrl.length() > 0 && imageUrl.toString().contains("https://")) {

            Picasso.with(context)
                    .load(imageUrl)
                    .transform(new PicassoCircleTransformation(context))
                    .into(imageViewInner);
        }

    }


    private void init() {
        helper = new SQLHelper(context);
        database = helper.getWritableDatabase();
        db = new DataHelper(context);
        db.open();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public Bitmap blur(Bitmap image) {

        if (null == image) return null;

        Bitmap outputBitmap = Bitmap.createBitmap(image);

        final RenderScript renderScript = RenderScript.create(this);

        Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);

        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);

        //Intrinsic Gausian blur filter

        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));

        theIntrinsic.setRadius(20);

        theIntrinsic.setInput(tmpIn);

        theIntrinsic.forEach(tmpOut);

        tmpOut.copyTo(outputBitmap);

        return outputBitmap;

    }

    public void deleteDialogConfirmation() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.delete_contact_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);



        alertDialogBuilderUserInput
                .setCancelable(false)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // ToDo get user input here
                        dialogBox.cancel();

                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();


                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
        Button btnPositive = alertDialogAndroid.getButton(AlertDialog.BUTTON_POSITIVE);//upgrade
        Button btnNegative = alertDialogAndroid.getButton(AlertDialog.BUTTON_NEGATIVE);//skip

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = 20;
        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);
        btnNegative.setGravity(Gravity.LEFT);
        btnPositive.setGravity(Gravity.RIGHT);

    }
    private void initActionBar() {
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Edit Contact");
       // ab.setDisplayShowTitleEnabled(false);
       // ab.setElevation(0);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_settings:
                //your code here
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void listeners() {

        etInputNameEditContact.addTextChangedListener(new EditProfileTextWatcher(etInputNameEditContact));
        etInputtEmailIdEditContact.addTextChangedListener(new EditProfileTextWatcher(etInputtEmailIdEditContact));
        etInputUserNameEditContact.addTextChangedListener(new EditProfileTextWatcher(etInputUserNameEditContact));

        btnImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(EditContactActivity.this, btnMenu);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.main, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Delete"))
                        {
                            deleteDialogConfirmation();
                        }else if(item.getTitle().equals("Share")){
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                            sendIntent.setType("text/plain");
                            startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.hint_UserId)));
                        }else if(item.getTitle().equals("Send Cryptocurrency")){

                            Intent intent=new Intent(EditContactActivity.this,SendCryptocurrencyActivity.class);
                            startActivity(intent);

                        }else if(item.getTitle().equals("Request")){
                            Intent intent=new Intent(EditContactActivity.this,RequestCryptocurrencyActivity.class);
                            startActivity(intent);
                        }

                        Toast.makeText(EditContactActivity.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });//closing the setOnClickListener method


    }

    private void setIds() {
        btnMenu=(ImageButton)findViewById(R.id.imgbtnmenu) ;
        btnImgBack=(ImageButton)findViewById(R.id.imgbtnback) ;


        inputLayoutNameEditContact = (TextInputLayout) findViewById(R.id.input_layout_edit_contact_name);
        inputLayoutEmailIdEditContact = (TextInputLayout) findViewById(R.id.input_layout_edit_contact_email_id);
        inputLayoutUserNameEditContact = (TextInputLayout) findViewById(R.id.input_layout_edit_contact_user_name);
        inputLayoutWalletAddressEditContact = (TextInputLayout) findViewById(R.id.input_layout_edit_contact_wallet_address);
        inputLayoutNotesEditContact = (TextInputLayout) findViewById(R.id.input_layout_edit_contact_notes);


        etInputNameEditContact = (EditText) findViewById(R.id.et_input_edit_contact_name);
        etInputtEmailIdEditContact = (EditText) findViewById(R.id.et_input_edit_contact_email_id);
        etInputUserNameEditContact = (EditText) findViewById(R.id.et_input_edit_contact_user_name);
        etInputAddressEditContact = (EditText) findViewById(R.id.et_input_edit_contact_wallet_address);

        etInputNotesEditContact = (EditText) findViewById(R.id.et_input_edit_contact_notes);

    }
    private void FirstNameValidation() {
        String firstName = etInputNameEditContact.getText().toString().trim();
        String resultString = new Validator().validateFirstLastName(firstName);

        if (resultString.equals(Constants.errorFirstNameMsg))
            inputLayoutNameEditContact.setError(resultString);
        else
            inputLayoutNameEditContact.setErrorEnabled(false);
    }

    private void userNameValidation() {

        String userName = etInputUserNameEditContact.getText().toString().trim();
        String resultString = new Validator().validateName(userName);
        if (resultString.equals(Constants.errorFirstNameMsg)) {

            inputLayoutUserNameEditContact.setError(getString(R.string.err_msg_user_name));
            //  requestFocus(etInputUserName);
        } else {
            inputLayoutUserNameEditContact.setErrorEnabled(false);
        }
    }


    public boolean validateEmail() {
        String email = etInputtEmailIdEditContact.getText().toString().trim();

        if (email.isEmpty() || !Validator.isValidEmail(email)) {
            inputLayoutEmailIdEditContact.setError(getString(R.string.err_msg_email));
            //   requestFocus(etInputUserId);
            return false;
        } else {
            inputLayoutEmailIdEditContact.setErrorEnabled(false);
        }

        return true;
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
                case R.id.et_input_edit_contact_name:
                    FirstNameValidation();
                    break;
                case R.id.et_input_edit_contact_user_name:
                    userNameValidation();
                    break;
                case R.id.et_input_edit_contact_email_id:
                    validateEmail();
                    break;



            }
        }
    }

}
