package in.bitstreet.com.itdwallet.model.model;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jay on 11/12/17.
 */

public class ForgotPasswordPojo {

    @SerializedName("errCode")
    private String errCode;

    @SerializedName("message")
    private String message;



    @SerializedName("status")
    private String status;


    @SerializedName("result")
    private JsonObject result;


    public String getErrorCode() {
        return errCode;
    }


    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public JsonObject getResult() {
        return result;
    }
}
