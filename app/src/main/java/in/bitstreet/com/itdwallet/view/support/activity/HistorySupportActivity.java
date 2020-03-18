package in.bitstreet.com.itdwallet.view.support.activity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import in.bitstreet.com.itdwallet.R;
import in.bitstreet.com.itdwallet.database.DataHelper;
import in.bitstreet.com.itdwallet.database.SQLHelper;
import in.bitstreet.com.itdwallet.utills.CallegraphyFontActivity;
import in.bitstreet.com.itdwallet.utills.PicassoCircleTransformation;
import in.bitstreet.com.itdwallet.utills.PrefManager;
import in.bitstreet.com.itdwallet.view.DashBoard.activity.EditProfileActivity;
import in.bitstreet.com.itdwallet.view.alternativecurrency.AlternativeCurrencyActivity;
import in.bitstreet.com.itdwallet.view.changepassword.ChangePasswordActivity;
import in.bitstreet.com.itdwallet.view.contact.adapter.DividerItemDecoration;
import in.bitstreet.com.itdwallet.view.support.adapter.HistoryRecyclerViewAdapter;

public class HistorySupportActivity extends CallegraphyFontActivity implements NavigationView.OnNavigationItemSelectedListener{
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recylerViewLayoutManager;
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

    String[] userName =
            {"John smith dolor sit amet dolor sit amet", "John smith dolor sit amet dolor sit amet", "John smith dolor sit amet", "john players dolor sit amet", "John smith dolor sit amet", "john players dolor sit amet", "John smith dolor sit amet", "john players dolor sit amet", "John smith dolor sit amet", "john players dolor sit amet", "John smith dolor sit amet", "john players dolor sit amet", "John smith dolor sit amet", "john players dolor sit amet", "John smith dolor sit amet", "john players dolor sit amet", "John smith dolor sit amet", "john players dolor sit amet", "John smith dolor sit amet", "john players dolor sit amet"
            };

    String[] openStatus =
            {"Opened on 06-04-2015", "Opened on 06-04-2015", "Opened on 06-04-2015", "Opened on 06-04-2015", "Opened on 06-04-2015", "Opened on 06-04-2015", "Opened on 06-04-2015", "Opened on 06-04-2015", "Opened on 06-04-2015", "Opened on 06-04-2015", "Opened on 06-04-2015", "Opened on 06-04-2015", "Opened on 06-04-2015", "Opened on 06-04-2015", "Opened on 06-04-2015", "Opened on 06-04-2015", "Opened on 06-04-2015", "Opened on 06-04-2015", "Opened on 06-04-2015", "Opened on 06-04-2015"
            };

    String[] closeStatus =
            {"", "Closed on 06-04-2015", "Closed on 06-04-2015", "Closed on 06-04-2015", "", "Closed on 06-04-2015", "Closed on 06-04-2015", "Closed on 06-04-2015", "Closed on 06-04-2015", "Closed on 06-04-2015", "Closed on 06-04-2015", "Closed on 06-04-2015", "Closed on 06-04-2015", "Closed on 06-04-2015", "Closed on 06-04-2015", "Closed on 06-04-2015", "Closed on 06-04-2015", "Closed on 06-04-2015", "Closed on 06-04-2015", "Closed on 06-04-2015"};

    String[] status =
            {"Closed", "Active", "Closed", "Closed", "Active", "Closed", "Active", "Active", "Closed", "Closed", "Active", "Closed", "Active", "Closed", "Active", "Closed", "Closed", "Active", "Closed", "Closed"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_drawer);

        ViewGroup inclusionViewGroup = (ViewGroup)findViewById(R.id.ll_container);

        View child1 = LayoutInflater.from(this).inflate(
                R.layout.content_history_support, null);

        inclusionViewGroup.addView(child1);

        init();
        initDB();
        setIds();
        listeners();
        populateUserImage();

    }



    private void setIds() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_history);
        btnSliding_common = (ImageButton) findViewById(R.id.btnSliding_common);
        drawer = (DrawerLayout) findViewById(R.id.common_drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.common_nav_view);
        View header = navigationView.getHeaderView(0);
        editProfile=(TextView)header. findViewById(R.id.textVieweditProfile) ;
        recylerViewLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(recylerViewLayoutManager);

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


    private void listeners() {

        recyclerViewAdapter = new HistoryRecyclerViewAdapter(HistorySupportActivity.this, userName, openStatus, closeStatus,status);

        recyclerView.setAdapter(recyclerViewAdapter);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

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
                Intent intent=new Intent(HistorySupportActivity.this,EditProfileActivity.class);
                startActivity(intent);

            }
        });
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
            Intent intent=new Intent(HistorySupportActivity.this,AlternativeCurrencyActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.mpin) {

        } else if (id == R.id.changepassword) {
            Intent intent=new Intent(HistorySupportActivity.this,ChangePasswordActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.support) {

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
