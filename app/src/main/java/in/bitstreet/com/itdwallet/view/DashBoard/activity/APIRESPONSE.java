package in.bitstreet.com.itdwallet.view.DashBoard.activity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shahbaz on 26/10/17.
 */

public class APIRESPONSE {

    @SerializedName("errCode")
    private int errCode;

    @SerializedName("errMsg")
    private String errMsg;

    @SerializedName("devicePublicKey")
    private String devicePublicKey;


    @SerializedName("devicePrivateKey")
    private String devicePrivateKey;


    @SerializedName("forceUpdate")
    private int forceUpdate;

    @SerializedName("userExist")
    private int userExist;

    @SerializedName("firstLogin")
    private int firstLogin;


    public int getForceUpdate() {
        return forceUpdate;
    }

    public int getErrCode() {
        return errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public String getDevicePublicKey() {
        return devicePublicKey;
    }

    public String getDevicePrivateKey() {
        return devicePrivateKey;
    }


    public int getUserExist() {
        return userExist;
    }

    public int getFirstLogin() {
        return firstLogin;
    }

}
