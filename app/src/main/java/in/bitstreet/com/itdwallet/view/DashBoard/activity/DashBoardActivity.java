package in.bitstreet.com.itdwallet.view.DashBoard.activity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.bitstreet.com.itdwallet.R;
import in.bitstreet.com.itdwallet.database.DataHelper;
import in.bitstreet.com.itdwallet.database.SQLHelper;
import in.bitstreet.com.itdwallet.utills.CallegraphyFontActivity;
import in.bitstreet.com.itdwallet.utills.Constants;
import in.bitstreet.com.itdwallet.utills.PicassoCircleTransformation;
import in.bitstreet.com.itdwallet.utills.PrefManager;
import in.bitstreet.com.itdwallet.view.DashBoard.fragmentadapter.PagerAdapter;
import in.bitstreet.com.itdwallet.view.alternativecurrency.AlternativeCurrencyActivity;
import in.bitstreet.com.itdwallet.view.changepassword.ChangePasswordActivity;
import in.bitstreet.com.itdwallet.view.contact.activity.AllContactsListActivity;
import in.bitstreet.com.itdwallet.view.login.LoginActivity;
import in.bitstreet.com.itdwallet.view.mpin.MPinActivity;
import in.bitstreet.com.itdwallet.view.support.activity.SupportMainActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class DashBoardActivity extends CallegraphyFontActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener {
    Toolbar toolbar;
    ViewPager simpleViewPager;
    TabLayout tabLayout;
    DrawerLayout drawer;
    NavigationView navigationViewRight, navigationViewLeft;
    ImageButton imgBtnUAMSliding;
    TextView editProfile, tvUserName;
    ImageView userImage;
    Context context;
    PrefManager prefManager;
    int mPosition = -1;
    DataHelper db;
    SQLHelper helper;
    SQLiteDatabase database;
    // Array of strings storing country names
    String[] currencyNameStr;

    // Array of integers points to images stored in /res/drawable-ldpi/
    int[] mLeftDrawerImages = new int[]{
            R.drawable.xyz,
            R.drawable.ripple,
            R.drawable.bitcoincash,
            R.drawable.nem,
            R.drawable.litecoin,
            R.drawable.monero,
            R.drawable.peercoin
    };

    // Array of strings to initial counts
    String[] currencyAmtStr = new String[]{
            "2.999999999 ETH", "2.999999999 XRP", "2.999999999 BCH", "2.999999999 XEM", "2.999999999 LTC",
            "2.999999999 XMR", "2.999999999 PPC"};

    private ListView mDrawerList;
    private List<HashMap<String, String>> mList;
    private SimpleAdapter mAdapter;
    final private String currencyName = "currencyName";
    final private String currencyImg = "currencyImg";
    final private String currencyAmt = "currencyAmt";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        context = this;
        prefManager = new PrefManager(context);


        leftSlidingDrawer();
        TextView editText = (TextView) findViewById(R.id.edit_profile_tv);

        init();
        initToolBar();
        setIDs();
        setAdapter();
        listeners();
        drawer.setDrawerListener(this);


    }

    private void init() {
        helper = new SQLHelper(context);
        database = helper.getWritableDatabase();
        db = new DataHelper(context);
        db.open();
    }


    @Override
    public void onDrawerOpened(View arg0) {
        populateUserImage();
        hideSoftKeyboard();
    }

    @Override
    public void onDrawerClosed(View arg0) {

    }

    @Override
    public void onDrawerSlide(View arg0, float arg1) {
        populateUserImage();
        hideSoftKeyboard();
    }

    @Override
    public void onDrawerStateChanged(int arg0) {
        populateUserImage();
        hideSoftKeyboard();
    }

    //for setting the font style for whole activity font
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }


    private void leftSlidingDrawer() {

        currencyNameStr = getResources().getStringArray(R.array.countries);

        // Title of the activity

        // Getting a reference to the drawer listview
        mDrawerList = (ListView) findViewById(R.id.drawer_list);


        // Each row in the list stores country name, count and flag
        mList = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 7; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put(currencyName, currencyNameStr[i]);
            hm.put(currencyAmt, currencyAmtStr[i]);
            hm.put(currencyImg, Integer.toString(mLeftDrawerImages[i]));
            mList.add(hm);
        }

// Keys used in Hashmap
        String[] from = {currencyImg, currencyName, currencyAmt};

// Ids of views in listview_layout
        int[] to = {R.id.currency_img, R.id.currency_name, R.id.currency_amt};

        mAdapter = new SimpleAdapter(this, mList, R.layout.drawer_layout, from, to);

        mDrawerList.setAdapter(mAdapter);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.contact) {
            Intent intent = new Intent(DashBoardActivity.this, AllContactsListActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.alternative_currency) {
            Intent intent = new Intent(DashBoardActivity.this, AlternativeCurrencyActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.mpin) {
            Intent intent = new Intent(DashBoardActivity.this, MPinActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.changepassword) {
            Intent intent = new Intent(DashBoardActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.support) {

            Intent intent = new Intent(DashBoardActivity.this, SupportMainActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.logout) {
            prefManager.clearUserIdSharedPreference();
            Intent intent = new Intent(DashBoardActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }
        }


        drawer.closeDrawer(GravityCompat.END);
        return true;
    }


    @Override
    public void onBackPressed() {
        //  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }


    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayShowTitleEnabled(false);
        ab.setElevation(0);

    }

    private void setIDs() {

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        imgBtnUAMSliding = (ImageButton) findViewById(R.id.uamSliding);



        navigationViewRight = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationViewRight.getHeaderView(0);
        editProfile = (TextView) header.findViewById(R.id.edit_profile_tv);


        // get the reference of ViewPager and TabLayout
        simpleViewPager = (ViewPager) findViewById(R.id.simpleViewPager);
        tabLayout = (TabLayout) findViewById(R.id.simpleTabLayout);
// Create a new Tab named "Transaction Tab"
        TabLayout.Tab transactionsTab = tabLayout.newTab();
        transactionsTab.setText(Constants.dashBoardTransactions);
        tabLayout.addTab(transactionsTab); // add  the tab at in the TabLayout
// Create a new Tab named "SendTab"
        TabLayout.Tab sendTab = tabLayout.newTab();
        sendTab.setText(Constants.dashBoardSend);
        tabLayout.addTab(sendTab);
// Create a new Tab named "Receive Tab"
        TabLayout.Tab receiveTab = tabLayout.newTab();
        receiveTab.setText(Constants.dashBoardReceive);
        tabLayout.addTab(receiveTab);


        userImage = (ImageView)header. findViewById(R.id.imageView);
        tvUserName = (TextView) header.findViewById(R.id.tvUserName);
        tvUserName.setText(prefManager.getUserNamePref());


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

    private void setAdapter() {
        PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        simpleViewPager.setAdapter(adapter);

    }


    private void listeners() {
        imgBtnUAMSliding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.END);
            }
        });

        navigationViewRight.setNavigationItemSelectedListener(this);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                }
                Intent intent = new Intent(DashBoardActivity.this, EditProfileActivity.class);
                startActivity(intent);

            }
        });


        // addOnPageChangeListener event change the tab on slide
        simpleViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//add tab click event
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                simpleViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
        // tabLayout.setupWithViewPager(simpleViewPager);
    }


}

