package in.bitstreet.com.itdwallet.utills;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import in.bitstreet.com.itdwallet.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CallegraphyFontActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callegraphy_font);

        //for setting the font style for whole activity font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    //for setting the font style for whole activity font
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
