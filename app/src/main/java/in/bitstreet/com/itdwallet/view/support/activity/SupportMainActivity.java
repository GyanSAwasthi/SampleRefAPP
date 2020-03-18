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
import in.bitstreet.com.itdwallet.view.contact.activity.AllContactsListActivity;
import in.bitstreet.com.itdwallet.view.mpin.MPinActivity;

public class SupportMainActivity extends CallegraphyFontActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawer;
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
                R.layout.content_support, null);

        inclusionViewGroup.addView(child1);


        init();
        initDB();
        setIds();
        populateUserImage();

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

    private void setIds() {
        ImageButton btnSliding_common = (ImageButton) findViewById(R.id.btnSliding_common);
        drawer = (DrawerLayout) findViewById(R.id.common_drawer_layout);
        NavigationView  navigationView = (NavigationView) findViewById(R.id.common_nav_view);
        View header = navigationView.getHeaderView(0);
        TextView editProfile=(TextView)header. findViewById(R.id.textVieweditProfile) ;

        TextView tvCreateTicket=(TextView) findViewById(R.id.tv_create_ticket) ;
        TextView tvActiveTicket=(TextView) findViewById(R.id.tv_active_ticket) ;
        TextView tvHistoryTicket=(TextView) findViewById(R.id.history_ticket) ;
        TextView tvFaqsTicket=(TextView) findViewById(R.id.tv_faq) ;

        userImage = (ImageView)header. findViewById(R.id.imageView);
        tvUserName = (TextView) header.findViewById(R.id.tvusernamecommon);
        tvUserName.setText(prefManager.getUserNamePref());


        listeners(btnSliding_common,editProfile,navigationView,tvCreateTicket,tvActiveTicket,tvHistoryTicket,tvFaqsTicket);

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


    private void listeners(ImageButton btnSliding_common,TextView editProfile, NavigationView  navigationView,TextView tvCreateTicket,TextView tvActiveTicket,TextView tvHistoryTicket,TextView tvFaqsTicket) {


        navigationView.setNavigationItemSelectedListener(SupportMainActivity.this);

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
                Intent intent=new Intent(SupportMainActivity.this,EditProfileActivity.class);
                startActivity(intent);

            }
        });

        tvCreateTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                }
                Intent intent=new Intent(SupportMainActivity.this,CreateTicketActivity.class);
                startActivity(intent);

            }
        });
        tvActiveTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(SupportMainActivity.this,ActiveChatSupportActivity.class);
                startActivity(intent);

            }
        });
        tvHistoryTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(SupportMainActivity.this,HistorySupportActivity.class);
                startActivity(intent);

            }
        });
        tvFaqsTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(SupportMainActivity.this,FAQActivity.class);
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
            Intent intent=new Intent(SupportMainActivity.this,AllContactsListActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.alternative_currency) {
            Intent intent=new Intent(SupportMainActivity.this,AlternativeCurrencyActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.mpin) {
            Intent intent=new Intent(SupportMainActivity.this,MPinActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.changepassword) {
            Intent intent=new Intent(SupportMainActivity.this,ChangePasswordActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.support) {

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
