/*
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nx.httplibrary;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.nx.httplibrary.okhttp.cache.CacheEntity;
import com.nx.httplibrary.okhttp.cache.CacheMode;
import com.nx.httplibrary.okhttp.cookie.CookieJarImpl;
import com.nx.httplibrary.okhttp.cookie.store.SPCookieStore;
import com.nx.httplibrary.okhttp.https.HttpsUtils;
import com.nx.httplibrary.okhttp.interceptor.HttpLoggingInterceptor;
import com.nx.httplibrary.okhttp.model.HttpHeaders;
import com.nx.httplibrary.okhttp.model.HttpParams;
import com.nx.httplibrary.okhttp.request.GetRequest;
import com.nx.httplibrary.okhttp.request.PostRequest;
import com.nx.httplibrary.okhttp.utils.HttpUtils;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * @类描述： 网络请求的入口类
 * @创建人：王成丞
 * @创建时间：2017/8/8 15:04
 */
public class NXHttpManager {
    public static final long DEFAULT_MILLISECONDS = 60000;      //默认的超时时间
    public static long REFRESH_TIME = 300;                      //回调刷新时间（单位ms）

    private Application context;            //全局上下文
    private Handler mDelivery;              //用于在主线程执行的调度器
    private okhttp3.OkHttpClient okHttpClient;      //ok请求的客户端
    private HttpParams mCommonParams;       //全局公共请求参数
    private HttpHeaders mCommonHeaders;     //全局公共请求头
    private int mRetryCount;                //全局超时重试次数
    private CacheMode mCacheMode;           //全局缓存模式
    private long mCacheTime;                //全局缓存过期时间,默认永不过期

    private NXHttpManager() {

    }

    public static NXHttpManager getInstance() {
        return NXHttpHolder.holder;
    }

    /**
     * 弹吐丝
     * @param msg
     */
    public void showToastMessage(String msg) {
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

    private static class NXHttpHolder {
        private static NXHttpManager holder = new NXHttpManager();
    }

    /**
     * get请求
     */
    public static <T> GetRequest<T> get(String url) {
        return new GetRequest<>(url);
    }

    /**
     * post请求
     */
    public static <T> PostRequest<T> post(String url) {
        return new PostRequest<>(url);
    }



    /**
     * 必须在全局Application先调用，获取context上下文，否则缓存无法使用
     */
    public NXHttpManager init(Application app, HttpOptions options) {
        mDelivery = new Handler(Looper.getMainLooper());
        context = app;
        HttpUtils.checkNotNull(app, "init failed ! param app is NULL !");
        HttpUtils.checkNotNull(options, "init failed ! param options is NULL !");

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //必须调用初始化

        if (options.isNeedLog) {
            //log相关
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("NXHttp");
            //log打印级别，决定了log显示的详细程度
            loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
            //log颜色级别，决定了log在控制台显示的颜色
            loggingInterceptor.setColorLevel(Level.INFO);
            builder.addInterceptor(loggingInterceptor);
        }


        setCacheMode(options.cacheMode);
        setCacheTime(options.cacheTime);
        //超时时间设置，默认60秒
        builder.connectTimeout(options.connectTimeout, TimeUnit.MILLISECONDS);   //全局的连接超时时间

        //自动管理cookie（或者叫session的保持)
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(app)));            //使用sp保持cookie，如果cookie不过期，则一直有效
        //https相关设置
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);

        setOkHttpClient(builder.build());

        //全局统一超时重连次数
        setRetryCount(options.retryCount);

        //全局公共头
        if (options.commonHeaders != null) {

            addCommonHeaders(options.commonHeaders);
        }

        //全局参数
        if (options.commonParams != null) {

            addCommonParams(options.commonParams);
        }
        return this;
    }

    /**
     * 获取全局上下文
     */
    public Context getContext() {
        HttpUtils.checkNotNull(context, "please call NXHttpManager.getInstance().init() first in application!");
        return context;
    }

    public Handler getDelivery() {
        return mDelivery;
    }

    public okhttp3.OkHttpClient getOkHttpClient() {
        HttpUtils.checkNotNull(okHttpClient, "please call NXHttpManager.getInstance().setOkHttpClient() first in application!");
        return okHttpClient;
    }

    /**
     * 必须设置
     */
    public NXHttpManager setOkHttpClient(okhttp3.OkHttpClient okHttpClient) {
        HttpUtils.checkNotNull(okHttpClient, "okHttpClient == null");
        this.okHttpClient = okHttpClient;
        return this;
    }

    /**
     * 获取全局的cookie实例
     */
    public CookieJarImpl getCookieJar() {
        return (CookieJarImpl) okHttpClient.cookieJar();
    }

    /**
     * 超时重试次数
     */
    public NXHttpManager setRetryCount(int retryCount) {
        if (retryCount < 0) throw new IllegalArgumentException("retryCount must > 0");
        mRetryCount = retryCount;
        return this;
    }

    /**
     * 超时重试次数
     */
    public int getRetryCount() {
        return mRetryCount;
    }

    /**
     * 全局的缓存模式
     */
    private NXHttpManager setCacheMode(CacheMode cacheMode) {
        mCacheMode = cacheMode;
        return this;
    }

    /**
     * 获取全局的缓存模式
     */
    public CacheMode getCacheMode() {
        return mCacheMode;
    }

    /**
     * 全局的缓存过期时间 毫秒
     */
    private NXHttpManager setCacheTime(long cacheTime) {
        if (cacheTime <= -1) cacheTime = CacheEntity.CACHE_NEVER_EXPIRE;
        mCacheTime = cacheTime;
        return this;
    }

    /**
     * 获取全局的缓存过期时间
     */
    public long getCacheTime() {
        return mCacheTime;
    }

    /**
     * 获取全局公共请求参数
     */
    public HttpParams getCommonParams() {
        return mCommonParams;
    }

    /**
     * 添加全局公共请求参数
     */
    public NXHttpManager addCommonParams(HttpParams commonParams) {
        if (mCommonParams == null) mCommonParams = new HttpParams();
        mCommonParams.put(commonParams);
        return this;
    }

    /**
     * 获取全局公共请求头
     */
    public HttpHeaders getCommonHeaders() {
        return mCommonHeaders;
    }

    /**
     * 添加全局公共请求参数
     */
    public NXHttpManager addCommonHeaders(HttpHeaders commonHeaders) {
        if (mCommonHeaders == null) mCommonHeaders = new HttpHeaders();
        mCommonHeaders.put(commonHeaders);
        return this;
    }

    /**
     * 根据Tag取消请求
     */
    public void cancelTag(Object tag) {
        if (tag == null) return;
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 根据Tag取消请求
     */
    public static void cancelTag(okhttp3.OkHttpClient client, Object tag) {
        if (client == null || tag == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 取消所有请求请求
     */
    public void cancelAll() {
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            call.cancel();
        }
    }

    /**
     * 取消所有请求请求
     */
    public static void cancelAll(okhttp3.OkHttpClient client) {
        if (client == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : client.dispatcher().runningCalls()) {
            call.cancel();
        }
    }
}
