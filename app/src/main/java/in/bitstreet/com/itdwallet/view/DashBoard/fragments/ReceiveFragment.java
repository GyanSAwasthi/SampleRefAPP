package in.bitstreet.com.itdwallet.view.DashBoard.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.bitstreet.com.itdwallet.R;
import in.bitstreet.com.itdwallet.utills.Constants;
import in.bitstreet.com.itdwallet.utills.Validator;
import in.bitstreet.com.itdwallet.view.DashBoard.fragmentadapter.ReceiverRecyclerViewAdapter;
import in.bitstreet.com.itdwallet.view.contact.activity.AllContactsListActivity;
import in.bitstreet.com.itdwallet.view.contact.adapter.RecyclerViewAdapter;
import in.bitstreet.com.itdwallet.view.login.SignUpActivity;

import static in.bitstreet.com.itdwallet.R.id.btn_generateaddress;
import static in.bitstreet.com.itdwallet.R.id.et_input_last_name;
import static in.bitstreet.com.itdwallet.R.id.et_input_walletaddressreceiver;
import static in.bitstreet.com.itdwallet.R.id.text;

public class ReceiveFragment extends Fragment implements ReceiverRecyclerViewAdapter.OnItemClick {
    private TextInputLayout inputLayoutLabelForAddress, inputLayoutWalletAddress, inputLayoutLabelForEditReceiveAddress, inputLayoutWalletAddressForEditReceiveAddress;
    private EditText etInputLabelForAddress, etInputWalletAddress, etInputLabelForEditReceiveAddress, etInputWalletAddressForEditReceiveAddress;
    RecyclerView recyclerViewCardContainer;
    LinearLayout linearLayoutMyWalletInfo, linearLayoutEditAddress,linearLayoutGenAddress;
    ReceiverRecyclerViewAdapter recyclerViewAdapter;

    ReceiverRecyclerViewAdapter.OnItemClick onitemClick = (ReceiverRecyclerViewAdapter.OnItemClick) this;

    ArrayList<String> labelsForAddArrayList, walletAddressArrayList;

