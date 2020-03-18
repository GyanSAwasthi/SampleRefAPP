package in.bitstreet.com.itdwallet.utills;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;

import in.bitstreet.com.itdwallet.R;

/**
 * Created by jay on 10/11/17.
 */

public class Validator {

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isvalidateFirstLastName(String firstName) {

        if (firstName.length() >= 1 && firstName.matches ("^[\\p{L} .'-]+$"))

            return true;
        else
            return false;

    }

    public static boolean isvalidateUserName(String userName) {

        if (userName.length() >= 1 && ( userName.matches ("^[a-zA-Z0-9]*$") || userName.matches ("^[\\p{L} .'-]+$")) )

            return true;
        else
            return false;

    }


        public String validateEmail(String email) {


        if (email.isEmpty() || !Validator.isValidEmail(email)) {
            return String.valueOf(R.string.err_msg_email);
        } else {
            return String.valueOf(R.string.success_msg_email);

        }


    }


    public String validateFirstLastName(String firstLastName) {


        if (!isvalidateFirstLastName(firstLastName)) {

            //  requestFocus(etInputFirstName);

            return Constants.errorFirstNameMsg;
        }

        return "";
    }


    public String validateName(String userName) {

        if (userName.isEmpty() || !isvalidateUserName(userName)) {

            return Constants.errorFirstNameMsg;
        }
        return "";
    }
}
