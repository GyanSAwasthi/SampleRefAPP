package in.bitstreet.com.itdwallet.view.mpin;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import in.bitstreet.com.itdwallet.R;
import in.bitstreet.com.itdwallet.SplashScreenActivity;
import in.bitstreet.com.itdwallet.utills.Constants;
import in.bitstreet.com.itdwallet.view.DashBoard.activity.EditProfileActivity;
import in.bitstreet.com.itdwallet.view.alternativecurrency.AlternativeCurrencyActivity;
import in.bitstreet.com.itdwallet.view.changepassword.ChangePasswordActivity;
import in.bitstreet.com.itdwallet.view.contact.activity.AllContactsListActivity;
import in.bitstreet.com.itdwallet.view.login.LoginActivity;
import in.bitstreet.com.itdwallet.view.login.ResetPasswordActivity;
import in.bitstreet.com.itdwallet.view.login.VerifyOTPActivity;
import in.bitstreet.com.itdwallet.view.support.activity.SupportMainActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MPinActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener {
    private EditText setPinFirstDigitEditText;
    private EditText setPinSecondDigitEditText;
    private EditText setPinThirdDigitEditText;
    private EditText setPinForthDigitEditText;
    private EditText setPinHiddenEditText;

    private EditText cnfSetPinFirstDigitEditText;
    private EditText cnfSetPinSecondDigitEditText;
    private EditText cnfSetPinThirdDigitEditText;
    private EditText cnfSetPinForthDigitEditText;
    private EditText cnfSetPinHiddenEditText;

    CardView  cardview;
    Button btnSave;


    DrawerLayout drawer;
    NavigationView navigationView;
    ImageButton imgBtnUAMSliding;
    TextView editProfile;
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
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new MPinActivity.MainLayout(this, null));

        initActionBar();


        //To hide the soft key board when activity launches
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//for setting the font style for whole activity font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Regular-.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        init();
        setPINListeners();
        setMpinMsgDialog();
        setFocus();
        drawer.setDrawerListener(this);


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


    public void setMpinMsgDialog() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.mpin_msg_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);



        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Set MPIN", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // ToDo get user input here
                        dialogBox.cancel();
                        cardview.setVisibility(View.VISIBLE);
                        //displayMpinFieldDialog();

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
        btnPositive. setTextColor(getResources().getColor(R.color.tab_color));
        btnPositive.setTextSize(16);
        btnNegative. setTextColor(getResources().getColor(R.color.black));
        btnPositive.setTextSize(14);
        btnNegative.setAllCaps(false);
        btnPositive.setAllCaps(false);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = 20;
        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);
        btnNegative.setGravity(Gravity.RIGHT);
        btnPositive.setGravity(Gravity.RIGHT);

    }

    public void displayMpinFieldDialog() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.display_mpin_field_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput
                .setCancelable(false)

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // ToDo get user input here
                        dialogBox.cancel();

                    }
                });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
        Button btnPositive = alertDialogAndroid.getButton(AlertDialog.BUTTON_POSITIVE);//upgrade
        btnPositive. setTextColor(getResources().getColor(R.color.black));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = 20;
        btnPositive.setLayoutParams(layoutParams);
        btnPositive.setGravity(Gravity.RIGHT);
        btnPositive.setTextSize(16);
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



    private void initActionBar() {

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.hide();

    }

    /**
     * Initialize EditText fields.
     */
    private void init() {


          cardview = (CardView)findViewById(R.id.card_view);
        btnSave = (Button) findViewById(R.id.btn_save);

        drawer = (DrawerLayout) findViewById(R.id.common_drawer_layout);
        imgBtnUAMSliding = (ImageButton) findViewById(R.id.btnSliding_common);
        navigationView = (NavigationView) findViewById(R.id.common_nav_view);
        View header = navigationView.getHeaderView(0);
        editProfile=(TextView)header. findViewById(R.id.textVieweditProfile) ;

        setPinFirstDigitEditText = (EditText) findViewById(R.id.setpin_first_edittext);
        setPinSecondDigitEditText = (EditText) findViewById(R.id.setpin_second_edittext);
        setPinThirdDigitEditText = (EditText) findViewById(R.id.setpin_third_edittext);
        setPinForthDigitEditText = (EditText) findViewById(R.id.setpin_forth_edittext);
        setPinHiddenEditText = (EditText) findViewById(R.id.setpin_hidden_edittext);

        //show the otp field
        setDefaultPinBackground(setPinFirstDigitEditText);
        setDefaultPinBackground(setPinSecondDigitEditText);
        setDefaultPinBackground(setPinThirdDigitEditText);
        setDefaultPinBackground(setPinForthDigitEditText);


        cnfSetPinFirstDigitEditText = (EditText) findViewById(R.id.cnf_pin_first_edittext);
        cnfSetPinSecondDigitEditText = (EditText) findViewById(R.id.cnf_pin_second_edittext);
        cnfSetPinThirdDigitEditText = (EditText) findViewById(R.id.cnf_pin_third_edittext);
        cnfSetPinForthDigitEditText = (EditText) findViewById(R.id.cnf_pin_forth_edittext);
        cnfSetPinHiddenEditText = (EditText) findViewById(R.id.cnf_pin_hidden_edittext);

        //show the otp field
        setDefaultPinBackground(cnfSetPinFirstDigitEditText);
        setDefaultPinBackground(cnfSetPinSecondDigitEditText);
        setDefaultPinBackground(cnfSetPinThirdDigitEditText);
        setDefaultPinBackground(cnfSetPinForthDigitEditText);
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
    private void setPINListeners() {

btnSave.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        displayMpinFieldDialog();
    }
});

        imgBtnUAMSliding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.END);
                hideSoftKeyboard();


            }
        });

        navigationView.setNavigationItemSelectedListener(this);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                }
                Intent intent=new Intent(MPinActivity.this,EditProfileActivity.class);
                startActivity(intent);

            }
        });
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.contact) {
            Intent intent=new Intent(MPinActivity.this,AllContactsListActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.alternative_currency) {

            Intent intent=new Intent(MPinActivity.this,AlternativeCurrencyActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.mpin) {

            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.changepassword) {
            Intent intent=new Intent(MPinActivity.this,ChangePasswordActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.support) {

            Intent intent=new Intent(MPinActivity.this,SupportMainActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }


        } else if (id == R.id.logout) {

        }


        drawer.closeDrawer(GravityCompat.END);
        return true;
    }




    /**
     * Custom LinearLayout with overridden onMeasure() method
     * for handling software keyboard show and hide events.
     */
    public class MainLayout extends LinearLayout {

        public MainLayout(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.common_drawer, this);


            ViewGroup inclusionViewGroup = (ViewGroup)findViewById(R.id.ll_container);

            View child = LayoutInflater.from(getContext()).inflate(
                    R.layout.activity_mpin, null);

            inclusionViewGroup.addView(child);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            final int proposedHeight = MeasureSpec.getSize(heightMeasureSpec);
            final int actualHeight = getHeight();

            Log.d("TAG", "proposed: " + proposedHeight + ", actual: " + actualHeight);

            if (actualHeight >= proposedHeight) {
                // Keyboard is shown
                if (setPinHiddenEditText.length() == 0)
                    setFocusedPinBackground(setPinFirstDigitEditText);
                else
                    setDefaultPinBackground(setPinFirstDigitEditText);
                if (cnfSetPinHiddenEditText.length() == 0)
                    setFocusedPinBackground(cnfSetPinFirstDigitEditText);
                else
                    setDefaultPinBackground(cnfSetPinFirstDigitEditText);
            }

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void setFocus() {
/////////////////////////////////////first text////////////////////////////////////////////
        final StringBuilder sbForFirstText = new StringBuilder();
        setPinFirstDigitEditText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (sbForFirstText.length() == 0 & setPinFirstDigitEditText.length() == 1) {
                    sbForFirstText.append(s);
                    setPinFirstDigitEditText.clearFocus();
                    setPinSecondDigitEditText.requestFocus();
                    setPinSecondDigitEditText.setCursorVisible(true);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                if (sbForFirstText.length() == 1) {
                    sbForFirstText.deleteCharAt(0);
                }
            }
            public void afterTextChanged(Editable s) {
                if (sbForFirstText.length() == 0) {
                    setPinFirstDigitEditText.requestFocus();
                }
            }
        });

////////////////////////////////////////second text///////////////////////////////
        final StringBuilder sbSecondText = new StringBuilder();
        setPinSecondDigitEditText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (sbSecondText.length() == 0 & setPinSecondDigitEditText.length() == 1) {
                    sbSecondText.append(s);
                    setPinSecondDigitEditText.clearFocus();
                    setPinThirdDigitEditText.requestFocus();
                    setPinThirdDigitEditText.setCursorVisible(true);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                if (sbSecondText.length() == 1) {
                    sbSecondText.deleteCharAt(0);
                }
            }
            public void afterTextChanged(Editable s) {
                if (sbSecondText.length() == 0) {
                    setPinSecondDigitEditText.requestFocus();

                }
            }
        });
        ////////////////////third text box
        final StringBuilder sbThirdText = new StringBuilder();
        setPinThirdDigitEditText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (sbThirdText.length() == 0 & setPinThirdDigitEditText.length() == 1) {
                    sbThirdText.append(s);
                    setPinThirdDigitEditText.clearFocus();
                    setPinForthDigitEditText.requestFocus();
                    setPinForthDigitEditText.setCursorVisible(true);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                if (sbThirdText.length() == 1) {
                    sbThirdText.deleteCharAt(0);
                }
            }
            public void afterTextChanged(Editable s) {
                if (sbThirdText.length() == 0) {
                    setPinThirdDigitEditText.requestFocus();
                }
            }
        });

        ////////////////////////for fourth text box
        final StringBuilder sbFourthText = new StringBuilder();
        setPinForthDigitEditText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (sbFourthText.length() == 0 & setPinForthDigitEditText.length() == 1) {
                    sbFourthText.append(s);
                    setPinForthDigitEditText.clearFocus();
                    cnfSetPinFirstDigitEditText.requestFocus();
                    cnfSetPinFirstDigitEditText.setCursorVisible(true);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                if (sbFourthText.length() == 1) {
                    sbFourthText.deleteCharAt(0);
                }
            }
            public void afterTextChanged(Editable s) {
                if (sbFourthText.length() == 0) {
                    setPinForthDigitEditText.requestFocus();

                }
                hideSoftKeyboard(setPinForthDigitEditText);
            }
        });


        ///////////////////////////////// cnfSetPinFirst text box///////////////////
        final StringBuilder sbcnfSetPinFirstText = new StringBuilder();
        cnfSetPinFirstDigitEditText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (sbcnfSetPinFirstText.length() == 0 & cnfSetPinFirstDigitEditText.length() == 1) {
                    sbcnfSetPinFirstText.append(s);
                    cnfSetPinFirstDigitEditText.clearFocus();
                    cnfSetPinSecondDigitEditText.requestFocus();
                    cnfSetPinSecondDigitEditText.setCursorVisible(true);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                if (sbcnfSetPinFirstText.length() == 1) {
                    sbcnfSetPinFirstText.deleteCharAt(0);
                }
            }
            public void afterTextChanged(Editable s) {
                if (sbcnfSetPinFirstText.length() == 0) {
                    cnfSetPinFirstDigitEditText.requestFocus();
                }
            }
        });

        final StringBuilder sbcnfSetPinSecondText = new StringBuilder();
        cnfSetPinSecondDigitEditText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (sbcnfSetPinSecondText.length() == 0 & cnfSetPinSecondDigitEditText.length() == 1) {
                    sbcnfSetPinSecondText.append(s);
                    cnfSetPinSecondDigitEditText.clearFocus();
                    cnfSetPinThirdDigitEditText.requestFocus();
                    cnfSetPinThirdDigitEditText.setCursorVisible(true);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                if (sbcnfSetPinSecondText.length() == 1) {
                    sbcnfSetPinSecondText.deleteCharAt(0);
                }
            }
            public void afterTextChanged(Editable s) {
                if (sbcnfSetPinSecondText.length() == 0) {
                    cnfSetPinSecondDigitEditText.requestFocus();
                }
            }
        });

        final StringBuilder sbcnfSetPinThirdText = new StringBuilder();
        cnfSetPinThirdDigitEditText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (sbcnfSetPinThirdText.length() == 0 & cnfSetPinThirdDigitEditText.length() == 1) {
                    sbcnfSetPinThirdText.append(s);
                    cnfSetPinThirdDigitEditText.clearFocus();
                    cnfSetPinForthDigitEditText.requestFocus();
                    cnfSetPinForthDigitEditText.setCursorVisible(true);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                if (sbcnfSetPinThirdText.length() == 1) {
                    sbcnfSetPinThirdText.deleteCharAt(0);
                }
            }
            public void afterTextChanged(Editable s) {
                if (sbcnfSetPinThirdText.length() == 0) {
                    cnfSetPinThirdDigitEditText.requestFocus();
                }
            }
        });

        final StringBuilder sbcnfSetPinForthText = new StringBuilder();
        cnfSetPinForthDigitEditText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (sbcnfSetPinForthText.length() == 0 & cnfSetPinForthDigitEditText.length() == 1) {
                    sbcnfSetPinForthText.append(s);
                    cnfSetPinForthDigitEditText.clearFocus();

                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                if (sbcnfSetPinForthText.length() == 1) {
                    sbcnfSetPinForthText.deleteCharAt(0);
                }
            }
            public void afterTextChanged(Editable s) {
                if (sbcnfSetPinForthText.length() == 0) {
                    cnfSetPinForthDigitEditText.requestFocus();

                }
                hideSoftKeyboard(cnfSetPinForthDigitEditText);
            }
        });



    }



}

