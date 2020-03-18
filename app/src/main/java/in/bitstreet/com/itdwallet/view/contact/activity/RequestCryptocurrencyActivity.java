package in.bitstreet.com.itdwallet.view.contact.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import in.bitstreet.com.itdwallet.R;
import in.bitstreet.com.itdwallet.utills.CallegraphyFontActivity;
import in.bitstreet.com.itdwallet.utills.Constants;
import in.bitstreet.com.itdwallet.utills.Validator;
import in.bitstreet.com.itdwallet.view.DashBoard.fragmentadapter.ReceiverRecyclerViewAdapter;
import in.bitstreet.com.itdwallet.view.DashBoard.fragments.ReceiveFragment;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RequestCryptocurrencyActivity extends CallegraphyFontActivity {
    private TextInputLayout inputLayoutReqCryptoAmt,inputLayoutReqSendContact, inputLayoutDestiWalletAddress;
    private EditText etInputReqCryptoAmt, etInputtReqSendContact, etInputDestiWalletAddress;
    Button btnSubmit;
    ImageButton imgBtnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_cryptocurrency);

        setIds();
        listeners();
    }



    private void setIds() {
        imgBtnBack=(ImageButton)findViewById(R.id.imgbtnbackreq);
        btnSubmit=(Button)findViewById(R.id.btnSubmitReq) ;
        inputLayoutReqCryptoAmt = (TextInputLayout) findViewById(R.id.input_layout_req_crypto_amt);
        inputLayoutReqSendContact = (TextInputLayout) findViewById(R.id.input_layout_req_crypto_contact);
        inputLayoutDestiWalletAddress = (TextInputLayout) findViewById(R.id.input_layout__req_crypto_send_wallet);


        etInputReqCryptoAmt = (EditText) findViewById(R.id.et_input__req_crypto_amt);
        etInputtReqSendContact = (EditText) findViewById(R.id.et_input__req_crypto_contact);
        etInputDestiWalletAddress = (EditText) findViewById(R.id.et_input_req_crypto_send_wallet);

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
                String cryptoAmt = etInputReqCryptoAmt.getText().toString().trim();
                String contactReqAddress = etInputtReqSendContact.getText().toString().trim();
                String cryptoWalletAddress = etInputDestiWalletAddress.getText().toString().trim();

                if(cryptoAmt.isEmpty() && contactReqAddress.isEmpty() && cryptoWalletAddress.isEmpty()){
                    inputLayoutReqCryptoAmt.setError(getString(R.string.err_msg_user_name));
                    inputLayoutReqSendContact.setError(getString(R.string.err_msg_user_name));
                    inputLayoutDestiWalletAddress.setError(getString(R.string.err_msg_user_name));

                }else if(cryptoAmt.isEmpty() || contactReqAddress.isEmpty() || cryptoWalletAddress.isEmpty()){
                        if(cryptoAmt.length()<=0){
                            inputLayoutReqCryptoAmt.setError(getString(R.string.err_msg_user_name));
                            inputLayoutReqSendContact.setErrorEnabled(false);
                            inputLayoutDestiWalletAddress.setErrorEnabled(false);
                        }else if(contactReqAddress.length()<=0){
                            inputLayoutReqSendContact.setError(getString(R.string.err_msg_user_name));
                            inputLayoutReqCryptoAmt.setErrorEnabled(false);
                            inputLayoutDestiWalletAddress.setErrorEnabled(false);

                        }else{
                            inputLayoutDestiWalletAddress.setError(getString(R.string.err_msg_user_name));
                            inputLayoutReqCryptoAmt.setErrorEnabled(false);
                            inputLayoutReqSendContact.setErrorEnabled(false);
                        }

                }else{
                    inputLayoutReqCryptoAmt.setErrorEnabled(false);
                    inputLayoutReqSendContact.setErrorEnabled(false);
                    inputLayoutDestiWalletAddress.setErrorEnabled(false);
                }
            }
        });

    }



}
