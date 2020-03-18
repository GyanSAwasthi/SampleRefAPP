package in.bitstreet.com.itdwallet.view.DashBoard.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import in.bitstreet.com.itdwallet.R;
import in.bitstreet.com.itdwallet.utills.Constants;
import in.bitstreet.com.itdwallet.view.DashBoard.activity.DashBoardActivity;
import in.bitstreet.com.itdwallet.view.DashBoard.activity.EditProfileActivity;
import in.bitstreet.com.itdwallet.view.contact.activity.AllContactsListActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SendFragment extends Fragment  {
    ImageView imgContact;
    EditText tvContactName;
    String username;
    Communicator communicator;



    public interface Communicator {
        public void sendDataToActivity(String s);
    }

    public static final String TAG = SendFragment.class.getSimpleName();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Communicator) {
            communicator = (Communicator) context;
        } else {
         /*  throw new IllegalArgumentException(
                    "Must implement " + TAG + ".Communicator on caller Activity");*/
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_send, container, false);
        imgContact = (ImageView) view.findViewById(R.id.contactbook);
        tvContactName = (EditText) view.findViewById(R.id.tvcontactname);

        if(!TextUtils.isEmpty(username)) {
            username = getArguments().getString("username").toString();
            tvContactName.setText(username);
        }

        listeners();

        return view;
    }




    private void listeners() {
       /* imgContact.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                communicator.sendDataToActivity(Constants.selectUserName);
                Intent intent = new Intent(getActivity(), AllContactsListActivity.class);
                startActivity(intent);

            }
        });*/
    }


}