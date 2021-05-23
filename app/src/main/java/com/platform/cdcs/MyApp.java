package com.platform.cdcs;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.util.Base64;

import com.platform.cdcs.model.PersonModel;
import com.platform.cdcs.tool.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by holytang on 2017/9/20.
 */
public class MyApp extends Application {

    private static final int NETWORK_TIMEOUT = 30 * 1000;
    private static MyApp app;
    private PersonModel model;

    public static MyApp getInstance() {
        return app;
    }

    private void initHttp() {
        //网络请求全局初始化
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplication()));
        OkHttpClient.Builder builder = new OkHttpClient.Builder().cookieJar(cookieJar).connectTimeout(NETWORK_TIMEOUT, TimeUnit.MILLISECONDS).
                readTimeout(NETWORK_TIMEOUT, TimeUnit.MILLISECONDS).writeTimeout(NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
//        try {
//            HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
//            builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
//            builder.hostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String s, SSLSession sslSession) {
//                    return true;
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        OkHttpUtils.initClient(builder.build());

    }

    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.VmPolicy.Builder build = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(build.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            build.detectFileUriExposure();
        }
        app = this;
        MultiDex.install(this);
        init();
    }

    public Application getApplication() {
        return this;
    }

    private void init() {
        initHttp();
    }

    public void onTerminate() {
        super.onTerminate();
    }

    public PersonModel getAccount() {
        if (model == null) {
            SharedPreferences loginshare = getApplication()
                    .getSharedPreferences(Constant.OA_PREFERENCE,
                            Context.MODE_PRIVATE);
            String userStr = loginshare.getString("login", "");
            byte[] userByte = Base64.decode(userStr.getBytes(), Base64.DEFAULT);
            ByteArrayInputStream bis = null;
            ObjectInputStream ois = null;
            try {
                bis = new ByteArrayInputStream(userByte);
                ois = new ObjectInputStream(bis);
                model = (PersonModel) ois.readObject();
            } catch (Exception e) {
                model = new PersonModel();
                model.setUserId("");
            } finally {
                try {
                    if (ois != null) {
                        ois.close();
                    }
                    if (bis != null) {
                        bis.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return model;
    }

    public void setAccount(PersonModel model) {
        SharedPreferences loginshare = getApplication()
                .getSharedPreferences(Constant.OA_PREFERENCE,
                        Context.MODE_PRIVATE);
        ByteArrayOutputStream bos = null;
        ObjectOutputStream ooS = null;
        try {
            bos = new ByteArrayOutputStream();
            ooS = new ObjectOutputStream(bos);
            ooS.writeObject(model);
            String loginMsg = new String(Base64.encode(bos.toByteArray(), Base64.DEFAULT));
            loginshare.edit().putString("login", loginMsg).commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ooS != null) {
                    ooS.flush();
                    ooS.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (Exception e) {

            }
        }
        this.model = model;
    }
}
