package in.bitstreet.com.itdwallet.view.contact.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.WindowCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.bitstreet.com.itdwallet.R;
import in.bitstreet.com.itdwallet.database.DataHelper;
import in.bitstreet.com.itdwallet.database.SQLHelper;
import in.bitstreet.com.itdwallet.utills.CallegraphyFontActivity;
import in.bitstreet.com.itdwallet.utills.Constants;
import in.bitstreet.com.itdwallet.utills.PicassoCircleTransformation;
import in.bitstreet.com.itdwallet.utills.PrefManager;
import in.bitstreet.com.itdwallet.view.DashBoard.activity.DashBoardActivity;
import in.bitstreet.com.itdwallet.view.DashBoard.activity.EditProfileActivity;
import in.bitstreet.com.itdwallet.view.DashBoard.fragments.SendFragment;
import in.bitstreet.com.itdwallet.view.alternativecurrency.AlternativeCurrencyActivity;
import in.bitstreet.com.itdwallet.view.alternativecurrency.adapter.AlternativeCurrencyAdapter;
import in.bitstreet.com.itdwallet.view.changepassword.ChangePasswordActivity;
import in.bitstreet.com.itdwallet.view.contact.adapter.DividerItemDecoration;
import in.bitstreet.com.itdwallet.view.contact.adapter.RecyclerViewAdapter;
import in.bitstreet.com.itdwallet.view.mpin.MPinActivity;
import in.bitstreet.com.itdwallet.view.support.activity.SupportMainActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AllContactsListActivity extends CallegraphyFontActivity implements NavigationView.OnNavigationItemSelectedListener, RecyclerViewAdapter.OnItemClick {
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recylerViewLayoutManager;
    ImageButton btnSliding_common;
    TextView editProfile;
    DrawerLayout drawer;
    NavigationView navigationView;
    String sendFragmentStringFlag;
    ImageView fabBtnAddContact;
    String clickedContactName, clickedUserEmail;

    TextView tvUserName;
    ImageView userImage;
    Context context;
    PrefManager prefManager;
    DataHelper db;
    SQLHelper helper;
    SQLiteDatabase database;

    EditText etSearchCurrency;
    ImageButton imgBtnBack;
    String searchedtextStr = "";


    ArrayList<String> nameArrList, emailsArrList, countryArrList, type_name_copy_arrList, type_email_copy_arrList, type_country_copy_arrList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_BAR);
        setContentView(R.layout.common_drawer);

        ViewGroup inclusionViewGroup = (ViewGroup) findViewById(R.id.ll_container);

        View child1 = LayoutInflater.from(this).inflate(
                R.layout.content_contact_list, null);

        inclusionViewGroup.addView(child1);
       /* SendFragment frag2 = new SendFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.fragment2, frag2);
        ft.commit();*/

        context = this;
        prefManager = new PrefManager(context);

        init();
        setIds();
        initlizeData();
        listeners();
        // initActionBar();

    }

    private void initlizeData() {
        for (int i = 0; i <= 10; i++) {
            nameArrList.add("John Smith" + i);
        }
        for (int i = 0; i <= 10; i++) {
            emailsArrList.add("john.smith" + i + "@hotmail.com");
        }

        countryArrList.add("India");
        countryArrList.add("India");
        countryArrList.add("Lanka");
        countryArrList.add("Nepal");
        countryArrList.add("kenya");
        countryArrList.add("bangladesh");
        countryArrList.add("america");
        countryArrList.add("japan");
        countryArrList.add("berma");
        countryArrList.add("kORIYA");
        countryArrList.add("kORIYA");

        /*for(int i=0;i<=10;i++) {
            countryArrList.add("India");
        }*/

        listUpdate(nameArrList, emailsArrList, countryArrList, searchedtextStr);

    }


    private void initActionBar() {
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        ab.setElevation(0);

    }


    public void listUpdate(ArrayList<String> nameArrList, ArrayList<String> emailsArrList, ArrayList<String> countryArrList, String searchedtextStr) {

        recyclerViewAdapter = new RecyclerViewAdapter(AllContactsListActivity.this, nameArrList, emailsArrList, countryArrList, this, searchedtextStr);

        recyclerView.setAdapter(recyclerViewAdapter);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);


    }

    private void listeners() {
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
                ArrayList<String> type_email_filter = new ArrayList<String>();
                ArrayList<String> type_country_filter = new ArrayList<String>();


                searchedtextStr = theWatchedText.toString();
                try {
                    for (int i = 0; i < nameArrList.size(); i++) {

                        if ((nameArrList.get(i).toLowerCase()).contains(searchedtextStr
                                .toLowerCase()) || (emailsArrList.get(i).toLowerCase()).contains(searchedtextStr
                                .toLowerCase()) || (countryArrList.get(i).toLowerCase()).contains(searchedtextStr
                                .toLowerCase())) {
                            type_name_filter.add(nameArrList.get(i));
                            type_email_filter.add(emailsArrList.get(i));
                            type_country_filter.add(countryArrList.get(i));

                        }
                    }

                    type_name_copy_arrList = type_name_filter;
                    type_email_copy_arrList = type_email_filter;
                    type_country_copy_arrList = type_country_filter;

                    listUpdate(type_name_copy_arrList, type_email_copy_arrList, type_country_copy_arrList, searchedtextStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        navigationView.setNavigationItemSelectedListener(this);

        btnSliding_common.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.END);
            }
        });

        fabBtnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AllContactsListActivity.this, AddContactActivity.class);
                startActivity(intent);

            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                }
                Intent intent = new Intent(AllContactsListActivity.this, EditProfileActivity.class);
                startActivity(intent);

            }
        });


    }

    private void setIds() {

        etSearchCurrency = (EditText) findViewById(R.id.etSearchCurrency);
        imgBtnBack = (ImageButton) findViewById(R.id.imgbtnback);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        btnSliding_common = (ImageButton) findViewById(R.id.btnSliding_common);
        drawer = (DrawerLayout) findViewById(R.id.common_drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.common_nav_view);
        fabBtnAddContact = (ImageView) findViewById(R.id.fabaddcontact);
        fabBtnAddContact.setImageResource(R.drawable.ic_add_circle_black_24dp);
        View header = navigationView.getHeaderView(0);
        editProfile = (TextView) header.findViewById(R.id.textVieweditProfile);
        recylerViewLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(recylerViewLayoutManager);

        userImage = (ImageView) header.findViewById(R.id.imageView);
        tvUserName = (TextView) header.findViewById(R.id.tvusernamecommon);
        tvUserName.setText(prefManager.getUserNamePref());
        populateUserImage();

    }

    public void populateUserImage() {

        String imageUrl = db.getUserImageFromDB(context);
        ;

        if (imageUrl.length() > 0 && imageUrl.toString().contains("https://")) {

            Picasso.with(context)
                    .load(imageUrl)
                    .transform(new PicassoCircleTransformation(context))
                    .into(userImage);
        }

    }

    private void init() {
        helper = new SQLHelper(context);
        database = helper.getWritableDatabase();
        db = new DataHelper(context);
        db.open();

        nameArrList = new ArrayList<String>();
        emailsArrList = new ArrayList<String>();
        countryArrList = new ArrayList<String>();

        type_name_copy_arrList = new ArrayList<String>();
        type_email_copy_arrList = new ArrayList<String>();
        type_country_copy_arrList = new ArrayList<String>();


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.contact) {

            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.alternative_currency) {
            Intent intent = new Intent(AllContactsListActivity.this, AlternativeCurrencyActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.mpin) {
            Intent intent = new Intent(AllContactsListActivity.this, MPinActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.changepassword) {
            Intent intent = new Intent(AllContactsListActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.support) {
            Intent intent = new Intent(AllContactsListActivity.this, SupportMainActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.logout) {

        }


        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }


    /* @Override
     public void sendDataToActivity(String sendMoneyflag) {
         sendFragmentStringFlag=sendMoneyflag;

         }
 */
    @Override
    public void onClick(String username, String clickedUserEmailid) {
        clickedContactName = username;
        clickedUserEmail = clickedUserEmailid;
        Intent intent = new Intent(AllContactsListActivity.this, EditContactActivity.class);
        intent.putExtra("username", clickedContactName);
        intent.putExtra("email", clickedUserEmail);
        startActivity(intent);

    /*    if(!TextUtils.isEmpty(sendFragmentStringFlag)) {
            if (sendFragmentStringFlag.equals(Constants.selectUserName)) {
                Toast.makeText(this, "You clicked " + username, Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                SendFragment sendFragment = new SendFragment();
                sendFragment.setArguments(bundle);
                finish();
            }
        }else{
            Toast.makeText(this, "USER NAME" + username, Toast.LENGTH_SHORT).show();
        }*/
    }
}

