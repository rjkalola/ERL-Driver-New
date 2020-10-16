package com.app.erldriver.injection.module;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.app.erldriver.BuildConfig;
import com.app.erldriver.ERLApp;
import com.app.erldriver.model.entity.response.User;
import com.app.erldriver.network.RxErrorHandlingCallAdapterFactory;
import com.app.erldriver.util.AppUtils;
import com.app.utilities.utils.StringHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    String mBaseUrl;

    public NetworkModule(String baseUrl) {
        this.mBaseUrl = baseUrl;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreference(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }


    @Provides  // Dagger will only look for methods annotated with @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.serializeNulls().create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {


        final TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        SSLSocketFactory sslSocketFactory = null;
        // Install the all-trusting trust manager
        try {
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
             sslSocketFactory = sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);

        builder.hostnameVerifier((hostname, session) -> true);

        builder.connectTimeout(60, TimeUnit.SECONDS);
        builder.readTimeout(100, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.MINUTES);

        builder.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder();
            try{
                User userInfo = AppUtils.getUserPrefrence(ERLApp.getContext());
                if ( userInfo != null && !StringHelper.isEmpty(userInfo.getApi_token())) {
                    requestBuilder.addHeader("Authorization", "Bearer "+userInfo.getApi_token());
                }
            }catch (Exception e){

            }
            Request request = requestBuilder.build();
            return chain.proceed(request);
        });

       if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }

        return builder.build();
    }

    @Provides
    @Singleton
    GsonConverterFactory providesGsonConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(GsonConverterFactory gsonConverterFactory, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                //.addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .build();
    }

}
