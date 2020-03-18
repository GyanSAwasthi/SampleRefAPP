package in.bitstreet.com.itdwallet.view.support.activity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.bitstreet.com.itdwallet.R;
import in.bitstreet.com.itdwallet.database.DataHelper;
import in.bitstreet.com.itdwallet.database.SQLHelper;
import in.bitstreet.com.itdwallet.utills.CallegraphyFontActivity;
import in.bitstreet.com.itdwallet.utills.PicassoCircleTransformation;
import in.bitstreet.com.itdwallet.utills.PrefManager;
import in.bitstreet.com.itdwallet.view.DashBoard.activity.EditProfileActivity;
import in.bitstreet.com.itdwallet.view.alternativecurrency.AlternativeCurrencyActivity;
import in.bitstreet.com.itdwallet.view.changepassword.ChangePasswordActivity;
import in.bitstreet.com.itdwallet.view.contact.activity.AllContactsListActivity;
import in.bitstreet.com.itdwallet.view.support.adapter.ExpandableListAdapter;

public class FAQActivity extends CallegraphyFontActivity implements NavigationView.OnNavigationItemSelectedListener{
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ImageButton btnSliding_common;
    TextView editProfile;
    DrawerLayout drawer;
    NavigationView navigationView;

    TextView tvUserName;
    ImageView userImage;
    DataHelper db;
    SQLHelper helper;
    SQLiteDatabase database;
    Context ctx;
    PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_drawer);

        ViewGroup inclusionViewGroup = (ViewGroup)findViewById(R.id.ll_container);

        View child1 = LayoutInflater.from(this).inflate(
                R.layout.content_faq_support, null);

        inclusionViewGroup.addView(child1);



        init();
        initDB();
        setIds();
        populateUserImage();
        // preparing list data
        prepareListData();
        listeners();
        }



    private void listeners() {

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.setGroupIndicator(null);
        expListView.setChildIndicator(null);
        expListView.setChildDivider(getResources().getDrawable(R.color.black));
        expListView.setDivider(getResources().getDrawable(R.color.backgroundcolor));
        expListView.setDividerHeight(2);



        expListView.setGroupIndicator(null);
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            public void onGroupExpand(int groupPosition) {
                int len = listAdapter.getGroupCount();
                for (int i = 0; i < len; i++) {
                    if (i != groupPosition) {
                        expListView.collapseGroup(i);
                    }
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(this);

        btnSliding_common.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.END);
            }
        });


        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                }
                Intent intent=new Intent(FAQActivity.this,EditProfileActivity.class);
                startActivity(intent);

            }
        });

    }

    private void setIds() {
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        btnSliding_common = (ImageButton) findViewById(R.id.btnSliding_common);
        drawer = (DrawerLayout) findViewById(R.id.common_drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.common_nav_view);
        View header = navigationView.getHeaderView(0);
        editProfile=(TextView)header. findViewById(R.id.textVieweditProfile) ;

        userImage = (ImageView)header. findViewById(R.id.imageView);
        tvUserName = (TextView) header.findViewById(R.id.tvusernamecommon);
        tvUserName.setText(prefManager.getUserNamePref());


    }

    private void init() {
        ctx = this;
        prefManager = new PrefManager(ctx);
    }

    private void initDB() {
        helper = new SQLHelper(ctx);
        database = helper.getWritableDatabase();
        db = new DataHelper(ctx);
        db.open();
    }

    public void populateUserImage() {

        String imageUrl = db.getUserImageFromDB(ctx);


        if (imageUrl.length() > 0 && imageUrl.toString().contains("https://")) {

            Picasso.with(ctx)
                    .load(imageUrl)
                    .transform(new PicassoCircleTransformation(ctx))
                    .into(userImage);
        }

    }




    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Is world press available in my language?");
        listDataHeader.add("What is the difference between Worldpress.com and worldpress.in?");
        listDataHeader.add("Why choose world press?");
       /* listDataHeader.add("Is world press available in my language ?");
        listDataHeader.add("What is the difference between Worldpress.com and worldpress.in?");
        listDataHeader.add("Why choose world press?");

        listDataHeader.add("Is world press available in my language? ");
        listDataHeader.add("What is the difference between Worldpress.com and worldpress.in?");
        listDataHeader.add("Why choose world press?");*/


        // Adding child data
        List<String> faq_1 = new ArrayList<String>();
        faq_1.add("World press is more roubust than any other application.It can extend to any website you want to create.");


        List<String> faq_2 = new ArrayList<String>();
        faq_2.add("World press is more roubust than any other application.It can extend to any website you want to create.");


        List<String> faq_3 = new ArrayList<String>();
        faq_3.add("World press is more roubust than any other application.It can extend to any website you want to create.");


        listDataChild.put(listDataHeader.get(0), faq_1); // Header, Child data
        listDataChild.put(listDataHeader.get(1), faq_2);
        listDataChild.put(listDataHeader.get(2), faq_3);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.contact) {
            Intent intent=new Intent(FAQActivity.this,AllContactsListActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.alternative_currency) {
            Intent intent=new Intent(FAQActivity.this,AlternativeCurrencyActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.mpin) {

        } else if (id == R.id.changepassword) {
            Intent intent=new Intent(FAQActivity.this,ChangePasswordActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.support) {
            Intent intent=new Intent(FAQActivity.this,SupportMainActivity.class);
            startActivity(intent);
            finish();
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

}