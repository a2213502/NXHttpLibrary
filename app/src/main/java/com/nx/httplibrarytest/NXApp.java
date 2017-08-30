package com.nx.httplibrarytest;

import android.app.Application;

import com.nx.httplibrary.HttpOptions;
import com.nx.httplibrary.NXHttpManager;
import com.nx.httplibrary.okhttp.cache.CacheMode;

/**
 * @类描述： TODO
 * @创建人：王成丞
 * @创建时间：2017/8/8 18:52
 */
public class NXApp extends Application {
    @Override
    public void onCreate() {

        //初始化http框架
        HttpOptions options = new HttpOptions.Builder().setCommonParams("appid", "1")
                .setCommonParams("phone_system", "1")
                .setCommonParams("version_code", "v1.2")
                .setConnectTimeout(1000)
                .setRetryCount(0)
                .setCacheMode(CacheMode.NO_CACHE)
                .setNeedResponseTest(true)
                .build();

//        HttpOptions aDefault = HttpOptions.createDefault();
        NXHttpManager.getInstance().init(this, options);
        super.onCreate();
    }
}
