package in.bitstreet.com.itdwallet.view.contact.activity;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import in.bitstreet.com.itdwallet.R;
import in.bitstreet.com.itdwallet.utills.CallegraphyFontActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SendCryptocurrencyActivity extends CallegraphyFontActivity {
    private TextInputLayout inputLayoutSendCryptoAmt,inputLayoutSendSendContact, inputLayoutDestiWalletAddress;
    private EditText etInputSendCryptoAmt, etInputtSendSendContact, etInputDestiWalletAddress;
    Button btnSubmit;
    ImageButton imgBtnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_cryptocurrency);

        // get the reference of View's
        setIds();
        listeners();

    }



    private void setIds() {
        imgBtnBack=(ImageButton)findViewById(R.id.imgbtnbackSend);
        btnSubmit=(Button)findViewById(R.id.btnSendbtc) ;
        inputLayoutSendCryptoAmt = (TextInputLayout) findViewById(R.id.input_layout_Send_crypto_amt);
        inputLayoutSendSendContact = (TextInputLayout) findViewById(R.id.input_layout_Send_crypto_contact);
        inputLayoutDestiWalletAddress = (TextInputLayout) findViewById(R.id.input_layout__Send_crypto_send_wallet);


        etInputSendCryptoAmt = (EditText) findViewById(R.id.et_input__Send_crypto_amt);
        etInputtSendSendContact = (EditText) findViewById(R.id.et_input__Send_crypto_contact);
        etInputDestiWalletAddress = (EditText) findViewById(R.id.et_input_Send_crypto_send_wallet);

    }

    private void listeners() {

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cryptoAmt = etInputSendCryptoAmt.getText().toString().trim();
                String contactSendAddress = etInputtSendSendContact.getText().toString().trim();
                String cryptoWalletAddress = etInputDestiWalletAddress.getText().toString().trim();

                if(cryptoAmt.isEmpty() && contactSendAddress.isEmpty() && cryptoWalletAddress.isEmpty()){
                    inputLayoutSendCryptoAmt.setError(getString(R.string.err_msg_user_name));
                    inputLayoutSendSendContact.setError(getString(R.string.err_msg_user_name));
                    inputLayoutDestiWalletAddress.setError(getString(R.string.err_msg_user_name));

                }else if(cryptoAmt.isEmpty() || contactSendAddress.isEmpty() || cryptoWalletAddress.isEmpty()){
                    if(cryptoAmt.length()<=0){
                        inputLayoutSendCryptoAmt.setError(getString(R.string.err_msg_user_name));
                        inputLayoutSendSendContact.setErrorEnabled(false);
                        inputLayoutDestiWalletAddress.setErrorEnabled(false);
                    }else if(contactSendAddress.length()<=0){
                        inputLayoutSendSendContact.setError(getString(R.string.err_msg_user_name));
                        inputLayoutSendCryptoAmt.setErrorEnabled(false);
                        inputLayoutDestiWalletAddress.setErrorEnabled(false);

                    }else{
                        inputLayoutDestiWalletAddress.setError(getString(R.string.err_msg_user_name));
                        inputLayoutSendCryptoAmt.setErrorEnabled(false);
                        inputLayoutSendSendContact.setErrorEnabled(false);
                    }

                }else{
                    inputLayoutSendCryptoAmt.setErrorEnabled(false);
                    inputLayoutSendSendContact.setErrorEnabled(false);
                    inputLayoutDestiWalletAddress.setErrorEnabled(false);
                }
            }
        });

    }


}