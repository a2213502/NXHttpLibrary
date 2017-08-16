package com.nx.httplibrary;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by zhnagqiang on 15/4/7.
 */
public class BusinessClient {

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
//		client.addHeader("Content-Type", "application/json");
        client.get(url, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
//		client.addHeader("Content-Type","application/json");
        client.post(url, params, responseHandler);
        Log.i("Tag", "访问路径：===========" + url);
        Log.w("hanshuai", "访问路径：  ============" + url + params.toString());

    }

}
