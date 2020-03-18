package in.bitstreet.com.itdwallet.model.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jay on 13/12/17.
 */

public class ErrorPojo {

    @SerializedName("success")
    private boolean status;

    /*  @SerializedName("status")
      private String statusCode;
  */
    @SerializedName("message")
    private String message;


    @SerializedName("errorCode")
    private String errorCode;




    public boolean getResult() {
        return status;
    }

    public String getErrorcode() {
        return message;
    }



    public String getMessage() {
        return message;
    }








}
