package in.bitstreet.com.itdwallet.view.support.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import in.bitstreet.com.itdwallet.R;
import in.bitstreet.com.itdwallet.database.DataHelper;
import in.bitstreet.com.itdwallet.database.SQLHelper;
import in.bitstreet.com.itdwallet.utills.PicassoCircleTransformation;
import in.bitstreet.com.itdwallet.utills.PrefManager;
import in.bitstreet.com.itdwallet.view.DashBoard.activity.EditProfileActivity;
import in.bitstreet.com.itdwallet.view.alternativecurrency.AlternativeCurrencyActivity;
import in.bitstreet.com.itdwallet.view.changepassword.ChangePasswordActivity;
import in.bitstreet.com.itdwallet.view.contact.activity.AllContactsListActivity;
import in.bitstreet.com.itdwallet.view.mpin.MPinActivity;
import in.bitstreet.com.itdwallet.view.support.adapter.ChatArrayAdapter;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ActiveChatSupportActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener{
    private static final String TAG = "ChatActivity";

    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    private boolean side = false;
    DrawerLayout drawer;
    NavigationView navigationView;
    ImageButton imgBtnUAMSliding;
    TextView editProfile;
    TextView tvUserName;
    ImageView userImage;
    DataHelper db;
    SQLHelper helper;
    SQLiteDatabase database;
    Context ctx;
    PrefManager prefManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_drawer_active_support);
       /* ViewGroup inclusionViewGroup = (ViewGroup)findViewById(R.id.ll_bottom);

        View child = LayoutInflater.from(this).inflate(
                R.layout.activity_support_active_chat, null);

        inclusionViewGroup.addView(child);*/


        //To hide the soft key board when activity launches
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();
        initDB();
        setIDs();
        listeners();
      drawer.setDrawerListener(this);
        populateUserImage();


    }

    @Override
    public void onDrawerOpened(View arg0) {
        hideSoftKeyboard();
    }

    @Override
    public void onDrawerClosed(View arg0) {

    }

    @Override
    public void onDrawerSlide(View arg0, float arg1) {
        hideSoftKeyboard();
    }

    @Override
    public void onDrawerStateChanged(int arg0) {
        hideSoftKeyboard();
    }

    //for setting the font style for whole activity font
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
    private void listeners() {
        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right);
        listView.setAdapter(chatArrayAdapter);

       // navigationView.setNavigationItemSelectedListener(this);

        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });


        imgBtnUAMSliding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.END);
            }
        });

        navigationView.setNavigationItemSelectedListener(this);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                }
                Intent intent=new Intent(ActiveChatSupportActivity.this,EditProfileActivity.class);
                startActivity(intent);

            }
        });
    }

    private void setIDs() {

        drawer = (DrawerLayout) findViewById(R.id.common_drawer_layout);
        imgBtnUAMSliding = (ImageButton) findViewById(R.id.btnSliding_common);
        navigationView = (NavigationView) findViewById(R.id.common_nav_view);
        View header = navigationView.getHeaderView(0);
        editProfile=(TextView)header. findViewById(R.id.textVieweditProfile) ;

        buttonSend = (Button) findViewById(R.id.send);

        listView = (ListView) findViewById(R.id.msgview);

        chatText = (EditText) findViewById(R.id.msg);

        userImage = (ImageView)header. findViewById(R.id.imageView);
        tvUserName = (TextView) header.findViewById(R.id.tvusernamecommon);
        tvUserName.setText(prefManager.getUserNamePref());

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

    private boolean sendChatMessage() {
        chatArrayAdapter.add(new ChatMessage(side, chatText.getText().toString()));
        chatText.setText("");
        side = !side;
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.contact) {
            Intent intent=new Intent(ActiveChatSupportActivity.this,AllContactsListActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.alternative_currency) {
            Intent intent=new Intent(ActiveChatSupportActivity.this,AlternativeCurrencyActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }

        } else if (id == R.id.mpin) {
            Intent intent=new Intent(ActiveChatSupportActivity.this,MPinActivity.class);
            startActivity(intent);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }


        } else if (id == R.id.changepassword) {
            Intent intent=new Intent(ActiveChatSupportActivity.this,ChangePasswordActivity.class);
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
}
