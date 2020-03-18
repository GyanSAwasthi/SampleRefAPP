package in.bitstreet.com.itdwallet.view.alternativecurrency;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import in.bitstreet.com.itdwallet.R;
import in.bitstreet.com.itdwallet.view.DashBoard.activity.DashBoardActivity;
import in.bitstreet.com.itdwallet.view.DashBoard.activity.EditProfileActivity;
import in.bitstreet.com.itdwallet.view.alternativecurrency.adapter.AlternativeCurrencyAdapter;
import in.bitstreet.com.itdwallet.view.changepassword.ChangePasswordActivity;
import in.bitstreet.com.itdwallet.view.contact.activity.AllContactsListActivity;
import in.bitstreet.com.itdwallet.view.contact.adapter.DividerItemDecoration;
import in.bitstreet.com.itdwallet.view.contact.adapter.RecyclerViewAdapter;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AlternativeCurrencyActivity extends AppCompatActivity  {
    DrawerLayout drawer;
    NavigationView navigationView;
    ImageButton imgBtnUAMSliding;
    TextView editProfile;
    RecyclerView recyclerView;
    RecyclerView.Adapter alternativeCurrencyAdapter;
    RecyclerView.LayoutManager recylerViewLayoutManager;
    EditText  etSearchCurrency ;
    ImageButton imgBtnBack;


    ArrayList<String> curencyList = new ArrayList<String>();
    ArrayList<String> type_name_copy = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alternative_currency);

        //for setting the font style for whole activity font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Regular-.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setIds();
      //  initActionBar();



        initializeData(curencyList) ;

        setIds();
        Listeners();


    }

    //for setting the font style for whole activity font
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }





    public void listUpdate(ArrayList<String> data) {

        alternativeCurrencyAdapter = new AlternativeCurrencyAdapter(AlternativeCurrencyActivity.this, data);

        recyclerView.setAdapter(alternativeCurrencyAdapter);


    }

    private void setIds() {
        etSearchCurrency = (EditText) findViewById(R.id.etSearchCurrency);
        imgBtnBack = (ImageButton) findViewById(R.id.imgbtnback);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewalternativecurrencey);
        recylerViewLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(recylerViewLayoutManager);
    }



    private void Listeners() {




        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);


        etSearchCurrency=(EditText) findViewById(R.id.etSearchCurrency);
        listUpdate(curencyList);

        etSearchCurrency.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable theWatchedText) {
                ArrayList<String> type_name_filter = new ArrayList<String>();

                String text = theWatchedText.toString();

                for (int i = 0; i < curencyList.size(); i++) {

                    if ((curencyList.get(i).toLowerCase()).contains(text
                            .toLowerCase())) {
                        type_name_filter.add(curencyList.get(i));

                    }
                }

                type_name_copy = type_name_filter;

                listUpdate(type_name_copy);
            }
        });

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();

        finish();
    }


    private void initializeData( ArrayList<String> curencyList) {


        curencyList.add("US Doller");
        curencyList.add("Burnei Doller");
        curencyList.add("Argentine peso");
        curencyList.add("Albanian Lek");
        curencyList.add("Euro");
        curencyList.add("Rupees");
        curencyList.add("Dirham");
        curencyList.add("Doller");
        curencyList.add("Yen");
        curencyList.add("zlkjlkj");
        curencyList.add("trijlkjl");

    }

}
