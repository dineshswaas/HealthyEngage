package APIServices;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import utils.Constants;


public class RetrofitAPIBuilder {

       static Retrofit retrofit = null;
    static Retrofit retrofit_authy = null;
        public static synchronized Retrofit getInstance() {

            final OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setReadTimeout(300, TimeUnit.SECONDS);
            okHttpClient.setConnectTimeout(300, TimeUnit.SECONDS);
            okHttpClient.networkInterceptors().add(new StethoInterceptor());

            if(retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.COMPANY_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(okHttpClient)
                        .build();
            }
            return retrofit;
        }

    public static synchronized Retrofit getInstanceAuthy() {

        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(300, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(300, TimeUnit.SECONDS);
        okHttpClient.networkInterceptors().add(new StethoInterceptor());

        if(retrofit_authy == null) {
            retrofit_authy = new Retrofit.Builder()
                    .baseUrl(Constants.AUTHY_API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit_authy;
    }



}
