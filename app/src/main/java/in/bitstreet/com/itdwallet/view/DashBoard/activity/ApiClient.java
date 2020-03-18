package in.bitstreet.com.itdwallet.view.DashBoard.activity;

import android.content.Context;

import okhttp3.OkHttpClient;
//import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by shahbaz on 2/6/17.
 */

public class ApiClient {

//    public static final String BASE_URL = "http://192.168.1.108/omblee/api/v1/";   // sanket

    public static final String BASE_URL = "https://staging.hansinfotech.in/omblee_web/api/v1/";
    public static final String GOOGLE_MAP_URL= "https://maps.googleapis.com";
    private static Retrofit retrofit = null;
    private static Retrofit retrofitGoogle = null;


    public static Retrofit getClient(Context context) {


        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .client(SelfSigningClientBuilder.createClient(context))
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

  /*  public static Retrofit getGoogleAPI() {
        *//*
        Caution!
            Don't forget to remove Interceptors (or change Logging Level to NONE) in production! Otherwise people will be able to see your request and response on Log Cat.
         *//*

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();
        retrofitGoogle = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(GOOGLE_MAP_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        return retrofitGoogle;
    }*/
}