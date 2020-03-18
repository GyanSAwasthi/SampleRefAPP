package in.bitstreet.com.itdwallet.model.model;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class LoginResult {

    @SerializedName("success")
    private boolean status;

    @SerializedName("status")
    private String statusCode;

    @SerializedName("message")
    private String message;


    @SerializedName("data")
    private JsonObject data;


    @SerializedName("statusCode")
    private String statusCodeChangePassword;

    @SerializedName("errorCode")
    private String errorCode;




    public boolean getResult() {
        return status;
    }

    public String getstatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public JsonObject getData() {
        return data;
    }

    public String getstatusCodeChangePwd() {
        return statusCodeChangePassword;
    }

    public String getErrorCode() {
        return errorCode;
    }





}
