package in.bitstreet.com.itdwallet.view.contact.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import in.bitstreet.com.itdwallet.R;
import in.bitstreet.com.itdwallet.SplashScreenActivity;
import in.bitstreet.com.itdwallet.utills.CallegraphyFontActivity;
import in.bitstreet.com.itdwallet.utills.Constants;
import in.bitstreet.com.itdwallet.utills.Validator;
import in.bitstreet.com.itdwallet.view.DashBoard.activity.EditProfileActivity;
import in.bitstreet.com.itdwallet.view.login.LoginActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static in.bitstreet.com.itdwallet.R.id.et_input_last_name_profile;

public class AddContactActivity extends CallegraphyFontActivity {
    private TextInputLayout inputLayoutName,inputLayoutEmailId, inputLayoutUserName, inputLayoutWalletAddress,inputLayoutNotes;
    private EditText etInputName, etInputtEmailId, etInputUserName, etInputAddress, etInputNotes;
    ImageButton imgBtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //for setting the font style for whole activity font

        setIds();
        listeners();
    }



    private void listeners() {

        etInputName.addTextChangedListener(new EditProfileTextWatcher(etInputName));
        etInputtEmailId.addTextChangedListener(new EditProfileTextWatcher(etInputtEmailId));
        etInputUserName.addTextChangedListener(new EditProfileTextWatcher(etInputUserName));
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forceUpgrade();
            }
        });

    }

    private void setIds() {

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name_add);
        inputLayoutEmailId = (TextInputLayout) findViewById(R.id.input_layout_add_contct_email_id);
        inputLayoutUserName = (TextInputLayout) findViewById(R.id.input_layout_user_name_add);
        inputLayoutWalletAddress = (TextInputLayout) findViewById(R.id.input_layout_add_contact_wallet_address);
        inputLayoutNotes = (TextInputLayout) findViewById(R.id.input_layout_add_contact_notes);


        etInputName = (EditText) findViewById(R.id.et_input_name_add);
        etInputtEmailId = (EditText) findViewById(R.id.et_input_add_contct_email_id);
        etInputUserName = (EditText) findViewById(R.id.et_input_user_name_add);
        etInputAddress = (EditText) findViewById(R.id.et_input_add_contact_wallet_address);

        etInputNotes = (EditText) findViewById(R.id.et_input_add_contact_notes);
        imgBtnBack = (ImageButton) findViewById(R.id.imgbtnback);

    }
    private void FirstNameValidation() {
        String firstName = etInputName.getText().toString().trim();
        String resultString = new Validator().validateFirstLastName(firstName);

        if (resultString.equals(Constants.errorFirstNameMsg))
            inputLayoutName.setError(resultString);
        else
            inputLayoutName.setErrorEnabled(false);
    }

    private void userNameValidation() {

        String userName = etInputUserName.getText().toString().trim();
        String resultString = new Validator().validateName(userName);
        if (resultString.equals(Constants.errorFirstNameMsg)) {

            inputLayoutUserName.setError(getString(R.string.err_msg_user_name));
            //  requestFocus(etInputUserName);
        } else {
            inputLayoutUserName.setErrorEnabled(false);
        }
    }


    public boolean validateEmail() {
        String email = etInputtEmailId.getText().toString().trim();

        if (email.isEmpty() || !Validator.isValidEmail(email)) {
            inputLayoutEmailId.setError(getString(R.string.err_msg_email));
            //   requestFocus(etInputUserId);
            return false;
        } else {
            inputLayoutEmailId.setErrorEnabled(false);
        }

        return true;
    }

    public void forceUpgrade() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.add_contact_exit_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);



        alertDialogBuilderUserInput
                .setCancelable(false)

                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // ToDo get user input here
                        dialogBox.cancel();

                        finish();
                    }
                })

                .setNegativeButton("Discard",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();

                                finish();

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
                case R.id.et_input_name_add:
                    FirstNameValidation();
                    break;
                case R.id.et_input_user_name_add:
                    userNameValidation();
                    break;
                case R.id.et_input_add_contct_email_id:
                    validateEmail();
                    break;



            }
        }
    }
}