    public ReceiveFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receive, container, false);
        inputLayoutLabelForAddress = (TextInputLayout) view.findViewById(R.id.input_layout_labelforaddressreceiver);
        inputLayoutWalletAddress = (TextInputLayout) view.findViewById(R.id.input_layout_walletaddressreceiver);

        inputLayoutLabelForEditReceiveAddress = (TextInputLayout) view.findViewById(R.id.input_layout_labelforeditreceiveaddress);
        inputLayoutWalletAddressForEditReceiveAddress = (TextInputLayout) view.findViewById(R.id.input_layout_walletaddressforeditreceiveaddress);

        etInputLabelForAddress = (EditText) view.findViewById(R.id.et_input_labelforaddressreceiver);
        etInputWalletAddress = (EditText) view.findViewById(R.id.et_input_walletaddressreceiver);


        etInputLabelForEditReceiveAddress = (EditText) view.findViewById(R.id.et_input_labelforeditreceiveaddress);
        etInputWalletAddressForEditReceiveAddress = (EditText) view.findViewById(R.id.et_input_walletaddress_foreditreceiveaddress);

        Button   btnCancel=(Button) view.findViewById(R.id.btn_cancel_edit);

        ImageView qrCodeImgView = (ImageView) view.findViewById(R.id.qr_code_img);
        Picasso.with(getActivity().getApplicationContext()).load("https://chart.googleapis.com/chart?cht=qr&chl=1234&chs=200x200").into(qrCodeImgView);


        ImageView btnFloating = (ImageView) view.findViewById(R.id.floatinbtnrecive);
        btnFloating.setImageResource(R.drawable.ic_add_circle_black_24dp);

        Button btnGenAddress = (Button) view.findViewById(R.id.btn_generateaddress);
        recyclerViewCardContainer = (RecyclerView) view.findViewById(R.id.rvreceivelist);
        RecyclerView.LayoutManager recylerViewLayoutManager = new LinearLayoutManager(getContext());

        recyclerViewCardContainer.setLayoutManager(recylerViewLayoutManager);

        linearLayoutMyWalletInfo = (LinearLayout) view.findViewById(R.id.ll_mywallet_address_info);

        linearLayoutMyWalletInfo = (LinearLayout) view.findViewById(R.id.ll_mywallet_address_info);
        walletAddressArrayList = new ArrayList<String>();
        labelsForAddArrayList = new ArrayList<String>();



    linearLayoutGenAddress = (LinearLayout) view.findViewById(R.id.ll_generate_address);
        linearLayoutEditAddress = (LinearLayout) view.findViewById(R.id.ll_edit_receiver_address);
        linearLayoutEditAddress.setVisibility(View.GONE);
        linearLayoutMyWalletInfo.setVisibility(View.VISIBLE);

        listeners(btnFloating, btnGenAddress,btnCancel);
        return view;
    }

    private void listeners(ImageView btnFloating,  Button btnGenAddress,Button btnCancel) {

        btnFloating.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                linearLayoutMyWalletInfo.setVisibility(View.GONE);
                linearLayoutGenAddress.setVisibility(View.VISIBLE);

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

               /* linearLayoutMyWalletInfo.setVisibility(View.VISIBLE);
                linearLayoutGenAddress.setVisibility(View.GONE);*/
            }
        });

        btnGenAddress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                /* if ((labelForAddressValidation()) && (inputWalletAddressValidation())) {
                    linearLayoutMyWalletInfo.setVisibility(View.VISIBLE);
                    linearLayoutGenAddress.setVisibility(View.GONE);
                    recyclerViewAdapter = new ReceiverRecyclerViewAdapter(getActivity(), labelsForAddArrayList, walletAddressArrayList, ReceiveFragment.this);

                    recyclerViewCardContainer.setAdapter(recyclerViewAdapter);
                }*/



                String LabelForAddress = etInputLabelForAddress.getText().toString().trim();
                String walletAddress = etInputWalletAddress.getText().toString().trim();
                if (LabelForAddress.isEmpty() && walletAddress.isEmpty() ) {
                    inputLayoutLabelForAddress.setError(getString(R.string.err_msg_user_name));
                    inputLayoutWalletAddress.setError(getString(R.string.err_msg_user_name));
                }
               else if((LabelForAddress.isEmpty()) && !(walletAddress.isEmpty())){
                    inputLayoutLabelForAddress.setError(getString(R.string.err_msg_user_name));
                    inputLayoutWalletAddress.setErrorEnabled(false);
                }
              else  if(!(LabelForAddress.isEmpty()) && (walletAddress.isEmpty())){
                    inputLayoutWalletAddress.setError(getString(R.string.err_msg_user_name));
                    inputLayoutLabelForAddress.setErrorEnabled(false);
                }

                else{
                    linearLayoutMyWalletInfo.setVisibility(View.VISIBLE);
                    linearLayoutGenAddress.setVisibility(View.GONE);
                    labelsForAddArrayList.add(LabelForAddress);
                    walletAddressArrayList.add(walletAddress);
                    recyclerViewAdapter = new ReceiverRecyclerViewAdapter(getActivity(), labelsForAddArrayList, walletAddressArrayList, ReceiveFragment.this);

                    recyclerViewCardContainer.setAdapter(recyclerViewAdapter);
                    inputLayoutWalletAddress.setErrorEnabled(false);
                    inputLayoutLabelForAddress.setErrorEnabled(false);

                }

               /* if ((labelForAddressValidation()) && (inputWalletAddressValidation())) {
                    linearLayoutMyWalletInfo.setVisibility(View.VISIBLE);
                    linearLayoutGenAddress.setVisibility(View.GONE);
                    recyclerViewAdapter = new ReceiverRecyclerViewAdapter(getActivity(), labelsForAddArrayList, walletAddressArrayList, ReceiveFragment.this);

                    recyclerViewCardContainer.setAdapter(recyclerViewAdapter);
                }*/

              /*  if(labelForAddressValidation()) {
                    if(inputWalletAddressValidation()) {

                    }
                }
*/
            }
        });


    }

    private boolean labelForAddressValidation() {
        String LabelForAddress = etInputLabelForAddress.getText().toString().trim();
        String resultString = new Validator().validateFirstLastName(LabelForAddress);

        if (resultString.equals(Constants.errorFirstNameMsg)) {
            inputLayoutLabelForAddress.setError(resultString);
            return false;
        } else {
            inputLayoutLabelForAddress.setErrorEnabled(false);
        }
        return true;
    }


    private boolean inputWalletAddressValidation() {

        String walletAddress = etInputWalletAddress.getText().toString().trim();
        String resultString = new Validator().validateFirstLastName(walletAddress);
        if (resultString.equals(Constants.errorFirstNameMsg)) {

            inputLayoutWalletAddress.setError(getString(R.string.err_msg_user_name));
            return false;
        } else {
            inputLayoutWalletAddress.setErrorEnabled(false);
        }
        return true;
    }

    @Override
    public void onClick(String clickedLabelForAddress, String clickedWalletAddress) {
        linearLayoutEditAddress.setVisibility(View.VISIBLE);
        linearLayoutMyWalletInfo.setVisibility(View.GONE);
        Toast.makeText(getContext(), "You clicked fragment" + clickedLabelForAddress, Toast.LENGTH_SHORT).show();
        etInputLabelForEditReceiveAddress.setText(clickedLabelForAddress);
        etInputWalletAddressForEditReceiveAddress.setText(clickedWalletAddress);

    }

    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    // handle back button


                    if (linearLayoutGenAddress.getVisibility() == View.VISIBLE) {
                        linearLayoutMyWalletInfo.setVisibility(View.VISIBLE);
                        linearLayoutGenAddress.setVisibility(View.GONE);

                    } else if(linearLayoutEditAddress.getVisibility() == View.VISIBLE){
                        linearLayoutEditAddress.setVisibility(View.GONE);
                        linearLayoutGenAddress.setVisibility(View.GONE);
                        linearLayoutMyWalletInfo.setVisibility(View.VISIBLE);


                    }else{
                        getActivity().finish();
                    }

                    return true;

                }

                return false;
            }
        });
    }


}