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
                .setCommonParams("version_code", "v1.2")//通用参数设置，每次网络请求会携带该参数
                .setConnectTimeout(1000) //链接超时时间
                .setRetryCount(0) //重试次数
                .setCacheMode(CacheMode.NO_CACHE) //设置缓存模式，默认为如果网络获取失败使用缓存。
                .setNeedResponseTest(true) //使用自定义服务器返回测试模式 ，测试数据在assets目录下存放自定义的json文本。默认不使用 。
                .build();
        NXHttpManager.getInstance().init(this, options);
        super.onCreate();
    }
}
