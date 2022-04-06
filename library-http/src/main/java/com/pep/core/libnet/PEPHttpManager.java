package com.pep.core.libnet;

import android.text.TextUtils;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.pep.core.libnet.PEPHttpCoinfig.BASE_URL;
import static com.pep.core.libnet.PEPHttpCoinfig.IS_DEBUG;


/**
 * The type Http manager.
 *
 * @author sunbaixin
 */
public class PEPHttpManager {

    private Retrofit retrofit;

    private PEPHttpManager() {
    }


    private static class InnerObject {
        private static PEPHttpManager single = new PEPHttpManager();
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static PEPHttpManager getInstance() {
        return InnerObject.single;
    }

    /**
     * interceptors.
     */
    private ArrayList<Interceptor> interceptors = new ArrayList<>();

    /**
     * networkInterceptor.
     */
    private ArrayList<Interceptor> networkInterceptor = new ArrayList<>();
    /**
     * networConverterFactoryk.
     */
    private ArrayList<Converter.Factory> networConverterFactoryk = new ArrayList<>();


    /**
     * Init.
     *
     * @param baseUrl the base url
     * @throws RuntimeException the runtime exception
     */
    public void init(String baseUrl) throws RuntimeException {
        if (TextUtils.isEmpty(baseUrl)) {
            throw new RuntimeException("baseUrl is null");
        }
        PEPHttpCoinfig.BASE_URL = baseUrl;
        OkHttpClient.Builder builder = getOkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        if (IS_DEBUG) {
            // Log Interceptor print
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

            //Log pring level
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            addInterceptor(loggingInterceptor);
        }

        // set interceptors in okhttp builder
        setInterceptors(builder);

        OkHttpClient client = builder.build();
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder().baseUrl(BASE_URL).client(client);
        setConverterFactorys(retrofitBuilder);
        retrofit = retrofitBuilder.build();
    }

    /**
     * 初始化
     * @param baseUrl 基础url
     * @param okHttpClient debug版用来测试的Client
     * @throws RuntimeException exception
     */
    public void init(String baseUrl,OkHttpClient okHttpClient) throws RuntimeException {
        if (TextUtils.isEmpty(baseUrl)) {
            throw new RuntimeException("baseUrl is null");
        }
        PEPHttpCoinfig.BASE_URL = baseUrl;
        OkHttpClient.Builder builder = okHttpClient.newBuilder().connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        if (IS_DEBUG) {
            // Log Interceptor print
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

            //Log pring level
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            addInterceptor(loggingInterceptor);
        }

        // set interceptors in okhttp builder
        setInterceptors(builder);

        OkHttpClient client = builder.build();
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder().baseUrl(BASE_URL).client(client);
        setConverterFactorys(retrofitBuilder);
        retrofit = retrofitBuilder.build();
    }

    private void setInterceptors(OkHttpClient.Builder builder) {

        for (int i = 0; i < interceptors.size(); i++) {
            builder.addInterceptor(interceptors.get(i));
        }

        for (int i = 0; i < networkInterceptor.size(); i++) {
            builder.addNetworkInterceptor(networkInterceptor.get(i));
        }
    }


    private void setConverterFactorys(Retrofit.Builder retrofitBuilder) {
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
    }

    public OkHttpClient getOkHttpClient() {
        OkHttpClient mOkHttpClient = null;
        try {
            if (mOkHttpClient == null){
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                mOkHttpClient = builder.build();
            }
            return mOkHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Add interceptor.
     *
     * @param interceptor the interceptor
     */
    public void addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
    }

    /**
     * Add interceptor.
     *
     * @param converterFactory the interceptor
     */
    public void addConverterFactory(Converter.Factory converterFactory) {
        networConverterFactoryk.add(converterFactory);
    }

    /**
     * Gets retrofit.
     *
     * @return the retrofit
     */
    public Retrofit getRetrofit() {
        if (retrofit == null) {
            return null;
        }
        return retrofit;
    }

    /**
     * Gets service.
     *
     * @param <T>     the type parameter
     * @param service the service
     * @return the service
     */
    public <T> T getService(final Class<T> service) {
        return retrofit.create(service);
    }

}
