package in.bitstreet.com.itdwallet.controller;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import in.bitstreet.com.itdwallet.utills.Constants;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jay on 8/12/17.
 */

public class ApiClient {


    private static Retrofit retrofit = null;


    public static Retrofit getClient(Context context) {


        if (retrofit==null) {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS).build();

            retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
