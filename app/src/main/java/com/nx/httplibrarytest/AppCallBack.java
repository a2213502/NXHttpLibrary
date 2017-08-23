package com.nx.httplibrarytest;

import com.nx.httplibrary.okhttp.callback.JsonCallback;
import com.nx.httplibrary.okhttp.model.Response;
import com.nx.httplibrary.okhttp.request.base.Request;

/**
 * @类描述： TODO
 * @创建人：王成丞
 * @创建时间：2017/8/23 10:41
 */
public class AppCallBack extends JsonCallback {



    @Override
    public void onStart(Request request) {

        super.onStart(request);
    }

    @Override
    public void onSuccess(Response response) {

    }

    @Override
    public void onError(Response response) {
        super.onError(response);
    }

    @Override
    public void onFinish() {
        super.onFinish();
    }
}
