package in.bitstreet.com.itdwallet.utills;

import android.content.SharedPreferences;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import android.content.SharedPreferences;
import android.util.Base64;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jay on 13/12/17.
 */

public class SignatureGenerator {



    public static String generateSignature(String api_text, String key) {
        String signature = "";

        try {
            Mac sha_HMAC = Mac.getInstance("HmacSHA512");
            SecretKeySpec secret_key = new SecretKeySpec(api_text.getBytes(), "HmacSHA512");
            sha_HMAC.init(secret_key);
            // Compute the hmac on input data bytes
            byte[] rawHmac = sha_HMAC.doFinal(key.getBytes());
            // Convert raw bytes to Hex
            byte[] hexBytes = new Hex().encode(rawHmac);
            //  Covert array of Hex bytes to a String
            signature = new String(hexBytes, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return signature;
    }


    public static String getHMAC256(String key) throws NoSuchAlgorithmException {
        MessageDigest objSHA = MessageDigest.getInstance("SHA-512");
        byte[] bytSHA = objSHA.digest(key.getBytes());
        BigInteger intNumber = new BigInteger(1, bytSHA);
        String strHashCode = intNumber.toString(16);

        while (strHashCode.length() < 128)
        {
            strHashCode = "0" + strHashCode;
        }
        System.out.println("SHA-512: \n" + strHashCode);
        return strHashCode;
    }
}
