package in.bitstreet.com.itdwallet.utills;

import android.app.Application;

public class MyApp extends Application {
  @Override
  public void onCreate() {
     super.onCreate();
    TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Roboto-Regular.ttf");
  }
  }